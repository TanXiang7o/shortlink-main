/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nageoffer.shortlink.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.nageoffer.shortlink.admin.common.biz.user.UserContext;
import com.nageoffer.shortlink.admin.common.convention.result.Result;
import com.nageoffer.shortlink.admin.common.convention.result.Results;
import com.nageoffer.shortlink.admin.dao.entity.TrafficDO;
import com.nageoffer.shortlink.admin.dao.entity.UserTodayTrafficDO;
import com.nageoffer.shortlink.admin.remote.ShortLinkActualRemoteService;
import com.nageoffer.shortlink.admin.remote.dto.req.ShortLinkBatchCreateReqDTO;
import com.nageoffer.shortlink.admin.remote.dto.req.ShortLinkCreateReqDTO;
import com.nageoffer.shortlink.admin.remote.dto.req.ShortLinkPageReqDTO;
import com.nageoffer.shortlink.admin.remote.dto.req.ShortLinkUpdateReqDTO;
import com.nageoffer.shortlink.admin.remote.dto.resp.ShortLinkBaseInfoRespDTO;
import com.nageoffer.shortlink.admin.remote.dto.resp.ShortLinkBatchCreateRespDTO;
import com.nageoffer.shortlink.admin.remote.dto.resp.ShortLinkCreateRespDTO;
import com.nageoffer.shortlink.admin.remote.dto.resp.ShortLinkPageRespDTO;
import com.nageoffer.shortlink.admin.service.ITrafficService;
import com.nageoffer.shortlink.admin.service.IUserTodayTrafficService;
import com.nageoffer.shortlink.admin.toolkit.EasyExcelWebUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 短链接后管控制层
 * 公众号：马丁玩编程，回复：加群，添加马哥微信（备注：link）获取项目资料
 */
@RestController(value = "shortLinkControllerByAdmin")
@RequiredArgsConstructor
public class ShortLinkController {

    private final ShortLinkActualRemoteService shortLinkActualRemoteService;

    private final IUserTodayTrafficService userTodayTrafficService;

    private final ITrafficService trafficService;

    /**
     * 创建短链接
     */
    @PostMapping("/api/short-link/admin/v1/create")
    @Transactional(rollbackFor = Exception.class)
    public Result<ShortLinkCreateRespDTO> createShortLink(@RequestBody ShortLinkCreateReqDTO requestParam) {
        //验证是否有资源次数
        UserTodayTrafficDO userTodayTrafficDO = userTodayTrafficService.getByUsername();
        if (userTodayTrafficDO == null){
            userTodayTrafficDO = redoTodayTraffic();
        } else if (!userTodayTrafficDO.getGmtModified().toLocalDate().equals(LocalDate.now())){
            //是今天第一次使用
            redoTodayTraffic(userTodayTrafficDO);
        }
        //检查是否有过期资源包
        if (userTodayTrafficDO.getNextExpirationTime()!=null &&  userTodayTrafficDO.getNextExpirationTime().isBefore(LocalDateTime.now())){
            redoTodayTraffic(userTodayTrafficDO);
        }
        if (userTodayTrafficDO.getTodayRemainTimes() <= 0){
            return Results.failure("无资源创建短链接次数");
        }
        userTodayTrafficDO.setTodayRemainTimes(userTodayTrafficDO.getTodayRemainTimes() - 1).setGmtModified(null);
        userTodayTrafficService.lambdaUpdate().eq(UserTodayTrafficDO::getUsername, UserContext.getUsername()).update(userTodayTrafficDO);
        return shortLinkActualRemoteService.createShortLink(requestParam);
    }
    @Transactional(rollbackFor = Exception.class)
    public void redoTodayTraffic(UserTodayTrafficDO userTodayTrafficDO) {
        List<TrafficDO> list = trafficService.lambdaQuery().eq(TrafficDO::getUsername, UserContext.getUsername()).list();
        long allTimes = 0;
        LocalDateTime nextExpireTime = LocalDateTime.MAX;
        for(TrafficDO trafficDO : list){
            if (trafficDO.getExpireDate() != null && trafficDO.getExpireDate().isBefore(LocalDateTime.now())){
                continue;
            }
            allTimes += trafficDO.getDayLimit();
            if (trafficDO.getExpireDate() != null && nextExpireTime.isAfter(trafficDO.getExpireDate())){
                nextExpireTime = trafficDO.getExpireDate();
            }
        }
        if (nextExpireTime == LocalDateTime.MAX) {
            nextExpireTime = null;
        }
        userTodayTrafficDO.setTodayAllTimes(allTimes)
                .setTodayRemainTimes(allTimes)
                .setNextExpirationTime(nextExpireTime)
                .setGmtModified(null);
        userTodayTrafficService.lambdaUpdate().eq(UserTodayTrafficDO::getUsername, UserContext.getUsername()).update(userTodayTrafficDO);
    }

