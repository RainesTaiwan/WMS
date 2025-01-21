package com.fw.wcs.core.constants;

public class CustomConstants {

    public static final String UPDATE_USER = "ADMIN";
    public static final String CREATE_USER = "Administrator";

    //QUEUE NAME
    public static final String REMOTE_COMMAND = "remote.command.process";

    //等待時間
    public static final long WAITINGRFID_GetPalletID = 1000;  // 等待讀取棧板時間
    public static final long WAITING_RFID_Update = 2000;      // 等待RFID更新
    public static final long WAITING_Conveyor_CarrierInfo = 500;      // 等待輸送帶讀取棧板
    public static final long WAITINGAGV_Simulation = 500;     // 模擬
    public static final long WAITING_Processing = 1000;    // 模擬
    public static final long WAITING_Conveyor_Transfer1 = 35000;    // 確認移動完畢 (等待35,000毫秒+30,000毫秒)
    public static final long WAITING_Conveyor_Transfer2 = 65000;    // 確認移動完畢 (等待35,000毫秒+30,000毫秒)

    //WCS<>LOG QUEUE NAME
    public static final String MQLOG = "MQ_LOG";

    //WCS<>WMS QUEUE NAME
    public static final String DASH_BOARD_DATA = "dash.board.data";
    public static final String HANDLING_UNIT_INFO = "handling.unit.info.process";
    public static final String HANDLING_UNIT_IN_REQUEST = "handling.unit.in.request";
    public static final String HANDLING_UNIT_IN_STORAGE = "handling.unit.in.storage";
    public static final String HANDLING_UNIT_OUT_STORAGE = "handling.unit.out.storage";
    public static final String HANDLING_UNIT_OUT_STATION = "handling.unit.out.station";

    //WCS<>VMS QUEUE NAME
    public static final String AGVReportWCS = "AGV.Report.WCS";
    public static final String AGVReportWCSAck = "AGV.Report.WCS.Ack";
    //public static final String RequestAGV = "Request.AGV";
    public static final String RequestAGV = "WCS-AGV-2";
    public static final String RequestAGVAck = "Request.AGV.Ack";
    public static final String AGVAlarmWCS = "AGV.Alarm.WCS";
    public static final String AGVAlarmWCSAck = "AGV.Alarm.WCS.Ack";
    public static final String AGVStatusWCS = "AGV.Status.WCS";
    public static final String AGVStatusWCSAck = "AGV.Status.WCS.Ack";
    //public static final String TransportCommand = "TransportCommand";
    //public static final String TransportStateReport = "TransportStateReport";

    //WCS<>Monitoring QUEUE NAME
    public static final String MonitoringRequest = "Surveillance.Request";
    public static final String MonitoringRequestAck = "Surveillance.Request.Ack";

    //WCS<>Conveyor QUEUE NAME
    public static final String ConveyorReportWCS = "carrier.on.machine";
    public static final String Conveyor_CarrierInfo = "carrier.info.report";

    //WCS<>ROBOT_ARM QUEUE NAME
    public static final String RequestRobotArm = "Request.Robotic.Arm";
    public static final String RequestRobotArmACK = "Request.Robotic.Arm.Ack";
    public static final String RobotArmReportWCS = "Robotic.Arm.Report.WCS";
    public static final String RobotArmReportWCSACK = "Robotic.Arm.Report.WCS.Ack";
    public static final String RoboticArmStatusWCS = "Robotic.Arm.Status.WCS";

    //任務狀態
    public static final String NEW = "NEW";
    public static final String START = "START";
    public static final String COMPLETE = "COMPLETE";
    public static final String UNCOMPLETE = "UNCOMPLETE";
    public static final String END = "END";

    //設備狀態
    public static final String IDLE = "IDLE";
    public static final String WORKING = "WORKING";
    public static final String ALARM = "ALARM";
    public static final String ALARM_DISCONNECT = "DISCONNECT";

    //RFID TAG狀態
    public static final String RFID_READ = "Read";
    public static final String RFID_UNREAD = "UnRead";

    //AGV狀態
    public static final String AGV_MAINTENANCE = "MAINTENANCE";
    public static final String AGV_IDLE = "IDLE";
    public static final String AGV_RUN = "RUN";
    public static final String AGV_DOWN = "DOWN";
    public static final String AGV_WALKING = "WALKING";
    public static final String AGV_CHARGING = "CHARGING";
    public static final String AGV_START = "START";
    public static final String AGV_FROM_LEFT = "FROM_LEFT";
    public static final String AGV_TO_LEFT = "TO_LEFT";
    public static final String AGV_FROM_ARRIVED = "FROM_ARRIVED";
    public static final String AGV_TO_ARRIVED = "TO_ARRIVED";
    public static final String AGV_END = "END";

    //RoboticArm狀態

    //出入庫型別
    public static final String TYPE_IN = "IN";
    public static final String TYPE_OUT = "OUT";

}
