package com.sap.ewm.biz.controller;

import com.sap.ewm.core.base.AjaxResult;
import com.sap.ewm.core.base.FrontPage;
import com.sap.ewm.core.security.annotation.Secured;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.model.ItemGroupMember;
import com.sap.ewm.biz.service.ItemGroupMemberService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * @author Syngna
 * @since 2020-07-06
 */
@RestController
@RequestMapping("/itemGroupMember")
public class ItemGroupMemberController {

    @Autowired
    public ItemGroupMemberService itemGroupMemberService;

    /**
    * 根據id查詢
    *
    * @param id 主鍵
    * @return
    */
    @ResponseBody
    @GetMapping("/{id:.+}")
    public AjaxResult getItemGroupMemberById(@PathVariable String id) {
        return AjaxResult.success( itemGroupMemberService.getById(id));
    }

    /**
     * 查詢所有數據
     *
     * @return
     */
    @ResponseBody
    @GetMapping("")
    public AjaxResult getItemGroupMemberList(ItemGroupMember itemGroupMember){
        List<ItemGroupMember> result;
        QueryWrapper<ItemGroupMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(itemGroupMember);
        result = itemGroupMemberService.list(queryWrapper);
        return AjaxResult.success(result);
    }

    /**
     * 分頁查詢數據
     *
     * @param frontPage  分頁資訊
     * @return
     */
    @ResponseBody
    @GetMapping("/page")
    public AjaxResult page(FrontPage<ItemGroupMember> frontPage, ItemGroupMember itemGroupMember){
        IPage result;
        QueryWrapper<ItemGroupMember> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(itemGroupMember);
        if (frontPage.getGlobalQuery() != null && !"".equals(frontPage.getGlobalQuery().trim())) {
            //TODO modify global query
            queryWrapper.lambda().and(wrapper -> wrapper
                .like(ItemGroupMember::getHandle, frontPage.getGlobalQuery())
                .or().like(ItemGroupMember::getItemGroupBo, frontPage.getGlobalQuery())
                .or().like(ItemGroupMember::getItemBo, frontPage.getGlobalQuery())
    );
        }
        result = itemGroupMemberService.page(frontPage.getPagePlus(), queryWrapper);
        return AjaxResult.success(result);
    }

    /**
     * 新增
     * @param itemGroupMember  傳遞的實體
     * @return  null 失敗  實體成功
     */
    @PostMapping
    @Secured("itemGroupMember_add")
    public AjaxResult save(@RequestBody ItemGroupMember itemGroupMember) {
        return AjaxResult.success(itemGroupMemberService.save(itemGroupMember));
    }

    /**
     * 修改
     * @param itemGroupMember  傳遞的實體
     * @return  null 失敗  實體成功
     */
    @PutMapping
    @Secured("itemGroupMember_edit")
    public AjaxResult updateById(@RequestBody ItemGroupMember itemGroupMember) {
        return AjaxResult.success(itemGroupMemberService.updateById(itemGroupMember));
    }

    /**
     * 根據id刪除對像
     * @param id  實體ID
     * @return 0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id:.+}")
    @Secured("itemGroupMember_delete")
    public AjaxResult removeById(@PathVariable("id") String id){
        return AjaxResult.success(itemGroupMemberService.removeById(id));
    }

    /**
     * 批量刪除對像
     * @param ids 實體集合ID
     * @return  0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/_batchDelete")
    @Secured("itemGroupMember_batch_delete")
    public AjaxResult removeByIds(List<String> ids){
        return AjaxResult.success(itemGroupMemberService.removeByIds(ids));
    }
}