package com.fw.wcs.sys.service;

import java.util.List;
import java.util.Map;

public interface DashBoardService {

    List<Map<String, Object>> getAgvData();
    List<Map<String, Object>> getReceiveStationDate();
}
