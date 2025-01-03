package com.sap.ewm.biz.controller;

import com.sap.ewm.core.base.AjaxResult;
import com.sap.ewm.core.base.FrontPage;
import com.sap.ewm.core.security.annotation.Secured;
import com.sap.ewm.core.security.common.CommonMethods;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.service.CarrierTypeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sap.ewm.biz.model.CarrierType;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Syngna
 * @since 2020-07-06
 */
@RestController
@RequestMapping("/carrier-type")
public class CarrierTypeController {

    @Autowired
    public CarrierTypeService carrierTypeService;

    /**
     * 根據id查詢
     *
     * @param id 主鍵
     * @return
     */
    @ResponseBody
    @GetMapping("/{id:.+}")
    public AjaxResult getCarrierTypeById(@PathVariable String id) {
        return AjaxResult.success(carrierTypeService.getById(id));
    }

    /**
     * 查詢所有數據
     *
     * @return
     */
    @ResponseBody
    @GetMapping("")
    public AjaxResult getCarrierTypeList(CarrierType carrierType) {
        List<CarrierType> result;
        QueryWrapper<CarrierType> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(carrierType);
        result = carrierTypeService.list(queryWrapper);
        return AjaxResult.success(result);
    }

    /**
     * 分頁查詢數據
     *
     * @param frontPage 分頁資訊
     * @return
     */
    @ResponseBody
    @GetMapping("/page")
    public AjaxResult page(FrontPage<CarrierType> frontPage, CarrierType carrierType) {
        IPage result;
        QueryWrapper<CarrierType> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(carrierType);
        if (frontPage.getGlobalQuery() != null && !"".equals(frontPage.getGlobalQuery().trim())) {
            //TODO modify global query
            queryWrapper.lambda().and(wrapper -> wrapper
                    .like(CarrierType::getHandle, frontPage.getGlobalQuery())
                    .or().like(CarrierType::getCarrierType, frontPage.getGlobalQuery())
                    .or().like(CarrierType::getDescription, frontPage.getGlobalQuery())
                    .or().like(CarrierType::getMixItem, frontPage.getGlobalQuery())
                    .or().like(CarrierType::getCreator, frontPage.getGlobalQuery())
                    .or().like(CarrierType::getUpdater, frontPage.getGlobalQuery())
            );
        }
        result = carrierTypeService.page(frontPage.getPagePlus(), queryWrapper);
        return AjaxResult.success(result);
    }

    /**
     * 新增
     *
     * @param carrierType 傳遞的實體
     * @return null 失敗  實體成功
     */
    @PostMapping
    @Secured("carrier_type_add")
    public AjaxResult save(@RequestBody CarrierType carrierType) {
        String user = CommonMethods.getUser();
        LocalDateTime now = LocalDateTime.now();
        carrierType.setCreator(user);
        carrierType.setUpdater(user);
        carrierType.setCreateTime(now);
        carrierType.setUpdateTime(now);
        return AjaxResult.success(carrierTypeService.save(carrierType));
    }

    /**
     * 修改
     *
     * @param carrierType 傳遞的實體
     * @return null 失敗  實體成功
     */
    @PutMapping
    @Secured("carrier_type_edit")
    public AjaxResult updateById(@RequestBody CarrierType carrierType) {
        String user = CommonMethods.getUser();
        carrierType.setUpdater(user);
        LocalDateTime now = LocalDateTime.now();
        carrierType.setUpdateTime(now);
        return AjaxResult.success(carrierTypeService.updateById(carrierType));
    }

    /**
     * 根據id刪除對像
     *
     * @param id 實體ID
     * @return 0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id:.+}")
    @Secured("carrier_type_delete")
    public AjaxResult removeById(@PathVariable("id") String id) {
        return AjaxResult.success(carrierTypeService.removeById(id));
    }

    /**
     * 批量刪除對像
     *
     * @param ids 實體集合ID
     * @return 0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/_batchDelete")
    @Secured("carrierType_batch_delete")
    public AjaxResult removeByIds(List<String> ids) {
        return AjaxResult.success(carrierTypeService.removeByIds(ids));
    }
}