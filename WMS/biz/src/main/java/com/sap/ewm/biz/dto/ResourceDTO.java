package com.sap.ewm.biz.dto;

import com.sap.ewm.biz.model.Resrce;

import java.util.List;

/**
 * @program: ewm
 * @description: 資源數據傳輸對像
 * @author: syngna
 * @create: 2020-07-05 20:51
 */
public class ResourceDTO extends Resrce {

    private List<String> resourceTypes;

    public List<String> getResourceTypes() {
        return resourceTypes;
    }

    public void setResourceTypes(List<String> resourceTypes) {
        this.resourceTypes = resourceTypes;
    }
}