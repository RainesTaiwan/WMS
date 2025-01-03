package com.fw.wcs.sys.consumer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fw.wcs.core.constants.CustomConstants;
import com.fw.wcs.core.utils.DateUtil;
import com.fw.wcs.sys.dto.CarrierInfoDto;
import com.fw.wcs.sys.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import javax.jms.Queue;
import java.util.UUID;
import java.time.LocalDateTime;


/**
 * @author Leon
 *
 *
 * 監聽接駁站系統 互動事件
 */
@Component
public class ResConsumer {

    private static Logger logger = LoggerFactory.getLogger(ResConsumer.class);


    @Autowired
    private CarrierTaskService carrierTaskService;
    @Autowired
    private CarrierRfidService carrierRfidService;
    @Autowired
    private ActiveMqSendService activeMqSendService;
    @Autowired
    private ReceiveStationBindService receiveStationBindService;

    /**
     * 載具資訊上報：
     *  高、寬 ：在第一工位檢測
     *  長、重 ：在第二工位檢測
     */
    @JmsListener(destination = "carrier.info.report", containerFactory="wmsFactory")
    public void carrierInfoReport(String text) {
        logger.info("Get carrier.info.report Text>>> {}", text);

        String station = null;
        String resource = null;
        try {
            JSONObject jsonObject = JSON.parseObject(text);
            CarrierInfoDto carrierInfoDto = JSONObject.toJavaObject(jsonObject, CarrierInfoDto.class);
            station = carrierInfoDto.getSTATION();
            resource = carrierInfoDto.getRESOURCE();
            String carrier = carrierInfoDto.getCARRIER();

            /*
            //載具編號查詢
            String tag = carrierInfoDto.getCARRIER();
            CarrierRfid carrierRfidModel = carrierRfidService.getCarrierByTag(tag);
            if (carrierRfidModel == null) {
                throw new BusinessException("上報編號【"+tag+"】沒有繫結載具編號");
            }
            String carrier = carrierRfidModel.getCarrier();
            carrierInfoDto.setCARRIER(carrierRfidModel.getCarrier());
            */
            receiveStationBindService.receiveStationBind(resource, station, carrier);

            //發送給WMS檢測數據
            carrierInfoDto.setMESSAGE_TYPE(CustomConstants.HANDLING_UNIT_INFO);
            carrierInfoDto.setSEND_TIME(DateUtil.getDateTime());
            activeMqSendService.sendMsgNoResponse4Wms(CustomConstants.HANDLING_UNIT_INFO, JSON.toJSONString(carrierInfoDto));
            /*
            // 模擬呼叫carrier.on.machine
            JSONObject JsonCarrierOnStation = new JSONObject();
            JsonCarrierOnStation.put("MESSAGE_ID", DateUtil.getDateTimemessageId());
            JsonCarrierOnStation.put("MESSAGE_TYPE", "carrier.on.machine");
            JsonCarrierOnStation.put("RESOURCE", resource);
            JsonCarrierOnStation.put("TYPE", "IN");
            JsonCarrierOnStation.put("STATION", "CV3");
            JsonCarrierOnStation.put("CARRIER", carrier);
            JsonCarrierOnStation.put("SEND_TIME", LocalDateTime.now().toString()); //DateUtil.getDateTime()
            activeMqSendService.sendMsgNoResponse4Wms("carrier.on.machine", JsonCarrierOnStation.toJSONString());
            */

        } catch (Exception e) {
            //發送接駁站停止指令
            activeMqSendService.sendRemoteStopCommand(resource, station, e.getMessage());
        }
    }

    /**
     * 載具到位
     */
    @JmsListener(destination = "carrier.on.machine", containerFactory="wmsFactory")
    public void carrierOnMachine(String text) {
        logger.info("Get carrier.on.machine Text>>> {}", text);

        String station = null;
        String resource = null;
        try {
            //解析請求參數
            JSONObject jsonObject = JSON.parseObject(text);
            station = jsonObject.getString("STATION");
            resource = jsonObject.getString("RESOURCE");
            String type = jsonObject.getString("TYPE");
            String carrier = jsonObject.getString("CARRIER");
            /*
            String carrier_rfid = jsonObject.getString("CARRIER_RFID");
            CarrierRfid carrierRfidModel = carrierRfidService.getCarrierByTag(carrier_rfid);
            if (carrierRfidModel == null) {
                throw new BusinessException("上報編號【"+carrier_rfid+"】沒有繫結載具編號");
            }
            String carrier = carrierRfidModel.getCarrier();
             */
            carrierTaskService.carrierOnMachine(resource, type, station, carrier);
        } catch (Exception e) {
            //發送接駁站停止指令
            activeMqSendService.sendRemoteStopCommand(resource, station, e.getMessage());

            JSONObject JsonTemp = new JSONObject();
            JsonTemp.put("QUEUE", "carrier.on.machine -e");
            JsonTemp.put("MESSAGE_BODY", e.getMessage());
            JsonTemp.put("CREATED_DATE_TIME", LocalDateTime.now().toString()); //System.currentTimeMillis()
            activeMqSendService.sendMsgNoResponse4Wms("MQ_LOG", JsonTemp.toJSONString());

        }
    }



}
