package com.sap.ewm.biz.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sap.ewm.core.base.FrontPage;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.sap.ewm.biz.model.Status;
import com.sap.ewm.biz.mapper.StatusMapper;
import com.sap.ewm.biz.service.StatusService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sap.ewm.core.utils.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;

/**
 * <p>
 * 狀態主數據 服務實現類
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class StatusServiceImpl extends ServiceImpl<StatusMapper, Status> implements StatusService {


    @Autowired
    private StatusMapper statusMapper;

    @Override
    public IPage<Status> selectPage(FrontPage<Status> frontPage, Status status) {
        QueryWrapper<Status> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(status);
        return super.page(frontPage.getPagePlus(), queryWrapper);
    }

    @Override
    public List<Status> selectList(Status status) {
        QueryWrapper<Status> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(status);
        return super.list(queryWrapper);
    }

    @Override
    public List<Status> listStatusByGroup(String statusGroup) {
        Locale locale = LocaleContextHolder.getLocale();
        return statusMapper.listStatusByStatusGroup(locale.toLanguageTag(), statusGroup);
    }
}