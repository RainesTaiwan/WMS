package com.fw.wcs.sys.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.fw.wcs.core.constants.CustomConstants;
import com.fw.wcs.core.exception.BusinessException;
import com.fw.wcs.core.utils.DateUtil;
import com.fw.wcs.sys.mapper.RFIDReaderTaskMapper;
import com.fw.wcs.sys.model.RFIDReaderMask;
import com.fw.wcs.sys.model.RFIDReaderTask;
import com.fw.wcs.sys.service.*;
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
public class RFIDReaderTaskServiceImpl extends ServiceImpl<RFIDReaderTaskMapper, RFIDReaderTask> implements RFIDReaderTaskService {

    @Autowired
    private RFIDReaderTaskMapper rfidReaderTaskMapper;

    @Autowired
    private RFIDReaderService rfidReaderService;

    @Autowired
    private RFIDReaderMaskService rfidReaderMaskService;

    @Autowired
    private ActiveMqSendService activeMqSendService;


    // 建立任務
    @Override
    public void createRFIDReaderTask(String voucherNo, String receiveStation, String station, String type){
        // 透過receiveStation與station確認要使用的RFIDReaderID
        // 再透過voucherNo與RFIDReaderID建立任務，並要求開啟指定RFIDReaderID
        // type有GetPalletID、RoboticArm、NoRoboticArm

        String RFIDReaderID = rfidReaderService.getRFIDReaderID(receiveStation, station);
        String messageID = "WCS_" + DateUtil.getDateTimeWithRandomNum();

        RFIDReaderTask rfidReaderTask = new RFIDReaderTask();
        rfidReaderTask.setHandle(DateUtil.getDateTimeWithRandomNum());
        rfidReaderTask.setVoucherNo(voucherNo);
        rfidReaderTask.setMessageID(messageID);
        rfidReaderTask.setReaderID(RFIDReaderID);
        rfidReaderTask.setStatus(CustomConstants.NEW);
        rfidReaderTask.setType(type);
        rfidReaderTask.setCreateUser("ADMIN");
        rfidReaderTask.setCreatedTime(new Date());
        rfidReaderTask.setUpdateUser("ADMIN");
        rfidReaderTask.setUpdatedTime(new Date());
        rfidReaderTaskMapper.insert(rfidReaderTask);

        //RFID READER 的 MASK
        JSONArray DATA_LIST = new JSONArray();
        List<RFIDReaderMask> list = rfidReaderMaskService.findRFIDList(voucherNo);
        if (list.size() > 0) {
            for (int i=0;i<list.size();i++){
                DATA_LIST.add(list.get(i).getRFID());
            }
        }

        //要求開啟指定RFIDReaderID
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("MESSAGE_TYPE", "Conveyor.Reader");
        jsonObject.put("MESSAGE_ID", messageID);
        jsonObject.put("START","1");
        jsonObject.put("RESOURCE", RFIDReaderID); //RFID READER的序號
        jsonObject.put("DATA_LIST", DATA_LIST); //RFID Tags清單
        jsonObject.put("SEND_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
        activeMqSendService.sendMsgNoResponse4Wms("Conveyor.Reader", jsonObject.toJSONString());
        /*
        JSONObject JsonE = new JSONObject();
        JsonE.put("QUEUE", "createRFIDReaderTask");
        JsonE.put("MESSAGE_BODY", jsonObject.toJSONString());
        JsonE.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
        activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.MQLOG, JsonE.toJSONString());*/
    }

    //透過MessageID找指定任務
    @Override
    public RFIDReaderTask findTaskByMessageID(String messageID){
        //return rfidReaderTaskMapper.findTaskByMessageID(messageID);
        List<RFIDReaderTask> list = rfidReaderTaskMapper.findTaskByMessageID(messageID);
        if(list.size()>0)   return list.get(0);
        return null;
    }

    //透過MessageID更新任務
    @Override
    public void updateTaskByMessageID(String messageID, RFIDReaderTask rfidReaderTask){
        rfidReaderTask.setMessageID(messageID);
        rfidReaderTask.setUpdateUser("ADMIN");
        rfidReaderTask.setUpdatedTime(new Date());

        Wrapper<RFIDReaderTask> wrapper = new EntityWrapper<>();
        wrapper.eq(RFIDReaderTask.MESSAGE_ID, rfidReaderTask.getMessageID());
        this.update(rfidReaderTask, wrapper);
    }

    //透過rfid找指定任務
    @Override
    public RFIDReaderTask findTaskByRFID(String rfidId){
        List<RFIDReaderTask> list = rfidReaderTaskMapper.findTaskByRFID(rfidId);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
}
