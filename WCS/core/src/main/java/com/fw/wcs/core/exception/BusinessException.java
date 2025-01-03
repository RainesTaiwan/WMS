package com.fw.wcs.core.exception;

public class BusinessException extends BaseException {

    private BusinessException( String code, Object[] params ){
        super( code, params );
    }

    public BusinessException( String message ){
        super( message );
    }


    public static BusinessException build(String code, Object... params ){
        return new BusinessException( code, params );
    }

    public static BusinessException build(String message ){
        return new BusinessException( message );
    }
}
