package com.sap.ewm.biz.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sap.ewm.biz.constants.CommonConstants;
import com.sap.ewm.biz.constants.StatusEnum;
import com.sap.ewm.biz.dto.*;
import com.sap.ewm.biz.mapper.HandlingUnitMapper;
import com.sap.ewm.biz.model.*;
import com.sap.ewm.biz.service.*;
import com.sap.ewm.core.base.AjaxResult;
import com.sap.ewm.core.exception.BusinessException;
import com.sap.ewm.core.utils.DateUtil;
import com.sap.ewm.core.utils.StringUtils;
import com.sap.ewm.biz.model.*;
import com.sap.ewm.biz.service.*;
import com.sap.ewm.dashboard.service.InventorySummaryService;
import com.sap.ewm.sys.service.MessageSendService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 處理單元 服務實現類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class HandlingUnitServiceImpl extends ServiceImpl<HandlingUnitMapper, HandlingUnit> implements HandlingUnitService {

    // 載具繫結
    public static final String ACTION_CARRIER_BIND = "CARRIER_BIND";
    // 載具解綁
    public static final String ACTION_CARRIER_UNBIND = "CARRIER_UNBIND";
    // 拆垛
    public static final String ACTION_SPLIT = "SPLIT";
    // 並垛
    public static final String ACTION_MERGE = "MERGE";
    // 入庫
    public static final String ACTION_IN_STORAGE_BIN = "IN_STORAGE_BIN";
    // 出庫
    public static final String ACTION_OUT_STORAGE_BIN = "OUT_STORAGE_BIN";
    // 盤點
    public static final String ACTION_CHECK = "CHECK";

    @Autowired
    private HandlingUnitMapper handlingUnitMapper;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private CarrierService carrierService;

    @Autowired
    private CarrierTypeService carrierTypeService;

    @Autowired
    private HandlingUnitLogService handlingUnitLogService;

    @Autowired
    private HandlingUnitChangeLogService handlingUnitChangeLogService;

    @Autowired
    private HandlingUnitLocationService handlingUnitLocationService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemTypeService itemTypeService;

    @Autowired
    private HandlingUnitSpecService handlingUnitSpecService;

    @Autowired
    private StorageBinStatusService storageBinStatusService;

    @Autowired
    private InventorySummaryService inventorySummaryService;

    @Autowired
    private StorageBinService storageBinService;

    @Autowired
    private MessageSendService messageSendService;

    @Override
    public IPage<HandlingUnitDTO> selectPageVo(Page page, Wrapper<HandlingUnitDTO> wrapper) {
        return handlingUnitMapper.selectVo(page, wrapper);
    }

    @Override
    public List<HandlingUnitDTO> selectList(Wrapper<HandlingUnitDTO> wrapper) {
        return handlingUnitMapper.selectVo(wrapper);
    }

    /**
     * 獲取傳入批次載具繫結的庫存總量
     *
     * @param batchNo
     * @return
     */
    @Override
    public BigDecimal batchNoTotalQty(String batchNo) {
        String inventoryBo = Inventory.genHandle(batchNo);
        BigDecimal totalQty = handlingUnitMapper.getTotalQtyByInventory(inventoryBo);
        return totalQty == null ? BigDecimal.ZERO : totalQty;
    }

    /**
     * 載具繫結
     *
     * @param user
     * @param carrierBindDTO
     */
    @Override
    public void carrierBind(String user, CarrierBindDTO carrierBindDTO) {
        /*
        JSONObject temp = new JSONObject();
        temp.put("batchNo", carrierBindDTO.getBatchNo());
        temp.put("Action", carrierBindDTO.getAction());
        temp.put("Carrier", carrierBindDTO.getCarrier());
        temp.put("Qty", carrierBindDTO.getQty());
        temp.put("Status", carrierBindDTO.getStatus());
        temp.put("All", carrierBindDTO.toString());
        messageSendService.send("MQ_LOG", temp);*/

        LocalDateTime now = LocalDateTime.now();
        String batchNo = carrierBindDTO.getBatchNo();
        String carrierNo = carrierBindDTO.getCarrier();
        String status = carrierBindDTO.getStatus();
        BigDecimal qty = carrierBindDTO.getQty();
        String carrierBo = Carrier.genHandle(carrierNo);
        String inventoryBo = Inventory.genHandle(batchNo);

        try{
            // 驗證載具繫結內容合法
            validateBeforeCarrierBind(carrierBindDTO);
        }catch (Exception e){
            JSONObject JsonE = new JSONObject();
            JsonE.put("QUEUE", "carrierBind -e");
            JsonE.put("MESSAGE_BODY", e.getMessage());
            JsonE.put("CREATED_DATE_TIME", LocalDateTime.now().toString());
            messageSendService.send(CommonConstants.MQ_LOG, JsonE);
        }

        HandlingUnit exists = null;
        String handlingId;
        HandlingUnit saveParam = null;
        String item = null;
        List<HandlingUnit> handlingUnitList = this.list(Wrappers.<HandlingUnit>lambdaQuery().eq(HandlingUnit::getCarrierBo, carrierBo));

        if (handlingUnitList != null && handlingUnitList.size() > 0) {
            handlingId = handlingUnitList.get(0).getHandlingId();
            Optional<HandlingUnit> opt = handlingUnitList.stream().filter(h -> h.getHandlingUnitContextGbo().equals(inventoryBo)).findFirst();
            if(opt.isPresent()) {
                saveParam = opt.get();
                saveParam.setInventoryQty(qty.add(saveParam.getInventoryQty()));
                saveParam.setStatus(status);
                saveParam.setUpdater(user);
                saveParam.setUpdateTime(now);
                this.updateById(saveParam);
            }
        } else {
            handlingId = (StringUtils.createQUID()); //DateUtil.getDateTimeWithRandomNum()
        }

        if(saveParam == null) {
            saveParam = new HandlingUnit(carrierBo, inventoryBo);
            saveParam.setHandlingId(handlingId);
            saveParam.setInventoryQty(qty);
            saveParam.setStatus(status);
            saveParam.setCreator(user);
            saveParam.setUpdater(user);
            saveParam.setCreateTime(now);
            saveParam.setUpdateTime(now);
            this.save(saveParam);
            handlingUnitList.add(saveParam);
        }

        // 如果混料則更新混料標識
        String isMixed = isMixed(handlingUnitList) ? CommonConstants.Y : CommonConstants.N;
        List<String> handlingUnitBoList = handlingUnitList.stream().map(HandlingUnit::getHandle).collect(Collectors.toList());
        this.update(Wrappers.<HandlingUnit>lambdaUpdate().in(HandlingUnit::getHandle, handlingUnitBoList).set(HandlingUnit::getMixed, isMixed));
        // change carrier status & dashboard
        Inventory inventory = inventoryService.getById(inventoryBo);
        item = inventory == null ? null : StringUtils.trimHandle(inventory.getItemBo());
        if(StrUtil.isBlank(carrierBindDTO.getAction())) {
            carrierService.changeStatus(carrierNo, item, "IN_STORAGE", CommonConstants.STATUS_WAIT_IN_STORAGE);
        }

        // create log
        HandlingUnitLog handlingUnitLog = new HandlingUnitLog();
        BeanUtils.copyProperties(saveParam, handlingUnitLog);
        handlingUnitLog.setInventoryBo(inventoryBo);
        handlingUnitLog.setCreator(user);
        handlingUnitLogService.saveOrUpdate(handlingUnitLog);
        // change log
        HandlingUnitChangeLog handlingUnitChangeLog = new HandlingUnitChangeLog();
        handlingUnitChangeLog.setHandle(StringUtils.createQUID()); //DateUtil.getDateTimeWithRandomNum()
        handlingUnitChangeLog.setHandlingId(handlingId);
        handlingUnitChangeLog.setInventoryQty(qty);
        handlingUnitChangeLog.setCreateTime(now);
        handlingUnitChangeLog.setCreator(user);
        handlingUnitChangeLog.setActionCode(StrUtil.isBlank(carrierBindDTO.getAction()) ? ACTION_CARRIER_BIND : carrierBindDTO.getAction());
        handlingUnitChangeLogService.save(handlingUnitChangeLog);
    }

    /**
     * 載具解綁(解綁作為繫結誤操作的反向操作，只有特殊許可權可用)
     *
     * @param user
     * @param handles
     */
    @Override
    public void carrierUnbind(String user, List<String> handles) {
        List<HandlingUnitChangeLog> handlingUnitChangeLogList = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        List<HandlingUnit> handlingUnitList = (List<HandlingUnit>) this.listByIds(handles);
        List<String> carrierBoList = handlingUnitList.stream().map(HandlingUnit::getCarrierBo).distinct().collect(Collectors.toList());

        List<HandlingUnitLocation> handlingUnitLocationList = handlingUnitLocationService.list(Wrappers.<HandlingUnitLocation>lambdaQuery()
                .in(HandlingUnitLocation::getCarrierBo, carrierBoList));
        if (handlingUnitLocationList.size() > 0) {
            List<String> carrierList = handlingUnitLocationList.stream().map(r -> StringUtils.trimHandle(r.getCarrierBo())).distinct().collect(Collectors.toList());
            throw BusinessException.build("載具" + carrierList + "在庫位中，不允許解綁");
        }
        this.removeByIds(handles);
        // 更新混料標識
        List<HandlingUnit> remainedHandlingUnitList = this.list(Wrappers.<HandlingUnit>lambdaQuery().in(HandlingUnit::getCarrierBo, carrierBoList));
        Map<String, List<HandlingUnit>> handlingUnitMapList = new HashMap<>();
        if (remainedHandlingUnitList != null && remainedHandlingUnitList.size() > 0) {
            handlingUnitMapList = remainedHandlingUnitList.stream().collect(Collectors.groupingBy(HandlingUnit::getCarrierBo));
        }
        for (Map.Entry<String, List<HandlingUnit>> entry : handlingUnitMapList.entrySet()) {
            String isMixed = isMixed(entry.getValue()) ? CommonConstants.Y : CommonConstants.N;
            List<String> handlingUnitBoList = entry.getValue().stream().map(HandlingUnit::getHandle).collect(Collectors.toList());
            this.update(Wrappers.<HandlingUnit>lambdaUpdate().in(HandlingUnit::getHandle, handlingUnitBoList).set(HandlingUnit::getMixed, isMixed));
        }

        // 移除建立的處理單元記錄（good?）
        handlingUnitLogService.removeByIds(handles);

        for (HandlingUnit handlingUnit : handlingUnitList) {
            // change log
            HandlingUnitChangeLog handlingUnitChangeLog = new HandlingUnitChangeLog();
            handlingUnitChangeLog.setHandlingId(handlingUnit.getHandlingId());
            handlingUnitChangeLog.setInventoryQty(handlingUnit.getInventoryQty());
            handlingUnitChangeLog.setCreateTime(now);
            handlingUnitChangeLog.setCreator(user);
            handlingUnitChangeLog.setActionCode(ACTION_CARRIER_UNBIND);
            handlingUnitChangeLogList.add(handlingUnitChangeLog);
        }
        handlingUnitChangeLogService.saveBatch(handlingUnitChangeLogList);
    }

    /**
     * 記錄處理單元規格資訊，並返回最優貨格
     *
     * @param user
     * @param wcsHandlingUnitSpecDTO
     */
    @Override
    public void handlingUnitInfoProcess(String user, WcsHandlingUnitSpecDTO wcsHandlingUnitSpecDTO) {
        LocalDateTime now = LocalDateTime.now();
        String carrier = wcsHandlingUnitSpecDTO.getCarrier();
        HandlingUnit handlingUnit = this.getOne(Wrappers.<HandlingUnit>lambdaQuery().eq(HandlingUnit::getCarrierBo, Carrier.genHandle(carrier)), false);
        if (handlingUnit == null) {
            // 發送報警資訊(載具沒有繫結庫存批)給WCS
            throw BusinessException.build("載具[" + carrier + "]沒有繫結庫存批");
        }
        // 校驗目前載具是否已在其它貨格存在
        HandlingUnitLocation existsHandlingUnitLocation = handlingUnitLocationService.getByCarrier(carrier);
        if (existsHandlingUnitLocation != null) {
            throw BusinessException.build("載具" + carrier + "已繫結貨格" + StringUtils.trimHandle(existsHandlingUnitLocation.getBindContextGbo()));
        }
        // 儲存處理單元規格資訊
        HandlingUnitSpec handlingUnitSpec = new HandlingUnitSpec();
        BeanUtils.copyProperties(wcsHandlingUnitSpecDTO, handlingUnitSpec);
        handlingUnitSpec.setHandle(handlingUnit.getHandlingId());
        handlingUnitSpec.setCarrierBo(Carrier.genHandle(carrier));
        handlingUnitSpec.setCreateTime(now);
        handlingUnitSpec.setCreator(user);
        handlingUnitSpecService.saveOrUpdate(handlingUnitSpec); // good?
    }

    /**
     * 處理單元入庫請求
     * @param carrier
     * @param busyStorageLocation
     * @return 最優貨格
     */
    @Override
    public StorageBin handlingUnitInRequest(String carrier, List<String> busyStorageLocation) {
        LocalDateTime now = LocalDateTime.now();
        HandlingUnit handlingUnit = this.getOne(Wrappers.<HandlingUnit>lambdaQuery().eq(HandlingUnit::getCarrierBo, Carrier.genHandle(carrier)), false);
        if (handlingUnit == null) {
            // 發送報警資訊(載具沒有繫結庫存批)給WCS
            throw BusinessException.build("載具[" + carrier + "]沒有繫結庫存批");
        }
        HandlingUnitSpec handlingUnitSpec = handlingUnitSpecService.getById(handlingUnit.getHandlingId());
        if (handlingUnitSpec == null) {
            throw BusinessException.build("載具" + carrier + "未獲取到長寬高資訊");
        }
        // 校驗目前載具是否已在其它貨格存在
        HandlingUnitLocation existsHandlingUnitLocation = handlingUnitLocationService.getByCarrier(carrier);
        if (existsHandlingUnitLocation != null) {
            throw BusinessException.build("載具" + carrier + "已繫結貨格" + StringUtils.trimHandle(existsHandlingUnitLocation.getBindContextGbo()));
        }
        BigDecimal width = handlingUnitSpec.getWidth();
        BigDecimal length = handlingUnitSpec.getLength();
        BigDecimal height = handlingUnitSpec.getHeight();
        BigDecimal weight = handlingUnitSpec.getWeight();
        WcsHandlingUnitSpecDTO wcsHandlingUnitSpecDTO = new WcsHandlingUnitSpecDTO(carrier, width, length, height, weight, busyStorageLocation);
        // 計算最優貨格
        StorageBin storageBin = handlingUnitLocationService.getOptimalStorageBin(Carrier.genHandle(carrier), wcsHandlingUnitSpecDTO);
        if(storageBin != null) {
            // 更新貨格狀態為入庫中狀態,佔用目前貨格防止重複派工到該貨格
            StorageBinStatus storageBinStatus = new StorageBinStatus();
            storageBinStatus.setHandle(storageBin.getHandle());
            storageBinStatus.setStorageBin(storageBin.getStorageBin());
            storageBinStatus.setStatus(CommonConstants.STATUS_IN_STORAGE);
            storageBinStatus.setCreateTime(now);
            storageBinStatus.setUpdateTime(now);
            storageBinStatusService.saveOrUpdate(storageBinStatus);
            // change carrier status
            Inventory inventory = inventoryService.getById(handlingUnit.getHandlingUnitContextGbo());
            String item = inventory == null ? null : StringUtils.trimHandle(inventory.getItemBo());
            carrierService.changeStatus(carrier, item, "IN_STORAGE", CommonConstants.STATUS_IN_STORAGE);
        }
        return storageBin;
    }

    @Override
    public boolean isMixed(String carrier) {
        HandlingUnit handlingUnit = this.getOne(Wrappers.<HandlingUnit>lambdaQuery().eq(HandlingUnit::getCarrierBo, Carrier.genHandle(carrier)), false);
        return handlingUnit == null ? false : CommonConstants.Y.equals(handlingUnit.getMixed());
    }

    /**
     * 入庫
     *
     * @param user
     * @param carrierNo
     * @param storageBin
     */
    @Override
    public void inStorage(String user, String carrierNo, String storageBin) {
        LocalDateTime now = LocalDateTime.now();
        List<HandlingUnit> handlingUnitList = this.list(Wrappers.<HandlingUnit>lambdaQuery().eq(HandlingUnit::getCarrierBo, Carrier.genHandle(carrierNo)));
        if (handlingUnitList == null || handlingUnitList.size() == 0) {
            throw BusinessException.build("載具" + carrierNo + "未繫結任何物料");
        }
        // 校驗目前載具是否已在其它貨格存在
        HandlingUnitLocation existsHandlingUnitLocation = handlingUnitLocationService.getByCarrier(carrierNo);
        if (existsHandlingUnitLocation != null) {
            throw BusinessException.build("載具" + carrierNo + "已繫結貨格" + StringUtils.trimHandle(existsHandlingUnitLocation.getBindContextGbo()));
        }
        // 校驗目前貨格是否已繫結載具
        HandlingUnitLocation existsCarrier = handlingUnitLocationService.getByStorageBin(storageBin);
        if (existsCarrier != null) {
            throw BusinessException.build("貨格" + storageBin + "已繫結載具" + StringUtils.trimHandle(existsCarrier.getCarrierBo()));
        }
        Carrier carrier = carrierService.getById(Carrier.genHandle(carrierNo));

        String handlingUnitId = null;
        Set<String> inventoryBoList = new TreeSet<>();
        for (HandlingUnit handlingUnit : handlingUnitList) {
            if (handlingUnitId == null) {
                handlingUnitId = handlingUnit.getHandlingId();
            }
            inventoryBoList.add(handlingUnit.getHandlingUnitContextGbo());
        }
        HandlingUnitLocation handlingUnitLocation = new HandlingUnitLocation(Carrier.genHandle(carrierNo), handlingUnitId, StorageBin.genHandle(storageBin));
        handlingUnitLocation.setCreateTime(LocalDateTime.now());
        handlingUnitLocation.setCreator(user);
        handlingUnitLocationService.save(handlingUnitLocation);
        // 更新庫存為已生效狀態
        inventoryService.update(Wrappers.<Inventory>lambdaUpdate().in(Inventory::getHandle, inventoryBoList).set(Inventory::getValid, CommonConstants.Y));
        // 更新貨格狀態為IN_USE
        StorageBinStatus storageBinStatus = storageBinStatusService.getById(StorageBin.genHandle(storageBin));
        if(storageBinStatus == null) {
            storageBinStatus = new StorageBinStatus();
            storageBinStatus.setHandle(StorageBin.genHandle(storageBin));
            storageBinStatus.setStorageBin(storageBin);
            storageBinStatus.setCreateTime(LocalDateTime.now());
        }
        storageBinStatus.setStatus(CommonConstants.STATUS_IN_USE);
        storageBinStatus.setUpdateTime(LocalDateTime.now());
        storageBinStatusService.saveOrUpdate(storageBinStatus);
        // change carrier status
        carrierService.changeStatus(carrierNo, null, null, CommonConstants.STATUS_BE_PUT_IN_STORAGE);

        HandlingUnitChangeLog handlingUnitChangeLog = new HandlingUnitChangeLog();
        handlingUnitChangeLog.setHandlingId(handlingUnitId);
        handlingUnitChangeLog.setStorageBinBo(StorageBin.genHandle(storageBin));
        handlingUnitChangeLog.setCreateTime(now);
        handlingUnitChangeLog.setCreator(user);
        handlingUnitChangeLog.setActionCode(ACTION_IN_STORAGE_BIN);
        handlingUnitChangeLogService.save(handlingUnitChangeLog);
        // add inventory dashboard summary info
        for(HandlingUnit handlingUnit : handlingUnitList) {
            String inventoryBo = handlingUnit.getHandlingUnitContextGbo();
            Inventory inventory = inventoryService.getById(inventoryBo);
            if(inventory != null && !CommonConstants.STATUS_EXCHANGE.equals(carrier.getStatus())) {
                LocalDate nowDate = LocalDate.now();
                inventorySummaryService.setTotalQty(inventory.getItemBo(), inventoryService.summaryQtyOnHandByItem(inventory.getItemBo()), nowDate);
                inventorySummaryService.inbound(inventory.getItemBo(), handlingUnit.getInventoryQty(), nowDate);
            }
        }
    }

    /**
     * 出庫
     *
     * @param user
     * @param carrierNo
     * @param storageBin
     */
    @Override
    public void outStorage(String user, String carrierNo, String storageBin) {
        LocalDateTime now = LocalDateTime.now();
        // 校驗目前載具是否在目前貨格
        HandlingUnitLocation handlingUnitLocation = handlingUnitLocationService.getByCarrier(carrierNo);
        if (handlingUnitLocation == null) {
            throw BusinessException.build("載具" + carrierNo + "不在任何貨格中");
        }
        if (!storageBin.equals(StringUtils.trimHandle(handlingUnitLocation.getBindContextGbo()))) {
            throw BusinessException.build("載具" + carrierNo + "不在貨格" + storageBin + "中，它在" + StringUtils.trimHandle(handlingUnitLocation.getBindContextGbo()) + "中");
        }
        Carrier carrier = carrierService.getById(Carrier.genHandle(carrierNo));
        if(carrier == null) {
            throw BusinessException.build("carrier " + carrierNo + " not exists");
        }
        // 從貨格拿出庫存批
        handlingUnitLocationService.removeById(handlingUnitLocation.getHandle());

        HandlingUnitChangeLog handlingUnitChangeLog = new HandlingUnitChangeLog();
        handlingUnitChangeLog.setHandlingId(handlingUnitLocation.getHandlingId());
        handlingUnitChangeLog.setStorageBinBo(StorageBin.genHandle(storageBin));
        handlingUnitChangeLog.setCreateTime(now);
        handlingUnitChangeLog.setCreator(user);
        handlingUnitChangeLog.setActionCode(ACTION_OUT_STORAGE_BIN);
        handlingUnitChangeLogService.save(handlingUnitChangeLog);
        // 更新貨格狀態為出庫中
        storageBinStatusService.update(Wrappers.<StorageBinStatus>lambdaUpdate().eq(StorageBinStatus::getHandle, StorageBin.genHandle(storageBin)).set(StorageBinStatus::getStatus, CommonConstants.STATUS_OUT_STORAGE));
        // change carrier status
        if(!CommonConstants.STATUS_EXCHANGE.equals(carrier.getStatus())) {
            carrierService.changeStatus(carrierNo, null,null, CommonConstants.STATUS_UNKNOWN);
        }
    }

    /**
     * 根據載具編號獲取處理單元標識
     *
     * @param carrier
     */
    @Override
    public String getHandlingId(String carrier) {
        HandlingUnit handlingUnit = this.getOne(Wrappers.<HandlingUnit>lambdaQuery().eq(HandlingUnit::getCarrierBo, Carrier.genHandle(carrier)), false);
        if (handlingUnit != null) {
            return handlingUnit.getHandlingId();
        }
        return null;
    }

    @Override
    public Map<String, BigDecimal> getHandlingUnitQty(String handlingId) {
        Map<String, BigDecimal> result = new HashMap<>();
        List<HandlingUnit> handlingUnitList = this.list(Wrappers.<HandlingUnit>lambdaQuery().eq(HandlingUnit::getHandlingId, handlingId));
        if(handlingUnitList != null && handlingUnitList.size() > 0) {
            for(HandlingUnit handlingUnit : handlingUnitList) {
                BigDecimal qty = handlingUnit.getInventoryQty();
                String inventoryBo = handlingUnit.getHandlingUnitContextGbo();
                Inventory inventory = inventoryService.getById(inventoryBo);
                if(inventory != null) {
                    if(result.containsKey(inventory.getItemBo())) {
                        qty = qty.add(result.get(inventory.getItemBo()));
                    }
                    result.put(inventory.getItemBo(), qty);
                }
            }
        }
        return result;
    }

    /**
     * Rearrangement handling unit.(重新整理處理單元)
     * remove exists handling unit. if handlingUnitContextList is not empty, recreate by handlingUnitContextList
     * @param user
     * @param carrierBo
     * @param handlingUnitContextList
     */
    @Override
    public void rearrangement(String user, String carrierBo, List<HandlingUnitContextDTO> handlingUnitContextList) {
        this.remove(Wrappers.<HandlingUnit>lambdaQuery().eq(HandlingUnit::getCarrierBo, carrierBo));
        if(handlingUnitContextList != null && handlingUnitContextList.size() > 0) {
            for(HandlingUnitContextDTO handlingUnitContextDTO : handlingUnitContextList) {
                CarrierBindDTO carrierBindDTO = new CarrierBindDTO();
                carrierBindDTO.setBatchNo(StringUtils.trimHandle(handlingUnitContextDTO.getHandlingUnitContextGbo()));
                carrierBindDTO.setCarrier(StringUtils.trimHandle(carrierBo));
                carrierBindDTO.setQty(handlingUnitContextDTO.getQty());
                carrierBindDTO.setStatus(handlingUnitContextDTO.getStatus());
                carrierBindDTO.setAction("REARRANGEMENT");
                this.carrierBind(user, carrierBindDTO);
            }
        }
    }

    @Override
    public void doStorageChange(StorageChangeDTO storageChangeDTO) {
        String queueName = "carrier.switch.notice.process";
        Carrier carrier = carrierService.getById(Carrier.genHandle(storageChangeDTO.getCarrier()));
        if (carrier == null) {
            throw BusinessException.build("carrier not exists");
        }
        StorageBin storageBin = storageBinService.getById(StorageBin.genHandle(storageChangeDTO.getSourceStorageBin()));
        if (storageBin == null) {
            throw BusinessException.build("source storage bin not exists");
        }
        StorageBin targetStorageBin = storageBinService.getById(StorageBin.genHandle(storageChangeDTO.getTargetStorageBin()));
        if (targetStorageBin == null) {
            throw BusinessException.build("target storage bin not exists");
        }
        HandlingUnitLocation sourceLocation = handlingUnitLocationService.getByStorageBin(storageChangeDTO.getSourceStorageBin());
        if (sourceLocation == null) {
            throw BusinessException.build("source storage bin is empty");
        }
        if (!storageChangeDTO.getCarrier().equals(StringUtils.trimHandle(sourceLocation.getCarrierBo()))) {
            throw BusinessException.build("carrier not in source storage bin");
        }
        HandlingUnitLocation handlingUnitLocation = handlingUnitLocationService.getByStorageBin(storageChangeDTO.getTargetStorageBin());
        if (handlingUnitLocation != null) {
            throw BusinessException.build("target storage bin already exists carrier " + StringUtils.trimHandle(handlingUnitLocation.getCarrierBo()));
        }
        String item = null;
        HandlingUnit handlingUnit = this.getOne(Wrappers.<HandlingUnit>lambdaQuery().eq(HandlingUnit::getCarrierBo, Carrier.genHandle(storageChangeDTO.getCarrier())), false);
        if(handlingUnit != null) {
            Inventory inventory = inventoryService.getById(handlingUnit.getHandlingUnitContextGbo());
            if(inventory != null) {
                item = StringUtils.trimHandle(inventory.getItemBo());
            }
        }
        carrierService.changeStatus(storageChangeDTO.getCarrier(), item, CommonConstants.STATUS_EXCHANGE, CommonConstants.STATUS_EXCHANGE);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("MESSAGE_TYPE" , queueName);
        jsonObject.put("MESSAGE_ID" , StringUtils.createQUID());
        jsonObject.put("CORRELATION_ID" , StringUtils.createQUID());
        jsonObject.put("SEND_TIME" , DateUtil.format(new Date()));
        jsonObject.put("CARRIER" , storageChangeDTO.getCarrier());
        jsonObject.put("STORAGE_LOCATION" , StringUtils.trimHandle(storageBin.getStorageLocationBo()));
        jsonObject.put("STORAGE_BIN" , storageChangeDTO.getSourceStorageBin());
        jsonObject.put("TARGET_STORAGE_BIN" , storageChangeDTO.getTargetStorageBin());
        messageSendService.send(queueName, jsonObject);
    }

    private boolean isMixed(List<HandlingUnit> handlingUnitList) {
        Set<String> inventoryHandleList = new TreeSet<>();
        for (HandlingUnit handlingUnit : handlingUnitList) {
            String inventoryHandle = handlingUnit.getHandlingUnitContextGbo();
            inventoryHandleList.add(inventoryHandle);
        }
        boolean mixed = false;
        if (inventoryHandleList.size() > 1) {
            List<Inventory> inventoryList = inventoryService.list(Wrappers.<Inventory>lambdaQuery().in(Inventory::getHandle, inventoryHandleList));
            if (inventoryList.size() > 0) {
                Set<String> itemBoList = new TreeSet<>();
                for (Inventory inventory : inventoryList) {
                    itemBoList.add(inventory.getItemBo());
                }
                if (itemBoList.size() > 1) {
                    mixed = true;
                }
            }
        }
        return mixed;
    }

    private void validateBeforeCarrierBind(CarrierBindDTO carrierBindDTO) {
        String batchNo = carrierBindDTO.getBatchNo();
        String carrierNo = carrierBindDTO.getCarrier();
        String status = carrierBindDTO.getStatus();
        BigDecimal qty = carrierBindDTO.getQty();
        if (StrUtil.isBlank(batchNo)) {
            throw BusinessException.build("庫存批號不能為空");
        }
        if (StrUtil.isBlank(carrierNo)) {
            throw BusinessException.build("棧板編號不能為空");
        }
        if (StrUtil.isBlank(status)) {
            throw BusinessException.build("狀態不能為空");
        }
        if (qty == null || qty.compareTo(BigDecimal.ZERO) <= 0) {
            throw BusinessException.build("數量必須為大於零的數字");
        }

        Inventory inventory = inventoryService.getById(Inventory.genHandle(batchNo));
        if (inventory == null) {
            throw BusinessException.build("庫存批號" + batchNo + "不存在");
        }
        Carrier carrier = carrierService.getById(Carrier.genHandle(carrierNo));
        if (carrier == null) {
            throw BusinessException.build("棧板" + carrierNo + "不存在");
        }
        // TODO 庫存為限制庫存，繫結的處理單元只能為限制狀態？？？
        if (StatusEnum.INVENTORY_RESTRICT.getCode().equals(inventory.getStatus())) {

        }
        BigDecimal totalQty = this.batchNoTotalQty(batchNo);
        totalQty = totalQty.add(qty);
        if (totalQty.compareTo(inventory.getQtyOnHand()) > 0) {
            throw BusinessException.build("庫存批號'" + batchNo + "'繫結總量'" + totalQty + "'已超過庫存可用數量'" + inventory.getQtyOnHand() + "'");
        }
        List<HandlingUnitLocation> handlingUnitLocationList = handlingUnitLocationService.list(Wrappers.<HandlingUnitLocation>lambdaQuery()
                .eq(HandlingUnitLocation::getCarrierBo, Carrier.genHandle(carrierNo)));
        if (handlingUnitLocationList.size() > 0) {
            List<String> carrierList = handlingUnitLocationList.stream().map(r -> StringUtils.trimHandle(r.getCarrierBo())).distinct().collect(Collectors.toList());
            throw BusinessException.build("棧板" + carrierList + "在庫位中，不允許繫結");
        }
        // 驗證混料 載具型別、物料型別、物料
        boolean mixed = false;
        // 不允許同物料混批放，領料時沒辦法拆分識別同一載具對應哪個批次
        boolean mixedBatch = false;
        QueryWrapper wrapper = new QueryWrapper<HandlingUnitDTO>();
        HandlingUnitDTO handlingUnitDTO = new HandlingUnitDTO();
        handlingUnitDTO.setCarrier(carrierNo);
        wrapper.setEntity(handlingUnitDTO);
        List<HandlingUnitDTO> handlingUnitDTOList = this.selectList(wrapper);
        for (HandlingUnitDTO dto : handlingUnitDTOList) {
            String itemBo = dto.getItemBo();
            // 混批
            if (inventory.getItemBo().equals(itemBo) && !batchNo.equals(dto.getBatchNo())) {
                throw BusinessException.build("棧板" + carrierNo + "已有相同物料的批次["  + dto.getBatchNo() +"]不允許混批");
            }
            if (!inventory.getItemBo().equals(itemBo)) {
                mixed = true;
                break;
            }
        }
        if (!mixed) {
            return;
        }
        // 載具混料校驗
        String carrierTypeBo = carrier.getCarrierTypeBo();
        if (StrUtil.isNotBlank(carrierTypeBo)) {
            CarrierType carrierType = carrierTypeService.getById(carrierTypeBo);
            if (carrierType != null && CommonConstants.N.equals(carrierType.getMixItem())) {
                throw BusinessException.build("棧板" + carrierNo + "不允許混料");
            }
        }
        // 物料型別混料校驗
        Item item = itemService.getById(inventory.getItemBo());
        if (item == null) {
            throw BusinessException.build("物料" + StringUtils.trimHandle(inventory.getItemBo()) + "不存在");
        }
        if (CommonConstants.N.equals(item.getMixItem())) {
            throw BusinessException.build("物料" + item.getItem() + "不允許混料");
        }
        String itemTypeBo = item.getItemTypeBo();
        if (StrUtil.isNotBlank(itemTypeBo)) {
            ItemType itemType = itemTypeService.getById(itemTypeBo);
            if (itemType != null && CommonConstants.N.equals(itemType.getMixItem())) {
                throw BusinessException.build("物料" + item.getItem() + "對應的物料型別" + itemType.getItemType() + "不允許混料");
            }
        }
    }
}