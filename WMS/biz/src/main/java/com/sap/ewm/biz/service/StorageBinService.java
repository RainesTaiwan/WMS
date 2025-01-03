package com.sap.ewm.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.dto.MaterialRequisitionStorageBinDTO;
import com.sap.ewm.biz.dto.StorageBinDTO;
import com.sap.ewm.biz.model.StorageBin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sap.ewm.core.base.FrontPage;

import java.util.List;

/**
 * <p>
 * 儲存貨格主數據 服務類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
public interface StorageBinService extends IService<StorageBin> {

    /**
     *  分頁查詢
     * @param frontPage
     * @return
     */
    IPage<StorageBin> selectPage(FrontPage<StorageBin> frontPage, StorageBin storageBin);

    List<StorageBin> selectList(StorageBin storageBin);

    boolean save(StorageBinDTO storageBin);

    boolean updateById(StorageBinDTO storageBin);

    /**
     * 列出領料對應的貨格
     * @param itemBo
     * @return
     */
    List<MaterialRequisitionStorageBinDTO> listMaterialRequisitionStorageBinList(String itemBo);

    MaterialRequisitionStorageBinDTO getOptimalMaterialRequisitionStorageBin(String itemBo);

    StorageBin findByHandle(String handle);
}