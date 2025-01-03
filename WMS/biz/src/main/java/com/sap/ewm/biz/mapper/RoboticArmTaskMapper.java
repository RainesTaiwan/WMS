package com.sap.ewm.biz.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sap.ewm.biz.model.RoboticArmTask;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 機械手臂任務主數據 Mapper 介面
 *
 * @author Glanz
 * @since 2020-05-20
 */
@Repository
public interface RoboticArmTaskMapper extends BaseMapper<RoboticArmTask>{
    List<RoboticArmTask> findRoboticArmTaskByVoucherNo(@Param("voucherNo") String voucherNo);
    List<RoboticArmTask> findRoboticArmTaskByWoSerial(@Param("woSerial") String woSerial);
    RoboticArmTask findRoboticArmTaskByHandle(@Param("handle") String handle);
}
