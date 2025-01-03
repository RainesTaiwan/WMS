package com.sap.ewm.sys.controller;

import com.sap.ewm.core.base.AjaxResult;
import com.sap.ewm.core.base.FrontPage;
import com.sap.ewm.core.security.annotation.Secured;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.sys.model.MqLog;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sap.ewm.sys.service.MqLogService;

import java.util.List;

/**
 *
 * @author syngna
 * @since 2020-07-14
 */
@RestController
@RequestMapping("/mqLog")
public class MqLogController {

    @Autowired
    public MqLogService mqLogService;

    /**
    * 根據id查詢
    *
    * @param id 主鍵
    * @return
    */
    @ResponseBody
    @GetMapping("/{id:.+}")
    public AjaxResult getMqLogById(@PathVariable String id) {
        return AjaxResult.success( mqLogService.getById(id));
    }

    /**
     * 查詢所有數據
     *
     * @return
     */
    @ResponseBody
    @GetMapping("")
    public AjaxResult getMqLogList(MqLog mqLog){
        List<MqLog> result;
        QueryWrapper<MqLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(mqLog);
        result = mqLogService.list(queryWrapper);
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
    public AjaxResult page(FrontPage<MqLog> frontPage, MqLog mqLog){
        IPage result;
        QueryWrapper<MqLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(mqLog);
        if (frontPage.getGlobalQuery() != null && !"".equals(frontPage.getGlobalQuery().trim())) {
            //TODO modify global query
            queryWrapper.lambda().and(wrapper -> wrapper
                .like(MqLog::getHandle, frontPage.getGlobalQuery())
                .or().like(MqLog::getServerId, frontPage.getGlobalQuery())
                .or().like(MqLog::getBroker, frontPage.getGlobalQuery())
                .or().like(MqLog::getQueue, frontPage.getGlobalQuery())
                .or().like(MqLog::getMessageBody, frontPage.getGlobalQuery())
                .or().like(MqLog::getResult, frontPage.getGlobalQuery())
                .or().like(MqLog::getError, frontPage.getGlobalQuery())
                .or().like(MqLog::getMessageType, frontPage.getGlobalQuery())
    );
        }
        result = mqLogService.page(frontPage.getPagePlus(), queryWrapper);
        return AjaxResult.success(result);
    }

    /**
     * 新增
     * @param mqLog  傳遞的實體
     * @return  null 失敗  實體成功
     */
    @PostMapping
    @Secured("mqLog_add")
    public AjaxResult save(@RequestBody MqLog mqLog) {
        return AjaxResult.success(mqLogService.save(mqLog));
    }

    /**
     * 修改
     * @param mqLog  傳遞的實體
     * @return  null 失敗  實體成功
     */
    @PutMapping
    @Secured("mqLog_edit")
    public AjaxResult updateById(@RequestBody MqLog mqLog) {
        return AjaxResult.success(mqLogService.updateById(mqLog));
    }

    /**
     * 根據id刪除對像
     * @param id  實體ID
     * @return 0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id:.+}")
    @Secured("mqLog_delete")
    public AjaxResult removeById(@PathVariable("id") String id){
        return AjaxResult.success(mqLogService.removeById(id));
    }

    /**
     * 批量刪除對像
     * @param ids 實體集合ID
     * @return  0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/_batchDelete")
    @Secured("mqLog_batch_delete")
    public AjaxResult removeByIds(List<String> ids){
        return AjaxResult.success(mqLogService.removeByIds(ids));
    }
}