package com.sap.ewm.biz.controller;

import com.sap.ewm.biz.dto.StayInReportDTO;
import com.sap.ewm.biz.dto.WarehouseReportDTO;
import com.sap.ewm.biz.service.ReportService;
import com.sap.ewm.biz.vo.StayInReportVO;
import com.sap.ewm.biz.vo.WarehouseReportVO;
import com.sap.ewm.core.base.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Ervin Chen
 * @date 2020/7/22 18:04
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @PostMapping("/warehouse/inventory/list")
    public AjaxResult getWarehouseInventoryList(@RequestBody WarehouseReportDTO dto){
        List<WarehouseReportVO> vos = reportService.listWarehouseInventoryList(dto);
        return AjaxResult.success(vos);
    }

    @PostMapping("/stayIn/inventory/list")
    public AjaxResult getStayInInventoryList(@RequestBody StayInReportDTO dto){
        List<StayInReportVO> vos = reportService.listStayInInventoryList(dto);
        return AjaxResult.success(vos);
    }
}
