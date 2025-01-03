package com.sap.ewm.biz.mapper;

import com.sap.ewm.biz.dto.MaterialRequisitionStorageBinDTO;
import com.sap.ewm.biz.model.StorageBin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 儲存貨格主數據 Mapper 介面
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
@Repository
public interface StorageBinMapper extends BaseMapper<StorageBin> {

    List<MaterialRequisitionStorageBinDTO> listMaterialRequisitionStorageBinList(@Param("itemBo") String itemBo);

    MaterialRequisitionStorageBinDTO getOptimalMaterialRequisitionStorageBin(@Param("itemBo") String itemBo);

    StorageBin findByHandle(@Param("handle") String handle);
}