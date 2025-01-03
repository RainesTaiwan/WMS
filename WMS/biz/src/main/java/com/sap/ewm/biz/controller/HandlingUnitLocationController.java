package com.sap.ewm.biz.controller;

import com.sap.ewm.core.base.AjaxResult;
import com.sap.ewm.core.base.FrontPage;
import com.sap.ewm.core.security.annotation.Secured;
import com.sap.ewm.core.security.common.CommonMethods;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.service.HandlingUnitLocationService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sap.ewm.biz.model.HandlingUnitLocation;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Syngna
 * @since 2020-07-06
 */
@RestController
@RequestMapping("/handlingUnitLocation")
public class HandlingUnitLocationController {

    @Autowired
    public HandlingUnitLocationService handlingUnitLocationService;

    /**
    * 根據id查詢
    *
    * @param id 主鍵
    * @return
    */
    @ResponseBody
    @GetMapping("/{id:.+}")
    public AjaxResult getHandlingUnitLocationById(@PathVariable String id) {
        return AjaxResult.success( handlingUnitLocationService.getById(id));
    }

    /**
     * 查詢所有數據
     *
     * @return
     */
    @ResponseBody
    @GetMapping("")
    public AjaxResult getHandlingUnitLocationList(HandlingUnitLocation handlingUnitLocation){
        List<HandlingUnitLocation> result;
        QueryWrapper<HandlingUnitLocation> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(handlingUnitLocation);
        result = handlingUnitLocationService.list(queryWrapper);
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
    public AjaxResult page(FrontPage<HandlingUnitLocation> frontPage, HandlingUnitLocation handlingUnitLocation){
        IPage result;
        QueryWrapper<HandlingUnitLocation> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(handlingUnitLocation);
        if (frontPage.getGlobalQuery() != null && !"".equals(frontPage.getGlobalQuery().trim())) {
            //TODO modify global query
            queryWrapper.lambda().and(wrapper -> wrapper
                .like(HandlingUnitLocation::getHandle, frontPage.getGlobalQuery())
                .or().like(HandlingUnitLocation::getHandlingId, frontPage.getGlobalQuery())
                .or().like(HandlingUnitLocation::getCarrierBo, frontPage.getGlobalQuery())
                .or().like(HandlingUnitLocation::getBindContextGbo, frontPage.getGlobalQuery())
                .or().like(HandlingUnitLocation::getBindType, frontPage.getGlobalQuery())
                .or().like(HandlingUnitLocation::getCreator, frontPage.getGlobalQuery())
    );
        }
        result = handlingUnitLocationService.page(frontPage.getPagePlus(), queryWrapper);
        return AjaxResult.success(result);
    }

    /**
     * 新增
     * @param handlingUnitLocation  傳遞的實體
     * @return  null 失敗  實體成功
     */
    @PostMapping
    @Secured("handlingUnitLocation_add")
    public AjaxResult save(@RequestBody HandlingUnitLocation handlingUnitLocation) {
        String user = CommonMethods.getUser();
        LocalDateTime now = LocalDateTime.now();
        handlingUnitLocation.setCreator(user);
        handlingUnitLocation.setCreateTime(now);
        return AjaxResult.success(handlingUnitLocationService.save(handlingUnitLocation));
    }

    /**
     * 修改
     * @param handlingUnitLocation  傳遞的實體
     * @return  null 失敗  實體成功
     */
    @PutMapping
    @Secured("handlingUnitLocation_edit")
    public AjaxResult updateById(@RequestBody HandlingUnitLocation handlingUnitLocation) {
        return AjaxResult.success(handlingUnitLocationService.updateById(handlingUnitLocation));
    }

    /**
     * 根據id刪除對像
     * @param id  實體ID
     * @return 0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id:.+}")
    @Secured("handlingUnitLocation_delete")
    public AjaxResult removeById(@PathVariable("id") String id){
        return AjaxResult.success(handlingUnitLocationService.removeById(id));
    }

    /**
     * 批量刪除對像
     * @param ids 實體集合ID
     * @return  0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/_batchDelete")
    @Secured("handlingUnitLocation_batch_delete")
    public AjaxResult removeByIds(List<String> ids){
        return AjaxResult.success(handlingUnitLocationService.removeByIds(ids));
    }
}