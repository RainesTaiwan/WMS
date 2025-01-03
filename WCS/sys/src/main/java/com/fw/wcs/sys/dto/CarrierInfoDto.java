package com.fw.wcs.sys.dto;

import com.alibaba.fastjson.annotation.JSONField;

public class CarrierInfoDto extends CommonDto {

    //接駁站編號
    @JSONField(name="RESOURCE")
    private String RESOURCE;
    //工位
    @JSONField(name="STATION")
    private String STATION;
    //載具ID
    @JSONField(name="CARRIER")
    private String CARRIER;
    //高度
    @JSONField(name="HEIGHT")
    private String HEIGHT;
    //寬度
    @JSONField(name="WIDTH")
    private String WIDTH;
    //長度
    @JSONField(name="LENGTH")
    private String LENGTH;
    //重量
    @JSONField(name="WEIGHT")
    private String WEIGHT;

    public String getRESOURCE() {
        return RESOURCE;
    }

    public void setRESOURCE(String RESOURCE) {
        this.RESOURCE = RESOURCE;
    }

    public String getSTATION() {
        return STATION;
    }

    public void setSTATION(String STATION) {
        this.STATION = STATION;
    }

    public String getCARRIER() {
        return CARRIER;
    }

    public void setCARRIER(String CARRIER) {
        this.CARRIER = CARRIER;
    }

    public String getHEIGHT() {
        return HEIGHT;
    }

    public void setHEIGHT(String HEIGHT) {
        this.HEIGHT = HEIGHT;
    }

    public String getWIDTH() {
        return WIDTH;
    }

    public void setWIDTH(String WIDTH) {
        this.WIDTH = WIDTH;
    }

    public String getLENGTH() {
        return LENGTH;
    }

    public void setLENGTH(String LENGTH) {
        this.LENGTH = LENGTH;
    }

    public String getWEIGHT() {
        return WEIGHT;
    }

    public void setWEIGHT(String WEIGHT) {
        this.WEIGHT = WEIGHT;
    }
}
