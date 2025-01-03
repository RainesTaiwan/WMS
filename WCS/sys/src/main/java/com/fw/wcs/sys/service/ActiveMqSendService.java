package com.fw.wcs.sys.service;

public interface ActiveMqSendService {

    void sendMessage4Topic(String queueName, String text);

    //發送訊息給接駁站、碼垛機、AGV系統
    void sendMsgNoResponse4Res(String queueName, String message);

    //發送訊息給接駁站、碼垛機、AGV系統,需要回復
    String sendMsgNeedResponse4Res(String queueName, String message);

    //發送訊息到MQ， 不需要等回覆
    void sendMsgNoResponse4Wms(String queueName, String message);

    //發送訊息到MQ， 需要等回覆
    String sendMsgNeedResponse4Wms(String queueName, String message);

    void sendRemoteStopCommand(String resource, String station, String message);
}
