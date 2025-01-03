package com.sap.ewm.biz.controller;

import com.sap.ewm.core.base.AjaxResult;
import com.sap.ewm.core.base.FrontPage;
import com.sap.ewm.core.security.annotation.Secured;
import com.sap.ewm.core.security.common.CommonMethods;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.model.HandlingUnitSpec;
import com.sap.ewm.biz.service.HandlingUnitSpecService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Syngna
 * @since 2020-07-06
 */
@RestController
@RequestMapping("/handlingUnitSpec")
public class HandlingUnitSpecController {

    @Autowired
    public HandlingUnitSpecService handlingUnitSpecService;

    /**
    * 根據id查詢
    *
    * @param id 主鍵
    * @return
    */
    @ResponseBody
    @GetMapping("/{id:.+}")
    public AjaxResult getHandlingUnitSpecById(@PathVariable String id) {
        return AjaxResult.success( handlingUnitSpecService.getById(id));
    }

    /**
     * 查詢所有數據
     *
     * @return
     */
    @ResponseBody
    @GetMapping("")
    public AjaxResult getHandlingUnitSpecList(HandlingUnitSpec handlingUnitSpec){
        List<HandlingUnitSpec> result;
        QueryWrapper<HandlingUnitSpec> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(handlingUnitSpec);
        result = handlingUnitSpecService.list(queryWrapper);
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
    public AjaxResult page(FrontPage<HandlingUnitSpec> frontPage, HandlingUnitSpec handlingUnitSpec){
        IPage result;
        QueryWrapper<HandlingUnitSpec> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(handlingUnitSpec);
        if (frontPage.getGlobalQuery() != null && !"".equals(frontPage.getGlobalQuery().trim())) {
            //TODO modify global query
            queryWrapper.lambda().and(wrapper -> wrapper
                .like(HandlingUnitSpec::getHandle, frontPage.getGlobalQuery())
                .or().like(HandlingUnitSpec::getCreator, frontPage.getGlobalQuery())
    );
        }
        result = handlingUnitSpecService.page(frontPage.getPagePlus(), queryWrapper);
        return AjaxResult.success(result);
    }

    /**
     * 新增
     * @param handlingUnitSpec  傳遞的實體
     * @return  null 失敗  實體成功
     */
    @PostMapping
    @Secured("handlingUnitSpec_add")
    public AjaxResult save(@RequestBody HandlingUnitSpec handlingUnitSpec) {
        String user = CommonMethods.getUser();
        LocalDateTime now = LocalDateTime.now();
        handlingUnitSpec.setCreator(user);
        handlingUnitSpec.setCreateTime(now);
        return AjaxResult.success(handlingUnitSpecService.save(handlingUnitSpec));
    }

    /**
     * 修改
     * @param handlingUnitSpec  傳遞的實體
     * @return  null 失敗  實體成功
     */
    @PutMapping
    @Secured("handlingUnitSpec_edit")
    public AjaxResult updateById(@RequestBody HandlingUnitSpec handlingUnitSpec) {
        return AjaxResult.success(handlingUnitSpecService.updateById(handlingUnitSpec));
    }

    /**
     * 根據id刪除對像
     * @param id  實體ID
     * @return 0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id:.+}")
    @Secured("handlingUnitSpec_delete")
    public AjaxResult removeById(@PathVariable("id") String id){
        return AjaxResult.success(handlingUnitSpecService.removeById(id));
    }

    /**
     * 批量刪除對像
     * @param ids 實體集合ID
     * @return  0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/_batchDelete")
    @Secured("handlingUnitSpec_batch_delete")
    public AjaxResult removeByIds(List<String> ids){
        return AjaxResult.success(handlingUnitSpecService.removeByIds(ids));
    }
}