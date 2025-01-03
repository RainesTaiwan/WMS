package com.sap.ewm.biz.mapper;

import com.sap.ewm.biz.model.Inventory;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * <p>
 * 庫存（物料批次） Mapper 介面
 * </p>
 *
 * @author Syngna
 * @since 2020-07-05
 */
@Repository
public interface InventoryMapper extends BaseMapper<Inventory> {

    BigDecimal summaryQtyOnHandByItem(@Param("itemBo") String itemBo);

    void deductionQty(@Param("user")String user, @Param("inventoryBo")String inventoryBo, @Param("qty")BigDecimal qty);
}