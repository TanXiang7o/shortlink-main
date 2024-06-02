package org.tx.shortlink.shop.DTO.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductOrderRespDTO {
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
     * 商品快照
     */
    private String productSnapshot;

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
     * 订单状态:WAIT_PAY-待支付,PAY-支付成功,CANCEL-取消
     */
    private String state;

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
     * 支付时间
     */
    private LocalDateTime payTime;

    /**
     * 修改时间
     */
    private LocalDateTime gmtModified;

    /**
     * 创建时间
     */
    private LocalDateTime gmtCreate;

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
