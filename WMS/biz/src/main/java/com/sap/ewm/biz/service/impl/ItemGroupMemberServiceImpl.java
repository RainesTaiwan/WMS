package com.sap.ewm.biz.service.impl;

import com.sap.ewm.core.base.FrontPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.model.ItemGroupMember;
import com.sap.ewm.biz.mapper.ItemGroupMemberMapper;
import com.sap.ewm.biz.service.ItemGroupMemberService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * <p>
 * 物料組&物料關聯關係 服務實現類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ItemGroupMemberServiceImpl extends ServiceImpl<ItemGroupMemberMapper, ItemGroupMember> implements ItemGroupMemberService {


    @Autowired
    private ItemGroupMemberMapper itemGroupMemberMapper;

    @Override
    public IPage<ItemGroupMember> selectPage(FrontPage<ItemGroupMember> frontPage, ItemGroupMember itemGroupMember) {
        QueryWrapper<ItemGroupMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(itemGroupMember);
        return super.page(frontPage.getPagePlus(), queryWrapper);
    }

    @Override
    public List<ItemGroupMember> selectList(ItemGroupMember itemGroupMember) {
        QueryWrapper<ItemGroupMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(itemGroupMember);
        return super.list(queryWrapper);
    }


}