package com.sap.ewm.biz.controller;

import com.sap.ewm.biz.dto.InventoryDTO;
import com.sap.ewm.core.base.AjaxResult;
import com.sap.ewm.core.base.FrontPage;
import com.sap.ewm.core.security.annotation.Secured;
import com.sap.ewm.core.security.common.CommonMethods;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.model.Inventory;
import com.sap.ewm.biz.service.InventoryService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Syngna
 * @since 2020-07-05
 */
@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    public InventoryService inventoryService;

    /**
    * 根據id查詢
    *
    * @param id 主鍵
    * @return
    */
    @ResponseBody
    @GetMapping("/{id:.+}")
    public AjaxResult getInventoryById(@PathVariable String id) {
        return AjaxResult.success( inventoryService.getById(id));
    }

    /**
     * 查詢所有數據
     *
     * @return
     */
    @ResponseBody
    @GetMapping("")
    public AjaxResult getInventoryList(Inventory inventory){
        List<Inventory> result;
        QueryWrapper<Inventory> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(inventory);
        result = inventoryService.list(queryWrapper);
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
    public AjaxResult page(FrontPage<Inventory> frontPage, Inventory inventory){
        IPage result;
        QueryWrapper<Inventory> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(inventory);
        if (frontPage.getGlobalQuery() != null && !"".equals(frontPage.getGlobalQuery().trim())) {
            //TODO modify global query
            queryWrapper.lambda().and(wrapper -> wrapper
                .like(Inventory::getStatus, frontPage.getGlobalQuery())
                .or().like(Inventory::getItemBo, frontPage.getGlobalQuery())
                .or().like(Inventory::getUnitOfMeasure, frontPage.getGlobalQuery())
                .or().like(Inventory::getBatchNo, frontPage.getGlobalQuery())
                .or().like(Inventory::getVendorBatchNo, frontPage.getGlobalQuery())
    );
        }
        result = inventoryService.page(frontPage.getPagePlus(), queryWrapper);
        return AjaxResult.success(result);
    }

    /**
     * 新增
     * @param inventoryDTO  傳遞的實體
     * @return  null 失敗  實體成功
     */
    @PostMapping
    @Secured("inventory_add")
    public AjaxResult save(@RequestBody InventoryDTO inventoryDTO) {
        String user = CommonMethods.getUser();
        LocalDateTime now = LocalDateTime.now();
        inventoryDTO.setCreator(user);
        inventoryDTO.setUpdater(user);
        inventoryDTO.setCreateTime(now);
        inventoryDTO.setUpdateTime(now);
        // TODO 標記庫存生效狀態
        return AjaxResult.success(inventoryService.save(inventoryDTO));
    }

    /**
     * 修改
     * @param inventoryDTO  傳遞的實體
     * @return  null 失敗  實體成功
     */
    @PutMapping
    @Secured("inventory_edit")
    public AjaxResult updateById(@RequestBody InventoryDTO inventoryDTO) {
		String user = CommonMethods.getUser();
        LocalDateTime now = LocalDateTime.now();
        inventoryDTO.setUpdater(user);
        inventoryDTO.setUpdateTime(now);
        return AjaxResult.success(inventoryService.updateById(inventoryDTO));
    }

    /**
     * 根據id刪除對像
     * @param id  實體ID
     * @return 0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id:.+}")
    @Secured("inventory_delete")
    public AjaxResult removeById(@PathVariable("id") String id){
        // todo 驗證是否可以刪除(一旦入過庫或在繫結狀態則不可刪除)
        return AjaxResult.success(inventoryService.removeById(id));
    }

    /**
     * 批量刪除對像
     * @param ids 實體集合ID
     * @return  0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/_batchDelete")
    @Secured("inventory_batch_delete")
    public AjaxResult removeByIds(List<String> ids){
        return AjaxResult.success(inventoryService.removeByIds(ids));
    }
}