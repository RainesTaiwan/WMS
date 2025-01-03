package com.sap.ewm.biz.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sap.ewm.biz.constants.CommonConstants;
import com.sap.ewm.biz.dto.HandlingUnitContextDTO;
import com.sap.ewm.biz.dto.MaterialRequisitionDTO;
import com.sap.ewm.biz.dto.MaterialRequisitionStorageBinDTO;
import com.sap.ewm.biz.model.*;
import com.sap.ewm.biz.service.*;
import com.sap.ewm.core.exception.BusinessException;
import com.sap.ewm.core.utils.CacheUtil;
import com.sap.ewm.biz.model.Item;
import com.sap.ewm.biz.model.StorageBin;
import com.sap.ewm.biz.model.StorageBinStatus;
import com.sap.ewm.biz.service.*;
import com.sap.ewm.core.utils.StringUtils;
import com.sap.ewm.sys.service.MessageSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 系統領料 服務實現類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MaterialRequisitionServiceImpl implements MaterialRequisitionService {

    @Autowired
    private ItemService itemService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private StorageBinService storageBinService;

    @Autowired
    private StorageBinStatusService storageBinStatusService;

    @Autowired
    private HandlingUnitService handlingUnitService;

    @Autowired
    private ShelfOffLogService shelfOffLogService;

    @Autowired
    private HandlingUnitLocationService handlingUnitLocationService;

    @Autowired
    private CarrierService carrierService;

    @Autowired
    private ASRSFRIDService asrsFRIDService;

    @Autowired
    private MessageSendService messageSendService;

    @Override
    public List<MaterialRequisitionStorageBinDTO> doMaterialRequisition(String user, MaterialRequisitionDTO materialRequisitionDTO) {
        String itemCode = materialRequisitionDTO.getItem();
        BigDecimal qty = materialRequisitionDTO.getQty();
        LocalDateTime now = LocalDateTime.now();
        List<MaterialRequisitionStorageBinDTO> materialRequisitionStorageBinDTOList = new ArrayList<>();
        if(StrUtil.isBlank(itemCode)) {
            throw BusinessException.build("料號不能為空");
        }
        if(qty == null || qty.compareTo(BigDecimal.ZERO) <= 0) {
            throw BusinessException.build("請填寫正確的領料數量");
        }
        Item item = itemService.getById(Item.genHandle(itemCode));
        if(item == null) {
            throw BusinessException.build("料號" + itemCode + "不存在");
        }
        List<StorageBinStatus> storageBinStatusList = new ArrayList<>();
        // 查詢貨格中的庫存批（先進先出）
        // TODO 根據物料消耗型別（先進先出、近效先出、後進先出）進行消耗，近效期先出是根據保質期近的先出庫，先進先出是根據入庫順序，先入庫的先出庫
        BigDecimal totalQty = BigDecimal.ZERO;
        while(totalQty.compareTo(qty) < 0) {
            MaterialRequisitionStorageBinDTO materialRequisitionStorageBinDTO = storageBinService.getOptimalMaterialRequisitionStorageBin(Item.genHandle(itemCode));
            if(materialRequisitionStorageBinDTO == null || materialRequisitionDTO.getQty() == null || materialRequisitionDTO.getQty().compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }
            totalQty = totalQty.add(materialRequisitionStorageBinDTO.getQty());
            // 混料需要拆包
            if(CommonConstants.Y.equals(materialRequisitionStorageBinDTO.getMixed())) {
                materialRequisitionStorageBinDTO.setSplit(true);
            }
            // 最後一格有多餘料未用完需要拆包
            if(totalQty.compareTo(qty) > 0) {
                materialRequisitionStorageBinDTO.setSplit(true);
                materialRequisitionStorageBinDTO.setQty(materialRequisitionStorageBinDTO.getQty().subtract(totalQty.subtract(qty)));
            }
            // 更新貨格狀態為待出庫,佔用目前貨格防止重複派工到該貨格
            materialRequisitionStorageBinDTOList.add(materialRequisitionStorageBinDTO);
            StorageBinStatus storageBinStatus = new StorageBinStatus();
            storageBinStatus.setHandle(StorageBin.genHandle(materialRequisitionStorageBinDTO.getStorageBin()));
            storageBinStatus.setStorageBin(materialRequisitionStorageBinDTO.getStorageBin());
            storageBinStatus.setStatus(CommonConstants.STATUS_WAIT_OUT_STORAGE);
            storageBinStatus.setCreateTime(now);
            storageBinStatus.setUpdateTime(now);
            storageBinStatusList.add(storageBinStatus);
            storageBinStatusService.saveOrUpdate(storageBinStatus);
            // change carrier status
            carrierService.changeStatus(StringUtils.trimHandle(materialRequisitionStorageBinDTO.getCarrierBo()), itemCode, "OUT_STORAGE", CommonConstants.STATUS_WAIT_OUT_STORAGE);
        }
        if(totalQty.compareTo(qty) < 0) {
            throw BusinessException.build("可用庫存數量不足");
        }
        // TODO 領料日誌
        return materialRequisitionStorageBinDTOList;
    }

    @Override
    public void outStorage(String user, String carrier, String storageBin, String correlationId) {
        handlingUnitService.outStorage(user, carrier, storageBin);

        // update RFID tags: OUT_STATION
        // 更新RFID狀態 STATUS: Processing、OnPallet、BindPallet、IN_STORAGE、OUT_STORAGE、WAIT_OUT_STATION、OUT_STATION、OUT_STATION_NO_REPORT
        List<AsrsRfid> asrsRfids = asrsFRIDService.findRFIDByCarrierWithStatus(carrier, CommonConstants.STATUS_IN_STORAGE);
        List<String> list = new ArrayList<>();
        for(int i=0; i<asrsRfids.size();i++){
            list.add(asrsRfids.get(i).getHandle());
        }
        asrsFRIDService.updateRFIDStatus(list, carrier, null, CommonConstants.STATUS_OUT_STORAGE);
    }

    /**
     * 出接駁站
     * @param user
     * @param carrier
     * @param correlationId
     */
    @Override
    public void outStation(String user, String carrier, String correlationId) {
        HandlingUnitLocation handlingUnitLocation = handlingUnitLocationService.getByCarrier(carrier);
        if(handlingUnitLocation != null) {
            throw BusinessException.build("carrier " + carrier + " current in storage " + StringUtils.trimHandle(handlingUnitLocation.getBindContextGbo()));
        }
        ShelfOffLog shelfOffLog = shelfOffLogService.getOne(Wrappers.<ShelfOffLog>lambdaQuery().eq(ShelfOffLog::getCorrelationId, correlationId), false);

        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("MESSAGE_ID", "");
        jsonObject1.put("MESSAGE_BODY", shelfOffLog.toString());
        jsonObject1.put("CREATED_DATE_TIME", System.currentTimeMillis());
        jsonObject1.put("QUEUE", "Code-outStation-2");
        messageSendService.send("MQ_LOG", jsonObject1);

        if(shelfOffLog != null && !CommonConstants.Y.equalsIgnoreCase(shelfOffLog.getExecuteResult())) {
            // remove inventory
            BigDecimal qty = shelfOffLog.getQty();
            String inventoryBo = shelfOffLog.getInventoryBo();

            inventoryService.deductionQty(user, inventoryBo, qty);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("MESSAGE_ID", "");
            jsonObject.put("MESSAGE_BODY", "carrier:" + carrier+"; correlationId:" + correlationId +"; qty: "+qty);
            jsonObject.put("CREATED_DATE_TIME", System.currentTimeMillis());
            jsonObject.put("QUEUE", "Code-outStation");
            messageSendService.send("MQ_LOG", jsonObject);

            // change handling unit
            List<HandlingUnit> handlingUnitList = handlingUnitService.list(Wrappers.<HandlingUnit>lambdaQuery().eq(HandlingUnit::getCarrierBo, Carrier.genHandle(shelfOffLog.getCarrier())));
            handlingUnitList.removeIf(h -> h.getHandlingUnitContextGbo().equals(inventoryBo));
            List<HandlingUnitContextDTO> handlingUnitContextDTOList = new ArrayList<>();
            if(handlingUnitList != null && handlingUnitList.size() > 0) {
                for (int i=0; i<handlingUnitList.size(); i++) {
                    HandlingUnitContextDTO handlingUnitContextDTO = new HandlingUnitContextDTO();
                    handlingUnitContextDTO.setHandlingUnitContextGbo(handlingUnitList.get(i).getHandlingUnitContextGbo());
                    handlingUnitContextDTO.setQty(handlingUnitList.get(i).getInventoryQty());
                    handlingUnitContextDTO.setStatus(handlingUnitList.get(i).getStatus());
                    handlingUnitContextDTOList.add(handlingUnitContextDTO);
                }
                /*
                for(HandlingUnit handlingUnit : handlingUnitList) {
                    HandlingUnitContextDTO handlingUnitContextDTO = new HandlingUnitContextDTO();
                    handlingUnitContextDTO.setHandlingUnitContextGbo(handlingUnit.getHandlingUnitContextGbo());
                    handlingUnitContextDTO.setQty(handlingUnit.getInventoryQty());
                    handlingUnitContextDTO.setStatus(handlingUnit.getStatus());
                    handlingUnitContextDTOList.add(handlingUnitContextDTO);
                }*/
            }
            handlingUnitService.rearrangement(user, Carrier.genHandle(carrier), handlingUnitContextDTOList);
            // change storage bin to idle if in out storage status
            storageBinStatusService.update(Wrappers.<StorageBinStatus>lambdaUpdate()
                    .eq(StorageBinStatus::getHandle, StorageBin.genHandle(shelfOffLog.getStorageBin()))
                    .eq(StorageBinStatus::getStatus, CommonConstants.STATUS_OUT_STORAGE)
                    .set(StorageBinStatus::getStatus, CommonConstants.STATUS_IDLE));
            // change carrier status
            Inventory inventory = inventoryService.getById(inventoryBo);
            String item = inventory == null ? null : StringUtils.trimHandle(inventory.getItemBo());
            carrierService.changeStatus(carrier, item, "OUT_STORAGE", CommonConstants.STATUS_OUT_STORAGE);

            // update RFID tags: OUT_STATION
            // 更新RFID狀態 STATUS: Processing、OnPallet、BindPallet、IN_STORAGE、OUT_STORAGE、OUT_STATION、OUT_STATION_NO_REPORT
            List<AsrsRfid> asrsRfids = asrsFRIDService.findRFIDByCarrierWithStatus(carrier, CommonConstants.STATUS_WAIT_OUT_STATION);
            List<String> list = new ArrayList<>();
            for(int i=0; i<asrsRfids.size();i++){
                list.add(asrsRfids.get(i).getHandle());
            }
            asrsFRIDService.updateRFIDStatus(list, carrier, null, CommonConstants.STATUS_OUT_STATION_NO_REPORT);

            // update shelf off log
            shelfOffLog.setExecuteResult(CommonConstants.Y);
            shelfOffLogService.updateById(shelfOffLog);
        }
    }
}