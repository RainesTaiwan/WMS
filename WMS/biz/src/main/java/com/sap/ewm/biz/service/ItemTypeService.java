package com.sap.ewm.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.model.ItemType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sap.ewm.core.base.FrontPage;

import java.util.List;

/**
 * <p>
 * 物料型別主數據 服務類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
public interface ItemTypeService extends IService<ItemType> {

    /**
     *  分頁查詢
     * @param frontPage
     * @return
     */
    IPage<ItemType> selectPage(FrontPage<ItemType> frontPage, ItemType itemType);

    List<ItemType> selectList(ItemType itemType);

    // 依據Handle取得ItemType
    ItemType findItemTypeByHandle(String handle);
}