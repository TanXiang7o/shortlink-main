package org.tx.shortlink.shop.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.tx.shortlink.shop.DTO.req.OrderQuery;
import org.tx.shortlink.shop.DTO.req.OrderStatusReqDTO;
import org.tx.shortlink.shop.DTO.resp.PageDTO;
import org.tx.shortlink.shop.DTO.resp.ProductOrderRespDTO;
import org.tx.shortlink.shop.entity.ProductOrderDO;
import org.tx.shortlink.shop.mapper.ProductOrderMapper;
import org.tx.shortlink.shop.service.IProductOrderService;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tx
 * @since 2024-06-02
 */
@Service
public class ProductOrderServiceImpl extends ServiceImpl<ProductOrderMapper, ProductOrderDO> implements IProductOrderService {


    @Override
    public boolean create(ProductOrderDO productOrderDO) {
        return save(productOrderDO);
    }

    /**
     * 根据订单号查询订单
     */
    @Override
    public ProductOrderRespDTO getByTradeNo(String tradeNo,Long userId) {
        ProductOrderDO productOrderDO = lambdaQuery().eq(ProductOrderDO::getUserId,userId).eq(ProductOrderDO::getTradeNo, tradeNo).one();
        ProductOrderRespDTO productOrderRespDTO = new ProductOrderRespDTO();
        BeanUtils.copyProperties(productOrderDO, productOrderRespDTO);
        return productOrderRespDTO;
    }

    /**
     * 根据用户id查询订单
     */
    @Override
    public List<ProductOrderRespDTO> getByUserId(Long userId) {
        List<ProductOrderDO> productOrderDOS = lambdaQuery().eq(ProductOrderDO::getUserId, userId).list();
        return productOrderDOS.stream().map(productOrderDO -> {
            ProductOrderRespDTO productOrderRespDTO = new ProductOrderRespDTO();
            BeanUtils.copyProperties(productOrderDO, productOrderRespDTO);
            return productOrderRespDTO;
        }).toList();
    }

    /**
     * 更新订单状态
     */
    @Override
    public Boolean updateStatus(OrderStatusReqDTO orderStatusReqDTO) {
        return lambdaUpdate().eq(ProductOrderDO::getUserId,orderStatusReqDTO.getUserId()).eq(ProductOrderDO::getTradeNo, orderStatusReqDTO.getTradeNo()).eq(ProductOrderDO::getState, orderStatusReqDTO.getOldState())
                .set(ProductOrderDO::getState, orderStatusReqDTO.getNewState()).update();
    }


    @Override
    public PageDTO<ProductOrderRespDTO> queryOrdersPage(OrderQuery query) {
        Page<ProductOrderDO> page = Page.of(query.getPageNo(), query.getPageSize());
        if(query.getOrderBy() != null) {
            page.addOrder(new OrderItem(query.getOrderBy(), query.getIsAsc()));
        }else {
            page.addOrder(OrderItem.desc("gmt_create"));
        }
        //根据userid分页查询
        lambdaQuery().eq(ProductOrderDO::getUserId, query.getUserId()).page(page);
        List<ProductOrderDO> records = page.getRecords();
        if (records == null || records.size() == 0){
            return new PageDTO<>(page.getTotal(),page.getPages(), Collections.emptyList());
        }
        List<ProductOrderRespDTO> productOrderRespDTOS = BeanUtil.copyToList(records, ProductOrderRespDTO.class);

        return new PageDTO<ProductOrderRespDTO>(page.getTotal(),page.getPages(),productOrderRespDTOS);
    }


}
