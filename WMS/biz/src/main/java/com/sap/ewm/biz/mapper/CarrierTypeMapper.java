package com.sap.ewm.biz.mapper;

import com.sap.ewm.biz.model.CarrierType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 載具型別主數據 Mapper 介面
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
@Repository
public interface CarrierTypeMapper extends BaseMapper<CarrierType> {
    CarrierType findCarrierTypeByID(@Param("carrierType") String carrierType);
}