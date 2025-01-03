package com.fw.wcs.generator.converts;

import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.ITypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;

/**
 * @author Ervin Chen
 * @date 2019/12/31 10:33
 */
public class HanaTypeConvert implements ITypeConvert {
    public HanaTypeConvert() {
    }

    @Override
    public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
        String t = fieldType.toLowerCase();
        if (t.contains("char")) {
            return DbColumnType.STRING;
        } else {
            if (!t.contains("date") && !t.contains("timestamp")) {
                if (t.contains("bigint")) {
                    return DbColumnType.LONG;
                }
                if (t.contains("int")) {
                    return DbColumnType.INTEGER;
                }
                if (t.contains("float")) {
                    return DbColumnType.FLOAT;
                }
                if (t.contains("double")) {
                    return DbColumnType.DOUBLE;
                }
                if( t.matches("decimal\\(+\\d+,0\\)") ){
                    return DbColumnType.LONG;
                }
                if( t.contains("decimal") ){
                    return DbColumnType.DOUBLE;
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

            } else {
                switch (globalConfig.getDateType()) {
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