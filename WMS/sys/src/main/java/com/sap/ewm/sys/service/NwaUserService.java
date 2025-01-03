package com.sap.ewm.sys.service;

import com.sap.security.api.UMException;

/**
 * @program: ewm
 * @description: NetWeaver使用者服務
 * @author: syngna
 * @create: 2020-06-22 16:18
 */
public interface NwaUserService {
    void getNwaAllUsers() throws UMException;
}