package com.nageoffer.shortlink.admin.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
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
@Builder
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_traffic")
public class TrafficDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品id
     */
    private Long productId;

    /**
     * 商品标题
     */
    private String productTitle;

    /**
     * 每天总次数
     */
    private Integer dayLimit;

    /**
     * 过期日期
     */
    private LocalDateTime expireDate;

    /**
     * 用户名
     */
    private String username;

    /**
     * 订单号
     */
    private String tradeNo;

    /**
     * 修改时间
     */
    private LocalDateTime gmtModified;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;
}
