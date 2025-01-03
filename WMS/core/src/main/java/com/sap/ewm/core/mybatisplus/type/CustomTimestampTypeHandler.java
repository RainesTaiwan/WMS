package com.sap.ewm.core.mybatisplus.type;

import oracle.sql.TIMESTAMP;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.Date;
import java.util.TimeZone;

/**
 * Mybatis Timestamp時間型別 查詢時轉換本地時間
 * 儲存時將時間轉換為0時區時間
 */
@MappedTypes({Object.class})
@MappedJdbcTypes(value = {JdbcType.TIMESTAMP})
public class CustomTimestampTypeHandler extends BaseTypeHandler<Object> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        if( parameter instanceof TIMESTAMP ) {
            ps.setTimestamp( i, new Timestamp( oracleTimestampToSqlTimestamp((TIMESTAMP) parameter).getTime() -
                    TimeZone.getDefault().getRawOffset()) );
        }else if( parameter instanceof Date){
            ps.setTimestamp( i, new Timestamp( ((Date) parameter).getTime() - TimeZone.getDefault().getRawOffset()) );
        }else{
            ps.setObject( i, parameter );
        }
    }

    @Override
    public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Object obj = rs.getObject( columnName );
        if( obj!=null ){
            if( obj instanceof TIMESTAMP ) {
                return new Timestamp( oracleTimestampToSqlTimestamp( (TIMESTAMP)obj ).getTime() + TimeZone.getDefault().getRawOffset() );
            }else if( obj instanceof Date){
                return new Timestamp( ( (Date)obj).getTime() + TimeZone.getDefault().getRawOffset() );
            }else{
                return obj;
            }
        }
        return null;
    }

    @Override
    public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Object obj = rs.getObject( columnIndex );
        if( obj!=null ){
            if( obj instanceof TIMESTAMP ) {
                return new Timestamp( oracleTimestampToSqlTimestamp( (TIMESTAMP)obj ).getTime() + TimeZone.getDefault().getRawOffset() );
            }else if( obj instanceof Date){
                return new Timestamp( ( (Timestamp)obj).getTime() + TimeZone.getDefault().getRawOffset() );
            }else{
                return obj;
            }
        }
        return null;
    }

    @Override
    public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Object obj = cs.getObject( columnIndex );
        if( obj!=null ){
            if( obj instanceof TIMESTAMP ) {
                return new Timestamp( oracleTimestampToSqlTimestamp( (TIMESTAMP)obj ).getTime() + TimeZone.getDefault().getRawOffset() );
            }else if( obj instanceof Date){
                return new Timestamp( ( (Date)obj).getTime() + TimeZone.getDefault().getRawOffset() );
            }else{
                return obj;
            }
        }
        return null;
    }

    public Timestamp oracleTimestampToSqlTimestamp( TIMESTAMP timestamp ) throws SQLException{
        Class clz = timestamp.getClass();
        try {
            Method m = clz.getMethod("timestampValue" );
            return new Timestamp( ( (Timestamp) m.invoke( timestamp ) ).getTime() );
        } catch (NoSuchMethodException e) {
            throw new SQLException( e );
        } catch (IllegalAccessException e) {
            throw new SQLException( e );
        } catch (InvocationTargetException e) {
            throw new SQLException( e );
        }
    }
}
