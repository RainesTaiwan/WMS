package com.sap.ewm.sys.listener;

import com.sap.ewm.core.security.PermissionListener;
import com.sap.ewm.core.security.PermissionSubject;
import com.sap.ewm.core.utils.CacheUtil;
import com.sap.ewm.sys.constants.CommonConstants;
import com.sap.ewm.sys.dto.UserInfo;
import com.sap.ewm.sys.model.SysUser;
import com.sap.ewm.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: ewm
 * @description:
 * @author: syngna
 * @create: 2020-06-28 15:47
 */
@Service
public class SecurityPermissionListener implements PermissionListener {

    @Autowired
    SysUserService sysUserService;

    public SecurityPermissionListener() {
        PermissionSubject.getInstance().registerListener(this);
    }

    @Override
    public boolean hasPermission(String userName, List<String> permissionList) {
        String[] userPermissions = null;
        if(CacheUtil.isExist(CommonConstants.USER_PERMISSION_CACHE_KEY + userName)) {
            userPermissions = (String[])CacheUtil.get(CommonConstants.USER_PERMISSION_CACHE_KEY + userName);
        } else {
            SysUser sysUser = sysUserService.selectUserByName(userName);
            UserInfo userInfo = sysUserService.findUserInfo(sysUser);
            if(userInfo != null) {
                userPermissions = userInfo.getPermissions();
                CacheUtil.set(CommonConstants.USER_PERMISSION_CACHE_KEY + userName, userPermissions);
            }
        }

        if(userPermissions == null) {
            return false;
        }
        for(String needPermission : permissionList) {
            boolean hasPermission = false;
            for(String userPermission : userPermissions) {
                if(userPermission.equalsIgnoreCase(needPermission)) {
                    hasPermission = true;
                    break;
                }
            }
            if(!hasPermission) {
                return false;
            }
        }
        return true;
    }
}