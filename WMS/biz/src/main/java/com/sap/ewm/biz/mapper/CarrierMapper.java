package com.sap.ewm.biz.mapper;

import com.sap.ewm.dashboard.model.CarrierUsage;
import com.sap.ewm.biz.model.Carrier;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 載具主數據 Mapper 介面
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
@Repository
public interface CarrierMapper extends BaseMapper<Carrier> {
    List<Carrier> findCarrierStatus();
    Carrier findCarrierStatus(@Param("carrier") String carrier, @Param("status") String carrier_status);
    Carrier findCarrierByID(@Param("carrier") String carrier);
}