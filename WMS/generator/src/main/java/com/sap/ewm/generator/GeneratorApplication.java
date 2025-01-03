package com.sap.ewm.generator;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.sap.ewm.generator.converts.MySqlTypeConvert;

/**
 * @author Ervin Chen
 * @date 2019/12/30 13:49
 */
public class GeneratorApplication {
    public static void main(String[] args) {
        generateCode();
    }

    public static void generateCode(){
        MpGenerator.MpGeneratorBuilder mpGeneratorBuilder = MpGenerator.builder();
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDbType( DbType.MYSQL );

        /*dataSourceConfig.setDriverName( "com.sap.db.jdbc.Driver" );
        dataSourceConfig.setUrl( "jdbc:sap:192.168.18.230:39015" );
        dataSourceConfig.setUsername( "WIP" );
        dataSourceConfig.setPassword( "Sap12345" );
        dataSourceConfig.setDbQuery( new HanaQuery() );
        dataSourceConfig.setTypeConvert( new HanaTypeConvert() );*/
        dataSourceConfig.setDriverName( "com.mysql.cj.jdbc.Driver" );
        dataSourceConfig.setUrl( "jdbc:mysql://39.99.218.78:3306/wms?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true" );
        dataSourceConfig.setUsername( "root" );
        dataSourceConfig.setPassword( "root" );
        dataSourceConfig.setTypeConvert( new MySqlTypeConvert() );
        MpGenerator mpGenerator = mpGeneratorBuilder.dataSourceConfig(dataSourceConfig)
                            .tablePrefix( "ZD_", "DS_", "Z_" )
                            .packageName( "com.sap.ewm.dashboard" )
                            .tables("ds_carrier_status")
                            .author("syngna")
                            .uiAppId("com.sap.ewm")
                            .uiPackage("com.sap.ewm.biz.master")
                            .build();
        mpGenerator.execute();
    }
}
