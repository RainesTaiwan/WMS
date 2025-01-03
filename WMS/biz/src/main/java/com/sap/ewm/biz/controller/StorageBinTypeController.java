package com.sap.ewm.biz.controller;

import com.sap.ewm.core.base.AjaxResult;
import com.sap.ewm.core.base.FrontPage;
import com.sap.ewm.core.security.annotation.Secured;
import com.sap.ewm.core.security.common.CommonMethods;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.service.StorageBinTypeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sap.ewm.biz.model.StorageBinType;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author syngna
 * @since 2020-07-20
 */
@RestController
@RequestMapping("/storage-bin-type")
public class StorageBinTypeController {

    @Autowired
    public StorageBinTypeService storageBinTypeService;

    /**
    * 根據id查詢
    *
    * @param id 主鍵
    * @return
    */
    @ResponseBody
    @GetMapping("/{id:.+}")
    public AjaxResult getStorageBinTypeById(@PathVariable String id) {
        return AjaxResult.success( storageBinTypeService.getById(id));
    }

    /**
     * 查詢所有數據
     *
     * @return
     */
    @ResponseBody
    @GetMapping("")
    public AjaxResult getStorageBinTypeList(StorageBinType storageBinType){
        List<StorageBinType> result;
        QueryWrapper<StorageBinType> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(storageBinType);
        result = storageBinTypeService.list(queryWrapper);
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
    public AjaxResult page(FrontPage<StorageBinType> frontPage, StorageBinType storageBinType){
        IPage result;
        QueryWrapper<StorageBinType> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(storageBinType);
        if (frontPage.getGlobalQuery() != null && !"".equals(frontPage.getGlobalQuery().trim())) {
            //TODO modify global query
            queryWrapper.lambda().and(wrapper -> wrapper
                .like(StorageBinType::getHandle, frontPage.getGlobalQuery())
                .or().like(StorageBinType::getStorageBinType, frontPage.getGlobalQuery())
                .or().like(StorageBinType::getDescription, frontPage.getGlobalQuery())
                .or().like(StorageBinType::getMixItem, frontPage.getGlobalQuery())
                .or().like(StorageBinType::getCreator, frontPage.getGlobalQuery())
                .or().like(StorageBinType::getUpdater, frontPage.getGlobalQuery())
    );
        }
        result = storageBinTypeService.page(frontPage.getPagePlus(), queryWrapper);
        return AjaxResult.success(result);
    }

    /**
     * 新增
     * @param storageBinType  傳遞的實體
     * @return  null 失敗  實體成功
     */
    @PostMapping
    @Secured("storage_bin_type_add")
    public AjaxResult save(@RequestBody StorageBinType storageBinType) {
        String user = CommonMethods.getUser();
        LocalDateTime now = LocalDateTime.now();
        storageBinType.setCreator(user);
        storageBinType.setUpdater(user);
        storageBinType.setCreateTime(now);
        storageBinType.setUpdateTime(now);
        return AjaxResult.success(storageBinTypeService.save(storageBinType));
    }

    /**
     * 修改
     * @param storageBinType  傳遞的實體
     * @return  null 失敗  實體成功
     */
    @PutMapping
    @Secured("storage_bin_type_edit")
    public AjaxResult updateById(@RequestBody StorageBinType storageBinType) {
		String user = CommonMethods.getUser();
        storageBinType.setUpdater(user);
		LocalDateTime now = LocalDateTime.now();
        storageBinType.setUpdateTime(now);
        return AjaxResult.success(storageBinTypeService.updateById(storageBinType));
    }

    /**
     * 根據id刪除對像
     * @param id  實體ID
     * @return 0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id:.+}")
    @Secured("storage_bin_type_delete")
    public AjaxResult removeById(@PathVariable("id") String id){
        return AjaxResult.success(storageBinTypeService.removeById(id));
    }

    /**
     * 批量刪除對像
     * @param ids 實體集合ID
     * @return  0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/delete-batch")
    @Secured("storage_bin_type_batch_delete")
    public AjaxResult removeByIds(List<String> ids){
        return AjaxResult.success(storageBinTypeService.removeByIds(ids));
    }
}