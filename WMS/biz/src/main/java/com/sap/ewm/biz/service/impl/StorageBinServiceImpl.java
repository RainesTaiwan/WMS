package com.sap.ewm.biz.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sap.ewm.biz.dto.MaterialRequisitionStorageBinDTO;
import com.sap.ewm.biz.dto.StorageBinDTO;
import com.sap.ewm.biz.mapper.StorageBinMapper;
import com.sap.ewm.biz.model.StorageBin;
import com.sap.ewm.biz.model.StorageBinType;
import com.sap.ewm.biz.model.StorageLocation;
import com.sap.ewm.biz.service.StorageBinService;
import com.sap.ewm.biz.service.StorageBinTypeService;
import com.sap.ewm.biz.service.StorageLocationService;
import com.sap.ewm.core.base.FrontPage;
import com.sap.ewm.core.exception.BusinessException;
import com.sap.ewm.core.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
/**
 * <p>
 * 儲存貨格主數據 服務實現類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class StorageBinServiceImpl extends ServiceImpl<StorageBinMapper, StorageBin> implements StorageBinService {


    @Autowired
    private StorageBinMapper storageBinMapper;

    @Autowired
    private StorageBinTypeService storageBinTypeService;

    @Autowired
    private StorageLocationService storageLocationService;

    @Override
    public IPage<StorageBin> selectPage(FrontPage<StorageBin> frontPage, StorageBin storageBin) {
        QueryWrapper<StorageBin> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(storageBin);
        return super.page(frontPage.getPagePlus(), queryWrapper);
    }

    @Override
    public List<StorageBin> selectList(StorageBin storageBin) {
        QueryWrapper<StorageBin> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(storageBin);
        return super.list(queryWrapper);
    }

    @Override
    public boolean save(StorageBinDTO storageBin) {
        validateAndAssemble(storageBin);
        storageBin.setHandle(StorageBin.genHandle(storageBin.getStorageBin()));
        int count;
        try {
            count = baseMapper.insert(storageBin);
        } catch(Exception e) {
            if( e instanceof SQLIntegrityConstraintViolationException || e instanceof DuplicateKeyException) {
                throw new MybatisPlusException("貨格'" + storageBin.getStorageBin() + "'已存在");
            } else {
                throw e;
            }
        }
        return retBool(count);
    }

    @Override
    public boolean updateById(StorageBinDTO storageBin) {
        validateAndAssemble(storageBin);
        storageBin.setHandle(StorageBin.genHandle(storageBin.getStorageBin()));
        int count = baseMapper.updateById(storageBin);
        return retBool(count);
    }

    /**
     * 列出領料對應的貨格
     * @param itemBo
     * @return
     */
    @Override
    public List<MaterialRequisitionStorageBinDTO> listMaterialRequisitionStorageBinList(String itemBo) {
        return storageBinMapper.listMaterialRequisitionStorageBinList(itemBo);
    }

    /**
     * 獲取領料對應的貨格
     * @param itemBo
     * @return
     */
    @Override
    public MaterialRequisitionStorageBinDTO getOptimalMaterialRequisitionStorageBin(String itemBo) {
        return storageBinMapper.getOptimalMaterialRequisitionStorageBin(itemBo);
    }

    private void validateAndAssemble(StorageBinDTO storageBinDTO) {
        String storageBinTypeBo = storageBinDTO.getStorageBinTypeBo();
        String storageLocationBo = storageBinDTO.getStorageLocationBo();
        if(StrUtil.isNotBlank(storageBinDTO.getStorageBinType())) {
            storageBinTypeBo = StorageBinType.genHandle(storageBinDTO.getStorageBinType());
        }
        if(StrUtil.isNotBlank(storageBinDTO.getStorageLocation())) {
            storageLocationBo = StorageLocation.genHandle(storageBinDTO.getStorageLocation());
        }
        StorageBinType storageBinType = storageBinTypeService.getById(storageBinTypeBo);
        if(storageBinType == null) {
            throw BusinessException.build("貨格型別" + StringUtils.trimHandle(storageBinTypeBo) + "不存在");
        }
        StorageLocation storageLocation = storageLocationService.getById(storageLocationBo);
        if(storageLocation == null) {
            throw BusinessException.build("儲存地點" + StringUtils.trimHandle(storageLocationBo) + "不存在");
        }
        storageBinDTO.setStorageBinTypeBo(storageBinTypeBo);
        storageBinDTO.setStorageLocationBo(storageLocationBo);
    }

    @Override
    public StorageBin findByHandle(String handle){
        return storageBinMapper.findByHandle(handle);
    }
}