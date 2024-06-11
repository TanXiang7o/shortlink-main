package com.nageoffer.shortlink.admin.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author tx
 * @since 2024-06-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_user_today_traffic")
public class UserTodayTrafficDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 总次数限制
     */
    private Long todayAllTimes;

    /**
     * 剩余次数
     */
    private Long todayRemainTimes;

    /**
     * 修改时间
     */
    private LocalDateTime gmtModified;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

    /**
     * 最近过期时间
     */
    private LocalDateTime nextExpirationTime;


}
