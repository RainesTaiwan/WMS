package com.sap.ewm.dashboard.dataprocessors;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sap.ewm.core.websocket.WebsocketUtil;
import com.sap.ewm.core.websocket.WsRequest;
import com.sap.ewm.core.websocket.WsResult;
import com.sap.ewm.dashboard.model.InventorySummary;
import com.sap.ewm.dashboard.service.DashboardService;
import com.sap.ewm.dashboard.service.InventorySummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * @program: wms
 * @description: Inventory change data processor
 * @author: syngna
 * @create: 2020-08-02 15:51
 */
@Component
public class DayInfraDataProcessor {

    @Autowired
    InventorySummaryService inventorySummaryService;

    @Autowired
    DashboardService dashboardService;

    public void inventoryDailySummaryMonitor(WsRequest wsRequest) {
        String channel = (String) wsRequest.get("channel");
        // LocalDate date = (LocalDate) wsRequest.get("date");
        LocalDate date = LocalDate.now();
        WebsocketUtil.publishMessage(channel, WsResult.ok("inventory_daily_summary", inventorySummaryService.list(Wrappers.<InventorySummary>lambdaQuery()
                .eq(InventorySummary::getStatisticDate, date))));
    }

    public void carrierUsageMonitor(WsRequest wsRequest) {
        String channel = (String) wsRequest.get("channel");
        WebsocketUtil.publishMessage(channel, WsResult.ok("carrier_usage", dashboardService.calculateUsage()));
    }

    public void storageBinUsageMonitor(WsRequest wsRequest) {
        String channel = (String) wsRequest.get("channel");
        WebsocketUtil.publishMessage(channel, WsResult.ok("storage_location_usage", dashboardService.calculateStorageBinUsage()));
    }
}