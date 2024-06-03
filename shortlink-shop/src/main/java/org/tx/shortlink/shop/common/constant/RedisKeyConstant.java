package org.tx.shortlink.shop.common.constant;

public class RedisKeyConstant {

    /**
     * token防重提交key
     */
    public static final String SUBMIT_ORDER_TOKEN_KEY = "order:submit:%s:%s";

    public static final String LOCK_ORDER_SUBMIT_KEY = "order:submit:lock:%s:%s";

}
