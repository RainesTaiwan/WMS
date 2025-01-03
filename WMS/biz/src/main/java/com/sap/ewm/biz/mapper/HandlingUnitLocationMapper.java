package com.sap.ewm.biz.mapper;

import com.sap.ewm.biz.model.HandlingUnitLocation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 處理單元位置繫結（記錄目前處理單元所在的貨格） Mapper 介面
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
@Repository
public interface HandlingUnitLocationMapper extends BaseMapper<HandlingUnitLocation> {

}