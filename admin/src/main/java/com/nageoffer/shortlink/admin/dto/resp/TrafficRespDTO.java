package com.nageoffer.shortlink.admin.dto.resp;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TrafficRespDTO {
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
    private LocalDate expireDate;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 订单号
     */
    private String tradeNo;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;
}
