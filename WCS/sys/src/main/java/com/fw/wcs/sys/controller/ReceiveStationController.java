package com.fw.wcs.sys.controller;

import com.fw.wcs.core.base.AjaxResult;
import com.fw.wcs.sys.service.ReceiveStationService;
import com.fw.wcs.sys.service.DashBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 *
 * @author Leon
 *
 *
 */
@Controller
@RequestMapping("/receiveStations")
public class ReceiveStationController {

    @Autowired
    public ReceiveStationService receiveStationService;

    @Autowired
    public DashBoardService dashBoardService;

    /**
     * 查询所有数据
     *
     * @return
     */
    @ResponseBody
    @GetMapping("/get")
    public AjaxResult getReceiveStation(String receiveStation) {
        return AjaxResult.success(receiveStationService.getReceiveStation(receiveStation));
    }

    @ResponseBody
    @GetMapping("/getData")
    public AjaxResult getReceiveStationData() {
    List<Map<String, Object>> data = dashBoardService.getReceiveStationDate();
    return AjaxResult.success(data); // 確保這裡傳入的數據是 AjaxResult.success() 能處理的
    }

}