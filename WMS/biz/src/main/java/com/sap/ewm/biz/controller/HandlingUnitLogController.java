package com.sap.ewm.biz.controller;

import com.sap.ewm.core.base.AjaxResult;
import com.sap.ewm.core.base.FrontPage;
import com.sap.ewm.core.security.annotation.Secured;
import com.sap.ewm.core.security.common.CommonMethods;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.service.HandlingUnitLogService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sap.ewm.biz.model.HandlingUnitLog;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Syngna
 * @since 2020-07-06
 */
@RestController
@RequestMapping("/handlingUnitLog")
public class HandlingUnitLogController {

    @Autowired
    public HandlingUnitLogService handlingUnitLogService;

    /**
    * 根據id查詢
    *
    * @param id 主鍵
    * @return
    */
    @ResponseBody
    @GetMapping("/{id:.+}")
    public AjaxResult getHandlingUnitLogById(@PathVariable String id) {
        return AjaxResult.success( handlingUnitLogService.getById(id));
    }

    /**
     * 查詢所有數據
     *
     * @return
     */
    @ResponseBody
    @GetMapping("")
    public AjaxResult getHandlingUnitLogList(HandlingUnitLog handlingUnitLog){
        List<HandlingUnitLog> result;
        QueryWrapper<HandlingUnitLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(handlingUnitLog);
        result = handlingUnitLogService.list(queryWrapper);
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
    public AjaxResult page(FrontPage<HandlingUnitLog> frontPage, HandlingUnitLog handlingUnitLog){
        IPage result;
        QueryWrapper<HandlingUnitLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(handlingUnitLog);
        if (frontPage.getGlobalQuery() != null && !"".equals(frontPage.getGlobalQuery().trim())) {
            //TODO modify global query
            queryWrapper.lambda().and(wrapper -> wrapper
                .like(HandlingUnitLog::getHandle, frontPage.getGlobalQuery())
                .or().like(HandlingUnitLog::getHandlingId, frontPage.getGlobalQuery())
                .or().like(HandlingUnitLog::getCarrierBo, frontPage.getGlobalQuery())
                .or().like(HandlingUnitLog::getStatus, frontPage.getGlobalQuery())
                .or().like(HandlingUnitLog::getInventoryBo, frontPage.getGlobalQuery())
                .or().like(HandlingUnitLog::getStorageBinBo, frontPage.getGlobalQuery())
                .or().like(HandlingUnitLog::getCreator, frontPage.getGlobalQuery())
    );
        }
        result = handlingUnitLogService.page(frontPage.getPagePlus(), queryWrapper);
        return AjaxResult.success(result);
    }

    /**
     * 新增
     * @param handlingUnitLog  傳遞的實體
     * @return  null 失敗  實體成功
     */
    @PostMapping
    @Secured("handlingUnitLog_add")
    public AjaxResult save(@RequestBody HandlingUnitLog handlingUnitLog) {
        String user = CommonMethods.getUser();
        LocalDateTime now = LocalDateTime.now();
        handlingUnitLog.setCreator(user);
        handlingUnitLog.setCreateTime(now);
        return AjaxResult.success(handlingUnitLogService.save(handlingUnitLog));
    }

    /**
     * 修改
     * @param handlingUnitLog  傳遞的實體
     * @return  null 失敗  實體成功
     */
    @PutMapping
    @Secured("handlingUnitLog_edit")
    public AjaxResult updateById(@RequestBody HandlingUnitLog handlingUnitLog) {
        return AjaxResult.success(handlingUnitLogService.updateById(handlingUnitLog));
    }

    /**
     * 根據id刪除對像
     * @param id  實體ID
     * @return 0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id:.+}")
    @Secured("handlingUnitLog_delete")
    public AjaxResult removeById(@PathVariable("id") String id){
        return AjaxResult.success(handlingUnitLogService.removeById(id));
    }

    /**
     * 批量刪除對像
     * @param ids 實體集合ID
     * @return  0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/_batchDelete")
    @Secured("handlingUnitLog_batch_delete")
    public AjaxResult removeByIds(List<String> ids){
        return AjaxResult.success(handlingUnitLogService.removeByIds(ids));
    }
}