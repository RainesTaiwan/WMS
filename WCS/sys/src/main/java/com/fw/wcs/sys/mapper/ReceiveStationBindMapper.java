package com.fw.wcs.sys.mapper;

import com.fw.wcs.sys.model.ReceiveStationBind;
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
public interface ReceiveStationBindMapper extends BaseMapper<ReceiveStationBind> {

    List<ReceiveStationBind> findReceiveStationBind(@Param("receiveStation") String receiveStation, @Param("carrier") String carrier);
}