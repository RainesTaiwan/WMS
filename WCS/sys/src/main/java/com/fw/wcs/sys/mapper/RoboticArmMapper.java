package com.fw.wcs.sys.mapper;

import com.fw.wcs.sys.model.RoboticArm;
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
public interface RoboticArmMapper extends BaseMapper<RoboticArm>{
    RoboticArm findRoboticArmByName(@Param("roboticArm") String roboticArm);
}
