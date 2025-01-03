package com.sap.ewm.biz.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sap.ewm.biz.constants.CommonConstants;
import com.sap.ewm.biz.dto.CarrierDTO;
import com.sap.ewm.dashboard.model.CarrierUsage;
import com.sap.ewm.biz.mapper.CarrierMapper;
import com.sap.ewm.biz.model.Carrier;
import com.sap.ewm.biz.model.CarrierType;
import com.sap.ewm.biz.model.Inventory;
import com.sap.ewm.biz.service.CarrierService;
import com.sap.ewm.biz.service.CarrierTypeService;
import com.sap.ewm.core.base.FrontPage;
import com.sap.ewm.core.exception.BusinessException;
import com.sap.ewm.core.utils.DateUtil;
import com.sap.ewm.core.utils.StringUtils;
import com.sap.ewm.sys.service.MessageSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
/**
 * <p>
 * 載具主數據 服務實現類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CarrierServiceImpl extends ServiceImpl<CarrierMapper, Carrier> implements CarrierService {


    @Autowired
    private CarrierMapper carrierMapper;

    @Autowired
    private CarrierTypeService carrierTypeService;

    @Autowired
    private MessageSendService messageSendService;

    @Override
    public IPage<Carrier> selectPage(FrontPage<Carrier> frontPage, Carrier carrier) {
        QueryWrapper<Carrier> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(carrier);
        return super.page(frontPage.getPagePlus(), queryWrapper);
    }

    @Override
    public List<Carrier> selectList(Carrier carrier) {
        QueryWrapper<Carrier> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(carrier);
        return super.list(queryWrapper);
    }

    @Override
    public boolean save(CarrierDTO carrierDTO) {
        validateAndAssemble(carrierDTO);
        carrierDTO.setHandle(Carrier.genHandle(carrierDTO.getCarrier()));
        int count;
        try {
            count = baseMapper.insert(carrierDTO);
        } catch(Exception e) {
            if( e instanceof SQLIntegrityConstraintViolationException || e instanceof DuplicateKeyException) {
                throw new MybatisPlusException("載具'" + carrierDTO.getCarrier() + "'已存在");
            } else {
                throw e;
            }
        }
        return retBool(count);
    }

    @Override
    public boolean updateById(CarrierDTO carrierDTO) {
        validateAndAssemble(carrierDTO);
        carrierDTO.setHandle(Inventory.genHandle(carrierDTO.getCarrier()));
        int count = baseMapper.updateById(carrierDTO);
        return retBool(count);
    }

    @Override
    public void changeStatus(String carrier, String item, String operation, String status) {
        this.update(Wrappers.<Carrier>lambdaUpdate()
                .eq(Carrier::getHandle, Carrier.genHandle(carrier))
                .set(Carrier::getStatus, status)
                .set(Carrier::getUpdateTime, LocalDateTime.now()));

        String queueName = "carrier.status.change";
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("MESSAGE_TYPE" , queueName);
        jsonObject.put("MESSAGE_ID" , StringUtils.createQUID());
        jsonObject.put("SEND_TIME" , DateUtil.format(new Date()));
        jsonObject.put("CARRIER" , carrier);
        jsonObject.put("ITEM" , item);
        jsonObject.put("OPERATION" , operation);
        jsonObject.put("STATUS" , status);
        messageSendService.send(queueName, jsonObject);
    }

    private void validateAndAssemble(CarrierDTO carrierDTO) {
        String carrierTypeBo = carrierDTO.getCarrierTypeBo();
        if(StrUtil.isNotBlank(carrierDTO.getCarrierType())) {
            carrierTypeBo = CarrierType.genHandle(carrierDTO.getCarrierType());
        }
        CarrierType carrierType = carrierTypeService.getById(carrierTypeBo);
        if(carrierType == null) {
            throw BusinessException.build("棧板型別" + StringUtils.trimHandle(carrierTypeBo) + "不存在");
        }
        carrierDTO.setCarrierTypeBo(carrierTypeBo);
    }

    //檢查是否有此棧板，有回傳true
    @Override
    public boolean checkCarrier(String carrierID){
        boolean checkResult = false;
        Carrier carrier = carrierMapper.findCarrierByID(carrierID);
        if(carrier==null) checkResult = true;
        return checkResult;
    }

    //新增棧板
    @Override
    public void insertCarrier(String carrierId, String type){
        Carrier carrier = new Carrier();
        carrier.setHandle("CarrierBO:"+carrierId);
        carrier.setCarrier(carrierId);
        carrier.setDescription(carrierId);
        carrier.setStatus("");
        carrier.setCarrierTypeBo("CarrierTypeBO:"+type);
        carrier.setCreator(CommonConstants.UPDATE_USER);
        carrier.setCreateTime(LocalDateTime.now());
        carrierMapper.insert(carrier);
    }
    /*
    //要求WCS放置新棧板
    @Override
    public Carrier askWCSNewCarrier(){
        // 呼叫
        Carrier carrier = new Carrier();
        return carrier;
    }
     */
}