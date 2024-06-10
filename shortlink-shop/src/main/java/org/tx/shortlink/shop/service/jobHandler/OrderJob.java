package org.tx.shortlink.shop.service.jobHandler;

import com.xxl.job.core.handler.annotation.XxlJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.tx.shortlink.shop.common.config.GetBeanUtils;
import org.tx.shortlink.shop.common.enums.OrderStatusEnum;
import org.tx.shortlink.shop.entity.ProductOrderDO;
import org.tx.shortlink.shop.service.IProductOrderService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class OrderJob {
    private static final Logger logger = LoggerFactory.getLogger(OrderJob.class);

    /**
     * 单个更新操作
     */
    @XxlJob("TimeoutOrderJobHandler1")
    @Transactional(rollbackFor = Exception.class)
    public void TimeoutOrderJobHandler1(){
        logger.info("TimeoutOrderJobHandler1");
        IProductOrderService productOrderService = GetBeanUtils.getBean(IProductOrderService.class);
        long start = System.currentTimeMillis();
        //轮询数据库，查询订单表字段锁为0的订单
        List<ProductOrderDO> productOrderDOS = productOrderService.lambdaQuery().eq(ProductOrderDO::getStateLock, "0").list();
        int size = productOrderDOS.size();
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < size; i++) {
            ProductOrderDO productOrderDO = productOrderDOS.get(i);
            if (productOrderDO.getState().equals("NEW") || productOrderDO.getState().equals("PAYING")){
                if (productOrderDO.getGmtCreate().isBefore(now.minusMinutes(1))){
                    //订单状态为NEW或PAYING且已经超时，更新状态锁，更新状态为PAY_TIMEOUT，关闭支付渠道
                    productOrderDO.setStateLock(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    productOrderDO.setState(OrderStatusEnum.PAY_TIMEOUT.name());
                    //TODO 关闭支付
                }//未超时不用修改
            }else {
                //订单状态为其它，更新状态锁
                productOrderDO.setStateLock(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
            productOrderService.lambdaUpdate().eq(ProductOrderDO::getUserId, productOrderDO.getUserId()).eq(ProductOrderDO::getTradeNo, productOrderDO.getTradeNo()).update(productOrderDO);
        }
        long end = System.currentTimeMillis();
        logger.info("TimeoutOrderJobHandler耗时：{}",end-start);
    }

    /**
     * 批量处理
     */
    @XxlJob("TimeoutOrderJobHandler2")
    @Transactional(rollbackFor = Exception.class)
    public void TimeoutOrderJobHandler2(){
        logger.info("TimeoutOrderJobHandler2");
        JdbcTemplate jdbcTemplate = GetBeanUtils.getBean(JdbcTemplate.class);
        LocalDateTime now = LocalDateTime.now();
        long start = System.currentTimeMillis();
        //轮询数据库，查询订单表字段锁为0的订单
        for (int i = 0; i < 16;i++){
            String sql = "select * from t_product_order_" + i + " where state_lock = '0'";
            String updateSql = "update t_product_order_" + i + " set state_lock = ?, state = ? where id = ?";
            List<ProductOrderDO> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ProductOrderDO.class));
            ArrayList<ProductOrderDO> listToUpdate = new ArrayList<>();
            for (ProductOrderDO productOrderDO:list){
                if (productOrderDO.getState().equals("NEW") || productOrderDO.getState().equals("PAYING")){
                    if (productOrderDO.getGmtCreate().isBefore(now.minusMinutes(1))){
                        //订单状态为NEW或PAYING且已经超时，更新状态锁，更新状态为PAY_TIMEOUT，关闭支付渠道
                        productOrderDO.setStateLock(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                        productOrderDO.setState(OrderStatusEnum.PAY_TIMEOUT.name());
                        listToUpdate.add(productOrderDO);
                        //TODO 关闭支付
                    }//未超时不用修改
                }else {
                    //订单状态为其它，更新状态锁
                    productOrderDO.setStateLock(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    listToUpdate.add(productOrderDO);
                }
            }
            jdbcTemplate.batchUpdate(updateSql, listToUpdate, listToUpdate.size(),
                    (ps, argument) -> {
                        ps.setString(1, argument.getStateLock());
                        ps.setString(2, argument.getState());
                        ps.setLong(3, argument.getId());
                    });
        }
        long end = System.currentTimeMillis();
        logger.info("TimeoutOrderJobHandler2 耗时：{}", end-start);
    }

    /**
     * 批量结合异步
     */
    @XxlJob("TimeoutOrderJobHandler3")
    public void TimeoutOrderJobHandler3(){
        logger.info("TimeoutOrderJobHandler3");
        JdbcTemplate jdbcTemplate = GetBeanUtils.getBean(JdbcTemplate.class);
        LocalDateTime now = LocalDateTime.now();
        long start = System.currentTimeMillis();
        CompletableFuture<?>[] futures = new CompletableFuture[16];
        for (int i = 0; i < 16; i++) {
            String sql = "select * from t_product_order_" + i + " where state_lock = '0'";
            String updateSql = "update t_product_order_" + i + " set state_lock = ?, state = ? where id = ?";
            CompletableFuture<String> future = GetBeanUtils.getBean(OrderJob.class).processSingleTable(sql, updateSql, now, jdbcTemplate);
            futures[i] = future;
        }
        // 等待所有任务完成
        CompletableFuture.allOf(futures).join();
        long end = System.currentTimeMillis();
        logger.info("TimeoutOrderJobHandler3 耗时：{}", end-start);
    }

    @Async
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
    public CompletableFuture<String> processSingleTable(String sql,String updateSql, LocalDateTime now, JdbcTemplate jdbcTemplate) {
        List<ProductOrderDO> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ProductOrderDO.class));
        ArrayList<ProductOrderDO> listToUpdate = new ArrayList<>();
        for (ProductOrderDO productOrderDO:list){
            if (productOrderDO.getState().equals("NEW") || productOrderDO.getState().equals("PAYING")){
                if (productOrderDO.getGmtCreate().isBefore(now.minusMinutes(1))){
                    //订单状态为NEW或PAYING且已经超时，更新状态锁，更新状态为PAY_TIMEOUT，关闭支付渠道
                    productOrderDO.setStateLock(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    productOrderDO.setState(OrderStatusEnum.PAY_TIMEOUT.name());
                    listToUpdate.add(productOrderDO);
                    //TODO 关闭支付
                }//未超时不用修改
            }else {
                //订单状态为其它，更新状态锁
                productOrderDO.setStateLock(now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                listToUpdate.add(productOrderDO);
            }
        }
        jdbcTemplate.batchUpdate(updateSql, listToUpdate, listToUpdate.size(),
                (ps, argument) -> {
                    ps.setString(1, argument.getStateLock());
                    ps.setString(2, argument.getState());
                    ps.setLong(3, argument.getId());
                });
//        if (i == 10){
//            throw new RuntimeException("测试事务生效");
//        }
        return CompletableFuture.completedFuture("success");
    }
}
