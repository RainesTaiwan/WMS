package com.fw.wcs.sys.controller;

import com.fw.wcs.sys.service.AgvTaskService;
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
@RequestMapping("/agvTasks")
public class AgvTaskController {

    @Autowired
    public AgvTaskService agvTaskService;


}