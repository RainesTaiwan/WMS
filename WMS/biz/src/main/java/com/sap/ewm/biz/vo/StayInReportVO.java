package com.sap.ewm.biz.vo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author Ervin Chen
 * @date 2020/7/22 17:09
 */
public class StayInReportVO {

    private String carrier;

    private String inventoryBo;

    private BigDecimal inventoryQty;

    private String item;

    private String itemDescription;

    private List<StayInReportVO> children;

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

    public List<StayInReportVO> getChildren() {
        return children;
    }

    public void setChildren(List<StayInReportVO> children) {
        this.children = children;
    }
}
