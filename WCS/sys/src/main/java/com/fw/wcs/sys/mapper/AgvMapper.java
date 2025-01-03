package com.fw.wcs.sys.mapper;

import com.fw.wcs.sys.model.Agv;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
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
public interface AgvMapper extends BaseMapper<Agv> {

    List<Agv> selectCanTransportAgv();
    Agv findAgv(@Param("agvNo") String agvNo);
    void updateByAgvNo(@Param("agvNo") String agvNo, @Param("et") Agv agv);
}