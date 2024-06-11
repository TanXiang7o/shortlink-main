package com.nageoffer.shortlink.admin.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.nageoffer.shortlink.admin.dao.entity.TrafficDO;
import com.nageoffer.shortlink.admin.dto.req.TrafficPageReqDTO;
import com.nageoffer.shortlink.admin.dto.resp.PageDTO;
import com.nageoffer.shortlink.admin.dto.resp.TrafficRespDTO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tx
 * @since 2024-06-11
 */
public interface ITrafficService extends IService<TrafficDO> {

    PageDTO<TrafficRespDTO> queryTrafficPage(TrafficPageReqDTO trafficPageReqDTO);

    TrafficRespDTO queryTrafficDetail(Long id);
}
