package com.sap.ewm.dashboard.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sap.ewm.dashboard.model.CarrierUsage;
import com.sap.ewm.dashboard.model.InventorySummary;
import com.sap.ewm.dashboard.model.StorageLocationUsage;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * inventory summary by day Mapper 介面
 * </p>
 *
 * @author syngna
 * @since 2020-08-03
 */
@Repository
public interface DashboardMapper extends BaseMapper<InventorySummary> {

    CarrierUsage calculateUsage();

    List<StorageLocationUsage> calculateStorageLocationUsage();
}