package com.sap.ewm.biz.controller;

import com.sap.ewm.core.base.AjaxResult;
import com.sap.ewm.core.base.FrontPage;
import com.sap.ewm.core.security.annotation.Secured;
import com.sap.ewm.core.security.common.CommonMethods;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.service.ConsumptionModeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sap.ewm.biz.model.ConsumptionMode;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author Syngna
 * @since 2020-07-06
 */
@RestController
@RequestMapping("/consumption-mode")
public class ConsumptionModeController {

    @Autowired
    public ConsumptionModeService consumptionModeService;

    /**
    * 根據id查詢
    *
    * @param id 主鍵
    * @return
    */
    @ResponseBody
    @GetMapping("/{id:.+}")
    public AjaxResult getConsumptionModeById(@PathVariable String id) {
        return AjaxResult.success( consumptionModeService.getById(id));
    }

    /**
     * 查詢所有數據
     *
     * @return
     */
    @ResponseBody
    @GetMapping("")
    public AjaxResult getConsumptionModeList(ConsumptionMode consumptionMode){
        List<ConsumptionMode> result;
        QueryWrapper<ConsumptionMode> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(consumptionMode);
        result = consumptionModeService.list(queryWrapper);
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
    public AjaxResult page(FrontPage<ConsumptionMode> frontPage, ConsumptionMode consumptionMode){
        IPage result;
        QueryWrapper<ConsumptionMode> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(consumptionMode);
        if (frontPage.getGlobalQuery() != null && !"".equals(frontPage.getGlobalQuery().trim())) {
            //TODO modify global query
            queryWrapper.lambda().and(wrapper -> wrapper
                .like(ConsumptionMode::getConsumptionMode, frontPage.getGlobalQuery())
                .or().like(ConsumptionMode::getDescription, frontPage.getGlobalQuery())
                .or().like(ConsumptionMode::getCreator, frontPage.getGlobalQuery())
                .or().like(ConsumptionMode::getUpdater, frontPage.getGlobalQuery())
    );
        }
        result = consumptionModeService.page(frontPage.getPagePlus(), queryWrapper);
        return AjaxResult.success(result);
    }

    /**
     * 新增
     * @param consumptionMode  傳遞的實體
     * @return  null 失敗  實體成功
     */
    @PostMapping
    @Secured("consumption-mode_add")
    public AjaxResult save(@RequestBody ConsumptionMode consumptionMode) {
        String user = CommonMethods.getUser();
        LocalDateTime now = LocalDateTime.now();
        consumptionMode.setCreator(user);
        consumptionMode.setUpdater(user);
        consumptionMode.setCreateTime(now);
        consumptionMode.setUpdateTime(now);
        return AjaxResult.success(consumptionModeService.save(consumptionMode));
    }

    /**
     * 修改
     * @param consumptionMode  傳遞的實體
     * @return  null 失敗  實體成功
     */
    @PutMapping
    @Secured("consumption-mode_edit")
    public AjaxResult updateById(@RequestBody ConsumptionMode consumptionMode) {
		String user = CommonMethods.getUser();
        consumptionMode.setUpdater(user);
		LocalDateTime now = LocalDateTime.now();
        consumptionMode.setUpdateTime(now);
        return AjaxResult.success(consumptionModeService.updateById(consumptionMode));
    }

    /**
     * 根據id刪除對像
     * @param id  實體ID
     * @return 0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id:.+}")
    @Secured("consumption-mode_delete")
    public AjaxResult removeById(@PathVariable("id") String id){
        return AjaxResult.success(consumptionModeService.removeById(id));
    }

    /**
     * 批量刪除對像
     * @param ids 實體集合ID
     * @return  0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/_batchDelete")
    @Secured("consumption-mode_batch_delete")
    public AjaxResult removeByIds(List<String> ids){
        return AjaxResult.success(consumptionModeService.removeByIds(ids));
    }
}