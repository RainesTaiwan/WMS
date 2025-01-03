package com.sap.ewm.biz.dto;

import java.math.BigDecimal;

/**
 * @program: ewm
 * @description:
 * @author: syngna
 * @create: 2020-07-05 20:51
 */
public class HandlingUnitContextDTO {

    private String handlingUnitContextGbo;

    private String status;

    private BigDecimal qty;

    public String getHandlingUnitContextGbo() {
        return handlingUnitContextGbo;
    }

    public void setHandlingUnitContextGbo(String handlingUnitContextGbo) {
        this.handlingUnitContextGbo = handlingUnitContextGbo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }
}