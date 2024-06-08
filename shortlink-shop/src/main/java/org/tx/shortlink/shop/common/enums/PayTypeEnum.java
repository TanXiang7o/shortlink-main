package org.tx.shortlink.shop.common.enums;

public enum PayTypeEnum {
    ALIPAY("ALIPAY", "支付宝"),
    WECHAT("WECHAT", "微信"),
    ;

    private final String code;
    private final String desc;

    PayTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
