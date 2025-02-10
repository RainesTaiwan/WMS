package com.sap.ewm.biz.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sap.ewm.biz.constants.CommonConstants;
import com.sap.ewm.biz.mapper.ConveyorMapper;
import com.sap.ewm.biz.model.Conveyor;
import com.sap.ewm.biz.service.ConveyorSerivce;
import com.sap.ewm.sys.service.MessageSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
/**
 * Conveyor主數據 服務實現類
 *
 * @author Glanz
 * @since 2020-08-10
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ConveyorSerivceImpl extends ServiceImpl<ConveyorMapper, Conveyor> implements ConveyorSerivce {
    @Autowired
    private ConveyorMapper conveyorMapper;

    @Autowired
    private ConveyorSerivce conveyorSerivce;

    @Autowired
    private MessageSendService messageSendService;

    // 取出輸送帶工作量
    @Override
    public int[] findAllConveyorWorkload(){
        List<Conveyor> conveyorList = conveyorMapper.findAllConveyor();
        int[] allConveyorWorkload = new int[conveyorList.size()];

        for(int i = 0; i<conveyorList.size();i++){
            allConveyorWorkload[i] = conveyorList.get(i).getWorkload();
        }

        return allConveyorWorkload;
    }

    // 取出指定輸送帶工作量
    @Override
    public int findConveyorWorkload(String conveyor){
        return conveyorMapper.findConveyorWorkload(conveyor);
    }

    // 取出輸送帶優先權
    @Override
    public int[] findAllConveyorPriority(){
        List<Conveyor> conveyorList = conveyorMapper.findAllConveyor();
        int[] allConveyorPriority = new int[conveyorList.size()];

        for(int i = 0; i<conveyorList.size();i++){
            allConveyorPriority[i] = conveyorList.get(i).getPriority();
        }

        return allConveyorPriority;
    }

    // 告知選擇輸送帶
    @Override
    public String selectConveyor(String woType){
        // 回傳的字串
        String selectConveyor = "";
        int[] conveyorWorkload = conveyorSerivce.findAllConveyorWorkload();
        int[] conveyorPriority = conveyorSerivce.findAllConveyorPriority();

        int select = 0;
        // WO1 整棧入庫 WO5理貨入庫  WO6單箱出庫:可選擇1、2、4、5
        if(woType.equals(CommonConstants.OrderType5)
                || woType.equals(CommonConstants.OrderType6)){
            for(int i=0;i<5;i++){
                if(i!=2 && conveyorWorkload[select]>=conveyorWorkload[i] && conveyorPriority[select]<=conveyorPriority[i]){
                    select = i;
                }
            }
        }
        // WO2 整棧出庫  WO7盤點:可選擇1、2、3、4、5
        else if(woType.equals(CommonConstants.OrderType2) 
                    || woType.equals(CommonConstants.OrderType7)
                    || woType.equals(CommonConstants.OrderType1)){
            for(int i=0;i<5;i++){
                if(conveyorWorkload[select]>=conveyorWorkload[i] && conveyorPriority[select]<=conveyorPriority[i]){
                    select = i;
                }
            }
        }
        // WO3散貨入庫  WO4散貨出庫:可選擇5
        else if(woType.equals(CommonConstants.OrderType3) || woType.equals(CommonConstants.OrderType4)){
            select = 4;
        }
        selectConveyor = "Conveyor" + String.valueOf(select+1);

        // 查看LOG
        JSONObject jsonTemp = new JSONObject();
        jsonTemp.put("QUEUE", "selectConveyor");
        jsonTemp.put("MESSAGE_BODY", "conveyor Workload: "+ Arrays.toString(conveyorWorkload)
                +", conveyor Priority: "+ Arrays.toString(conveyorPriority)
                +", conveyorPriority["+select+"]: "+ conveyorPriority[select]);
        jsonTemp.put("SEND_TIME", System.currentTimeMillis());
        messageSendService.send(CommonConstants.MQ_LOG, jsonTemp);

        return selectConveyor;
    }

    // 增加輸送帶工作量
    @Override
    public void changeConveyorWorkload(String conveyor, int num){
        Conveyor targetConveyor = conveyorMapper.findConveyorByName(conveyor);
        int conveyorWorkload = targetConveyor.getWorkload()+num;
        targetConveyor.setWorkload(conveyorWorkload);
    }
}
