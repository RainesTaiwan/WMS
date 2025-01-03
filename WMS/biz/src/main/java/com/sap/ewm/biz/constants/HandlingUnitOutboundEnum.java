package com.sap.ewm.biz.constants;

/**
 * @author syngna
 * @date 2020/2/5 14:50
 */
public enum HandlingUnitOutboundEnum {

    /**
     * 領料
     */
    MATERIAL_REQUISITION("1");

    private String code;

    HandlingUnitOutboundEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }}
