package com.sap.ewm.biz.dto;

import com.sap.ewm.biz.model.Inventory;

import java.math.BigDecimal;

/**
 * @program: ewm
 * @description: 庫存前臺互動對像
 * @author: syngna
 * @create: 2020-07-05 20:51
 */
public class CarrierBindDTO extends Inventory {

    private String batchNo;

    private String carrier;

    private String status;

    private BigDecimal qty;

    private String action;

    @Override
    public String getBatchNo() {
        return batchNo;
    }

    @Override
    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}