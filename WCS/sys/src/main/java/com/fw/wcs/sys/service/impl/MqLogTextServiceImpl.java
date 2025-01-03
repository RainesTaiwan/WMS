package com.fw.wcs.sys.service.impl;

import com.fw.wcs.core.base.FrontPage;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fw.wcs.sys.model.MqLogText;
import com.fw.wcs.sys.mapper.MqLogTextMapper;
import com.fw.wcs.sys.service.MqLogTextService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
/**
 * <p>
 *  服務實現類
 * </p>
 *
 * @author Leon
 *
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class MqLogTextServiceImpl extends ServiceImpl<MqLogTextMapper, MqLogText> implements MqLogTextService {


    @Autowired
    private MqLogTextMapper mqLogTextMapper;

    @Override
    public Page<MqLogText> selectPage(FrontPage<MqLogText> frontPage, MqLogText mqLogText) {
        EntityWrapper<MqLogText> queryWrapper = new EntityWrapper<>();
        queryWrapper.setEntity(mqLogText);
        return super.selectPage(frontPage.getPagePlus(), queryWrapper);
    }

    @Override
    public List<MqLogText> selectList(MqLogText mqLogText) {
        EntityWrapper<MqLogText> queryWrapper = new EntityWrapper<>();
        queryWrapper.setEntity(mqLogText);
        return super.selectList(queryWrapper);
    }


}