    @Transactional(rollbackFor = Exception.class)
    public UserTodayTrafficDO redoTodayTraffic() {
        UserTodayTrafficDO userTodayTrafficDO = new UserTodayTrafficDO();
        List<TrafficDO> list = trafficService.lambdaQuery().eq(TrafficDO::getUsername, UserContext.getUsername()).list();
        long allTimes = 0;
        LocalDateTime nextExpireTime = LocalDateTime.MAX;
        for(TrafficDO trafficDO : list){
            if (trafficDO.getExpireDate() != null && trafficDO.getExpireDate().isBefore(LocalDateTime.now())){
                continue;
            }
            allTimes += trafficDO.getDayLimit();
            if (trafficDO.getExpireDate() != null && nextExpireTime.isAfter(trafficDO.getExpireDate())){
                nextExpireTime = trafficDO.getExpireDate();
            }
        }
        if (nextExpireTime == LocalDateTime.MAX) {
            nextExpireTime = null;
        }
        userTodayTrafficDO.setTodayAllTimes(allTimes)
                .setTodayRemainTimes(allTimes)
                .setNextExpirationTime(nextExpireTime)
                .setUsername(UserContext.getUsername());
        userTodayTrafficService.save(userTodayTrafficDO);
        return userTodayTrafficDO;
    }

    /**
     * 批量创建短链接
     */
    @SneakyThrows
    @PostMapping("/api/short-link/admin/v1/create/batch")
    public void batchCreateShortLink(@RequestBody ShortLinkBatchCreateReqDTO requestParam, HttpServletResponse response) {
        Result<ShortLinkBatchCreateRespDTO> shortLinkBatchCreateRespDTOResult = shortLinkActualRemoteService.batchCreateShortLink(requestParam);
        if (shortLinkBatchCreateRespDTOResult.isSuccess()) {
            List<ShortLinkBaseInfoRespDTO> baseLinkInfos = shortLinkBatchCreateRespDTOResult.getData().getBaseLinkInfos();
            EasyExcelWebUtil.write(response, "批量创建短链接-SaaS短链接系统", ShortLinkBaseInfoRespDTO.class, baseLinkInfos);
        }
    }

    /**
     * 修改短链接
     */
    @PostMapping("/api/short-link/admin/v1/update")
    public Result<Void> updateShortLink(@RequestBody ShortLinkUpdateReqDTO requestParam) {
        shortLinkActualRemoteService.updateShortLink(requestParam);
        return Results.success();
    }

    /**
     * 分页查询短链接
     */
    @GetMapping("/api/short-link/admin/v1/page")
    public Result<Page<ShortLinkPageRespDTO>> pageShortLink(ShortLinkPageReqDTO requestParam) {
        return shortLinkActualRemoteService.pageShortLink(requestParam.getGid(), requestParam.getOrderTag(), requestParam.getCurrent(), requestParam.getSize());
    }
}
