package com.sap.ewm.biz.mapper;

import com.sap.ewm.biz.dto.StorageLocationDTO;
import com.sap.ewm.biz.model.StorageBin;
import com.sap.ewm.biz.model.StorageLocation;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 儲存位置主數據 Mapper 介面
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
@Repository
public interface StorageLocationMapper extends BaseMapper<StorageLocation> {

    List<StorageLocationDTO> listUsableStorageLocation();

    List<StorageBin> listUsableEmptyStorageBin(@Param("storageLocationBo")String storageLocationBo, @Param("mixItem")String mixItem, @Param("width")BigDecimal width, @Param("height")BigDecimal height, @Param("length")BigDecimal length, @Param("weight")BigDecimal weight);
}