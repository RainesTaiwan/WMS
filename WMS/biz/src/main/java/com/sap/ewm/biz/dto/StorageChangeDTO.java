package com.sap.ewm.biz.dto;

/**
 * @program: ewm
 * @description:
 * @author: syngna
 * @create: 2020-07-10 16:20
 */
public class StorageChangeDTO {

    private String carrier;

    private String sourceStorageBin;

    private String targetStorageBin;

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getSourceStorageBin() {
        return sourceStorageBin;
    }

    public void setSourceStorageBin(String sourceStorageBin) {
        this.sourceStorageBin = sourceStorageBin;
    }

    public String getTargetStorageBin() {
        return targetStorageBin;
    }

    public void setTargetStorageBin(String targetStorageBin) {
        this.targetStorageBin = targetStorageBin;
    }
}