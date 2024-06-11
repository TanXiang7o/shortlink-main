package com.nageoffer.shortlink.admin.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nageoffer.shortlink.admin.common.biz.user.UserContext;
import com.nageoffer.shortlink.admin.dao.entity.UserTodayTrafficDO;
import com.nageoffer.shortlink.admin.dao.mapper.UserTodayTrafficMapper;
import com.nageoffer.shortlink.admin.service.IUserTodayTrafficService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author tx
 * @since 2024-06-11
 */
@Service
public class UserTodayTrafficServiceImpl extends ServiceImpl<UserTodayTrafficMapper, UserTodayTrafficDO> implements IUserTodayTrafficService {

    @Override
    public UserTodayTrafficDO getByUsername() {
        return lambdaQuery().eq(UserTodayTrafficDO::getUsername, UserContext.getUsername()).one();
    }
}
