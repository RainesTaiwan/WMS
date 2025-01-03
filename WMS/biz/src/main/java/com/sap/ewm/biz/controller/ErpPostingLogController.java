package com.sap.ewm.biz.controller;

import com.sap.ewm.core.base.AjaxResult;
import com.sap.ewm.core.base.FrontPage;
import com.sap.ewm.core.security.annotation.Secured;
import com.sap.ewm.core.security.common.CommonMethods;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.service.ErpPostingLogService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sap.ewm.biz.model.ErpPostingLog;
import java.util.List;

/**
 *
 * @author Syngna
 * @since 2020-07-06
 */
@RestController
@RequestMapping("/erpPostingLog")
public class ErpPostingLogController {

    @Autowired
    public ErpPostingLogService erpPostingLogService;

    /**
    * 根據id查詢
    *
    * @param id 主鍵
    * @return
    */
    @ResponseBody
    @GetMapping("/{id:.+}")
    public AjaxResult getErpPostingLogById(@PathVariable String id) {
        return AjaxResult.success( erpPostingLogService.getById(id));
    }

    /**
     * 查詢所有數據
     *
     * @return
     */
    @ResponseBody
    @GetMapping("")
    public AjaxResult getErpPostingLogList(ErpPostingLog erpPostingLog){
        List<ErpPostingLog> result;
        QueryWrapper<ErpPostingLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(erpPostingLog);
        result = erpPostingLogService.list(queryWrapper);
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
    public AjaxResult page(FrontPage<ErpPostingLog> frontPage, ErpPostingLog erpPostingLog){
        IPage result;
        QueryWrapper<ErpPostingLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(erpPostingLog);
        if (frontPage.getGlobalQuery() != null && !"".equals(frontPage.getGlobalQuery().trim())) {
            //TODO modify global query
            queryWrapper.lambda().and(wrapper -> wrapper
                .like(ErpPostingLog::getHandle, frontPage.getGlobalQuery())
                .or().like(ErpPostingLog::getName, frontPage.getGlobalQuery())
                .or().like(ErpPostingLog::getType, frontPage.getGlobalQuery())
                .or().like(ErpPostingLog::getResourceBo, frontPage.getGlobalQuery())
                .or().like(ErpPostingLog::getKey1, frontPage.getGlobalQuery())
                .or().like(ErpPostingLog::getKey2, frontPage.getGlobalQuery())
                .or().like(ErpPostingLog::getKey3, frontPage.getGlobalQuery())
                .or().like(ErpPostingLog::getmessageId, frontPage.getGlobalQuery())
                .or().like(ErpPostingLog::getResult, frontPage.getGlobalQuery())
                .or().like(ErpPostingLog::getMsg, frontPage.getGlobalQuery())
                .or().like(ErpPostingLog::getRequestBody, frontPage.getGlobalQuery())
                .or().like(ErpPostingLog::getResponseBody, frontPage.getGlobalQuery())
                .or().like(ErpPostingLog::getProcessTime, frontPage.getGlobalQuery())
                .or().like(ErpPostingLog::getCreator, frontPage.getGlobalQuery())
    );
        }
        result = erpPostingLogService.page(frontPage.getPagePlus(), queryWrapper);
        return AjaxResult.success(result);
    }

    /**
     * 新增
     * @param erpPostingLog  傳遞的實體
     * @return  null 失敗  實體成功
     */
    @PostMapping
    @Secured("erpPostingLog_add")
    public AjaxResult save(@RequestBody ErpPostingLog erpPostingLog) {
        String user = CommonMethods.getUser();
        erpPostingLog.setCreator(user);
        return AjaxResult.success(erpPostingLogService.save(erpPostingLog));
    }

    /**
     * 修改
     * @param erpPostingLog  傳遞的實體
     * @return  null 失敗  實體成功
     */
    @PutMapping
    @Secured("erpPostingLog_edit")
    public AjaxResult updateById(@RequestBody ErpPostingLog erpPostingLog) {
        return AjaxResult.success(erpPostingLogService.updateById(erpPostingLog));
    }

    /**
     * 根據id刪除對像
     * @param id  實體ID
     * @return 0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id:.+}")
    @Secured("erpPostingLog_delete")
    public AjaxResult removeById(@PathVariable("id") String id){
        return AjaxResult.success(erpPostingLogService.removeById(id));
    }

    /**
     * 批量刪除對像
     * @param ids 實體集合ID
     * @return  0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/_batchDelete")
    @Secured("erpPostingLog_batch_delete")
    public AjaxResult removeByIds(List<String> ids){
        return AjaxResult.success(erpPostingLogService.removeByIds(ids));
    }
}