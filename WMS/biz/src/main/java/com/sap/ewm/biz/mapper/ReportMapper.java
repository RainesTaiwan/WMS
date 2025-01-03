package com.sap.ewm.biz.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.sap.ewm.biz.dto.StayInReportDTO;
import com.sap.ewm.biz.dto.WarehouseReportDTO;
import com.sap.ewm.biz.vo.StayInReportVO;
import com.sap.ewm.biz.vo.WarehouseReportVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Ervin Chen
 * @date 2020/7/22 17:18
 */
@Repository
public interface ReportMapper {

    List<WarehouseReportVO> selectWarehouseInventoryList(@Param(Constants.WRAPPER) Wrapper<WarehouseReportDTO> wrapper);

    List<StayInReportVO> selectStayInInventoryList(@Param(Constants.WRAPPER) Wrapper<StayInReportDTO> wrapper);
}
