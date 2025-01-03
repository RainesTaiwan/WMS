package com.fw.wcs.generator.converts;

import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.ITypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;

/**
 * @author Ervin Chen
 * @date 2019/12/31 10:35
 */
public class OracleTypeConvert implements ITypeConvert {

    public OracleTypeConvert(){

    }

    @Override
    public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
        String t = fieldType.toLowerCase();
        if (t.contains("char")) {
            return DbColumnType.STRING;
        } else {
            if (!t.contains("date") && !t.contains("timestamp")) {
                if (t.contains("number")) {
                    if (t.matches("number\\(+\\d\\)")) {
                        return DbColumnType.INTEGER;
                    }

                    if (t.matches("number\\(+\\d{2}+\\)")) {
                        return DbColumnType.LONG;
                    }

                    return DbColumnType.DOUBLE;
                }

                if (t.contains("float")) {
                    return DbColumnType.FLOAT;
                }

                if (t.contains("clob")) {
                    return DbColumnType.STRING;
                }

                if (t.contains("blob")) {
                    return DbColumnType.BLOB;
                }

                if (t.contains("binary")) {
                    return DbColumnType.BYTE_ARRAY;
                }

                if (t.contains("raw")) {
                    return DbColumnType.BYTE_ARRAY;
                }
            } else {
                switch(globalConfig.getDateType()) {
                    case ONLY_DATE:
                        return DbColumnType.DATE;
                    case SQL_PACK:
                        return DbColumnType.TIMESTAMP;
                    case TIME_PACK:
                        return DbColumnType.LOCAL_DATE_TIME;
                }
            }

            return DbColumnType.STRING;
        }
    }
}
