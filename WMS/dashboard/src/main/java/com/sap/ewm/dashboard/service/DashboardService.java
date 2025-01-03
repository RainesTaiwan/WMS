package com.sap.ewm.dashboard.service;

import com.sap.ewm.dashboard.model.CarrierUsage;
import com.sap.ewm.dashboard.model.StorageLocationUsage;

import java.util.List;

/**
 * <p>
 * dashboard service
 * </p>
 *
 * @author syngna
 * @since 2020-08-06
 */
public interface DashboardService {

    CarrierUsage calculateUsage();

    List<StorageLocationUsage> calculateStorageBinUsage();
}