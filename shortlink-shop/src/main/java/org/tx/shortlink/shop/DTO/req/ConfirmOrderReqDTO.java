package org.tx.shortlink.shop.DTO.req;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class ConfirmOrderReqDTO {
    /**
     * 商品id
     */
    private Long productId;

    /**
     * 商品标题
     */
    private String productTitle;

    /**
     * 商品价格
     */
    private BigDecimal productAmount;

    /**
     * 购买数量
     */
    private Integer buyNum;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 订单号
     */
    private String tradeNo;


    /**
     * 订单总额
     */
    private BigDecimal totalAmount;

    /**
     * 支付金额
     */
    private BigDecimal payAmount;

    /**
     * 支付类型:ALIPAY-支付宝,WECHAT-微信
     */
    private String payType;


    /**
     * 发票类型:0->不开发票;1->电子发票;2->纸质发票
     */
    private String billType;

    /**
     * 发票抬头
     */
    private String billHeader;

    /**
     * 发票内容
     */
    private String billContent;

    /**
     * 收票人电话
     */
    private String billReceiverPhone;

    /**
     * 收票人邮箱
     */
    private String billReceiverEmail;
}
