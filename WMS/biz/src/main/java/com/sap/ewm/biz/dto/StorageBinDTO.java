package com.sap.ewm.biz.dto;

import com.sap.ewm.biz.model.StorageBin;

/**
 * @program: ewm
 * @description:
 * @author: syngna
 * @create: 2020-07-30 14:19
 */
public class StorageBinDTO extends StorageBin {

    private String storageBinType;

    private String storageLocation;

    public String getStorageBinType() {
        return storageBinType;
    }

    public void setStorageBinType(String storageBinType) {
        this.storageBinType = storageBinType;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }
}