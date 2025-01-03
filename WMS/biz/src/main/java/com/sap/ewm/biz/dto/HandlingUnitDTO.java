package com.sap.ewm.biz.dto;

import com.sap.ewm.biz.model.HandlingUnit;

/**
 * @program: ewm
 * @description: 庫存前臺互動對像
 * @author: syngna
 * @create: 2020-07-05 20:51
 */
public class HandlingUnitDTO extends HandlingUnit {

    private String batchNo;

    private String carrier;

    private String itemBo;

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getItemBo() {
        return itemBo;
    }

    public void setItemBo(String itemBo) {
        this.itemBo = itemBo;
    }
}