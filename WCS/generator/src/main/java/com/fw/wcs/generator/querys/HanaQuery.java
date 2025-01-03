package com.fw.wcs.generator.querys;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.config.querys.AbstractDbQuery;

/**
 * @author Ervin Chen
 * @date 2019/12/30 16:46
 */
public class HanaQuery extends AbstractDbQuery {

    @Override
    public DbType dbType() {
        return DbType.ORACLE;
    }

    @Override
    public String tablesSql() {
        return "select TABLE_NAME, '' AS COMMENTS from tables where schema_name = '%s' ";
    }

    @Override
    public String tableFieldsSql() {
        return "select COLUMN_NAME, DATA_TYPE_NAME, CASE WHEN DATA_TYPE_NAME='DECIMAL' THEN DATA_TYPE_NAME||'('||LENGTH||','||SCALE||')' ELSE DATA_TYPE_NAME END AS DATA_TYPE, COMMENTS, max(map(POSITION, '1', 'PRI')) AS KEY from TABLE_COLUMNS where TABLE_NAME = '%s' GROUP BY COLUMN_NAME, DATA_TYPE_NAME, LENGTH, SCALE, COMMENTS, POSITION ORDER BY POSITION ";
    }

    @Override
    public String tableName() {
        return "TABLE_NAME";
    }

    @Override
    public String tableComment() {
        return "COMMENTS";
    }

    @Override
    public String fieldName() {
        return "COLUMN_NAME";
    }

    @Override
    public String fieldType() {
        return "DATA_TYPE";
    }

    @Override
    public String fieldComment() {
        return "COMMENTS";
    }

    @Override
    public String fieldKey() {
        return "KEY";
    }
}
