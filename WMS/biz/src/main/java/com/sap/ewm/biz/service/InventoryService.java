package com.sap.ewm.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.dto.InventoryDTO;
import com.sap.ewm.biz.model.Inventory;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sap.ewm.core.base.FrontPage;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 庫存（物料批次） 服務類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-05
 */
public interface InventoryService extends IService<Inventory> {

    /**
     *  分頁查詢
     * @param frontPage
     * @return
     */
    IPage<Inventory> selectPage(FrontPage<Inventory> frontPage, Inventory inventory);

    List<Inventory> selectList(Inventory inventory);

    boolean save(InventoryDTO inventoryDTO);

    boolean updateById(InventoryDTO inventoryDTO);

    boolean deductionQty(String user, String inventoryBo, BigDecimal qty);

    BigDecimal summaryQtyOnHandByItem(String itemBo);

    boolean removeById(String id);

    //將資料存在Inventory資料庫
    void saveASRSInStorageData(String voucherNo, String item, String itemQty);
}