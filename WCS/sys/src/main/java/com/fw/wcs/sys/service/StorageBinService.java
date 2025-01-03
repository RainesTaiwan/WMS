package com.fw.wcs.sys.service;

import com.baomidou.mybatisplus.service.IService;
import com.fw.wcs.sys.model.StorageBin;

/**
 * <p>
 *  服務類
 * </p>
 *
 * @author Leon
 *
 */
public interface StorageBinService extends IService<StorageBin> {

    StorageBin getStorageBin(String storageBin);
}