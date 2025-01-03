package com.sap.ewm.biz.dto;

import com.sap.ewm.biz.model.Carrier;

/**
 * @program: ewm
 * @description: 載具前臺互動對像
 * @author: syngna
 * @create: 2020-07-05 20:51
 */
public class CarrierDTO extends Carrier {

    private String carrierType;

    public String getCarrierType() {
        return carrierType;
    }

    public void setCarrierType(String carrierType) {
        this.carrierType = carrierType;
    }
}