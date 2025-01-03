package com.fw.wcs.sys.mapper;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface DashBoardMapper {

    List<Map<String, Object>> selectAgvData();
    List<Map<String, Object>> selectReceiveStationData();
}
