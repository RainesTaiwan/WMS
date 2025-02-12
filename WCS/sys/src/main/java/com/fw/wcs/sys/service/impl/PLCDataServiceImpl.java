package com.fw.wcs.sys.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fw.wcs.core.base.FrontPage;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fw.wcs.core.constants.CustomConstants;
import com.fw.wcs.core.exception.BusinessException;
import com.fw.wcs.core.utils.DateUtil;
import com.fw.wcs.core.utils.StringUtils;
import com.fw.wcs.sys.model.*;
import com.fw.wcs.sys.mapper.PLCDataMapper;
import com.fw.wcs.sys.service.*;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.lang.*;
import java.time.LocalDateTime;

/**
 *  服務實現類
 *
 * @author Glanz
 *
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class PLCDataServiceImpl extends ServiceImpl<PLCDataMapper, PLCData> implements PLCDataService{
    @Autowired
    private PLCDataMapper plcDataMapper;
    @Autowired
    private ReceiveStationService receiveStationService;
    @Autowired
    private ReceiveStationTaskService receiveStationTaskService;
    @Autowired
    private RoboticArmTaskService roboticArmTaskService;
    @Autowired
    private PLCDataService plcDataService;
    @Autowired
    private ActiveMqSendService activeMqSendService;
    @Autowired
    private PressButtonService pressButtonService;
    @Autowired
    private RFIDReaderTaskService rfidReaderTaskService;


    //透過輸送帶找尋PLCData
    @Override
    public PLCData findPLCDataByConveyor(String conveyor){
        List<PLCData> list = plcDataMapper.findPLCDataByConveyor(conveyor);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    //將plcData存入
    @Override
    public void insertPLCData(PLCData plcData){
        plcDataMapper.insert(plcData);
    }

    // 將16 位元的int轉成 2個 Byte
    @Override
    public byte[] hexIntToByteArray(int value) {
        byte[] byteArray = new byte[2];

        byteArray[1] = (byte) (value >> 8);
        byteArray[0] = (byte) (value);

        return byteArray;
    }

    // 將  32 位元 共 4個Byte/16 位元 共 2個Byte 轉成 int，num表示有幾個Byte
    @Override
    public int byteArrayToInt(byte[] hexByte, int num){
        int resoultInt = 0;
        if(num==4){
            resoultInt = ((hexByte[3] & 0xFF) << 24 ) | ((hexByte[2] & 0xFF) << 16) |
                         ((hexByte[1] & 0xFF) << 8  ) | ((hexByte[0] & 0xFF) << 0 );
        }
        else if(num==2){
            resoultInt = (((hexByte[1] & 0xFF) << 8 ) | ((hexByte[0] & 0xFF) << 0 ));
        }
        return  resoultInt;
    }

    // plcData進行判斷處理
    @Override
    public void readPLCData(String lenNum, List<String> dataList){
        // 確認輸送帶ID
        String receiveStationId = "";
        if("1".equals(lenNum))  receiveStationId = "Conveyor1";
        else if("2".equals(lenNum))  receiveStationId = "Conveyor2";
        else if("3".equals(lenNum))  receiveStationId = "Conveyor3";
        else if("4".equals(lenNum))  receiveStationId = "Conveyor4";
        else if("5".equals(lenNum))  receiveStationId = "Conveyor5";

        this.analyzePLCData(receiveStationId, dataList);
    }

    // plc 資料分析
    @Override
    public void analyzePLCData(String conveyor, List<String> dataList){
        // 將PLC資料轉為INT
        int[] plcDataIntList = new int[dataList.size()];
        for (int i = 0; i < dataList.size(); i++){
            plcDataIntList[i] = Integer.parseInt(dataList.get(i));
        }

        ReceiveStation receiveStation = receiveStationService.getReceiveStation(conveyor);

        //D3018    0=設備開機啟動中；1=設備開機完成/待命中；2=設備故障；3=設備正在移動棧板
        receiveStation.setState(dataList.get(18));
        if(plcDataIntList[18]==0 || plcDataIntList[18]==2){
            // 若感測到上一個監測時間已是異常，則不再重複發送
            if(CustomConstants.ALARM.equals(receiveStation.getStatus()));
            else{
                receiveStation.setStatus(CustomConstants.ALARM);
                receiveStationService.reportASRS(conveyor, CustomConstants.ALARM);
            }
        }
        else if(plcDataIntList[18]==3){
            // 若感測到上一個監測時間已運作，則不再重複發送
            if(CustomConstants.WORKING.equals(receiveStation.getStatus()));
            else{
                receiveStation.setStatus(CustomConstants.WORKING);
                receiveStationService.reportASRS(conveyor, CustomConstants.WORKING);
            }
        }
        else if(plcDataIntList[18]==1){
            // 若感測到上一個監測時間是ALARM，則重新設置IDLE
            if(CustomConstants.ALARM.equals(receiveStation.getStatus()) ||
                    CustomConstants.ALARM_DISCONNECT.equals(receiveStation.getStatus())){
                // 上一個時間點是ALARM，下一個時間點恢復正常
                if("".equals(receiveStation.getTaskGoal())){
                    receiveStation.setStatus(CustomConstants.IDLE);
                    receiveStationService.reportASRS(conveyor, CustomConstants.IDLE);
                }
                else{
                    receiveStation.setStatus(CustomConstants.WORKING);
                    receiveStationService.reportASRS(conveyor, CustomConstants.WORKING);
                }
            }
            // 除了ALARM，若工作結束應該發出IDLE通知
            else if("".equals(receiveStation.getTaskGoal())){
                if(CustomConstants.IDLE.equals(receiveStation.getStatus()));
                else{
                    receiveStation.setStatus(CustomConstants.IDLE);
                    receiveStationService.reportASRS(conveyor, CustomConstants.IDLE);
                }
            }
        }

        // (for read)CV1的狀態 D3046-D3048 D3061
        /* D3046    DIRECTION Status(運輸方向)    1:CV1-->CV2、2:CV1-->退出
           D3047    HOLD status(設備工作遏制) 當HOLD值=0時，設備照常運作
                                            當HOLD值=1時，設備暫時停止MOVE=1的驅動，其他Senor數值持續更新
           D3048    MOVE Query(驅動棧板移動詢問)   MOVE值=1時為Ready ；MOVE完成後更新狀態為1
                                                MOVE值=2時為MOVE Query
                                                MOVE值=3時為傳輸異常
                                                MOVE值=4時CV運轉中
           D3061    Sensor(SENSOR 狀態表示)
                    出庫在席=1->BIT[0] : 0 入庫減速/OFF，1 入庫減速/ON
                    出庫減速=2->BIT[1] : 0 入庫在席/OFF，1 入庫在席/ON
                    入庫減速=4->BIT[2] : 0 出庫減速/OFF，1 出庫減速/ON
                    入庫在席=8->BIT[3] : 0 出庫在席/OFF，1 出庫在席/ON
                    當有複數SENSOR狀態時，會由複數的數值總和表示該CV1 SENSOR狀態
        */
        receiveStation.setCv1Direction(dataList.get(46));
        receiveStation.setCv1Hold(dataList.get(47));
        receiveStation.setCv1Move(dataList.get(48));
        receiveStation.setCv1PalletSensor(dataList.get(61));

        // (for read)CV2的狀態 D3050-D3052 D3062
        /* D3050    DIRECTION Status(運輸方向)  1:CV2-->CV3、2:CV2-->CV1
           D3051    HOLD_Status(設備工作遏制)
                    當HOLD值=0時，設備照常運作
                    當HOLD值=1時，設備暫時停止MOVE=1的驅動，其他Senor數值持續更新
           D3052    MOVE Query(驅動棧板移動詢問)   MOVE值=1時為Ready ；MOVE完成後更新狀態為1
                                                MOVE值=2時為MOVE Query
                                                MOVE值=3時為傳輸異常
                                                MOVE值=4時CV運轉中
           D3062    Sensor(SENSOR 狀態表示)
                    出庫在席=1->BIT[0] : 0 入庫減速/OFF，1 入庫減速/ON
                    出庫減速=2->BIT[1] : 0 入庫在席/OFF，1 入庫在席/ON
                    入庫減速=4->BIT[2] : 0 出庫減速/OFF，1 出庫減速/ON
                    入庫在席=8->BIT[3] : 0 出庫在席/OFF，1 出庫在席/ON
                    當有複數SENSOR狀態時，會由複數的數值總和表示該CV2 SENSOR狀態
        */
        receiveStation.setCv2Direction(dataList.get(50));
        receiveStation.setCv2Hold(dataList.get(51));
        receiveStation.setCv2Move(dataList.get(52));
        receiveStation.setCv2PalletSensor(dataList.get(62));

        // (for read)CV3的狀態 D3054-D3056 D3063
        /* D3054    DIRECTION Status(運輸方向)   1:CV3-->CV2、2:CV3-->AGV、3:AGV-->CV3
           D3055    HOLD_Status(設備工作遏制)
                    當HOLD值=0時，設備照常運作
                    當HOLD值=1時，設備暫時停止MOVE=1的驅動，其他Senor數值持續更新
           D3056    MOVE Query(驅動棧板移動詢問)   MOVE值=1時為Ready ；MOVE完成後更新狀態為1
                                                MOVE值=2時為MOVE Query
                                                MOVE值=3時為傳輸異常
                                                MOVE值=4時CV運轉中
           D3063    Sensor(SENSOR 狀態表示)
                    出庫在席=1->BIT[0] : 0 入庫減速/OFF，1 入庫減速/ON
                    出庫減速=2->BIT[1] : 0 入庫在席/OFF，1 入庫在席/ON
                    入庫減速=4->BIT[2] : 0 出庫減速/OFF，1 出庫減速/ON
                    入庫在席=8->BIT[3] : 0 出庫在席/OFF，1 出庫在席/ON
                    當有複數SENSOR狀態時，會由複數的數值總和表示該CV3 SENSOR狀態
        */
        receiveStation.setCv3Direction(dataList.get(54));
        receiveStation.setCv3Hold(dataList.get(55));
        receiveStation.setCv3Move(dataList.get(56));
        receiveStation.setCv3PalletSensor(dataList.get(63));


        // 高度為直接數值      D3039+D3038	HEIGHT	提供載口感應的棧板高度數值表示單位(mm)
        byte[] height0 = plcDataService.hexIntToByteArray(plcDataIntList[38]);
        byte[] height1 = plcDataService.hexIntToByteArray(plcDataIntList[39]);
        byte[] heightByte = new byte[4];
        heightByte[0] = height0[0]; heightByte[1] = height0[1];
        heightByte[2] = height1[0]; heightByte[3] = height1[1];
        int heightInt = plcDataService.byteArrayToInt(heightByte, 4);
        receiveStation.setHeight(Integer.toString(heightInt));

        // 寬度為直接數值      D3041+D3040	WIDTH	提供載口感應的棧板寬度
        byte[] width0 = plcDataService.hexIntToByteArray(plcDataIntList[40]);
        byte[] width1 = plcDataService.hexIntToByteArray(plcDataIntList[41]);
        byte[] widthByte = new byte[4];
        widthByte[0] = width0[0]; widthByte[1] = width0[1];
        widthByte[2] = width1[0]; widthByte[3] = width1[1];
        int widthInt = plcDataService.byteArrayToInt(widthByte, 4);
        receiveStation.setWeight(Integer.toString(widthInt));

        // 長度為直接數值      D3043+D3042	LENGTH	提供載口感應的棧板長度 數值表示(單位另訂)
        byte[] length0 = plcDataService.hexIntToByteArray(plcDataIntList[42]);
        byte[] length1 = plcDataService.hexIntToByteArray(plcDataIntList[43]);
        byte[] lengthByte = new byte[4];
        lengthByte[0] = length0[0]; lengthByte[1] = length0[1];
        lengthByte[2] = length1[0]; lengthByte[3] = length1[1];
        int lengthInt = plcDataService.byteArrayToInt(lengthByte, 4);
        receiveStation.setLength(Integer.toString(lengthInt));

        // 重量為IEEE754浮點數    D3045+D3044	WEIGHT	提供左載口感應的棧板重量（精度：KG）數值表示
        //byte[] weight0 = plcDataService.hexIntToByteArray(plcDataIntList[44]);
        //byte[] weight1 = plcDataService.hexIntToByteArray(plcDataIntList[45]);
        byte[] weight0 = plcDataService.hexIntToByteArray(plcDataIntList[87]);
        byte[] weight1 = plcDataService.hexIntToByteArray(plcDataIntList[88]);
        byte[] weightByte = new byte[4];
        weightByte[0] = weight0[0]; weightByte[1] = weight0[1];
        weightByte[2] = weight1[0]; weightByte[3] = weight1[1];
        int weightIEEE754Int = plcDataService.byteArrayToInt(weightByte, 4);
        float weightFloat = Float.intBitsToFloat(weightIEEE754Int);
        receiveStation.setWeight(Float.toString(weightFloat));

        // 按鈕    D3064  Button  黃色-預留按鈕=1；綠色-入庫允許入料按鈕=2，當有複數SENSOR狀態時，會由複數的數值總和表示該 button狀態
        receiveStation.setButton(dataList.get(64));

        //更新資料
        receiveStationService.updateReceiveStation(receiveStation);

        // 確認是否有任務要在席SENSOR CV1
        if( plcDataIntList[61]==2  || plcDataIntList[61]==3 || plcDataIntList[61]==6 ){
            // 檢查 ReceiveStationTask
            receiveStationService.plcCheckReceiveStation(conveyor, "CV1");
        }
        /*else if("OutStation".equals(receiveStation.getTaskGoal()) && plcDataIntList[61]==0){
            pressButtonService.endPressButtonTask(conveyor);
        }*/
        // 確認是否有任務要在席SENSOR CV2
        if(plcDataIntList[62]==7 || plcDataIntList[62]==14 || plcDataIntList[62]==15){
            // 檢查 ReceiveStationTask
            receiveStationService.plcCheckReceiveStation(conveyor, "CV2");
        }
        // 確認是否有任務要在席SENSOR CV3
        if( plcDataIntList[63]==6 || plcDataIntList[63]==12 ){
            // 檢查 ReceiveStationTask
            receiveStationService.plcCheckReceiveStation(conveyor, "CV3");
        }
        // 按鈕任務 - TYPE類型：IN-CV1toCV2、IN-CV1toCV3、OutStation、PutPallet、EmptyPallet、PutBasketOnPallet、BasketOutPallet
        if(plcDataIntList[64]==2){
            ButtonTask buttonTask = pressButtonService.findButtonTask(conveyor);
            if((buttonTask!=null)&&(CustomConstants.START.equals(buttonTask.getStatus()))){
                if(("IN-CV1toCV2".equals(buttonTask.getType()))||
                        ("IN-CV1toCV3".equals(buttonTask.getType()))||("PutPallet".equals(buttonTask.getType()))){
                    //需要確認CV1在席SENSOR值為3 or 6
                    if(plcDataIntList[61]==2 || plcDataIntList[61]==3 || plcDataIntList[61]==6) {
                        pressButtonService.endPressButtonTask(conveyor);
                    }
                }
                else if("EmptyPallet".equals(buttonTask.getType()) || "PutBasketOnPallet".equals(buttonTask.getType()) ||
                        "BasketOutPallet".equals(buttonTask.getType())){
                    pressButtonService.endPressButtonTask(conveyor);
                }
                else if("WO01".equals(buttonTask.getType()) || "WO02".equals(buttonTask.getType())
                        || "test".equals(buttonTask.getType())){
                pressButtonService.endPressButtonTask(conveyor);
                }
            }
        }
        /*
        else if(plcDataIntList[64]==8){
            ButtonTask buttonTask = pressButtonService.findButtonTask(conveyor);
            if((buttonTask!=null)&&(CustomConstants.START.equals(buttonTask.getStatus()))){
                if(("OutStation".equals(buttonTask.getType()))){
                    //需要確認CV1在席SENSOR值為0
                    if(plcDataIntList[61]==0) {
                        pressButtonService.endPressButtonTask(conveyor);
                    }
                }
            }
        }
        */
    }

    // plcData進行書寫判斷處理
    @Override
    public void writePLCData(String lenNum, int[] data){
        //資料只寫D3100~D3119的資料 共20個點位
        JSONObject requestPLC = new JSONObject();
        requestPLC.put("REQ_ID", "REQ_" + DateUtil.getDateTimeWithRandomNum());
        requestPLC.put("MX_NUM", lenNum);   //MX 編號
        requestPLC.put("AREA", "D");        //點位區域
        requestPLC.put("POINT", "3100");    //起始點位
        requestPLC.put("COUNT", "20");     //資料長度
        requestPLC.put("RESOURCE", "Conveyor"+lenNum);  //設備編號
        JSONArray plcDataJsonArray = new JSONArray();
        for (int i = 0; i < data.length; i++){
            plcDataJsonArray.add(data[i]);
        }
        requestPLC.put("DATA", plcDataJsonArray);//資料
        requestPLC.put("SEND_TIME", DateUtil.getDateTime());

        String mqName = "plc.writer.request."+lenNum;
        String plcResponse = activeMqSendService.sendMsgNeedResponse4Wms(mqName, requestPLC.toJSONString());
        if (StringUtils.isBlank(plcResponse)) {
            this.plcNoResponse(lenNum);
            throw new BusinessException("【LAN_" + lenNum + "_PLC】未迴應");
        }

        JSONObject responseJson = JSON.parseObject(plcResponse);
        String result = responseJson.getString("RESULT");
        //值大於0表示傳輸出問題
        if (!"0".equals(result)) {
            throw new BusinessException(responseJson.getString("MESSAGE"));
        }
    }

    // plc 無響應處理
    @Override
    public void plcNoResponse(String lenNum){
        // 確認輸送帶ID
        String conveyor = "";
        if("1".equals(lenNum))  conveyor = "Conveyor1";
        else if("2".equals(lenNum))  conveyor = "Conveyor2";
        else if("3".equals(lenNum))  conveyor = "Conveyor3";
        else if("4".equals(lenNum))  conveyor = "Conveyor4";
        else if("5".equals(lenNum))  conveyor = "Conveyor5";

        // 查看狀態
        ReceiveStation receiveStation = receiveStationService.getReceiveStation(conveyor);
        if(CustomConstants.ALARM_DISCONNECT.equals(receiveStation.getStatus()));
        else{
            // 需要報告ASRS DISCONNECT
            receiveStationService.reportASRS(conveyor, CustomConstants.ALARM_DISCONNECT);
            receiveStation.setStatus(CustomConstants.ALARM_DISCONNECT);
            //更新資料
            receiveStationService.updateReceiveStation(receiveStation);
        }
    }

    // 模擬發送PLCData
    // 直接發送 plc data 給plc.reader.sync.process 分析
    @Override
    public void SimulationPLCData(String conveyor, String type, int[] data){
        try {
            Thread.sleep(new Long(2000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //確認輸送帶的PLC的lenNum
        String lenNum = "";
        if("Conveyor1".equals(conveyor))  lenNum = "1";
        else if("Conveyor2".equals(conveyor))  lenNum = "2";
        else if("Conveyor3".equals(conveyor))  lenNum = "3";
        else if("Conveyor4".equals(conveyor))  lenNum = "4";
        else if("Conveyor5".equals(conveyor))  lenNum = "5";

        // 找尋輸送帶資料，若無資料則新增一個
        PLCData plcData = this.findPLCDataByConveyor(conveyor);
        int[] plcDataIntList = new int[100];
        if(plcData==null){
            for (int i = 0; i < 100; i++){
                plcDataIntList[i] = 0;
            }
        }
        else{
            // 取出PLC資料，轉成需要的格式
            String plcDataArray = "{\"DATA\":"+plcData.getData()+"}";
            JSONObject JsonNewplcData = JSON.parseObject(plcDataArray);
            JSONArray plcdata = JsonNewplcData.getJSONArray("DATA");
            for (int i = 0; i < 100; i++){
                plcDataIntList[i] = Integer.parseInt(plcdata.getString(i));
            }
        }

        //D3018    0=設備開機啟動中；1=設備開機完成/待命中；2=設備故障；3=設備正在移動棧板
        plcDataIntList[18] = 1;

        // CV1~CV3正常運行
        plcDataIntList[46] = 1;
        plcDataIntList[47] = 0;
        plcDataIntList[48] = 1;
        plcDataIntList[50] = 1;
        plcDataIntList[51] = 0;
        plcDataIntList[52] = 1;
        plcDataIntList[54] = 1;
        plcDataIntList[55] = 0;
        plcDataIntList[56] = 1;

        ReceiveStation receiveStation = receiveStationService.getReceiveStation(conveyor);

        // TYPE為按鈕任務 "PressButton"
        // 若data[12]=1/2/4 則修改 D3064 為 1/2/4
        // 若data[12]=0 則修改 D3064 為 0
        // 若data[12]=8 則修改 D3064 為 8 且其任務為"OutStation" 需設置CV1在席為空
        if("PressButton".equals(type)){
            plcDataIntList[64] = data[12];
            if(data[12]==8){
                plcDataIntList[61] = 0;
            }

            ButtonTask buttonTask = pressButtonService.findButtonTask(conveyor);
            if((buttonTask!=null)&&("START".equals(buttonTask.getStatus()))){
                if(("IN-CV1toCV2".equals(buttonTask.getType()))||
                   ("IN-CV1toCV3".equals(buttonTask.getType()))||("PutPallet".equals(buttonTask.getType()))){
                    plcDataIntList[61]=3;
                }
            }
        }

        // TYPE為在席任務 "Transfer"
        if("Transfer".equals(type)){
            //D3018    0=設備開機啟動中；1=設備開機完成/待命中；2=設備故障；3=設備正在移動棧板
            plcDataIntList[18] = 3;
            // 若data[0]=1 && data[1]=0 && data[2]=1 表示 CV1-->CV2(入庫)
            // 則修改 D3062(CV2 SENSOR在席SENSOR 狀態表示) = 14/15
            //       D3061(CV1 SENSOR在席SENSOR 狀態表示) = 0
            if(data[0]==1 && data[1]==0 && data[2]==1){ //CV1-->CV2(入庫)
                plcDataIntList[62] = 14;
                plcDataIntList[61] = 0;
                if("ROBOTIC_ARM".equals(receiveStation.getTaskGoal())){
                    plcDataIntList[63] = 12;
                }
            }
            // 若data[4]=1 && data[5]=0 && data[6]=1 表示 CV2-->CV3
            // 則修改 D3063(CV3 SENSOR在席SENSOR 狀態表示) = 6/12
            //       D3062(CV2 SENSOR在席SENSOR 狀態表示) = 0
            else if(data[4]==1 && data[5]==0 && data[6]==1){ //CV2-->CV3
                plcDataIntList[63] = 12;
                plcDataIntList[62] = 0;
            }
            // 若data[4]=2 && data[5]=0 && data[6]=1 表示 CV2-->CV1
            // 則修改 D3061(CV1 SENSOR在席SENSOR 狀態表示) = 3/6
            //       D3062(CV2 SENSOR在席SENSOR 狀態表示) = 0
            else if(data[4]==2 && data[5]==0 && data[6]==1){ //CV2-->CV1
                plcDataIntList[61] = 3;
                plcDataIntList[62] = 0;
            }
            // 若data[8]=1 && data[9]=0 && data[10]=1 表示 CV3-->CV2
            // 則修改 D3062(CV2 SENSOR在席SENSOR 狀態表示) = 14/15
            //       D3063(CV3 SENSOR在席SENSOR 狀態表示) = 0
            else if(data[8]==1 && data[9]==0 && data[10]==1){ //CV3-->CV2
                plcDataIntList[62] = 14;
                plcDataIntList[63] = 0;
            }
        }

        // 傳送任務給 plc.reader.sync.process
        JSONObject requestPLC = new JSONObject();
        requestPLC.put("REQ_ID", "REQ_" + DateUtil.getDateTimeWithRandomNum());
        requestPLC.put("MX_NUM", lenNum);   //MX 編號
        requestPLC.put("AREA", "D");        //點位區域
        requestPLC.put("POINT", "3000");    //起始點位
        requestPLC.put("COUNT", "100");     //資料長度
        requestPLC.put("RESOURCE", conveyor);  //設備編號
        JSONArray plcDataJsonArray = new JSONArray();
        for (int i = 0; i < plcDataIntList.length; i++){
            plcDataJsonArray.add(plcDataIntList[i]);
        }
        requestPLC.put("DATA", plcDataJsonArray);//資料
        requestPLC.put("SEND_TIME", LocalDateTime.now().toString()); //DateUtil.getDateTime()

        String mqName = "plc.writer.request."+lenNum;
        activeMqSendService.sendMsgNoResponse4Wms("plc.reader.sync.process", requestPLC.toJSONString());

        // TYPE為在席任務 "Transfer"
        if("Transfer".equals(type)){
            try {
                Thread.sleep(new Long(500));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //D3018    0=設備開機啟動中；1=設備開機完成/待命中；2=設備故障；3=設備正在移動棧板
            plcDataIntList[18] = 1;

            // 傳送任務給 plc.reader.sync.process
            JSONObject requestPLC_2 = new JSONObject();
            requestPLC_2.put("REQ_ID", "REQ_" + DateUtil.getDateTimeWithRandomNum());
            requestPLC_2.put("MX_NUM", lenNum);   //MX 編號
            requestPLC_2.put("AREA", "D");        //點位區域
            requestPLC_2.put("POINT", "3000");    //起始點位
            requestPLC_2.put("COUNT", "100");     //資料長度
            requestPLC_2.put("RESOURCE", conveyor);  //設備編號
            JSONArray plcDataJsonArray_2 = new JSONArray();
            for (int i = 0; i < plcDataIntList.length; i++){
                plcDataJsonArray_2.add(plcDataIntList[i]);
            }
            requestPLC_2.put("DATA", plcDataJsonArray_2);//資料
            requestPLC_2.put("SEND_TIME", LocalDateTime.now().toString()); //DateUtil.getDateTime()

            activeMqSendService.sendMsgNoResponse4Wms("plc.reader.sync.process", requestPLC_2.toJSONString());
        }
    }
}
