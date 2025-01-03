package com.sap.ewm.core.mybatisplus.plugins.pagination.dialects;


import com.baomidou.mybatisplus.extension.plugins.pagination.DialectModel;
import com.baomidou.mybatisplus.extension.plugins.pagination.dialects.IDialect;

/**
 * @author Ervin Chen
 * @date 2020/1/6 16:13
 */
public class HanaDialect implements IDialect {

    @Override
    public DialectModel buildPaginationSql(String originalSql, long offset, long limit) {
        String sql = originalSql + " limit " + "?" + " offset " + "?";
        return (new DialectModel(sql, limit, offset)).setConsumerChain();
    }
}
