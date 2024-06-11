package com.nageoffer.shortlink.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nageoffer.shortlink.admin.common.biz.user.UserContext;
import com.nageoffer.shortlink.admin.dao.entity.TrafficDO;
import com.nageoffer.shortlink.admin.dao.mapper.TrafficMapper;
import com.nageoffer.shortlink.admin.dto.req.TrafficPageReqDTO;
import com.nageoffer.shortlink.admin.dto.resp.PageDTO;
import com.nageoffer.shortlink.admin.dto.resp.TrafficRespDTO;
import com.nageoffer.shortlink.admin.service.ITrafficService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tx
 * @since 2024-06-11
 */
@Service
public class TrafficServiceImpl extends ServiceImpl<TrafficMapper, TrafficDO> implements ITrafficService {

    @Override
    public PageDTO<TrafficRespDTO> queryTrafficPage(TrafficPageReqDTO trafficPageReqDTO) {
        Page<TrafficDO> page = Page.of(trafficPageReqDTO.getPageNo(), trafficPageReqDTO.getPageSize());
        if (trafficPageReqDTO.getOrderBy()!=null){
            page.addOrder(new OrderItem(trafficPageReqDTO.getOrderBy(),trafficPageReqDTO.getIsAsc()));
        }else {
            page.addOrder(OrderItem.desc("gmt_create"));
        }
        long username = Long.parseLong(UserContext.getUsername());
        lambdaQuery().eq(TrafficDO::getUsername,username).page(page);
        List<TrafficDO> records = page.getRecords();
        if (records == null || records.isEmpty()){
            return new PageDTO<>(page.getTotal(),page.getPages(), Collections.emptyList());
        }
        List<TrafficRespDTO> trafficRespDTOS = BeanUtil.copyToList(records, TrafficRespDTO.class);
        return new PageDTO<>(page.getTotal(),page.getPages(), trafficRespDTOS);
    }

    @Override
    public TrafficRespDTO queryTrafficDetail(Long id) {
        TrafficDO trafficDO = getById(id);
        return BeanUtil.copyProperties(trafficDO, TrafficRespDTO.class);
    }
}
