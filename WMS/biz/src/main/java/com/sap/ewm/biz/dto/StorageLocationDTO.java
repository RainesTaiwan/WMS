package com.sap.ewm.biz.dto;

import com.sap.ewm.biz.model.StorageLocation;

import java.math.BigDecimal;

/**
 * @program: ewm
 * @description: 儲存地點數據傳輸對像
 * @author: syngna
 * @create: 2020-07-13 14:51
 */
public class StorageLocationDTO extends StorageLocation {

    private Integer storageBinCount;

    private Integer usedBinCount;

    private Integer usableCount;

    private BigDecimal usedProportion;

    private String storageLocationType;

    private String warehouse;

    public Integer getStorageBinCount() {
        return storageBinCount;
    }

    public void setStorageBinCount(Integer storageBinCount) {
        this.storageBinCount = storageBinCount;
    }

    public Integer getUsedBinCount() {
        return usedBinCount;
    }

    public void setUsedBinCount(Integer usedBinCount) {
        this.usedBinCount = usedBinCount;
    }

    public Integer getUsableCount() {
        return usableCount;
    }

    public void setUsableCount(Integer usableCount) {
        this.usableCount = usableCount;
    }

    public BigDecimal getUsedProportion() {
        return usedProportion;
    }

    public void setUsedProportion(BigDecimal usedProportion) {
        this.usedProportion = usedProportion;
    }

    public String getStorageLocationType() {
        return storageLocationType;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public void setStorageLocationType(String storageLocationType) {
        this.storageLocationType = storageLocationType;
    }
}