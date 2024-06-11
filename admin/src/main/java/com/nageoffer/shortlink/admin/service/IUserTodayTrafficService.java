package com.nageoffer.shortlink.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nageoffer.shortlink.admin.dao.entity.UserTodayTrafficDO;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author tx
 * @since 2024-06-11
 */
public interface IUserTodayTrafficService extends IService<UserTodayTrafficDO> {

    UserTodayTrafficDO getByUsername();
}
