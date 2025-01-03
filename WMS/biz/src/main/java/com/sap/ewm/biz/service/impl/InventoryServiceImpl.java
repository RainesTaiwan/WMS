package com.sap.ewm.biz.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sap.ewm.biz.constants.CommonConstants;
import com.sap.ewm.biz.constants.StatusEnum;
import com.sap.ewm.biz.dto.InventoryDTO;
import com.sap.ewm.biz.mapper.InventoryMapper;
import com.sap.ewm.biz.model.HandlingUnit;
import com.sap.ewm.biz.model.Inventory;
import com.sap.ewm.biz.model.Item;
import com.sap.ewm.biz.service.HandlingUnitService;
import com.sap.ewm.biz.service.InventoryService;
import com.sap.ewm.biz.service.ItemService;
import com.sap.ewm.core.base.FrontPage;
import com.sap.ewm.core.exception.BusinessException;
import com.sap.ewm.core.utils.DateUtil;
import com.sap.ewm.core.utils.StringUtils;
import com.sap.ewm.dashboard.service.InventorySummaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 庫存（物料批次） 服務實現類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-05
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class InventoryServiceImpl extends ServiceImpl<InventoryMapper, Inventory> implements InventoryService {

    @Autowired
    private InventoryMapper inventoryMapper;

    @Autowired
    private ItemService itemService;

    @Autowired
    private HandlingUnitService handlingUnitService;

    @Autowired
    private InventorySummaryService inventorySummaryService;

    @Override
    public IPage<Inventory> selectPage(FrontPage<Inventory> frontPage, Inventory inventory) {
        QueryWrapper<Inventory> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(inventory);
        return super.page(frontPage.getPagePlus(), queryWrapper);
    }

    @Override
    public List<Inventory> selectList(Inventory inventory) {
        QueryWrapper<Inventory> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(inventory);
        return super.list(queryWrapper);
    }

    @Override
    public boolean save(InventoryDTO inventoryDTO) {
        validateAndAssemble(inventoryDTO, false);
        inventoryDTO.setHandle(Inventory.genHandle(inventoryDTO.getBatchNo()));
        // 計算有效期
        processExpireDate(inventoryDTO);
        inventoryDTO.setOriginalQty(inventoryDTO.getQtyOnHand());
        int count;
        try {
            count = baseMapper.insert(inventoryDTO);
        } catch (Exception e) {
            if (e instanceof SQLIntegrityConstraintViolationException || e instanceof DuplicateKeyException) {
                throw new MybatisPlusException("批次號'" + inventoryDTO.getBatchNo() + "'已存在");
            } else {
                throw e;
            }
        }
        // TODO 庫存變更歷史
        return retBool(count);
    }

    @Override
    public boolean updateById(InventoryDTO inventoryDTO) {
        validateAndAssemble(inventoryDTO, true);
        inventoryDTO.setHandle(Inventory.genHandle(inventoryDTO.getBatchNo()));
        // 計算有效期
        processExpireDate(inventoryDTO);
        inventoryDTO.setOriginalQty(inventoryDTO.getQtyOnHand());
        int count = baseMapper.updateById(inventoryDTO);
        // TODO 庫存變更歷史
        return retBool(count);
    }

    /**
     * 數量扣減
     *
     * @param user
     * @param inventoryBo
     * @param qty
     * @return
     */
    @Override
    public boolean deductionQty(String user, String inventoryBo, BigDecimal qty) {
        inventoryMapper.deductionQty(user, inventoryBo, qty);
        // inventory summary for dashboard
        LocalDate now = LocalDate.now();
        Inventory inventory = this.getById(inventoryBo);
        BigDecimal summaryQty = this.summaryQtyOnHandByItem(inventory.getItemBo());
        inventorySummaryService.setTotalQty(inventory.getItemBo(), summaryQty, now);
        inventorySummaryService.outBound(inventory.getItemBo(), qty, now);
        return true;
    }

    @Override
    public BigDecimal summaryQtyOnHandByItem(String itemBo) {
        return inventoryMapper.summaryQtyOnHandByItem(itemBo);
    }

    @Override
    public boolean removeById(String id) {
        Inventory inventory = this.getById(id);
        if (inventory == null) {
            throw BusinessException.build("庫存批" + inventory.getBatchNo() + "不存在");
        }
        if (CommonConstants.Y.equals(inventory.getValid())) {
            throw BusinessException.build("庫存批" + inventory.getBatchNo() + "已生效，不允許刪除");
        }
        Integer count = handlingUnitService.count(Wrappers.<HandlingUnit>lambdaQuery().eq(HandlingUnit::getHandlingUnitContextGbo, id));
        if (count != null && count > 0) {
            throw BusinessException.build("庫存批" + inventory.getBatchNo() + "已繫結載具，不允許刪除");
        }
        return super.removeById(id);
    }

    private void validateAndAssemble(InventoryDTO inventoryDTO, boolean ignoreInspectionRequired) {
        String itemBo = inventoryDTO.getItemBo();
        if (StrUtil.isNotBlank(inventoryDTO.getItem())) {
            itemBo = Item.genHandle(inventoryDTO.getItem());
        }
        Item item = itemService.getById(itemBo);
        if (item == null) {
            throw BusinessException.build("物料" + StringUtils.trimHandle(itemBo) + "不存在");
        }
        if (!ignoreInspectionRequired) {
            if (CommonConstants.Y.equalsIgnoreCase(item.getInspectionRequired()) && !StatusEnum.INVENTORY_RESTRICT.getCode().equalsIgnoreCase(inventoryDTO.getStatus())) {
                throw BusinessException.build("物料" + StringUtils.trimHandle(itemBo) + "需要檢驗，只能接收為限制庫存");
            }
        }
        inventoryDTO.setItemBo(item.getHandle());
        inventoryDTO.setUnitOfMeasure(item.getUnitOfMeasure());
    }

    private void processExpireDate(InventoryDTO inventoryDTO) {
        Integer validDurations = inventoryDTO.getValidDurations();
        String timeUnit = inventoryDTO.getTimeUnit();
        LocalDateTime productionDateTime = inventoryDTO.getProductionDate();
        if (validDurations != null && StrUtil.isNotBlank(timeUnit)) {
            LocalDateTime expireDate = DateUtil.dateTimeAdd(productionDateTime, validDurations, timeUnit);
            inventoryDTO.setExpireDate(expireDate);
        }
    }

    @Override
    public void saveASRSInStorageData(String voucherNo, String item, String itemQty){
        LocalDateTime now = LocalDateTime.now();
        //將東西存放至 Inventory
        InventoryDTO inventoryDTO = new InventoryDTO();
        inventoryDTO.setHandle("inventoryBo:"+voucherNo);
        inventoryDTO.setStatus("AVAILABLE");
        inventoryDTO.setItemBo("ItemBO:"+item);
        inventoryDTO.setBatchNo(voucherNo);
        inventoryDTO.setVendorBatchNo(voucherNo);
        inventoryDTO.setValid("Y");
        inventoryDTO.setCreator("ADMIN");
        inventoryDTO.setUpdater("ADMIN");
        inventoryDTO.setCreateTime(now);
        inventoryDTO.setUpdateTime(now);
        inventoryDTO.setProductionDate(now);

        BigDecimal Qty = new BigDecimal(itemQty);
        inventoryDTO.setQtyOnHand(Qty);
        inventoryDTO.setOriginalQty(Qty);
        this.save(inventoryDTO);
    }
}