package com.fw.wcs.sys.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.fw.wcs.sys.service.AgvService;

/**
 *
 * @author Leon
 *
 *
 */
@Controller
@RequestMapping("/agvs")
public class AgvController {

    @Autowired
    public AgvService agvService;


}