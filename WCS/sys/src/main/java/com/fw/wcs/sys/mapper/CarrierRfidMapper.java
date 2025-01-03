package com.fw.wcs.sys.mapper;

import com.fw.wcs.sys.model.CarrierRfid;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Leon
 *
 */
@Repository
public interface CarrierRfidMapper extends BaseMapper<CarrierRfid> {

    CarrierRfid selectCarrierByTag(@Param("tag") String tag);
    CarrierRfid findCarrierByID(@Param("carrier") String carrier);
}