package com.sap.ewm.core.security;

import java.util.List;

/**
 * @program: ewm
 * @description: 許可權監聽
 * @author: syngna
 * @create: 2020-06-28 15:04
 */
public interface PermissionListener {
    boolean hasPermission(String userName, List<String> permissionList);
}