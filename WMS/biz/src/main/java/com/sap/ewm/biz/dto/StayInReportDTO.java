package com.sap.ewm.biz.dto;

/**
 * @author Ervin Chen
 * @date 2020/7/22 21:19
 */
public class StayInReportDTO {

    public static final String ITEM = "ITEM";

    public static final String CARRIER = "CARRIER";

    public static final String BATCH_NO = "BATCH_NO";

    private String item;

    private String carrier;

    private String batchNo;

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
