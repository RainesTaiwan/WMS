package com.sap.ewm.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.model.Status;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sap.ewm.core.base.FrontPage;

import java.util.List;

/**
 * <p>
 * 狀態主數據 服務類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
public interface StatusService extends IService<Status> {

    /**
     *  分頁查詢
     * @param frontPage
     * @return
     */
    IPage<Status> selectPage(FrontPage<Status> frontPage, Status status);

    List<Status> selectList(Status status);

    List<Status> listStatusByGroup(String statusGroup);
}