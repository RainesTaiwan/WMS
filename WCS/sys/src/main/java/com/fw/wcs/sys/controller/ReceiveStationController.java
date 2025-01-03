package com.fw.wcs.sys.controller;

import com.fw.wcs.core.base.AjaxResult;
import com.fw.wcs.sys.service.ReceiveStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
}