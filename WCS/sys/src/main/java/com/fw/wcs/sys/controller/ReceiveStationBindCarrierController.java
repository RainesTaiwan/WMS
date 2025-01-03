package com.fw.wcs.sys.controller;

import com.fw.wcs.sys.service.ReceiveStationBindService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Leon
 *
 *
 */
@Controller
@RequestMapping("/receiveStationBindCarriers")
public class ReceiveStationBindCarrierController {

    @Autowired
    public ReceiveStationBindService receiveStationBindService;


}