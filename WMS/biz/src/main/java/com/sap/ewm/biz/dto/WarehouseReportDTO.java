package com.sap.ewm.biz.dto;

/**
 * @author Ervin Chen
 * @date 2020/7/22 21:19
 */
public class WarehouseReportDTO {

    public static final String WAREHOUSE = "WAREHOUSE";

    public static final String STORAGE_LOCATION = "STORAGE_LOCATION";

    public static final String STORAGE_BIN = "STORAGE_BIN";

    public static final String ITEM = "ITEM";

    public static final String CARRIER = "CARRIER";

    public static final String BATCH_NO = "BATCH_NO";

    private String warehouse;

    private String storageLocation;

    private String storageBin;

    private String item;

    private String carrier;

    private String batchNo;

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    public String getStorageBin() {
        return storageBin;
    }

    public void setStorageBin(String storageBin) {
        this.storageBin = storageBin;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }
}
