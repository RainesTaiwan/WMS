package com.fw.wcs.sys.mapper;

import com.fw.wcs.sys.model.StorageLocation;
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
public interface StorageLocationMapper extends BaseMapper<StorageLocation> {

    StorageLocation selectStorageLocationByBin(@Param("storageBin") String storageBin);
}