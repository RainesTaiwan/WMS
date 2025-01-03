package com.sap.ewm.biz.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Ervin Chen
 * @date 2020/7/22 17:09
 */
public class WarehouseReportVO {

    private String warehouse;

    private String storageLocation;

    private String storageLocationDescription;

    private String storageBin;

    private String storageBinDescription;

    private String status;

    private String carrier;

    private String inventoryBo;

    private BigDecimal inventoryQty;

    private String item;

    private String itemDescription;

    private String inventoryStatus;

    private List<WarehouseReportVO> children;

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

    public String getStorageLocationDescription() {
        return storageLocationDescription;
    }

    public void setStorageLocationDescription(String storageLocationDescription) {
        this.storageLocationDescription = storageLocationDescription;
    }

    public String getStorageBin() {
        return storageBin;
    }

    public void setStorageBin(String storageBin) {
        this.storageBin = storageBin;
    }

    public String getStorageBinDescription() {
        return storageBinDescription;
    }

    public void setStorageBinDescription(String storageBinDescription) {
        this.storageBinDescription = storageBinDescription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getInventoryBo() {
        return inventoryBo;
    }

    public void setInventoryBo(String inventoryBo) {
        this.inventoryBo = inventoryBo;
    }

    public BigDecimal getInventoryQty() {
        return inventoryQty;
    }

    public void setInventoryQty(BigDecimal inventoryQty) {
        this.inventoryQty = inventoryQty;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getInventoryStatus() {
        return inventoryStatus;
    }

    public void setInventoryStatus(String inventoryStatus) {
        this.inventoryStatus = inventoryStatus;
    }

    public List<WarehouseReportVO> getChildren() {
        return children;
    }

    public void setChildren(List<WarehouseReportVO> children) {
        this.children = children;
    }
}
