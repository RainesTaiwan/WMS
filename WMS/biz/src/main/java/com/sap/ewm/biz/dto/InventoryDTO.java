package com.sap.ewm.biz.dto;

import com.sap.ewm.biz.model.Inventory;

/**
 * @program: ewm
 * @description: 庫存前臺互動對像
 * @author: syngna
 * @create: 2020-07-05 20:51
 */
public class InventoryDTO extends Inventory {

    private String item;

    private Integer validDurations;

    private String timeUnit;

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Integer getValidDurations() {
        return validDurations;
    }

    public void setValidDurations(Integer validDurations) {
        this.validDurations = validDurations;
    }

    public String getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(String timeUnit) {
        this.timeUnit = timeUnit;
    }
}