package com.nageoffer.shortlink.admin.remote.dto.resp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRespDTO {
    private Long id;

    /**
     * 商品标题
     */
    private String title;

    /**
     * 商品详情
     */
    private String detail;

    /**
     * 图片
     */
    private String img;

    /**
     * 产品层级:FIRST小杯，SECOND中杯，THIRD大杯
     */
    private String level;

    /**
     * 原价
     */
    private BigDecimal oldAmount;

    /**
     * 现价
     */
    private BigDecimal amount;

    /**
     * 每天次数
     */
    private Integer dayTimes;

    /**
     * 总次数
     */
    private Integer totalTimes;

    /**
     * 跳转统计次数
     */
    private Integer jumpStatisticsTimes;

    /**
     * 类型:1-订阅，2-流量包
     */
    private Integer type;

    /**
     * 有效天数:NULL-永久
     */
    private Integer validDays;
}