package com.sap.ewm.biz.controller;

import com.sap.ewm.core.base.AjaxResult;
import com.sap.ewm.core.base.FrontPage;
import com.sap.ewm.core.security.annotation.Secured;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.model.StorageBinStatus;
import com.sap.ewm.biz.service.StorageBinStatusService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author syngna
 * @since 2020-07-14
 */
@RestController
@RequestMapping("/storageBinStatus")
public class StorageBinStatusController {

    @Autowired
    public StorageBinStatusService storageBinStatusService;

    /**
    * 根據id查詢
    *
    * @param id 主鍵
    * @return
    */
    @ResponseBody
    @GetMapping("/{id:.+}")
    public AjaxResult getStorageBinStatusById(@PathVariable String id) {
        return AjaxResult.success( storageBinStatusService.getById(id));
    }

    /**
     * 查詢所有數據
     *
     * @return
     */
    @ResponseBody
    @GetMapping("")
    public AjaxResult getStorageBinStatusList(StorageBinStatus storageBinStatus){
        List<StorageBinStatus> result;
        QueryWrapper<StorageBinStatus> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(storageBinStatus);
        result = storageBinStatusService.list(queryWrapper);
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
    public AjaxResult page(FrontPage<StorageBinStatus> frontPage, StorageBinStatus storageBinStatus){
        IPage result;
        QueryWrapper<StorageBinStatus> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(storageBinStatus);
        if (frontPage.getGlobalQuery() != null && !"".equals(frontPage.getGlobalQuery().trim())) {
            //TODO modify global query
            queryWrapper.lambda().and(wrapper -> wrapper
                .like(StorageBinStatus::getHandle, frontPage.getGlobalQuery())
                .or().like(StorageBinStatus::getStorageBin, frontPage.getGlobalQuery())
                .or().like(StorageBinStatus::getStatus, frontPage.getGlobalQuery())
    );
        }
        result = storageBinStatusService.page(frontPage.getPagePlus(), queryWrapper);
        return AjaxResult.success(result);
    }

    /**
     * 新增
     * @param storageBinStatus  傳遞的實體
     * @return  null 失敗  實體成功
     */
    @PostMapping
    @Secured("storageBinStatus_add")
    public AjaxResult save(@RequestBody StorageBinStatus storageBinStatus) {
        LocalDateTime now = LocalDateTime.now();
        storageBinStatus.setCreateTime(now);
        storageBinStatus.setUpdateTime(now);
        return AjaxResult.success(storageBinStatusService.save(storageBinStatus));
    }

    /**
     * 修改
     * @param storageBinStatus  傳遞的實體
     * @return  null 失敗  實體成功
     */
    @PutMapping
    @Secured("storageBinStatus_edit")
    public AjaxResult updateById(@RequestBody StorageBinStatus storageBinStatus) {
		LocalDateTime now = LocalDateTime.now();
        storageBinStatus.setUpdateTime(now);
        return AjaxResult.success(storageBinStatusService.updateById(storageBinStatus));
    }

    /**
     * 根據id刪除對像
     * @param id  實體ID
     * @return 0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id:.+}")
    @Secured("storageBinStatus_delete")
    public AjaxResult removeById(@PathVariable("id") String id){
        return AjaxResult.success(storageBinStatusService.removeById(id));
    }

    /**
     * 批量刪除對像
     * @param ids 實體集合ID
     * @return  0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/_batchDelete")
    @Secured("storageBinStatus_batch_delete")
    public AjaxResult removeByIds(List<String> ids){
        return AjaxResult.success(storageBinStatusService.removeByIds(ids));
    }
}