package com.sap.ewm.biz.constants;

/**
 * Created by Syngna on 2020/3/12.
 */
public enum StatusEnum {
    INVENTORY_AVAILABLE("AVAILABLE"),
    INVENTORY_FROZEN("FROZEN"),
    INVENTORY_RESTRICT("RESTRICT");

    private String code;

    StatusEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}