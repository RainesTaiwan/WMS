package com.sap.ewm.biz.mapper;

import com.sap.ewm.biz.model.Conveyor;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Conveyor主數據 Mapper 介面
 *
 * @author Glanz
 * @since 2021-08-10
 */
@Repository
public interface ConveyorMapper extends BaseMapper<Conveyor>{
    List<Conveyor> findAllConveyor();
    Conveyor findConveyorByName(@Param("conveyor") String conveyor);
    int findConveyorWorkload(@Param("conveyor") String conveyor);
}
