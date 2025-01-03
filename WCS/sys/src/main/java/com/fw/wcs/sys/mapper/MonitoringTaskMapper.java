package com.fw.wcs.sys.mapper;

import com.fw.wcs.sys.model.MonitoringTask;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Glanz
 *
 */
@Repository
public interface MonitoringTaskMapper extends BaseMapper<MonitoringTask>{

    MonitoringTask findMonitoringTaskByID(@Param("handle") String handle);
    void updateMonitoringTaskByID(@Param("handle") String handle, @Param("et") MonitoringTask monitoringTask);
}
