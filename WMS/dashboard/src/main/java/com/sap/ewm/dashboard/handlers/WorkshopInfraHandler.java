package com.sap.ewm.dashboard.handlers;

import com.sap.ewm.core.annotation.WebSocketHandler;
import com.sap.ewm.core.websocket.*;

/**
 * @program: wms
 * @description: Workshop Infra Handler
 * @author: syngna
 * @create: 2020-08-02 15:36
 */
@WebSocketHandler(channel = "dash.board.data")
public class WorkshopInfraHandler extends WebSocketWrapper {

    @Override
    public void init() {
        this.channel = String.join("@" , this.channel);
    }

    @Override
    public void onMessage(WsRequest request) {

    }

    @Override
    public void doAfterOpen(Connection connection) {

    }

    @Override
    public void doAfterClose() {
        DataProcessManager.removeDataProcessor(this.channel);
    }
}