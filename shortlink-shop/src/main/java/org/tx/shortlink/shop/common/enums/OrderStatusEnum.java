package org.tx.shortlink.shop.common.enums;

/**
 * 订单状态枚举类
 * 用于表示订单的各种状态，覆盖了从创建到最终处理完成的整个过程中的所有可能状态。
 */
public enum OrderStatusEnum {
    // 订单创建成功，等待支付
    NEW,
    // 支付过程中
    PAYING,
    // 支付成功
    PAY_SUCCESS,
    // 支付失败
    PAY_FAIL,
    // 支付超时
    PAY_TIMEOUT,
    // 订单取消
    CANCEL,
    // 退款申请
    REFUND,
    // 退款成功
    REFUND_SUCCESS,
    // 退款失败
    REFUND_FAIL,
    // 退款超时
    REFUND_TIMEOUT,
    // 退款取消
    REFUND_CANCEL,
    // 退款取消成功
    REFUND_CANCEL_SUCCESS,
    // 退款取消失败
    REFUND_CANCEL_FAIL,
    // 退款取消超时
    REFUND_CANCEL_TIMEOUT,
    // 退款取消取消（可能由于系统错误或其他原因）
    REFUND_CANCEL_CANCEL,
    // 退款取消取消成功
    REFUND_CANCEL_CANCEL_SUCCESS,
}
