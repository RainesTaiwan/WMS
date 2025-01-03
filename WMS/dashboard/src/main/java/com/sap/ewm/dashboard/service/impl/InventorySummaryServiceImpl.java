package com.sap.ewm.dashboard.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sap.ewm.core.base.FrontPage;
import com.sap.ewm.core.utils.StringUtils;
import com.sap.ewm.dashboard.mapper.InventorySummaryMapper;
import com.sap.ewm.dashboard.model.InventorySummary;
import com.sap.ewm.dashboard.service.InventorySummaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * inventory summary by day 服務實現類
 * </p>
 *
 * @author syngna
 * @since 2020-08-03
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class InventorySummaryServiceImpl extends ServiceImpl<InventorySummaryMapper, InventorySummary> implements InventorySummaryService {

    private final Logger logger = LoggerFactory.getLogger(InventorySummaryServiceImpl.class);

    @Autowired
    private InventorySummaryMapper inventorySummaryMapper;

    @Override
    public IPage<InventorySummary> selectPage(FrontPage<InventorySummary> frontPage, InventorySummary inventorySummary) {
        QueryWrapper<InventorySummary> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(inventorySummary);
        return super.page(frontPage.getPagePlus(), queryWrapper);
    }

    @Override
    public List<InventorySummary> selectList(InventorySummary inventorySummary) {
        QueryWrapper<InventorySummary> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(inventorySummary);
        return super.list(queryWrapper);
    }

    @Override
    public boolean inbound(String itemBo, BigDecimal inboundQty, LocalDate date) {
        logger.info("inventory summary inbound: {}, {}, {}", itemBo, inboundQty, date);
        LocalDate now = LocalDate.now();
        return inventorySummaryMapper.inboundQtyUpdate(StringUtils.trimHandle(itemBo), inboundQty, now) > 0 ? true : false;
    }

    @Override
    public void setTotalQty(String itemBo, BigDecimal summaryQty, LocalDate date) {
        boolean updated = this.update(Wrappers.<InventorySummary>lambdaUpdate()
                .eq(InventorySummary::getItem, StringUtils.trimHandle(itemBo))
                .eq(InventorySummary::getStatisticDate, date).set(InventorySummary::getTotalQty, summaryQty));
        if(!updated) {
            InventorySummary inventorySummary = new InventorySummary();
            inventorySummary.setItem(StringUtils.trimHandle(itemBo));
            inventorySummary.setTotalQty(summaryQty);
            inventorySummary.setOutboundQty(BigDecimal.ZERO);
            inventorySummary.setInboundQty(BigDecimal.ZERO);
            inventorySummary.setStatisticDate(date);
            this.save(inventorySummary);
        }
    }

    @Override
    public boolean outBound(String itemBo, BigDecimal qty, LocalDate date) {
        return inventorySummaryMapper.outboundQtyUpdate(StringUtils.trimHandle(itemBo), qty, date) > 0 ? true : false;
    }
}