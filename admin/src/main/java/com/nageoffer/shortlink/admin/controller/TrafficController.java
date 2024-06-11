package com.nageoffer.shortlink.admin.controller;


import com.nageoffer.shortlink.admin.common.convention.result.Result;
import com.nageoffer.shortlink.admin.common.convention.result.Results;
import com.nageoffer.shortlink.admin.dto.req.TrafficPageReqDTO;
import com.nageoffer.shortlink.admin.dto.resp.PageDTO;
import com.nageoffer.shortlink.admin.dto.resp.TrafficRespDTO;
import com.nageoffer.shortlink.admin.service.ITrafficService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author tx
 * @since 2024-06-11
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/traffic")
public class TrafficController {
    private ITrafficService trafficService;

    @GetMapping("/page")
    public Result<PageDTO<TrafficRespDTO>> queryTrafficPage(TrafficPageReqDTO trafficPageReqDTO){
        return Results.success(trafficService.queryTrafficPage(trafficPageReqDTO));
    }

    @GetMapping("/detail")
    public Result<TrafficRespDTO> queryTrafficDetail(Long id){
        return Results.success(trafficService.queryTrafficDetail(id));
    }
}
