package com.sap.ewm.biz.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sap.ewm.biz.dto.*;
import com.sap.ewm.biz.model.HandlingUnit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sap.ewm.biz.model.StorageBin;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 處理單元 服務類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
public interface HandlingUnitService extends IService<HandlingUnit> {

    IPage<HandlingUnitDTO> selectPageVo(Page page, Wrapper<HandlingUnitDTO> wrapper);

    List<HandlingUnitDTO> selectList(Wrapper<HandlingUnitDTO> wrapper);

    /**
     * 獲取傳入批次載具繫結的庫存總量
     * @param batchNo
     * @return
     */
    BigDecimal batchNoTotalQty(String batchNo);

    /**
     * 載具繫結
     * @param user
     * @param carrierBindDTO
     */
    void carrierBind(String user, CarrierBindDTO carrierBindDTO);

    /**
     * 載具解綁
     * @param user
     * @param handles
     */
    void carrierUnbind(String user, List<String> handles);

    /**
     * 記錄處理單元規格資訊，並返回最優貨格
     * @param user
     * @param wcsHandlingUnitSpecDTO
     */
    void handlingUnitInfoProcess(String user, WcsHandlingUnitSpecDTO wcsHandlingUnitSpecDTO);

    StorageBin handlingUnitInRequest(String carrier, List<String> busyStorageLocation);

    boolean isMixed(String carrier);

    /**
     * 入庫
     * @param user
     * @param carrier
     * @param storageBin
     */
    void inStorage(String user, String carrier, String storageBin);

    /**
     * 根據載具編號獲取處理單元標識
     * @param carrier
     */
    String getHandlingId(String carrier);

    /**
     * 出庫
     * @param user
     * @param carrier
     * @param storageBin
     */
    void outStorage(String user, String carrier, String storageBin);

    Map<String, BigDecimal> getHandlingUnitQty(String handlingId);

    void rearrangement(String user, String carrierBo, List<HandlingUnitContextDTO> handlingUnitContextList);

    void doStorageChange(StorageChangeDTO storageChangeDTO);
}