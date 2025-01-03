package com.sap.ewm.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.model.ItemGroupMember;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sap.ewm.core.base.FrontPage;

import java.util.List;

/**
 * <p>
 * 物料組&物料關聯關係 服務類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
public interface ItemGroupMemberService extends IService<ItemGroupMember> {

    /**
     *  分頁查詢
     * @param frontPage
     * @return
     */
    IPage<ItemGroupMember> selectPage(FrontPage<ItemGroupMember> frontPage, ItemGroupMember itemGroupMember);

    List<ItemGroupMember> selectList(ItemGroupMember itemGroupMember);
}