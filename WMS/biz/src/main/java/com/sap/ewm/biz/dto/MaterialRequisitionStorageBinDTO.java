package com.sap.ewm.biz.dto;

import java.math.BigDecimal;

/**
 * @program: ewm
 * @description: 領料數據傳輸對像
 * @author: syngna
 * @create: 2020-07-10 16:20
 */
public class MaterialRequisitionStorageBinDTO {
    /**
     * 載具id
     */
    private String carrierBo;

    /**
     * 貨格
     */
    private String storageBin;

    /**
     * 儲存地點id
     */
    private String storageLocationBo;

    /**
     * 庫存id
     */
    private String inventoryBo;

    /**
     * 批次號
     */
    private String batchNo;

    /**
     * 是否混料
     */
    private String mixed;

    /**
     * 數量
     */
    private BigDecimal qty;

    /**
     * 是否拆包
     */
    private boolean split;

    public String getCarrierBo() {
        return carrierBo;
    }

    public void setCarrierBo(String carrierBo) {
        this.carrierBo = carrierBo;
    }

    public String getStorageBin() {
        return storageBin;
    }

    public void setStorageBin(String storageBin) {
        this.storageBin = storageBin;
    }

    public String getStorageLocationBo() {
        return storageLocationBo;
    }

    public void setStorageLocationBo(String storageLocationBo) {
        this.storageLocationBo = storageLocationBo;
    }

    public String getInventoryBo() {
        return inventoryBo;
    }

    public void setInventoryBo(String inventoryBo) {
        this.inventoryBo = inventoryBo;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getMixed() {
        return mixed;
    }

    public void setMixed(String mixed) {
        this.mixed = mixed;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public boolean isSplit() {
        return split;
    }

    public void setSplit(boolean split) {
        this.split = split;
    }
}