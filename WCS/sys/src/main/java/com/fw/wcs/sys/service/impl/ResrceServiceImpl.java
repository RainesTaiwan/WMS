package com.fw.wcs.sys.service.impl;

import com.fw.wcs.core.base.FrontPage;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fw.wcs.sys.model.Resrce;
import com.fw.wcs.sys.mapper.ResrceMapper;
import com.fw.wcs.sys.service.ResrceService;
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
public class ResrceServiceImpl extends ServiceImpl<ResrceMapper, Resrce> implements ResrceService {


    @Autowired
    private ResrceMapper resrceMapper;

    @Override
    public Page<Resrce> selectPage(FrontPage<Resrce> frontPage, Resrce resrce) {
        EntityWrapper<Resrce> queryWrapper = new EntityWrapper<>();
        queryWrapper.setEntity(resrce);
        return super.selectPage(frontPage.getPagePlus(), queryWrapper);
    }

    @Override
    public List<Resrce> selectList(Resrce resrce) {
        EntityWrapper<Resrce> queryWrapper = new EntityWrapper<>();
        queryWrapper.setEntity(resrce);
        return super.selectList(queryWrapper);
    }


}