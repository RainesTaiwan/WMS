package com.fw.wcs.sys.mapper;

import com.fw.wcs.sys.model.AgvAlarm;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Leon
 *
 */
@Repository
public interface AgvAlarmMapper extends BaseMapper<AgvAlarm> {

    List<AgvAlarm> selectNoCompletedAlarm(@Param("agvNo") String agvNo, @Param("alarmCode") String alarmCode);
}