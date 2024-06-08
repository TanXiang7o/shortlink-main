package org.tx.shortlink.shop.controller;


import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.tx.shortlink.shop.DTO.req.ConfirmOrderReqDTO;
import org.tx.shortlink.shop.DTO.req.OrderQuery;
import org.tx.shortlink.shop.DTO.req.OrderStatusReqDTO;
import org.tx.shortlink.shop.DTO.resp.PageDTO;
import org.tx.shortlink.shop.DTO.resp.ProductOrderRespDTO;
import org.tx.shortlink.shop.annotation.RepeatSubmit;
import org.tx.shortlink.shop.common.config.SnowflakeDistributeId;
import org.tx.shortlink.shop.common.convention.result.Result;
import org.tx.shortlink.shop.common.convention.result.Results;
import org.tx.shortlink.shop.common.enums.BillTypeEnum;
import org.tx.shortlink.shop.common.enums.OrderStatusEnum;
import org.tx.shortlink.shop.common.enums.PayTypeEnum;
import org.tx.shortlink.shop.entity.ProductDO;
import org.tx.shortlink.shop.entity.ProductOrderDO;
import org.tx.shortlink.shop.service.IProductOrderService;
import org.tx.shortlink.shop.service.IProductService;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tx
 * @since 2024-06-02
 */
@RestController
@RequestMapping("/product-order")
@Slf4j
public class ProductOrderController {
    @Autowired
    private IProductOrderService productOrderService;
    @Autowired
    private IProductService productService;
    @Autowired
    private SnowflakeDistributeId snowflakeDistributeId;

    @PostMapping("/create")
    public Result<String> create(@RequestBody ConfirmOrderReqDTO confirmOrderReqDTO){
        //防重提交
        ProductDO productDO = productService.getById(confirmOrderReqDTO.getProductId());
        String tradeNo = String.valueOf(snowflakeDistributeId.nextId());
        //获取最新的价格，验证价格
        //后端计算价格
        BigDecimal bizTotal = BigDecimal.valueOf(confirmOrderReqDTO.getBuyNum()).multiply(productDO.getAmount());

        //前端传递总价和后端计算总价格是否一致, 如果有优惠券，也在这里进行计算
        if( bizTotal.compareTo(confirmOrderReqDTO.getPayAmount()) !=0 ){
            log.error("验证价格失败{}",confirmOrderReqDTO);
            throw new RuntimeException("验证订单价格失败");
        }
        //创建订单
        ProductOrderDO productOrderDO = ProductOrderDO.builder()
                .productId(confirmOrderReqDTO.getProductId())
                .productTitle(confirmOrderReqDTO.getProductTitle())
                .productSnapshot(JSONUtil.toJsonStr(productDO))
                .productAmount(productDO.getAmount())
                .userId(confirmOrderReqDTO.getUserId())
                .buyNum(confirmOrderReqDTO.getBuyNum())
                .state(OrderStatusEnum.NEW.name())
                .tradeNo(tradeNo)
                .payAmount(confirmOrderReqDTO.getPayAmount())
                .totalAmount(confirmOrderReqDTO.getTotalAmount())
                .payType(PayTypeEnum.ALIPAY.getCode())
                .billType(BillTypeEnum.NO_BILL.getCode())
                .billHeader(confirmOrderReqDTO.getBillHeader())
                .billReceiverPhone(confirmOrderReqDTO.getBillReceiverPhone())
                .billReceiverEmail(confirmOrderReqDTO.getBillReceiverEmail())
                .billContent(confirmOrderReqDTO.getBillContent())
                .build();

        boolean saved = productOrderService.save(productOrderDO);
        return Results.success(tradeNo);
    }

    @GetMapping("/token")
    public Result<String> getToken(Long userId){
        return Results.success(productOrderService.getToken(userId));
    }

    @GetMapping("/detail/trade")
    @RepeatSubmit(limitType = RepeatSubmit.Type.TOKEN)
    public Result<ProductOrderRespDTO> getByTradeNo(String tradeNo, Long userId){
        return Results.success(productOrderService.getByTradeNo(tradeNo,userId));
    }

    @GetMapping("/list")
    public Result<List<ProductOrderRespDTO>> getByUserId(@RequestParam Long userId){
        return Results.success(productOrderService.getByUserId(userId));
    }

    @PutMapping("/order/update")
    public Result<Void> updateStatus(@RequestBody OrderStatusReqDTO orderStatusReqDTO){
        Boolean updateStatus = productOrderService.updateStatus(orderStatusReqDTO);
        if (updateStatus){
            return Results.success();
        }else{
            return Results.failure();
        }
    }

    @GetMapping("/order/page")
    public Result<PageDTO<ProductOrderRespDTO>> queryOrdersPage(OrderQuery query){
        return Results.success(productOrderService.queryOrdersPage(query));
    }

    @GetMapping("/order/state")
    public Result<String> queryState(String tradeNo, Long userId){
        return Results.success(productOrderService.getByTradeNo(tradeNo,userId).getState());
    }

}
