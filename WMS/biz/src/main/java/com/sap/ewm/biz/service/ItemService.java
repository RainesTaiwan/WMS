package com.sap.ewm.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.dto.ItemDTO;
import com.sap.ewm.biz.model.Item;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sap.ewm.core.base.FrontPage;

import java.util.List;

/**
 * <p>
 * 物料主數據 服務類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
public interface ItemService extends IService<Item> {

    /**
     *  分頁查詢
     * @param frontPage
     * @return
     */
    IPage<Item> selectPage(FrontPage<Item> frontPage, Item item);

    List<Item> selectList(Item item);

    boolean save(ItemDTO item);

    boolean updateById(ItemDTO item);

    // 確認是否有該物料，沒有則自動新增
    void itemVerify(String itemCode, String itemType);
    // 確認是否有該物料，有則回報true，沒有則回報false
    boolean itemExist(String itemCode);
    // 取得物料包裝資訊
    String itemContainer(String itemCode);
}