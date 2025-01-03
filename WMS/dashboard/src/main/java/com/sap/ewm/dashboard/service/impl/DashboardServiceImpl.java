package com.sap.ewm.dashboard.service.impl;

import com.sap.ewm.dashboard.mapper.DashboardMapper;
import com.sap.ewm.dashboard.model.CarrierUsage;
import com.sap.ewm.dashboard.model.StorageLocationUsage;
import com.sap.ewm.dashboard.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * handling unit stataus summary 服務實現類
 * </p>
 *
 * @author syngna
 * @since 2020-08-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DashboardServiceImpl  implements DashboardService {


    @Autowired
    private DashboardMapper dashboardMapper;


    @Override
    public CarrierUsage calculateUsage() {
        return dashboardMapper.calculateUsage();
    }

    @Override
    public List<StorageLocationUsage> calculateStorageBinUsage() {
        return dashboardMapper.calculateStorageLocationUsage();
    }
}