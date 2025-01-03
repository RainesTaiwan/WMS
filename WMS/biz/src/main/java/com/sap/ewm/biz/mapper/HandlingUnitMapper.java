package com.sap.ewm.biz.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sap.ewm.biz.dto.HandlingUnitDTO;
import com.sap.ewm.biz.model.HandlingUnit;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 處理單元 Mapper 介面
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
@Repository
public interface HandlingUnitMapper extends BaseMapper<HandlingUnit> {

    IPage<HandlingUnitDTO> selectVo(Page<HandlingUnitDTO> page, @Param(Constants.WRAPPER) Wrapper<HandlingUnitDTO> wrapper);

    List<HandlingUnitDTO> selectVo(@Param(Constants.WRAPPER) Wrapper<HandlingUnitDTO> wrapper);

    BigDecimal getTotalQtyByInventory(@Param("inventoryBo") String inventoryBo);
}