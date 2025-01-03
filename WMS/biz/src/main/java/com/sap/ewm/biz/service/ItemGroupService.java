package com.sap.ewm.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.model.ItemGroup;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sap.ewm.core.base.FrontPage;

import java.util.List;

/**
 * <p>
 * 物料組主數據 服務類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
public interface ItemGroupService extends IService<ItemGroup> {

    /**
     *  分頁查詢
     * @param frontPage
     * @return
     */
    IPage<ItemGroup> selectPage(FrontPage<ItemGroup> frontPage, ItemGroup itemGroup);

    List<ItemGroup> selectList(ItemGroup itemGroup);
}