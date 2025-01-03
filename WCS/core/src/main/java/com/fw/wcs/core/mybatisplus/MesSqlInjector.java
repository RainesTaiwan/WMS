package com.fw.wcs.core.mybatisplus;

import com.baomidou.mybatisplus.mapper.AutoSqlInjector;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.mapping.*;

/**
 * @author Ervin Chen
 * @date 2020/1/7 17:00
 */
public class MesSqlInjector extends AutoSqlInjector {

    @Override
    public MappedStatement addMappedStatement(Class<?> mapperClass, String id, SqlSource sqlSource, SqlCommandType sqlCommandType, Class<?> parameterClass, String resultMap, Class<?> resultType, KeyGenerator keyGenerator, String keyProperty, String keyColumn) {
        String statementName = mapperClass.getName() + "." + id;
        /*if (this.hasMappedStatement(statementName)) {
            MappedStatement mappedStatement = this.configuration.getMappedStatement( statementName );
            System.err.println("{" + statementName + "} Has been loaded by XML or SqlProvider, ignoring the injection of the SQL.");
            return null;
        } else {
            boolean isSelect = false;
            if (sqlCommandType == SqlCommandType.SELECT) {
                isSelect = true;
            }

            return this.builderAssistant.addMappedStatement(id, sqlSource, StatementType.PREPARED, sqlCommandType, (Integer)null, (Integer)null, (String)null, parameterClass, resultMap, resultType, (ResultSetType)null, !isSelect, isSelect, false, keyGenerator, keyProperty, keyColumn, this.configuration.getDatabaseId(), this.languageDriver, (String)null);
        }*/
        boolean isSelect = false;
        if (sqlCommandType == SqlCommandType.SELECT) {
            isSelect = true;
        }
        return this.builderAssistant.addMappedStatement(id, sqlSource, StatementType.PREPARED, sqlCommandType, (Integer)null, (Integer)null, (String)null, parameterClass, resultMap, resultType, (ResultSetType)null, !isSelect, isSelect, false, keyGenerator, keyProperty, keyColumn, this.configuration.getDatabaseId(), this.languageDriver, (String)null);
        //return this.addMappedStatement(id, sqlSource, StatementType.PREPARED, sqlCommandType, (Integer)null, (Integer)null, (String)null, parameterClass, resultMap, resultType, (ResultSetType)null, !isSelect, isSelect, false, keyGenerator, keyProperty, keyColumn, this.configuration.getDatabaseId(), this.languageDriver, (String)null);
    }
/*
    public MappedStatement addMappedStatement(String id, SqlSource sqlSource, StatementType statementType, SqlCommandType sqlCommandType, Integer fetchSize, Integer timeout, String parameterMap, Class<?> parameterType, String resultMap, Class<?> resultType, ResultSetType resultSetType, boolean flushCache, boolean useCache, boolean resultOrdered, KeyGenerator keyGenerator, String keyProperty, String keyColumn, String databaseId, LanguageDriver lang, String resultSets) {
        id = this.builderAssistant.applyCurrentNamespace(id, false);
        boolean isSelect = sqlCommandType == SqlCommandType.SELECT;
        org.apache.ibatis.mapping.MappedStatement.Builder statementBuilder = (new org.apache.ibatis.mapping.MappedStatement.Builder(this.configuration, id, sqlSource, sqlCommandType)).resource(this.resource).fetchSize(fetchSize).timeout(timeout).statementType(statementType).keyGenerator(keyGenerator).keyProperty(keyProperty).keyColumn(keyColumn).databaseId(databaseId).lang(lang).resultOrdered(resultOrdered).resultSets(resultSets).resultMaps(this.getStatementResultMaps(resultMap, resultType, id)).resultSetType(resultSetType).flushCacheRequired((Boolean)this.valueOrDefault(flushCache, !isSelect)).useCache((Boolean)this.valueOrDefault(useCache, isSelect)).cache(this.currentCache);
        ParameterMap statementParameterMap = this.getStatementParameterMap(parameterMap, parameterType, id);
        if (statementParameterMap != null) {
            statementBuilder.parameterMap(statementParameterMap);
        }

        MappedStatement statement = statementBuilder.build();
        this.configuration.addMappedStatement(statement);
        return statement;
    }

    private ParameterMap getStatementParameterMap(String parameterMapName, Class<?> parameterTypeClass, String statementId) {
        parameterMapName = this.builderAssistant.applyCurrentNamespace(parameterMapName, true);
        ParameterMap parameterMap = null;
        if (parameterMapName != null) {
            try {
                parameterMap = this.configuration.getParameterMap(parameterMapName);
            } catch (IllegalArgumentException var6) {
                throw new IncompleteElementException("Could not find parameter map " + parameterMapName, var6);
            }
        } else if (parameterTypeClass != null) {
            List<ParameterMapping> parameterMappings = new ArrayList();
            parameterMap = (new ParameterMap.Builder(this.configuration, statementId + "-Inline", parameterTypeClass, parameterMappings)).build();
        }

        return parameterMap;
    }*/

    private boolean hasMappedStatement(String mappedStatement) {
        return this.configuration.hasStatement(mappedStatement, false);
    }
}
