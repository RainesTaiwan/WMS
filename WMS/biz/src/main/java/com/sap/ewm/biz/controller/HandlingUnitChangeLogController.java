package com.sap.ewm.biz.controller;

import com.sap.ewm.core.base.AjaxResult;
import com.sap.ewm.core.base.FrontPage;
import com.sap.ewm.core.security.annotation.Secured;
import com.sap.ewm.core.security.common.CommonMethods;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.service.HandlingUnitChangeLogService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sap.ewm.biz.model.HandlingUnitChangeLog;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Syngna
 * @since 2020-07-06
 */
@RestController
@RequestMapping("/handlingUnitChangeLog")
public class HandlingUnitChangeLogController {

    @Autowired
    public HandlingUnitChangeLogService handlingUnitChangeLogService;

    /**
    * 根據id查詢
    *
    * @param id 主鍵
    * @return
    */
    @ResponseBody
    @GetMapping("/{id:.+}")
    public AjaxResult getHandlingUnitChangeLogById(@PathVariable String id) {
        return AjaxResult.success( handlingUnitChangeLogService.getById(id));
    }

    /**
     * 查詢所有數據
     *
     * @return
     */
    @ResponseBody
    @GetMapping("")
    public AjaxResult getHandlingUnitChangeLogList(HandlingUnitChangeLog handlingUnitChangeLog){
        List<HandlingUnitChangeLog> result;
        QueryWrapper<HandlingUnitChangeLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(handlingUnitChangeLog);
        result = handlingUnitChangeLogService.list(queryWrapper);
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
    public AjaxResult page(FrontPage<HandlingUnitChangeLog> frontPage, HandlingUnitChangeLog handlingUnitChangeLog){
        IPage result;
        QueryWrapper<HandlingUnitChangeLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(handlingUnitChangeLog);
        if (frontPage.getGlobalQuery() != null && !"".equals(frontPage.getGlobalQuery().trim())) {
            //TODO modify global query
            queryWrapper.lambda().and(wrapper -> wrapper
                .like(HandlingUnitChangeLog::getHandle, frontPage.getGlobalQuery())
                .or().like(HandlingUnitChangeLog::getHandlingId, frontPage.getGlobalQuery())
                .or().like(HandlingUnitChangeLog::getActionCode, frontPage.getGlobalQuery())
                .or().like(HandlingUnitChangeLog::getStorageBinBo, frontPage.getGlobalQuery())
                .or().like(HandlingUnitChangeLog::getCreator, frontPage.getGlobalQuery())
    );
        }
        result = handlingUnitChangeLogService.page(frontPage.getPagePlus(), queryWrapper);
        return AjaxResult.success(result);
    }

    /**
     * 新增
     * @param handlingUnitChangeLog  傳遞的實體
     * @return  null 失敗  實體成功
     */
    @PostMapping
    @Secured("handlingUnitChangeLog_add")
    public AjaxResult save(@RequestBody HandlingUnitChangeLog handlingUnitChangeLog) {
        String user = CommonMethods.getUser();
        LocalDateTime now = LocalDateTime.now();
        handlingUnitChangeLog.setCreator(user);
        handlingUnitChangeLog.setCreateTime(now);
        return AjaxResult.success(handlingUnitChangeLogService.save(handlingUnitChangeLog));
    }

    /**
     * 修改
     * @param handlingUnitChangeLog  傳遞的實體
     * @return  null 失敗  實體成功
     */
    @PutMapping
    @Secured("handlingUnitChangeLog_edit")
    public AjaxResult updateById(@RequestBody HandlingUnitChangeLog handlingUnitChangeLog) {
        return AjaxResult.success(handlingUnitChangeLogService.updateById(handlingUnitChangeLog));
    }

    /**
     * 根據id刪除對像
     * @param id  實體ID
     * @return 0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id:.+}")
    @Secured("handlingUnitChangeLog_delete")
    public AjaxResult removeById(@PathVariable("id") String id){
        return AjaxResult.success(handlingUnitChangeLogService.removeById(id));
    }

    /**
     * 批量刪除對像
     * @param ids 實體集合ID
     * @return  0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/_batchDelete")
    @Secured("handlingUnitChangeLog_batch_delete")
    public AjaxResult removeByIds(List<String> ids){
        return AjaxResult.success(handlingUnitChangeLogService.removeByIds(ids));
    }
}