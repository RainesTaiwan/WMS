package com.sap.ewm.biz.controller;

import com.sap.ewm.core.base.AjaxResult;
import com.sap.ewm.core.base.FrontPage;
import com.sap.ewm.core.security.annotation.Secured;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sap.ewm.biz.service.MeasureUnitMatrixService;
import com.sap.ewm.biz.model.MeasureUnitMatrix;
import java.util.List;

/**
 *
 * @author syngna
 * @since 2020-07-19
 */
@RestController
@RequestMapping("/measure-unit-matrix")
public class MeasureUnitMatrixController {

    @Autowired
    public MeasureUnitMatrixService measureUnitMatrixService;

    /**
    * 根據id查詢
    *
    * @param id 主鍵
    * @return
    */
    @ResponseBody
    @GetMapping("/{id:.+}")
    public AjaxResult getMeasureUnitMatrixById(@PathVariable String id) {
        return AjaxResult.success( measureUnitMatrixService.getById(id));
    }

    /**
     * 查詢所有數據
     *
     * @return
     */
    @ResponseBody
    @GetMapping("")
    public AjaxResult getMeasureUnitMatrixList(MeasureUnitMatrix measureUnitMatrix){
        List<MeasureUnitMatrix> result;
        QueryWrapper<MeasureUnitMatrix> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(measureUnitMatrix);
        result = measureUnitMatrixService.list(queryWrapper);
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
    public AjaxResult page(FrontPage<MeasureUnitMatrix> frontPage, MeasureUnitMatrix measureUnitMatrix){
        IPage result;
        QueryWrapper<MeasureUnitMatrix> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(measureUnitMatrix);
        if (frontPage.getGlobalQuery() != null && !"".equals(frontPage.getGlobalQuery().trim())) {
            //TODO modify global query
            queryWrapper.lambda().and(wrapper -> wrapper
                .like(MeasureUnitMatrix::getHandle, frontPage.getGlobalQuery())
                .or().like(MeasureUnitMatrix::getMeasureUnit, frontPage.getGlobalQuery())
                .or().like(MeasureUnitMatrix::getDestMeasureUnit, frontPage.getGlobalQuery())
    );
        }
        result = measureUnitMatrixService.page(frontPage.getPagePlus(), queryWrapper);
        return AjaxResult.success(result);
    }

    /**
     * 新增
     * @param measureUnitMatrix  傳遞的實體
     * @return  null 失敗  實體成功
     */
    @PostMapping
    @Secured("measureUnitMatrix_add")
    public AjaxResult save(@RequestBody MeasureUnitMatrix measureUnitMatrix) {
        return AjaxResult.success(measureUnitMatrixService.save(measureUnitMatrix));
    }

    /**
     * 修改
     * @param measureUnitMatrix  傳遞的實體
     * @return  null 失敗  實體成功
     */
    @PutMapping
    @Secured("measure_unit_matrix_edit")
    public AjaxResult updateById(@RequestBody MeasureUnitMatrix measureUnitMatrix) {
        return AjaxResult.success(measureUnitMatrixService.updateById(measureUnitMatrix));
    }

    /**
     * 根據id刪除對像
     * @param id  實體ID
     * @return 0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id:.+}")
    @Secured("measure_unit_matrix_delete")
    public AjaxResult removeById(@PathVariable("id") String id){
        return AjaxResult.success(measureUnitMatrixService.removeById(id));
    }

    /**
     * 批量刪除對像
     * @param ids 實體集合ID
     * @return  0 失敗  1 成功
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/delete-batch")
    @Secured("measure_unit_matrix_batch_delete")
    public AjaxResult removeByIds(List<String> ids){
        return AjaxResult.success(measureUnitMatrixService.removeByIds(ids));
    }
}