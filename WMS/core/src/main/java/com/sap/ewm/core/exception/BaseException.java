package com.sap.ewm.core.exception;


import com.sap.ewm.core.utils.I18nUtil;
import org.apache.commons.lang3.StringUtils;

public class BaseException extends RuntimeException {

    private String code;

    private Object[] params;

    private String message;

    public BaseException( String code, Object[] params ){
        this.code = code;
        this.params = params;
    }

    public BaseException( String message ){
        this.message = message;
    }

    @Override
    public String getMessage(){
        String temp = "";
        if( StringUtils.isNotBlank( code ) ){
            String key = code;
            temp = I18nUtil.getI18nText( key, params );
        }
        if( StringUtils.isNotBlank( temp ) ){
            return temp;
        }else{
            return message;
        }
    }


}
