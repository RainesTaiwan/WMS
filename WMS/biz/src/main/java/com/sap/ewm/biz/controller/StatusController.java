package com.sap.ewm.biz.controller;

import com.sap.ewm.core.base.AjaxResult;
import com.sap.ewm.core.base.FrontPage;
import com.sap.ewm.core.security.annotation.Secured;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.service.StatusService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sap.ewm.biz.model.Status;
import java.util.List;

/**
 *
 * @author Syngna
 * @since 2020-07-06
 */
@RestController
@RequestMapping("/status")
public class StatusController {

    @Autowired
    public StatusService statusService;

    /**
    * 根據id查詢
    *
    * @param id 主鍵
    * @return
    */
    @ResponseBody
    @GetMapping("/{id:.+}")
    public AjaxResult getStatusById(@PathVariable String id) {
        return AjaxResult.success( statusService.getById(id));
    }

    /**
     * 查詢所有數據
     *
     * @return
     */
    @ResponseBody
    @GetMapping("")
    public AjaxResult getStatusList(Status status){
        List<Status> result;
        QueryWrapper<Status> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(status);
        result = statusService.list(queryWrapper);
        return AjaxResult.success(result);
    }

    /**
     * 根據group查詢
     *
     * @return
     */
    @ResponseBody
    @GetMapping("/group/list")
    public AjaxResult getStatusList(String group){
        List<Status> result;
        result = statusService.listStatusByGroup(group);
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
    public AjaxResult page(FrontPage<Status> frontPage, Status status){
        IPage result;
        QueryWrapper<Status> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(status);
        if (frontPage.getGlobalQuery() != null && !"".equals(frontPage.getGlobalQuery().trim())) {
            //TODO modify global query
            queryWrapper.lambda().and(wrapper -> wrapper
                .like(Status::getHandle, frontPage.getGlobalQuery())
                .or().like(Status::getStatus, frontPage.getGlobalQuery())
                .or().like(Status::getDescription, frontPage.getGlobalQuery())
                .or().like(Status::getStatusGroup, frontPage.getGlobalQuery())
    );
        }
        result = statusService.page(frontPage.getPagePlus(), queryWrapper);
        return AjaxResult.success(result);
    }

    /**
     * 新增
     * @param status  傳遞的實體
     * @return  null 失敗  實體成功
     */
    @PostMapping
    @Secured("status_add")
    public AjaxResult save(@RequestBody Status status) {
        return AjaxResult.success(statusService.save(status));
    }

    /**
     * 修改
     * @param status  傳遞的實體
     * @return  null 失敗  實體成功
     */
    @PutMapping
    @Secured("status_edit")
    public AjaxResult updateById(@RequestBody Status status) {
        return AjaxResult.success(statusService.updateById(status));
    }

    /**
     * 根據id刪除對像
     * @param id  實體ID
     * @return 0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id:.+}")
    @Secured("status_delete")
    public AjaxResult removeById(@PathVariable("id") String id){
        return AjaxResult.success(statusService.removeById(id));
    }

    /**
     * 批量刪除對像
     * @param ids 實體集合ID
     * @return  0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/_batchDelete")
    @Secured("status_batch_delete")
    public AjaxResult removeByIds(List<String> ids){
        return AjaxResult.success(statusService.removeByIds(ids));
    }
}