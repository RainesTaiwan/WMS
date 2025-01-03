package com.sap.ewm.biz.dto;

import java.math.BigDecimal;

/**
 * @program: ewm
 * @description: 領料數據傳輸對像
 * @author: syngna
 * @create: 2020-07-10 16:20
 */
public class MaterialRequisitionDTO {
    /**
     * 物料編號
     */
    private String item;
    /**
     * 數量
     */
    private BigDecimal qty;

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }
}