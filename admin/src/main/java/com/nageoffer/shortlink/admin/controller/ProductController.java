package com.nageoffer.shortlink.admin.controller;

import com.nageoffer.shortlink.admin.common.convention.result.Result;
import com.nageoffer.shortlink.admin.remote.ProductRemoteService;
import com.nageoffer.shortlink.admin.remote.dto.resp.ProductRespDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController(value = "ProductControllerByAdmin")
@RequiredArgsConstructor
public class ProductController {
    private final ProductRemoteService productRemoteService;

    @GetMapping("/admin/product/detail/{id}")
    public Result<ProductRespDTO> getDetailById(@PathVariable("id")Long id){
        return productRemoteService.getDetailById(id);
    }

    @GetMapping("/admin/product/list")
    public Result<List<ProductRespDTO>> list(){
        return productRemoteService.list();
    }
}
