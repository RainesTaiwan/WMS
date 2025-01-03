package com.sap.ewm.biz.controller;

import com.sap.ewm.core.base.AjaxResult;
import com.sap.ewm.core.base.FrontPage;
import com.sap.ewm.core.security.annotation.Secured;
import com.sap.ewm.core.security.common.CommonMethods;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.service.WarehouseService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sap.ewm.biz.model.Warehouse;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Syngna
 * @since 2020-07-06
 */
@RestController
@RequestMapping("/warehouse")
public class WarehouseController {

    @Autowired
    public WarehouseService warehouseService;

    /**
    * 根據id查詢
    *
    * @param id 主鍵
    * @return
    */
    @ResponseBody
    @GetMapping("/{id:.+}")
    public AjaxResult getWarehouseById(@PathVariable String id) {
        return AjaxResult.success( warehouseService.getById(id));
    }

    /**
     * 查詢所有數據
     *
     * @return
     */
    @ResponseBody
    @GetMapping("")
    public AjaxResult getWarehouseList(Warehouse warehouse){
        List<Warehouse> result;
        QueryWrapper<Warehouse> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(warehouse);
        result = warehouseService.list(queryWrapper);
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
    public AjaxResult page(FrontPage<Warehouse> frontPage, Warehouse warehouse){
        IPage result;
        QueryWrapper<Warehouse> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(warehouse);
        if (frontPage.getGlobalQuery() != null && !"".equals(frontPage.getGlobalQuery().trim())) {
            //TODO modify global query
            queryWrapper.lambda().and(wrapper -> wrapper
                .like(Warehouse::getHandle, frontPage.getGlobalQuery())
                .or().like(Warehouse::getWarehouse, frontPage.getGlobalQuery())
                .or().like(Warehouse::getDescription, frontPage.getGlobalQuery())
                .or().like(Warehouse::getCreator, frontPage.getGlobalQuery())
                .or().like(Warehouse::getUpdater, frontPage.getGlobalQuery())
    );
        }
        result = warehouseService.page(frontPage.getPagePlus(), queryWrapper);
        return AjaxResult.success(result);
    }

    /**
     * 新增
     * @param warehouse  傳遞的實體
     * @return  null 失敗  實體成功
     */
    @PostMapping
    @Secured("warehouse_add")
    public AjaxResult save(@RequestBody Warehouse warehouse) {
        String user = CommonMethods.getUser();
        LocalDateTime now = LocalDateTime.now();
        warehouse.setCreator(user);
        warehouse.setUpdater(user);
        warehouse.setCreateTime(now);
        warehouse.setUpdateTime(now);
        return AjaxResult.success(warehouseService.save(warehouse));
    }

    /**
     * 修改
     * @param warehouse  傳遞的實體
     * @return  null 失敗  實體成功
     */
    @PutMapping
    @Secured("warehouse_edit")
    public AjaxResult updateById(@RequestBody Warehouse warehouse) {
		String user = CommonMethods.getUser();
        warehouse.setUpdater(user);
		LocalDateTime now = LocalDateTime.now();
        warehouse.setUpdateTime(now);
        return AjaxResult.success(warehouseService.updateById(warehouse));
    }

    /**
     * 根據id刪除對像
     * @param id  實體ID
     * @return 0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id:.+}")
    @Secured("warehouse_delete")
    public AjaxResult removeById(@PathVariable("id") String id){
        return AjaxResult.success(warehouseService.removeById(id));
    }

    /**
     * 批量刪除對像
     * @param ids 實體集合ID
     * @return  0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/_batchDelete")
    @Secured("warehouse_batch_delete")
    public AjaxResult removeByIds(List<String> ids){
        return AjaxResult.success(warehouseService.removeByIds(ids));
    }
}