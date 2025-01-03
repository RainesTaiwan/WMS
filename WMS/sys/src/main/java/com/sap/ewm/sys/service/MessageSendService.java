package com.sap.ewm.sys.service;

import com.alibaba.fastjson.JSONObject;

/**
 * @author syngna
 * @date 2020/3/27 11:05
 */
public interface MessageSendService {

    void send(String queue, JSONObject message);

    void sendMessage4Topic(String queueName, JSONObject message);

    JSONObject sendAndReceive(String queue, JSONObject message);
}
