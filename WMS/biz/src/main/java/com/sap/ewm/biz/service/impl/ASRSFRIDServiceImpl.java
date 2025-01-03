package com.sap.ewm.biz.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sap.ewm.biz.constants.CommonConstants;
import com.sap.ewm.biz.model.AsrsRfid;
import com.sap.ewm.sys.service.MessageSendService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.sap.ewm.biz.mapper.ASRSFRIDMapper;
import com.sap.ewm.biz.service.ASRSFRIDService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.sap.ewm.core.utils.DateUtil;

/**
 * ASRS訂單物料主數據 服務實現類
 *
 * @author Glanz
 * @since 2020-04-20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ASRSFRIDServiceImpl extends ServiceImpl<ASRSFRIDMapper, AsrsRfid> implements ASRSFRIDService {

    @Autowired
    ASRSFRIDMapper asrsRFIDMapper;
    @Autowired
    MessageSendService messageSendService;
    @Autowired
    ASRSFRIDService asrsFRIDService;

    //放入資料庫
    @Override
    public void insertASRSRFID(AsrsRfid asrsRfid){
        asrsRFIDMapper.insert(asrsRfid);
    }

    //確認資料庫該RFID不存在
    @Override
    public boolean isExistingAsrsRfidInDB(String handle){
        AsrsRfid asrsRfid = asrsRFIDMapper.findAsrsRfidByID(handle);
        if(asrsRfid == null) return false;
        else return true;
    }

    //確認RFID是否存在重複值
    @Override
    public boolean hasDuplicateAsrsRfid(List<String> rfidList){
        for(int i=0; i<rfidList.size();i++){
            if(this.isExistingAsrsRfidInDB(rfidList.get(i))){
                return true;
            }
            for(int j=i+1; j<rfidList.size();j++){
                if(rfidList.get(i).equals(rfidList.get(j))){
                    return true;
                }
            }//End for(int j=i+1; j<rfidList.size();j++)
        }//End for(int i=0; i<rfidList.size();i++)
        return false;
    }

    // 要求將RFID資料傳給WCS
    @Override
    public boolean sentRFIDDataToWCS(String voucherNo, String operationType, String ioType, String carrier){
        // TYPE為"ADD"將憑單號的所有RFID送出，"DELETE"送出RFID空字串
        JSONArray dataList = new JSONArray();
        if(operationType.equals(CommonConstants.Operation_ADD)){
            if(ioType.equals(CommonConstants.Type_IN)){
                List<AsrsRfid> list = this.findRFIDByVoucherNo(voucherNo);
                if(list == null || list.size()==0);
                else{
                    for(int i=0; i<list.size();i++){
                        dataList.add(list.get(i).getHandle());
                    }
                }
            }
            else if(ioType.equals(CommonConstants.Type_OUT)){
                try{
                    List<AsrsRfid> list = this.findRFIDByCarrierWithStatus(carrier, CommonConstants.STATUS_OUT_STORAGE);
                    if(list == null || list.size()==0);
                    else{
                        for(int i=0; i<list.size();i++){
                            dataList.add(list.get(i).getHandle());
                        }
                    }
                }catch (Exception e){
                    JSONObject JsonTemp1 = new JSONObject();
                    JsonTemp1.put("QUEUE", "deployRoboticArmTask -e");
                    JsonTemp1.put("MESSAGE_BODY", e.getMessage());
                    JsonTemp1.put("CREATED_DATE_TIME", LocalDateTime.now().toString());
                    messageSendService.send(CommonConstants.MQ_LOG, JsonTemp1);
                }
            }
        }
        else if(operationType.equals(CommonConstants.Operation_DELETE));
        //送出MQ資料給WCS
        JSONObject JsonRFID = new JSONObject();
        JsonRFID.put("MESSAGE_ID", DateUtil.getDateTimeWithRandomNum());
        JsonRFID.put("MESSAGE_TYPE", CommonConstants.Request_Provide_RFID);
        JsonRFID.put("VOUCHER_NO", voucherNo);
        JsonRFID.put("DATA_LIST", dataList);
        JsonRFID.put("SEND_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
        messageSendService.send(CommonConstants.Request_Provide_RFID, JsonRFID);
        return true;
    }

    // 找指定RFID資料
    @Override
    public AsrsRfid findRFIDByID(String rfidID){
        return asrsRFIDMapper.findAsrsRfidByID(rfidID);
    }

    // 找所有符合HandlingID的RFID
    @Override
    public List<AsrsRfid> findRFIDByHandlingID(String handlingId){
        return asrsRFIDMapper.findRFIDByHandlingID(handlingId);
    }

    // 找所有符合HandlingID+指定狀態的RFID
    @Override
    public List<AsrsRfid> findRFIDByHandlingIDWithStatus(String handlingId, String status){
        List<AsrsRfid> list = asrsRFIDMapper.findRFIDByHandlingID(handlingId);
        ArrayList<AsrsRfid> returnList = new ArrayList<>();
        if(list==null || list.size()==0);
        else{
            for(int i=0; i<list.size();i++){
                if(list.get(i).getStatus().equals(status)){
                    returnList.add(list.get(i));
                }
            }
        }

        return returnList;
    }

    // 找所有不完全出庫還在棧板上的RFID應該有的voucherNo
    @Override
    public String findHandlingIDForRemainRFID(String handlingId){
        List<AsrsRfid> list = asrsRFIDMapper.findRFIDByHandlingID(handlingId); // 此為預計出庫的RFID清單
        if(list==null || list.size()==0) return null;
        else{
            String voucherNo = list.get(0).getVoucherNo();
            List<AsrsRfid> list2 = asrsRFIDMapper.findRFIDByVoucherNoWithStatus(voucherNo, CommonConstants.STATUS_OUT_STORAGE);
            if(list==null || list.size()==0) return null;
            else{
                return list2.get(0).getHandlingId();
            }
        }
    }

    // 找所有符合voucherNo的RFID
    @Override
    public List<AsrsRfid> findRFIDByVoucherNo(String voucherNo){
        return asrsRFIDMapper.findRFIDByVoucherNo(voucherNo);
    }

    // 找所有符合carrier+指定狀態的RFID
    @Override
    public List<AsrsRfid> findRFIDByCarrierWithStatus(String carrier, String status){
        return asrsRFIDMapper.findRFIDByCarrierWithStatus(carrier, status);
    }

    // 找所有符合voucherNo+指定狀態的RFID
    @Override
    public List<AsrsRfid> findRFIDByVoucherNoWithStatus(String voucherNo, String status){
        return asrsRFIDMapper.findRFIDByVoucherNoWithStatus(voucherNo, status);
    }

    // 將WCS讀取RFID清單更新RFID的狀態為指定狀態"OnPallet"
    // 綁定RFID清單與棧板時，更新RFID的狀態為指定狀態"BindPallet"
    // 狀態：Processing、OnPallet、BindPallet、IN_STORAGE、OUT_STORAGE、WAIT_OUT_STATION、OUT_STATION、OUT_STATION_NO_REPORT
    @Override
    public void updateRFIDStatus(List<String> rfidList, String pallet, String HandlingId, String status){
        AsrsRfid asrsRfid = new AsrsRfid();
        LocalDateTime now = LocalDateTime.now();
        for(int i=0; i<rfidList.size();i++){
            asrsRfid = asrsRFIDMapper.findAsrsRfidByID(rfidList.get(i));
            if(status.equals(CommonConstants.STATUS_ON_PALLET)){
                asrsRfid.setStatus(status);
                asrsRfid.setCarrier(pallet);
            }
            else if(status.equals(CommonConstants.STATUS_IN_STORAGE) || status.equals(CommonConstants.STATUS_OUT_STORAGE) ||
                    status.equals(CommonConstants.STATUS_OUT_STATION_NO_REPORT)){
                asrsRfid.setStatus(status);
            }
            else if(status.equals(CommonConstants.STATUS_WAIT_OUT_STATION)){
                asrsRfid.setStatus(status);
                asrsRfid.setHandlingId(HandlingId);
            }
            else if(status.equals(CommonConstants.STATUS_BIND_PALLET)){
                asrsRfid.setStatus(status);
                asrsRfid.setHandlingId(HandlingId);
            }
            else if(status.equals(CommonConstants.STATUS_OUT_STATION)){
                asrsRfid.setStatus(status);
                asrsRfid.setAsrsOrderBo(null);
                asrsRfid.setCarrier(null);
                asrsRfid.setHandlingId(null);
                asrsRfid.setNetWeight(null);
                asrsRfid.setMeasureUnit(null);
                asrsRfid.setVoucherNo(null);
            }

            asrsRfid.setUpdater(CommonConstants.UPDATE_USER);
            asrsRfid.setUpdateTime(now);
            asrsFRIDService.saveOrUpdate(asrsRfid);
        }//End for(int i=0; i<rfidList.size();i++)
    }

    // 找所有符合HandlingID的RFID
    @Override
    public JSONArray findRFIDByHandlingID(String handlingId, String type){
        JSONArray DATA_LIST = new JSONArray();
        List<AsrsRfid> list = asrsRFIDMapper.findRFIDByHandlingID(handlingId);

        if(list==null || list.size()==0);
        else{
            if(CommonConstants.Type_IN.equals(type)){
                String status = CommonConstants.STATUS_IN_STORAGE;
                for(int i=0; i<list.size();i++){
                    if(list.get(i).getStatus().equals(status)){
                        DATA_LIST.add(list.get(i).getHandle());
                    }
                }
            }
            else if(CommonConstants.Type_OUT.equals(type)){
                String status = CommonConstants.STATUS_OUT_STATION_NO_REPORT;
                ArrayList<String> listdata = new ArrayList<>();
                for(int i=0; i<list.size();i++){
                    if(list.get(i).getStatus().equals(status)){
                        DATA_LIST.add(list.get(i).getHandle());
                        listdata.add(list.get(i).getHandle());
                    }
                }
                this.updateRFIDStatus(listdata, null, null, CommonConstants.STATUS_OUT_STATION);
            }
        }
        return DATA_LIST;
    }
}
