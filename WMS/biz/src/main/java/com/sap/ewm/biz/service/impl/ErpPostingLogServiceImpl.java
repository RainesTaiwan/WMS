package com.sap.ewm.biz.service.impl;

import com.sap.ewm.core.base.FrontPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.model.ErpPostingLog;
import com.sap.ewm.biz.mapper.ErpPostingLogMapper;
import com.sap.ewm.biz.service.ErpPostingLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * <p>
 * ERP互動日誌 服務實現類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ErpPostingLogServiceImpl extends ServiceImpl<ErpPostingLogMapper, ErpPostingLog> implements ErpPostingLogService {


    @Autowired
    private ErpPostingLogMapper erpPostingLogMapper;

    @Override
    public IPage<ErpPostingLog> selectPage(FrontPage<ErpPostingLog> frontPage, ErpPostingLog erpPostingLog) {
        QueryWrapper<ErpPostingLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(erpPostingLog);
        return super.page(frontPage.getPagePlus(), queryWrapper);
    }

    @Override
    public List<ErpPostingLog> selectList(ErpPostingLog erpPostingLog) {
        QueryWrapper<ErpPostingLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(erpPostingLog);
        return super.list(queryWrapper);
    }


}