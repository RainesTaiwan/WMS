package com.sap.ewm.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sap.ewm.biz.dto.StayInReportDTO;
import com.sap.ewm.biz.dto.WarehouseReportDTO;
import com.sap.ewm.biz.mapper.ReportMapper;
import com.sap.ewm.biz.service.ReportService;
import com.sap.ewm.biz.vo.StayInReportVO;
import com.sap.ewm.biz.vo.WarehouseReportVO;
import com.sap.ewm.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Ervin Chen
 * @date 2020/7/22 17:21
 */
@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    ReportMapper reportMapper;

    @Override
    public List<WarehouseReportVO> listWarehouseInventoryList(WarehouseReportDTO dto) {
        List<WarehouseReportVO> vos = new ArrayList<>();

        Wrapper<WarehouseReportDTO> wrapper = new QueryWrapper<WarehouseReportDTO>()
                .like(StringUtils.notBlank(dto.getWarehouse()), WarehouseReportDTO.WAREHOUSE, dto.getWarehouse())
                .like(StringUtils.notBlank(dto.getStorageLocation()), WarehouseReportDTO.STORAGE_LOCATION, dto.getStorageLocation())
                .like(StringUtils.notBlank(dto.getStorageBin()), WarehouseReportDTO.STORAGE_BIN, dto.getStorageBin())
                .like(StringUtils.notBlank(dto.getItem()), WarehouseReportDTO.ITEM, dto.getItem())
                .like(StringUtils.notBlank(dto.getCarrier()), WarehouseReportDTO.CARRIER, dto.getCarrier())
                .like(StringUtils.notBlank(dto.getBatchNo()), WarehouseReportDTO.BATCH_NO, dto.getBatchNo());
        reportMapper.selectWarehouseInventoryList(wrapper)
                .stream()
                .collect(Collectors.groupingBy(o -> String.join("-", o.getWarehouse(), o.getStorageLocation(), o.getStorageBin(), o.getCarrier()), Collectors.toList()))
                .forEach((key, subList) -> {
                    if(!subList.isEmpty()){
                        WarehouseReportVO vo = new WarehouseReportVO();
                        vo.setWarehouse(subList.get(0).getWarehouse());
                        vo.setStorageLocation(subList.get(0).getStorageLocation());
                        vo.setStorageLocationDescription(subList.get(0).getStorageLocationDescription());
                        vo.setStorageBin(subList.get(0).getStorageBin());
                        vo.setStorageBinDescription(subList.get(0).getStorageBinDescription());
                        vo.setStatus(subList.get(0).getStatus());
                        vo.setCarrier(subList.get(0).getCarrier());
                        subList.forEach(o -> {
                            o.setWarehouse("");
                            o.setStorageLocation("");
                            o.setStorageLocationDescription("");
                            o.setStorageBin("");
                            o.setStorageBinDescription("");
                            o.setStatus("");
                            o.setCarrier("");
                        });
                        vo.setChildren(subList);
                        vos.add(vo);
                    }
                });
        return vos;
    }

    @Override
    public List<StayInReportVO> listStayInInventoryList(StayInReportDTO dto) {
        List<StayInReportVO> vos = new ArrayList<>();

        Wrapper<StayInReportDTO> wrapper = new QueryWrapper<StayInReportDTO>()
                .like(StringUtils.notBlank(dto.getItem()), WarehouseReportDTO.ITEM, dto.getItem())
                .like(StringUtils.notBlank(dto.getCarrier()), WarehouseReportDTO.CARRIER, dto.getCarrier())
                .like(StringUtils.notBlank(dto.getBatchNo()), WarehouseReportDTO.BATCH_NO, dto.getBatchNo());
        reportMapper.selectStayInInventoryList(wrapper)
                .stream()
                .collect(Collectors.groupingBy(o -> o.getCarrier(), Collectors.toList()))
                .forEach((key, subList) -> {
                    if(!subList.isEmpty()){
                        StayInReportVO vo = new StayInReportVO();
                        vo.setCarrier(subList.get(0).getCarrier());
                        subList.forEach(o -> {
                            o.setCarrier("");
                        });
                        vo.setChildren(subList);
                        vos.add(vo);
                    }
                });
        return vos;
    }
}
