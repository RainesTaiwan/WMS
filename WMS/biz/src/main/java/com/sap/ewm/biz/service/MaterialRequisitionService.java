package com.sap.ewm.biz.service;

import com.sap.ewm.biz.dto.MaterialRequisitionDTO;
import com.sap.ewm.biz.dto.MaterialRequisitionStorageBinDTO;

import java.util.List;

/**
 * 系統領料 服務介面
 */
public interface MaterialRequisitionService {

    List<MaterialRequisitionStorageBinDTO> doMaterialRequisition(String user, MaterialRequisitionDTO materialRequisitionDTO);

    void outStorage(String user, String carrier, String storageBin, String correlationId);

    void outStation(String user, String carrier, String correlationId);
}
