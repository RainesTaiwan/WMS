package com.sap.ewm.dashboard.handlers;

import com.sap.ewm.core.annotation.WebSocketHandler;
import com.sap.ewm.core.websocket.*;
import com.sap.ewm.dashboard.dataprocessors.DayInfraDataProcessor;
import com.sap.ewm.dashboard.service.CarrierStatusService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * @program: wms
 * @description: Invenotry Change Handler
 * @author: syngna
 * @create: 2020-08-02 15:36
 */
@WebSocketHandler(channel = "dayInfra")
public class DayInfraHandler extends WebSocketWrapper {

    public long delay = 5;

    public Date date = null;

    @Autowired
    DayInfraDataProcessor dayInfraDataProcessor;

    @Autowired
    CarrierStatusService carrierStatusService;

    @Override
    public void init() {
        this.channel = String.join("@" , this.channel);
    }

    @Override
    public void onMessage(WsRequest request) {

    }

    @Override
    public void doAfterOpen(Connection connection) {
        WsRequest wsRequest = WsRequest.create()
                .put("channel" , this.channel)
                .put("date", date);
        DataProcessManager.addDataProcessor(this.channel, this.delay, wsRequest,
                dayInfraDataProcessor::inventoryDailySummaryMonitor,
                dayInfraDataProcessor::carrierUsageMonitor,
                dayInfraDataProcessor::storageBinUsageMonitor
                );

        // carrier status change
        WebsocketUtil.publishMessage(channel, WsResult.ok("carrier_status_change", carrierStatusService.list()));
    }

    @Override
    public void doAfterClose() {
        DataProcessManager.removeDataProcessor(this.channel);
    }
}