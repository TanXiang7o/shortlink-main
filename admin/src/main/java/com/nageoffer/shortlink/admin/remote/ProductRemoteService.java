package com.nageoffer.shortlink.admin.remote;


import com.nageoffer.shortlink.admin.common.convention.result.Result;
import com.nageoffer.shortlink.admin.remote.dto.resp.ProductRespDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "short-link-shop")
public interface ProductRemoteService {
    @GetMapping("/product/detail/{id}")
    Result<ProductRespDTO> getDetailById(@PathVariable("id")Long id);

    @GetMapping("/product/list")
    Result<List<ProductRespDTO>> list();
}
