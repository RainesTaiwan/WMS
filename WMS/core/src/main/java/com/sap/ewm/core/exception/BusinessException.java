package com.sap.ewm.core.exception;

public class BusinessException extends BaseException {

    private BusinessException( String code, Object[] params ){
        super( code, params );
    }

    private BusinessException( String message ){
        super( message );
    }


    public static BusinessException build(String code, Object... params ){
        return new BusinessException( code, params );
    }

    public static BusinessException build(String message ){
        return new BusinessException( message );
    }
}
