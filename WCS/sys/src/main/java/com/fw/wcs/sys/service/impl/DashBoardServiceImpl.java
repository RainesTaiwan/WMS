package com.fw.wcs.sys.service.impl;

import com.fw.wcs.sys.mapper.DashBoardMapper;
import com.fw.wcs.sys.service.DashBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional(rollbackFor = Exception.class)
public class DashBoardServiceImpl implements DashBoardService {

    @Autowired
    private DashBoardMapper dashBoardMapper;

    @Override
    public List<Map<String, Object>> getAgvData() {
        return dashBoardMapper.selectAgvData();
    }

    @Override
    public List<Map<String, Object>> getReceiveStationDate() {
        return dashBoardMapper.selectReceiveStationData();
    }
}
