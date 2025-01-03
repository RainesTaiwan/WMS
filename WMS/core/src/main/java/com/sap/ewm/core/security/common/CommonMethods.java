package com.sap.ewm.core.security.common;

import cn.hutool.core.util.StrUtil;
import com.sap.ewm.core.security.interceptor.SecurityInterceptor;

public class CommonMethods {

    public static String getUser() {
        String user = SecurityInterceptor.threadLocal.get().getUser();
        if(StrUtil.isBlank(user) || "NULL".equals(user)) {
            user = "NULL";
        }
        return user;
    }

    public static boolean isLocal() {
        if("Y".equals(System.getProperty("local"))) {
            return true;
        }
        return false;
    }
}
