package com.sap.ewm.core.mybatisplus.type;

/**
 * Created by Administrator on 2018/10/12.
 */
import org.apache.ibatis.type.DateTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author Clinton Begin
 */
public class CustomDateTypeHandler extends DateTypeHandler {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Date parameter, JdbcType jdbcType) throws SQLException {
        //ps.setTimestamp(i, new Timestamp((parameter).getTime()));
        ps.setTimestamp(i, new Timestamp(parameter.getTime() - TimeZone.getDefault().getRawOffset()));
    }

    @Override
    public Date getNullableResult(ResultSet rs, String columnName) throws SQLException {
        Timestamp sqlTimestamp = rs.getTimestamp(columnName);
        if (sqlTimestamp != null) {
            //return new Date(sqlTimestamp.getTime());
            return new Date(sqlTimestamp.getTime() + TimeZone.getDefault().getRawOffset());
        }
        return null;
    }

    @Override
    public Date getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        Timestamp sqlTimestamp = rs.getTimestamp(columnIndex);
        if (sqlTimestamp != null) {
            //return new Date(sqlTimestamp.getTime());
            return new Date(sqlTimestamp.getTime() + TimeZone.getDefault().getRawOffset());
        }
        return null;
    }

    @Override
    public Date getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        Timestamp sqlTimestamp = cs.getTimestamp(columnIndex);
        if (sqlTimestamp != null) {
            //return new Date(sqlTimestamp.getTime());
            return new Date(sqlTimestamp.getTime() + TimeZone.getDefault().getRawOffset());
        }
        return null;
    }
}
