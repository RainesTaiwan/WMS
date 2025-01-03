package com.sap.ewm.dashboard.mapper;

import com.sap.ewm.dashboard.model.InventorySummary;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <p>
 * inventory summary by day Mapper 介面
 * </p>
 *
 * @author syngna
 * @since 2020-08-03
 */
@Repository
public interface InventorySummaryMapper extends BaseMapper<InventorySummary> {

    int inboundQtyUpdate(@Param("item") String item, @Param("inboundQty") BigDecimal inboundQty, @Param("date") LocalDate date);

    int outboundQtyUpdate(@Param("item") String item, @Param("outboundQty") BigDecimal outboundQty, @Param("date") LocalDate date);
}