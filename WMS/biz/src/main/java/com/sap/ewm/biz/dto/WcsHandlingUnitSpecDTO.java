package com.sap.ewm.biz.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * @program: ewm
 * @description: WCS處理單元規格資訊數據傳輸對像
 * @author: syngna
 * @create: 2020-07-10 16:20
 */
public class WcsHandlingUnitSpecDTO {
    /**
     * 載具編號
     */
    private String carrier;
    /**
     * 寬度
     */
    private BigDecimal width;
    /**
     * 高度
     */
    private BigDecimal height;
    /**
     * 長度
     */
    private BigDecimal length;
    /**
     * 重量
     */
    private BigDecimal weight;

    /**
     * 已被排程佔用的貨架列表
     */
    private List<String> busyStorageLocationList;

    public WcsHandlingUnitSpecDTO() {

    }

    public WcsHandlingUnitSpecDTO(String carrier, BigDecimal width, BigDecimal height, BigDecimal length, BigDecimal weight, List<String> busyStorageLocationList) {
        this.carrier = carrier;
        this.width = width;
        this.height = height;
        this.length = length;
        this.weight = weight;
        this.busyStorageLocationList = busyStorageLocationList;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public BigDecimal getWidth() {
        return width;
    }

    public void setWidth(BigDecimal width) {
        this.width = width;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public BigDecimal getLength() {
        return length;
    }

    public void setLength(BigDecimal length) {
        this.length = length;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public List<String> getBusyStorageLocationList() {
        return busyStorageLocationList;
    }

    public void setBusyStorageLocationList(List<String> busyStorageLocationList) {
        this.busyStorageLocationList = busyStorageLocationList;
    }
}