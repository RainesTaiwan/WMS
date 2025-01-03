package com.sap.ewm.dashboard.model;

/**
 * @program: ewm
 * @description:
 * @author: syngna
 * @create: 2020-07-05 20:51
 */
public class StorageLocationUsage {

    private String storageLocation;

    private Integer totalQty;

    private Integer usedQty;

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    public Integer getTotalQty() {
        return totalQty;
    }

    public void setTotalQty(Integer totalQty) {
        this.totalQty = totalQty;
    }

    public Integer getUsedQty() {
        return usedQty;
    }

    public void setUsedQty(Integer usedQty) {
        this.usedQty = usedQty;
    }
}