package com.sap.ewm.biz.service;

import com.sap.ewm.biz.dto.StayInReportDTO;
import com.sap.ewm.biz.dto.WarehouseReportDTO;
import com.sap.ewm.biz.vo.StayInReportVO;
import com.sap.ewm.biz.vo.WarehouseReportVO;

import java.util.List;

/**
 * @author Ervin Chen
 * @date 2020/7/22 17:20
 */
public interface ReportService {

    List<WarehouseReportVO> listWarehouseInventoryList(WarehouseReportDTO warehouseReportDTO);

    List<StayInReportVO> listStayInInventoryList(StayInReportDTO stayInReportDTO);
}
