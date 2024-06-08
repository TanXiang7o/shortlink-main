package org.tx.shortlink.shop;

import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.tx.shortlink.shop.common.config.SnowflakeDistributeId;
import org.tx.shortlink.shop.common.enums.BillTypeEnum;
import org.tx.shortlink.shop.common.enums.OrderStatusEnum;
import org.tx.shortlink.shop.common.enums.PayTypeEnum;
import org.tx.shortlink.shop.entity.ProductDO;
import org.tx.shortlink.shop.entity.ProductOrderDO;
import org.tx.shortlink.shop.service.IProductOrderService;
import org.tx.shortlink.shop.service.IProductService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class ProductOrderTest {

    @Autowired
    private SnowflakeDistributeId snowflakeDistributeId;
    @Autowired
    private IProductOrderService productOrderService;
    @Autowired
    private IProductService productService;
    @Test
    public void testcreateBatch(){
        ProductDO productDO = productService.getById(2L);
        List<ProductOrderDO> list = new ArrayList<>(100);
        for (long i = 101; i <= 102; i++){
            ProductOrderDO productOrderDO = ProductOrderDO.builder()
                    .tradeNo(String.valueOf(snowflakeDistributeId.nextId()))
                    .buyNum(1)
                    .payAmount(new BigDecimal(50))
                    .totalAmount(new BigDecimal(50))
                    .state(OrderStatusEnum.NEW.name())
                    .userId(i)
                    .productId(productDO.getId())
                    .productAmount(productDO.getAmount())
                    .productTitle(productDO.getTitle())
                    .productSnapshot(JSONUtil.toJsonStr(productDO))
                    .payType(PayTypeEnum.ALIPAY.getCode())
                    .billContent(".")
                    .billType(BillTypeEnum.NO_BILL.getCode())
                    .billHeader("重庆邮电大学")
                    .billReceiverEmail(String.format("xx%dx@gmail.com",i))
                    .billReceiverPhone(String.format("xxx%dxxx",i))
                    .build();
            list.add(productOrderDO);
            if(i%100 == 0){
                productOrderService.saveBatch(list);
                list.clear();
            }
        }
    }

    @Test
    public void testcreate(){
        ProductDO productDO = productService.getById(2L);
        Long userId = 102L;
        ProductOrderDO productOrderDO = ProductOrderDO.builder()
                .tradeNo(String.valueOf(snowflakeDistributeId.nextId()))
                .buyNum(1)
                .payAmount(new BigDecimal(50))
                .totalAmount(new BigDecimal(50))
                .state(OrderStatusEnum.NEW.name())
                .userId(userId)
                .productId(productDO.getId())
                .productAmount(productDO.getAmount())
                .productTitle(productDO.getTitle())
                .productSnapshot(JSONUtil.toJsonStr(productDO))
                .payType(PayTypeEnum.ALIPAY.getCode())
                .billContent(".")
                .billType(BillTypeEnum.NO_BILL.getCode())
                .billHeader("重庆邮电大学")
                .billReceiverEmail(String.format("xx%dx@gmail.com",userId))
                .billReceiverPhone(String.format("xxx%dxxx",userId))
                .build();
        productOrderService.save(productOrderDO);

    }
}
