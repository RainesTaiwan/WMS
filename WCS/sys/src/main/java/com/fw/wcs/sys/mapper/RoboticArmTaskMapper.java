package com.fw.wcs.sys.mapper;

import com.fw.wcs.sys.model.RoboticArmTask;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Glanz
 *
 */

@Repository
public interface RoboticArmTaskMapper extends BaseMapper<RoboticArmTask>{
    RoboticArmTask  findRoboticArmTaskByHandle(@Param("handle") String handle);
    List<RoboticArmTask> findRoboticArmTaskByMessageID(@Param("messageID") String messageID);
    List<RoboticArmTask> findRoboticArmTaskByResource(@Param("resource") String resource);
    List<RoboticArmTask> findRoboticArmTaskByVoucherNo(@Param("voucherNo") String voucherNo);
}