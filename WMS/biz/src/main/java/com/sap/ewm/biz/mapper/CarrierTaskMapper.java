package com.sap.ewm.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sap.ewm.biz.model.CarrierTask;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 棧板任務主數據 Mapper 介面
 *
 * @author Glanz
 * @since 2021-06-01
 */
@Repository
public interface CarrierTaskMapper extends BaseMapper<CarrierTask>{
    List<CarrierTask> findCarrierTaskByWoSerial(@Param("woSerial") String woSerial);
    List<CarrierTask> findCarrierTaskByVoucherNo(@Param("voucherNo") String voucherNo);
    List<CarrierTask> findCarrierTaskByRArmTaskId(@Param("rArmTaskId") String rArmTaskId);
    CarrierTask findCarrierTaskById(@Param("handle") String handle);
}
