package com.fw.wcs.sys.controller;

import com.fw.wcs.sys.service.StorageBinTypeService;
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
@RequestMapping("/storageBinTypes")
public class StorageBinTypeController {

    @Autowired
    public StorageBinTypeService storageBinTypeService;


}