package org.tx.shortlink.shop.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.tx.shortlink.shop.DTO.resp.ProductOrderRespDTO;
import org.tx.shortlink.shop.common.convention.result.Result;
import org.tx.shortlink.shop.common.convention.result.Results;
import org.tx.shortlink.shop.entity.ProductOrderDO;
import org.tx.shortlink.shop.service.IProductOrderService;

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
public class ProductOrderController {
    @Autowired
    private IProductOrderService productOrderService;

    @PostMapping("/create")
    public Result<Void> create(@RequestBody ProductOrderDO productOrderDO){
        boolean saved = productOrderService.save(productOrderDO);
        if (saved)return Results.success();
        else{
            return Results.failure();
        }
    }

    @GetMapping("/detail/{tradeNo}")
    public Result<ProductOrderRespDTO> getByTradeNo(@PathVariable String tradeNo){
        return Results.success(productOrderService.getByTradeNo(tradeNo));
    }

    @GetMapping("/list")
    public Result<List<ProductOrderRespDTO>> getByUserId(@RequestParam Long userId){
        return Results.success(productOrderService.getByUserId(userId));
    }

}
