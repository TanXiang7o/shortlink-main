package org.tx.shortlink.shop.common.enums;

public enum BillTypeEnum {
    NO_BILL("NO_BILL","不开发票"),
    E_BILL("E_BILL","电子发票"),
    P_BILL("P_BILL","纸质发票");

    private final String code;
    private final String desc;

    BillTypeEnum(String code,String desc){
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
