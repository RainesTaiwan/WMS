package com.fw.wcs.sys.dto;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * WCS與VMS系統互動事件參數類
 *
 */
public class TransportCommandDto extends CommonDto{

    /**
     * AGV小車編號
     */
    @JSONField(name = "VEHICLE_ID")
    private String VehicleId;
    /**
     * 運輸任務型別
     *  'TRANSPORT'/'CHARGING'/'MAINTENANCE'/'STANDBY'
     */
    @JSONField(name = "TASK_TYPE")
    private String TaskType;
    /**
     * 載具編號
     */
    @JSONField(name = "CARRIER_ID")
    private String CarrierId;
    /**
     * 任務開始站點
     */
    @JSONField(name = "FROM_NODE_NO")
    private String FromNodeNo;
    /**
     * 任務目的站點
     */
    @JSONField(name = "TO_NODE_NO")
    private String ToNodeNo;

    public String getVehicleId() {
        return VehicleId;
    }

    public void setVehicleId(String vehicleId) {
        VehicleId = vehicleId;
    }

    public String getTaskType() {
        return TaskType;
    }

    public void setTaskType(String taskType) {
        TaskType = taskType;
    }

    public String getCarrierId() {
        return CarrierId;
    }

    public void setCarrierId(String carrierId) {
        CarrierId = carrierId;
    }

    public String getFromNodeNo() {
        return FromNodeNo;
    }

    public void setFromNodeNo(String fromNodeNo) {
        FromNodeNo = fromNodeNo;
    }

    public String getToNodeNo() {
        return ToNodeNo;
    }

    public void setToNodeNo(String toNodeNo) {
        ToNodeNo = toNodeNo;
    }
}
