package com.fw.wcs.sys.mapper;

import com.fw.wcs.sys.model.StorageBin;
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
public interface StorageBinMapper extends BaseMapper<StorageBin> {

    StorageBin selectStorageBin(@Param("storageBin") String storageBin);
}