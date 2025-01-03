package com.fw.wcs.sys.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fw.wcs.core.constants.CustomConstants;
import com.fw.wcs.core.utils.DateUtil;
import com.fw.wcs.sys.mapper.RFIDReaderMaskMapper;
import com.fw.wcs.sys.model.RFIDReaderMask;
import com.fw.wcs.sys.model.RoboticArmTask;
import com.fw.wcs.sys.service.ActiveMqSendService;
import com.fw.wcs.sys.service.RFIDReaderMaskService;

import com.fw.wcs.sys.service.RoboticArmTaskService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;

/**
 *  服務實現類
 *
 * @author Glanz
 *
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class RFIDReaderMaskServiceImpl extends ServiceImpl<RFIDReaderMaskMapper, RFIDReaderMask> implements RFIDReaderMaskService {

    @Autowired
    private RFIDReaderMaskMapper rfidReaderMaskMapper;

    @Autowired
    private RFIDReaderMaskService rfidReaderMaskService;

    @Autowired
    private ActiveMqSendService activeMqSendService;

    @Autowired
    private RoboticArmTaskService roboticArmTaskService;

    // 刪除指定RFID清單
    // 由 voucherNo(憑單號) 決定一組 DataList(RFID Tags清單)
    @Override
    public void deleteRFID(String voucherNo){
        rfidReaderMaskMapper.deleteRFID(voucherNo);
    }

    // 存放RFID清單
    // 由 voucherNo(憑單號) 決定一組 DataList(RFID Tags清單)
    @Override
    public void insertRFIDTags(String voucherNo, JSONArray DataList){
        // 要先將舊有資料刪除，再將新資料存入
        this.deleteRFID(voucherNo);

        RFIDReaderMask rfidReaderMask = new RFIDReaderMask();
        rfidReaderMask.setVoucherNo(voucherNo);
        rfidReaderMask.setCreateUser("ADMIN");

        if (DataList.size()>0) {
            for (int i=0;i<DataList.size();i++){
                rfidReaderMask.setHandle(DateUtil.getDateTimeWithRandomNum());
                rfidReaderMask.setCreatedTime(new Date());
                rfidReaderMask.setRFID(DataList.getString(i));
                rfidReaderMask.setStatus(CustomConstants.RFID_UNREAD);//RFID MASK STATUS狀態：Read、UnRead
                rfidReaderMaskMapper.insert(rfidReaderMask);
            }
        }
    }

    // 更新RFID清單
    // 由 voucherNo(憑單號) 決定一組 DataList(RFID Tags清單)
    @Override
    public void updateRFIDTags(String voucherNo, JSONArray DataList, String pallet){

        RFIDReaderMask rfidReaderMask = new RFIDReaderMask();
        rfidReaderMask.setVoucherNo(voucherNo);
        rfidReaderMask.setPallet(pallet);
        rfidReaderMask.setStatus(CustomConstants.RFID_READ);//RFID MASK STATUS狀態：Read、UnRead
        rfidReaderMask.setUpdateUser("ADMIN");
        rfidReaderMask.setUpdatedTime(new Date());

        if (DataList.size()>0) {
            for (int i=0;i<DataList.size();i++){
                rfidReaderMask.setRFID(DataList.getString(i));

                Wrapper<RFIDReaderMask> wrapper = new EntityWrapper<>();
                wrapper.eq(RFIDReaderMask.RFID, DataList.getString(i));
                this.update(rfidReaderMask, wrapper);
            }
        }
    }

    //透過voucherNo找指定RFID MASK集合 告知WMS
    @Override
    public void reportReadRFIDList(String voucherNo){
        String carrier = "";
        //RFID READER 的 MASK
        JSONArray DATA_LIST = new JSONArray();
        List<RFIDReaderMask> list = rfidReaderMaskService.findRFIDList(voucherNo);
        if (list.size() > 0) {
            for (int i=0;i<list.size();i++){
                if(CustomConstants.RFID_READ.equals(list.get(i).getStatus())){  //RFID MASK STATUS狀態：Read、UnRead
                    DATA_LIST.add(list.get(i).getRFID());
                    if(list.get(i).getPallet().length()>0)  carrier = list.get(i).getPallet();
                }
            }

            JSONObject JsonTemp = new JSONObject();
            JsonTemp.put("QUEUE", "reportReadRFIDList - RFID");
            JsonTemp.put("MESSAGE_BODY", DATA_LIST);
            JsonTemp.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
            activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonTemp.toJSONString());
        }

        //告知WMS
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("MESSAGE_TYPE", "Get.RFID.Tags");
        jsonObject.put("MESSAGE_ID", "WCS_"+DateUtil.getDateTimeWithRandomNum());
        jsonObject.put("VOUCHER_NO", voucherNo);
        jsonObject.put("PALLET",carrier);
        jsonObject.put("DATA_LIST", DATA_LIST); //RFID READER的序號
        jsonObject.put("SEND_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
        activeMqSendService.sendMsgNoResponse4Wms("Get.RFID.Tags", jsonObject.toJSONString());
    }

    // 找到指定工單的RFID清單
    @Override
    public List<RFIDReaderMask> findRFIDList(String voucherNo){
        return rfidReaderMaskMapper.findRFIDList(voucherNo);
    }

    // 模擬RFID Reader上位機 - 給定voucherNo傳送RFID清單
    @Override
    public void SimulationRFIDReaderTransTags(String voucherNo, String messageId, String resource){
        // carrier資料
        int num = (int)(Math.random()*2000)+1; //產生1~10000的數值
        String pattern = "%05d"; // 格式化字串，整數，長度4，不足部分左邊補0
        String carrier = "ASRS_PALLET_" + String.format(pattern, num);

        JSONObject JsonTemp = new JSONObject();
        JsonTemp.put("QUEUE", "SimulationRFIDReaderTransTags");
        JsonTemp.put("MESSAGE_BODY", "voucherNo: "+ voucherNo+", messageId: "+messageId+", resource:"+resource);
        JsonTemp.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
        activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonTemp.toJSONString());

        // 確認要取得的RFID數量
        List<RoboticArmTask> rmtList =roboticArmTaskService.findRoboticArmTaskByVoucherNo(voucherNo);
        int getRFIDTotalNum = 0;
        int getRFIDNum = 0;
        if(rmtList.size()==0 || rmtList==null);
        else{
            // 取出DO_QTY資料，轉成JSONArray格式
            String dataArray_DOQTY = "{\"QTY\":"+rmtList.get(0).getDoQty()+"}";
            JSONObject JsonNewData = JSON.parseObject(dataArray_DOQTY);
            JSONArray data = JsonNewData.getJSONArray("QTY");
            for(int i=0;i<data.size();i++){
                int j = Integer.parseInt(data.getString(i));
                if(j>0)    getRFIDTotalNum = j;
            }
        }

        //RFID READER 的 MASK
        JSONArray DATA_LIST = new JSONArray();
        List<RFIDReaderMask> list = rfidReaderMaskService.findRFIDList(voucherNo);
        if (list.size() > 0) {
            // 讓同一批工作的RFID要同一棧板ID
            for (int i=0;i<list.size();i++){
                if(list.get(i).getPallet()==null);
                else{
                    carrier = list.get(i).getPallet();
                }
            }
            for (int i=0;i<list.size();i++){
                if(getRFIDNum< getRFIDTotalNum){
                    DATA_LIST.add(list.get(i).getRFID());
                    getRFIDNum++;
                }
            }
            JsonTemp.put("QUEUE", "SimulationRFIDReaderTransTags - RFID");
            JsonTemp.put("MESSAGE_BODY", DATA_LIST.toString());
            JsonTemp.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
            activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonTemp.toJSONString());
        }

        JSONArray CARRIER_LIST = new JSONArray();
        CARRIER_LIST.add(carrier);
        //傳送MQ
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("MESSAGE_TYPE", "Conveyor.Tags.WCS");
        jsonObject.put("CORRELATION_ID", messageId);
        jsonObject.put("RESOURCE",resource);//RFID READER的序號
        jsonObject.put("CARRIER", carrier);
        //jsonObject.put("CARRIER", CARRIER_LIST);
        jsonObject.put("DATA_LIST", DATA_LIST.toString()); //RFID Tags清單
        jsonObject.put("ALARM_TYPE", "0"); //ALARM_TYPE: 0: 無異常；1:沒讀到棧板標籤；2:讀到多個棧板標籤
        jsonObject.put("MSG", ""); //錯誤訊息
        jsonObject.put("SEND_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
        activeMqSendService.sendMsgNoResponse4Wms("Conveyor.Tags.WCS", jsonObject.toJSONString());
    }
}
