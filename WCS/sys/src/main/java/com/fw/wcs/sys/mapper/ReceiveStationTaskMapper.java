package com.fw.wcs.sys.mapper;
import com.fw.wcs.sys.model.ReceiveStationTask;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Glanz
 */
@Repository
public interface ReceiveStationTaskMapper extends BaseMapper<ReceiveStationTask>{
    List<ReceiveStationTask> findReceiveStationTaskByReceiveStation(@Param("receiveStation") String receiveStation);
    List<ReceiveStationTask> findReceiveStationTaskByReceiveStationForStatus
            (@Param("receiveStation") String receiveStation, @Param("status") String status);
    List<ReceiveStationTask> findReceiveStationTaskByReceiveStationForStatusAndStartStation
            (@Param("receiveStation") String receiveStation, @Param("status") String status, @Param("startStation") String startStation);
    List<ReceiveStationTask> findReceiveStationTaskByReceiveStationForStatusAndEndStation
            (@Param("receiveStation") String receiveStation, @Param("status") String status, @Param("endStation") String endStation);
    List<ReceiveStationTask> findReceiveStationTaskByMessageId(@Param("messageId") String messageId);
    void deleteTask(@Param("handle") String handle);
}
