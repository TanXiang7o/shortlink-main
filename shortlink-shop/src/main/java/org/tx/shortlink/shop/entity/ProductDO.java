package org.tx.shortlink.shop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author tx
 * @since 2024-06-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("product")
public class ProductDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商品标题
     */
    @TableField("title")
    private String title;

    /**
     * 商品详情
     */
    @TableField("detail")
    private String detail;

    /**
     * 图片
     */
    @TableField("img")
    private String img;

    /**
     * 产品层级:FIRST小杯，SECOND中杯，THIRD大杯
     */
    @TableField("level")
    private String level;

    /**
     * 原价
     */
    @TableField("old_amount")
    private BigDecimal oldAmount;

    /**
     * 现价
     */
    @TableField("amount")
    private BigDecimal amount;

    /**
     * 每天次数
     */
    @TableField("day_times")
    private Integer dayTimes;

    /**
     * 总次数
     */
    @TableField("total_times")
    private Integer totalTimes;

    /**
     * 跳转统计次数
     */
    @TableField("jump_statistics_times")
    private Integer jumpStatisticsTimes;

    /**
     * 类型:1-订阅，2-流量包
     */
    @TableField("type")
    private Integer type;

    /**
     * 有效天数:NULL-永久
     */
    @TableField("valid_days")
    private Integer validDays;

    /**
     * 修改时间
     */
    @TableField("gmt_modified")
    private LocalDateTime gmtModified;

    /**
     * 创建时间
     */
    @TableField("gmt_create")
    private LocalDateTime gmtCreate;
}
