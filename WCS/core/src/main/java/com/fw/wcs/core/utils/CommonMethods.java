package com.fw.wcs.core.utils;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

public class CommonMethods {

    private static final String SITE = "SITE";
    private static final String USER_ID = "USER_ID";
    private static final String USER_BO_FORMAT = "UserBO:{0},{1}";
    private static final ThreadLocal<Map<String,Object>> threadLocalMap = new InheritableThreadLocal<Map<String, Object>>();

    public static String getSite(){
        String site = ServletUtils.getParameter( SITE );
        return site;
    }

    public static String getUser(){
        String userId = ServletUtils.getParameter( USER_ID );
        if( StringUtils.isBlank( userId ) ){
            userId = "GUEST";
        }
        setValue( USER_ID, userId );
        return userId;
    }

    public static String getUserBo(){
        return MessageFormat.format( USER_BO_FORMAT, getSite(), getUser() );
    }

    private static Object getValue( String key ){
        if( threadLocalMap.get() == null ){
            threadLocalMap.set( new HashMap<String, Object>());
        }
        return threadLocalMap.get().get( key );
    }

    private static void setValue( String key, Object value ){
        if( threadLocalMap.get() == null ){
            threadLocalMap.set( new HashMap<String, Object>());
        }
        threadLocalMap.get().put( key, value );
    }
}
