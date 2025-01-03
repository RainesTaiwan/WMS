package com.sap.ewm.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sap.ewm.biz.mapper.ShelfOffLogMapper;
import com.sap.ewm.biz.model.ShelfOffLog;
import com.sap.ewm.biz.service.ShelfOffLogService;
import com.sap.ewm.core.base.FrontPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * shelf off log 服務實現類
 * </p>
 *
 * @author syngna
 * @since 2020-08-04
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ShelfOffLogServiceImpl extends ServiceImpl<ShelfOffLogMapper, ShelfOffLog> implements ShelfOffLogService {


    @Autowired
    private ShelfOffLogMapper shelfOffLogMapper;

    @Override
    public IPage<ShelfOffLog> selectPage(FrontPage<ShelfOffLog> frontPage, ShelfOffLog shelfOffLog) {
        QueryWrapper<ShelfOffLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(shelfOffLog);
        return super.page(frontPage.getPagePlus(), queryWrapper);
    }

    @Override
    public List<ShelfOffLog> selectList(ShelfOffLog shelfOffLog) {
        QueryWrapper<ShelfOffLog> queryWrapper = new QueryWrapper<>();
        queryWrapper.setEntity(shelfOffLog);
        return super.list(queryWrapper);
    }

    // 透過MessageId找ShelfOffLog
    @Override
    public ShelfOffLog findShelfOffLogByMessageId(String messageId){
        return shelfOffLogMapper.findShelfOffLogByMessageId(messageId);
    }
}