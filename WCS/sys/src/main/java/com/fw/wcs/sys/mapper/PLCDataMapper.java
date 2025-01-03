package com.fw.wcs.sys.mapper;

import com.fw.wcs.sys.model.PLCData;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 *  Mapper 接口
 *
 * @author Glanz
 *
 */
@Repository
public interface PLCDataMapper extends BaseMapper<PLCData>{
    void deleteDataByTime(@Param("createdTime") Date createdTime);
    List<PLCData> findPLCDataByConveyor(@Param("plcId") String plcId);
}
