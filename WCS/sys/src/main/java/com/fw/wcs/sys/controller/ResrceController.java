package com.fw.wcs.sys.controller;

import com.fw.wcs.sys.service.ResrceService;
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
@RequestMapping("/resrces")
public class ResrceController {

    @Autowired
    public ResrceService resrceService;


}