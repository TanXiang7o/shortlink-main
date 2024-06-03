package org.tx.shortlink.shop;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.tx.shortlink.shop.common.config.SnowflakeDistributeId;
import org.tx.shortlink.shop.common.enums.OrderStatusEnum;
import org.tx.shortlink.shop.entity.ProductOrderDO;
import org.tx.shortlink.shop.service.IProductOrderService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@SpringBootTest
public class ProductOrderTest {

    @Autowired
    private SnowflakeDistributeId snowflakeDistributeId;
    @Autowired
    private IProductOrderService productOrderService;;
    @Test
    public void testcreate(){
        ProductOrderDO productOrderDO = ProductOrderDO.builder()
                .tradeNo(String.valueOf(snowflakeDistributeId.nextId()))
                .payAmount(new BigDecimal(50))
                .state(OrderStatusEnum.NEW.name())
                .userId(10L)
                .productId(2L)
                .payType("ALIPAY")
                .payTime(LocalDateTime.now()).build();
        productOrderService.create(productOrderDO);
    }


}
