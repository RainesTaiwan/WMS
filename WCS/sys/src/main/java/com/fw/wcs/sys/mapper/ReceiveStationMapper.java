package com.fw.wcs.sys.mapper;

import com.fw.wcs.sys.model.ReceiveStation;
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
public interface ReceiveStationMapper extends BaseMapper<ReceiveStation> {

    List<ReceiveStation> selectOutboundReceiveStation();
    ReceiveStation selectReceiveStation(@Param("receiveStation") String receiveStation);
}