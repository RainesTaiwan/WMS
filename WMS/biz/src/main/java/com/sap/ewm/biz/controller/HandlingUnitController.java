package com.sap.ewm.biz.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.constants.CommonConstants;
import com.sap.ewm.biz.dto.CarrierBindDTO;
import com.sap.ewm.biz.dto.HandlingUnitDTO;
import com.sap.ewm.biz.dto.StorageChangeDTO;
import com.sap.ewm.biz.model.Carrier;
import com.sap.ewm.biz.model.HandlingUnit;
import com.sap.ewm.biz.model.HandlingUnitLocation;
import com.sap.ewm.biz.model.StorageBin;
import com.sap.ewm.biz.service.HandlingUnitService;
import com.sap.ewm.core.base.AjaxResult;
import com.sap.ewm.core.base.FrontPage;
import com.sap.ewm.core.exception.BusinessException;
import com.sap.ewm.core.security.annotation.Secured;
import com.sap.ewm.core.security.common.CommonMethods;
import com.sap.ewm.core.utils.DateUtil;
import com.sap.ewm.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author Syngna
 * @since 2020-07-06
 */
@RestController
@RequestMapping("/handling-unit")
public class HandlingUnitController {

    @Autowired
    public HandlingUnitService handlingUnitService;

    /**
     * 根據id查詢
     *
     * @param id 主鍵
     * @return
     */
    @ResponseBody
    @GetMapping("/{id:.+}")
    public AjaxResult getHandlingUnitById(@PathVariable String id) {
        return AjaxResult.success(handlingUnitService.getById(id));
    }

    /**
     * 查詢所有數據
     *
     * @return
     */
    @ResponseBody
    @GetMapping("")
    public AjaxResult getHandlingUnitList(HandlingUnitDTO handlingUnit) {
        List<HandlingUnitDTO> result;
        QueryWrapper<HandlingUnitDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(handlingUnit);
        result = handlingUnitService.selectList(queryWrapper);
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
    public AjaxResult page(FrontPage<HandlingUnitDTO> frontPage, HandlingUnitDTO handlingUnit) {
        IPage result;
        QueryWrapper<HandlingUnitDTO> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(handlingUnit);
        if (frontPage.getGlobalQuery() != null && !"".equals(frontPage.getGlobalQuery().trim())) {
            //TODO modify global query
            queryWrapper.lambda().and(wrapper -> wrapper
                    .like(HandlingUnit::getCarrierBo, frontPage.getGlobalQuery())
                    .or().like(HandlingUnit::getStatus, frontPage.getGlobalQuery())
                    .or().like(HandlingUnit::getHandlingUnitContextGbo, frontPage.getGlobalQuery())
                    .or().like(HandlingUnit::getCreator, frontPage.getGlobalQuery())
                    .or().like(HandlingUnit::getUpdater, frontPage.getGlobalQuery())
            );
        }
        result = handlingUnitService.selectPageVo(frontPage.getPagePlus(), queryWrapper);
        return AjaxResult.success(result);
    }

    /**
     * 載具繫結
     *
     * @param carrierBindDTO 傳遞載具繫結實體
     * @return null 失敗  實體成功
     */
    @PostMapping("/carrier-bind")
    @Secured("handling_unit_carrier_bind")
    public AjaxResult carrierBind(@RequestBody CarrierBindDTO carrierBindDTO) {
        String user = CommonMethods.getUser();
        handlingUnitService.carrierBind(user, carrierBindDTO);
        return AjaxResult.success();
    }

    /**
     * 載具解綁
     *
     * @param handles 需解綁的handle列表
     * @return null 失敗  實體成功
     */
    @PostMapping("/carrier-unbind")
    @Secured("handling_unit_carrier_unbind")
    public AjaxResult carrierUnbind(@RequestBody List<String> handles) {
        String user = CommonMethods.getUser();
        handlingUnitService.carrierUnbind(user, handles);
        return AjaxResult.success();
    }

    /**
     * @param storageChangeDTO
     * @return
     */
    @ResponseBody
    @GetMapping("/storage-change")
    public AjaxResult doStorageChange(StorageChangeDTO storageChangeDTO) {
        handlingUnitService.doStorageChange(storageChangeDTO);
        return AjaxResult.success();
    }

    /**
     * 修改
     *
     * @param handlingUnit 傳遞的實體
     * @return null 失敗  實體成功
     */
    @PutMapping
    @Secured("handlingUnit_edit")
    public AjaxResult updateById(@RequestBody HandlingUnit handlingUnit) {
        String user = CommonMethods.getUser();
        handlingUnit.setUpdater(user);
        LocalDateTime now = LocalDateTime.now();
        handlingUnit.setUpdateTime(now);
        return AjaxResult.success(handlingUnitService.updateById(handlingUnit));
    }

    /**
     * 根據id刪除對像
     *
     * @param id 實體ID
     * @return 0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id:.+}")
    @Secured("handlingUnit_delete")
    public AjaxResult removeById(@PathVariable("id") String id) {
        return AjaxResult.success(handlingUnitService.removeById(id));
    }

    /**
     * 批量刪除對像
     *
     * @param ids 實體集合ID
     * @return 0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/_batchDelete")
    @Secured("handlingUnit_batch_delete")
    public AjaxResult removeByIds(List<String> ids) {
        return AjaxResult.success(handlingUnitService.removeByIds(ids));
    }
}