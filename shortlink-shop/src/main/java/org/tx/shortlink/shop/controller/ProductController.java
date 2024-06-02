package org.tx.shortlink.shop.controller;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tx.shortlink.shop.DTO.resp.ProductRespDTO;
import org.tx.shortlink.shop.common.convention.result.Result;
import org.tx.shortlink.shop.common.convention.result.Results;
import org.tx.shortlink.shop.entity.ProductDO;
import org.tx.shortlink.shop.service.IProductService;

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
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private IProductService productService;

    @GetMapping("/list")
    public Result<List<ProductRespDTO>> list(){
        List<ProductDO> productDOS = productService.list();
        List<ProductRespDTO> productRespDTOS = productDOS.stream().map(productDO -> {
            ProductRespDTO productRespDTO = new ProductRespDTO();
            BeanUtils.copyProperties(productDO, productRespDTO);
            return productRespDTO;
        }).toList();
        return Results.success(productRespDTOS);
    }

    @GetMapping("/detail/{id}")
    public Result<ProductRespDTO> getDetailById(@PathVariable("id")Long id){
        return Results.success(productService.getDetailById(id));
    }
}
