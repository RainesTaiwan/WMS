package com.fw.wcs.sys.mapper;

import com.fw.wcs.sys.model.Agv;
import com.fw.wcs.sys.model.AgvTask;
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
public interface AgvTaskMapper extends BaseMapper<AgvTask> {

    AgvTask selectAgvTask(@Param("agvNo") String agvNo);

    AgvTask selectAgvTaskByTaskID(@Param("handle") String handle);

    List<AgvTask> selectAgvTaskByPositionInfo(@Param("startPosition") String startPosition, @Param("targetPosition") String targetPosition);

}