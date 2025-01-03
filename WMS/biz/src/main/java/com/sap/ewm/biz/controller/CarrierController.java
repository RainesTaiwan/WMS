package com.sap.ewm.biz.controller;

import com.sap.ewm.biz.dto.CarrierDTO;
import com.sap.ewm.core.base.AjaxResult;
import com.sap.ewm.core.base.FrontPage;
import com.sap.ewm.core.security.annotation.Secured;
import com.sap.ewm.core.security.common.CommonMethods;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.service.CarrierService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sap.ewm.biz.model.Carrier;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Syngna
 * @since 2020-07-06
 */
@RestController
@RequestMapping("/carrier")
public class CarrierController {

    @Autowired
    public CarrierService carrierService;

    /**
     * 根據id查詢
     *
     * @param id 主鍵
     * @return
     */
    @ResponseBody
    @GetMapping("/{id:.+}")
    public AjaxResult getCarrierById(@PathVariable String id) {
        return AjaxResult.success(carrierService.getById(id));
    }

    /**
     * 查詢所有數據
     *
     * @return
     */
    @ResponseBody
    @GetMapping("")
    public AjaxResult getCarrierList(Carrier carrier) {
        List<Carrier> result;
        QueryWrapper<Carrier> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(carrier);
        result = carrierService.list(queryWrapper);
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
    public AjaxResult page(FrontPage<Carrier> frontPage, Carrier carrier) {
        IPage result;
        QueryWrapper<Carrier> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(carrier);
        if (frontPage.getGlobalQuery() != null && !"".equals(frontPage.getGlobalQuery().trim())) {
            //TODO modify global query
            queryWrapper.lambda().and(wrapper -> wrapper
                    .like(Carrier::getHandle, frontPage.getGlobalQuery())
                    .or().like(Carrier::getCarrier, frontPage.getGlobalQuery())
                    .or().like(Carrier::getDescription, frontPage.getGlobalQuery())
                    .or().like(Carrier::getCarrierTypeBo, frontPage.getGlobalQuery())
                    .or().like(Carrier::getCreator, frontPage.getGlobalQuery())
                    .or().like(Carrier::getUpdater, frontPage.getGlobalQuery())
            );
        }
        result = carrierService.page(frontPage.getPagePlus(), queryWrapper);
        return AjaxResult.success(result);
    }

    /**
     * 新增
     *
     * @param carrier 傳遞的實體
     * @return null 失敗  實體成功
     */
    @PostMapping
    @Secured("carrier_add")
    public AjaxResult save(@RequestBody CarrierDTO carrier) {
        String user = CommonMethods.getUser();
        LocalDateTime now = LocalDateTime.now();
        carrier.setCreator(user);
        carrier.setUpdater(user);
        carrier.setCreateTime(now);
        carrier.setUpdateTime(now);
        return AjaxResult.success(carrierService.save(carrier));
    }

    /**
     * 修改
     *
     * @param carrier 傳遞的實體
     * @return null 失敗  實體成功
     */
    @PutMapping
    @Secured("carrier_edit")
    public AjaxResult updateById(@RequestBody CarrierDTO carrier) {
        String user = CommonMethods.getUser();
        carrier.setUpdater(user);
        LocalDateTime now = LocalDateTime.now();
        carrier.setUpdateTime(now);
        return AjaxResult.success(carrierService.updateById(carrier));
    }

    /**
     * 根據id刪除對像
     *
     * @param id 實體ID
     * @return 0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id:.+}")
    @Secured("carrier_delete")
    public AjaxResult removeById(@PathVariable("id") String id) {
        return AjaxResult.success(carrierService.removeById(id));
    }

    /**
     * 批量刪除對像
     *
     * @param ids 實體集合ID
     * @return 0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/_batchDelete")
    @Secured("carrier_batch_delete")
    public AjaxResult removeByIds(List<String> ids) {
        return AjaxResult.success(carrierService.removeByIds(ids));
    }
}