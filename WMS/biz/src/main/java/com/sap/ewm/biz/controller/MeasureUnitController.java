package com.sap.ewm.biz.controller;

import com.sap.ewm.core.base.AjaxResult;
import com.sap.ewm.core.base.FrontPage;
import com.sap.ewm.core.security.annotation.Secured;
import com.sap.ewm.core.security.common.CommonMethods;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sap.ewm.biz.service.MeasureUnitService;
import com.sap.ewm.biz.model.MeasureUnit;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author syngna
 * @since 2020-07-19
 */
@RestController
@RequestMapping("/measure-unit")
public class MeasureUnitController {

    @Autowired
    public MeasureUnitService measureUnitService;

    /**
    * 根據id查詢
    *
    * @param id 主鍵
    * @return
    */
    @ResponseBody
    @GetMapping("/{id:.+}")
    public AjaxResult getMeasureUnitById(@PathVariable String id) {
        return AjaxResult.success( measureUnitService.getById(id));
    }

    /**
     * 查詢所有數據
     *
     * @return
     */
    @ResponseBody
    @GetMapping("")
    public AjaxResult getMeasureUnitList(MeasureUnit measureUnit){
        List<MeasureUnit> result;
        QueryWrapper<MeasureUnit> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(measureUnit);
        result = measureUnitService.list(queryWrapper);
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
    public AjaxResult page(FrontPage<MeasureUnit> frontPage, MeasureUnit measureUnit){
        IPage result;
        QueryWrapper<MeasureUnit> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(measureUnit);
        if (frontPage.getGlobalQuery() != null && !"".equals(frontPage.getGlobalQuery().trim())) {
            //TODO modify global query
            queryWrapper.lambda().and(wrapper -> wrapper
                .like(MeasureUnit::getMeasureUnit, frontPage.getGlobalQuery())
                .or().like(MeasureUnit::getDescription, frontPage.getGlobalQuery())
                .or().like(MeasureUnit::getCreator, frontPage.getGlobalQuery())
                .or().like(MeasureUnit::getUpdater, frontPage.getGlobalQuery())
    );
        }
        result = measureUnitService.page(frontPage.getPagePlus(), queryWrapper);
        return AjaxResult.success(result);
    }

    /**
     * 新增
     * @param measureUnit  傳遞的實體
     * @return  null 失敗  實體成功
     */
    @PostMapping
    @Secured("measureUnit_add")
    public AjaxResult save(@RequestBody MeasureUnit measureUnit) {
        String user = CommonMethods.getUser();
        LocalDateTime now = LocalDateTime.now();
        measureUnit.setCreator(user);
        measureUnit.setUpdater(user);
        measureUnit.setCreateTime(now);
        measureUnit.setUpdateTime(now);
        return AjaxResult.success(measureUnitService.save(measureUnit));
    }

    /**
     * 修改
     * @param measureUnit  傳遞的實體
     * @return  null 失敗  實體成功
     */
    @PutMapping
    @Secured("measure_unit_edit")
    public AjaxResult updateById(@RequestBody MeasureUnit measureUnit) {
		String user = CommonMethods.getUser();
        measureUnit.setUpdater(user);
		LocalDateTime now = LocalDateTime.now();
        measureUnit.setUpdateTime(now);
        return AjaxResult.success(measureUnitService.updateById(measureUnit));
    }

    /**
     * 根據id刪除對像
     * @param id  實體ID
     * @return 0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id:.+}")
    @Secured("measure_unit_delete")
    public AjaxResult removeById(@PathVariable("id") String id){
        return AjaxResult.success(measureUnitService.removeById(id));
    }

    /**
     * 批量刪除對像
     * @param ids 實體集合ID
     * @return  0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/delete-batch")
    @Secured("measure_unit_batch_delete")
    public AjaxResult removeByIds(List<String> ids){
        return AjaxResult.success(measureUnitService.removeByIds(ids));
    }
}