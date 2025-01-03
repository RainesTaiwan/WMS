package com.fw.wcs.sys.mapper;

import com.fw.wcs.sys.model.CarrierTask;
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
public interface CarrierTaskMapper extends BaseMapper<CarrierTask> {

    List<CarrierTask> findCarrierTask(@Param("carrier") String carrier, @Param("status")  String status);
    List<CarrierTask> findNoCompletedTask(@Param("carrier") String carrier, @Param("category") String category, @Param("startPosition") String startPosition);
}