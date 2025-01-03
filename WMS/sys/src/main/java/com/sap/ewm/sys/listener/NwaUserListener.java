package com.sap.ewm.sys.listener;


import com.sap.ewm.sys.model.SysUser;
import com.sap.ewm.sys.service.SysUserService;
import com.sap.security.api.UMException;
import com.sap.security.api.UserListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: mesext
 * @description: netweaver使用者監聽
 * @author: syngna
 * @create: 2020-06-15 20:58
 */
@Service
public class NwaUserListener implements UserListener {

    private final Logger logger = LoggerFactory.getLogger(NwaUserListener.class);

    @Autowired
    private SysUserService sysUserService;

    @Override
    public void userAdded(String s) throws UMException {

    }

    @Override
    public void userRemoved(String s) throws UMException {
        logger.info("user remove start: " + s);
        String userName = s.substring(s.indexOf(":") + 1);
        SysUser sysUser = sysUserService.selectUserByName(userName);
        if(sysUser == null) {
            return;
        }
        sysUserService.deleteUserById(sysUser);
        logger.info("user remove complete: " + s);
    }
}