package com.sap.ewm.biz.constants;

public interface CommonConstants {

    String Y = "Y";
    String N = "N";
    String STATUS_IDLE = "IDLE";
    String STATUS_IN_USE = "IN_USE";
    String STATUS_BE_PUT_IN_STORAGE = "BE_PUT_IN_STORAGE";
    String STATUS_IN_STORAGE = "IN_STORAGE";
    String STATUS_OUT_STORAGE = "OUT_STORAGE";
    String STATUS_WAIT_IN_STORAGE = "WAIT_IN_STORAGE";
    String STATUS_WAIT_OUT_STORAGE = "WAIT_OUT_STORAGE";
    String STATUS_OUT_STATION = "OUT_STATION";
    String STATUS_OUT_STATION_NO_REPORT = "OUT_STATION_NO_REPORT";
    String STATUS_WAIT_OUT_STATION = "WAIT_OUT_STATION";
    String STATUS_EXCHANGE = "EXCHANGE";
    String STATUS_UNKNOWN = "UNKNOWN";
    String STATUS_NEW = "NEW";
    String STATUS_START = "START";
    String STATUS_END = "END";
    String STATUS_ASSIGN = "ASSIGN";
    String STATUS_COMPLETE = "COMPLETE";
    String STATUS_UNCOMPLETE = "UNCOMPLETE";
    String STATUS_ERROR_END = "ERROR_END";
    String STATUS_PROCESSING = "PROCESSING";
    String STATUS_ON_PALLET = "OnPallet";
    String STATUS_BIND_PALLET = "BindPallet";


    String WCS_USER = "WCS";
    String UPDATE_USER = "ADMIN";
    String CREATE_USER = "Administrator";

    String MQ_ORIGIN_CORRELATION_CACHE = "MQ_ORIGIN_CORRELATION_CACHE_";

    //LOG
    String MQ_LOG = "MQ_LOG";

    //WMS<>ASRS QUEUE NAME
    String ASRSRequest = "ASRS.Request";
    String ASRS_RequestAck = "ASRS.Request.Ack";
    String ASRS_RequestAlarm = "ASRS.Request.Alarm";
    String ASRS_Conveyor_Location = "Conveyor.Location.ASRS";// 告知ASRS 選擇輸送帶
    String ASRS_WOResult = "WO.Result";// 告知ASRS 工單最終結果


    //WMS<>WCS QUEUE NAME
    String Request_RoboticArm = "WMS.Request.Robotic.Arm";// 告知WCS 機械手臂任務//Provide.RFID.Tags
    String Request_Provide_RFID = "Provide.RFID.Tags";// 告知WCS工單的RFID
    String Request_Conveyor_Status = "WMS.Request.Conveyor.Status"; // 向WCS詢問輸送帶的在席SENSOR
    String Conveyor_To_StorageBin = "Conveyor.To.Storage.Bin";
    String StorageBin_To_Conveyor = "Storage.Bin.To.Conveyor";
    String Get_Pallet_ID = "Get.Pallet.ID";

    //ASRS
    String VERIFY_VALID = "Valid";
    String VERIFY_INVALID = "Invalid";

    //ASRS Order Type
    String OrderType1 = "WO1"; //整棧入庫
    String OrderType2 = "WO2"; //整棧出庫
    String OrderType3 = "WO3"; //散貨入庫
    String OrderType4 = "WO4"; //散貨出庫
    String OrderType5 = "WO5"; //理貨入庫
    String OrderType6 = "WO6"; //單箱出庫
    String OrderType7 = "WO7"; //盤點

    // RFID
    String Operation_ADD = "ADD";
    String Operation_DELETE = "DELETE";

    // Type
    String Type_IN = "IN";
    String Type_OUT = "OUT";



    // 預設等待時間
    long WAITING_Processing = 1000;    // 模擬
}
