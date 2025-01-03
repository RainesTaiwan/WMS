package com.sap.ewm.biz.dto;

import com.sap.ewm.biz.model.Item;

import java.util.List;

/**
 * @program: ewm
 * @description: 物料數據傳輸對像
 * @author: syngna
 * @create: 2020-07-05 20:51
 */
public class ItemDTO extends Item {

    private String itemType;

    private List<String> itemGroups;

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public List<String> getItemGroups() {
        return itemGroups;
    }

    public void setItemGroups(List<String> itemGroups) {
        this.itemGroups = itemGroups;
    }
}