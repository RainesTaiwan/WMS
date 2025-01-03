package com.sap.ewm.generator;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.sap.ewm.generator.mybatis.UiTemplateConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ervin Chen
 * @date 2019/12/30 14:04
 */
public class MpGenerator {
    private AutoGenerator autoGenerator;

    private MpGenerator( AutoGenerator autoGenerator ){
        this.autoGenerator = autoGenerator;
    }

    public static MpGeneratorBuilder builder(){
        return new MpGeneratorBuilder();
    }

    public void execute(){
        this.autoGenerator.execute();
    }

    static class MpGeneratorBuilder{
        private GlobalConfig globalConfig = null;
        private DataSourceConfig dataSourceConfig = null;
        private StrategyConfig strategyConfig = null;
        private PackageConfig packageConfig = null;
        private InjectionConfig injectionConfig = null;
        private TemplateConfig templateConfig = null;
        private UiTemplateConfig uiTemplateConfig = null;
        private String basePath = null;
        private String[] tablePrefix = null;
        private String packageName = null;
        private String versionFieldName = null;
        private String author = null;
        private String[] tables = null;
        private String uiAppId = null;
        private String uiPackage = null;

        public MpGeneratorBuilder globalConfig( GlobalConfig globalConfig ){
            this.globalConfig = globalConfig;
            return this;
        }

        public MpGeneratorBuilder dataSourceConfig( DataSourceConfig dataSourceConfig ){
            this.dataSourceConfig = dataSourceConfig;
            return this;
        }

        public MpGeneratorBuilder strategyConfig( StrategyConfig strategyConfig ){
            this.strategyConfig = strategyConfig;
            return this;
        }

        public MpGeneratorBuilder packageConfig( PackageConfig packageConfig ){
            this.strategyConfig = strategyConfig;
            return this;
        }

        public MpGeneratorBuilder injectionConfig( InjectionConfig injectionConfig ){
            this.injectionConfig = injectionConfig;
            return this;
        }

        public MpGeneratorBuilder templateConfig( TemplateConfig templateConfig ){
            this.templateConfig = templateConfig;
            return this;
        }

        public MpGeneratorBuilder basePath( String basePath ){
            this.basePath = basePath;
            return this;
        }

        public MpGeneratorBuilder tablePrefix( String ...tablePrefix ){
            this.tablePrefix = tablePrefix;
            return this;
        }

        public MpGeneratorBuilder packageName( String packageName ){
            this.packageName = packageName;
            return this;
        }

        public MpGeneratorBuilder versionFieldName( String versionFieldName ){
            this.versionFieldName = versionFieldName;
            return this;
        }

        public MpGeneratorBuilder author( String author ){
            this.author = author;
            return this;
        }

        public MpGeneratorBuilder tables( String ...tables ){
            this.tables = tables;
            return this;
        }

        public MpGeneratorBuilder uiAppId( String uiAppId ){
            this.uiAppId = uiAppId;
            return this;
        }

        public MpGeneratorBuilder uiPackage( String uiPackage ){
            this.uiPackage = uiPackage;
            return this;
        }

        public MpGenerator build(){
            AutoGenerator mpg = new AutoGenerator();

            if( globalConfig == null ){
                globalConfig = new GlobalConfig();
                globalConfig.setFileOverride(true);
                globalConfig.setActiveRecord(true);// 不需要ActiveRecord特性的請改為false
                globalConfig.setEnableCache(false);// XML 二級快取
                globalConfig.setBaseResultMap(true);// XML ResultMap
                globalConfig.setBaseColumnList(true);// XML columList
                globalConfig.setDateType(DateType.TIME_PACK);
                globalConfig.setServiceName("%sService");
            }
            if( this.basePath != null ){
                globalConfig.setOutputDir( basePath );
            }else{
                globalConfig.setOutputDir( "C://#tmp" );
            }
            if( this.author == null ){
                throw new IllegalArgumentException("人員[author]不能為空");
            }else{
                globalConfig.setAuthor( author );
            }
            mpg.setGlobalConfig( globalConfig );

            if( this.dataSourceConfig == null ){
                throw new IllegalArgumentException("數據源[dataSourceConfig]不能為空");
            }
            mpg.setDataSource( dataSourceConfig );

            if( this.strategyConfig == null ){
                this.strategyConfig = new StrategyConfig();
                strategyConfig.setNaming(NamingStrategy.underline_to_camel);// 表名產生策略
                strategyConfig.setEntityColumnConstant(true);
            }
            if( tablePrefix != null ){
                strategyConfig.setTablePrefix( tablePrefix );
            }
            if( tables == null ){
                throw new IllegalArgumentException("表名[packageName]不能為空");
            }else{
                strategyConfig.setInclude( tables );
            }
            if( versionFieldName != null ){
                strategyConfig.setVersionFieldName( versionFieldName );
            }
            mpg.setStrategy( strategyConfig );

            if( this.packageConfig == null ){
                this.packageConfig = new PackageConfig();
                packageConfig.setController("controller");
                packageConfig.setEntity("model");
            }
            if( packageName == null ){
                throw new IllegalArgumentException("包名[packageName]不能為空");
            }else{
                packageConfig.setParent( packageName );
            }
            mpg.setPackageInfo( packageConfig );

            //產生ui
            if(uiTemplateConfig == null){
                uiTemplateConfig = new UiTemplateConfig();
                uiTemplateConfig.setView("/mybatis-templates/ui.view.xml.vm");
                uiTemplateConfig.setController("/mybatis-templates/ui.controller.js.vm");

                String uiPath = !StringUtils.endsWith(globalConfig.getOutputDir(), File.separator) ? globalConfig.getOutputDir() + File.separator : globalConfig.getOutputDir();
                String uiRelativePath = uiPackage.replaceAll(uiAppId + "\\.","ui.").replaceAll("\\.", "\\" + File.separator);

                List<FileOutConfig> focList = new ArrayList<>();
                focList.add(new FileOutConfig(uiTemplateConfig.getView()) {
                    @Override
                    public String outputFile(TableInfo tableInfo) {
                        return uiPath + uiRelativePath + File.separator + tableInfo.getEntityName() + "Maintain.view.xml";
                    }
                });
                focList.add(new FileOutConfig(uiTemplateConfig.getController()) {
                    @Override
                    public String outputFile(TableInfo tableInfo) {
                        return uiPath + uiRelativePath + File.separator + tableInfo.getEntityName() + "Maintain.controller.js";
                    }
                });
                if(injectionConfig == null){
                    injectionConfig = new InjectionConfig() {
                        @Override
                        public void initMap() {
                            Map<String, Object> map = new HashMap<>();
                            map.put("package", uiPackage);
                            this.setMap(map);
                        }
                    };
                }
                injectionConfig.setFileOutConfigList(focList);
                mpg.setCfg(injectionConfig);
            }

            if( this.templateConfig == null ){
                templateConfig = new TemplateConfig();
                templateConfig.setController("/mybatis-templates/controller.java.vm");
                templateConfig.setService("/mybatis-templates/service.java.vm");
                templateConfig.setServiceImpl("/mybatis-templates/serviceImpl.java.vm");
                templateConfig.setEntity("/mybatis-templates/entity.java.vm");
                templateConfig.setMapper("/mybatis-templates/mapper.java.vm");
                templateConfig.setXml("/mybatis-templates/mapper.xml.vm");
            }
            mpg.setTemplate( templateConfig );

            return new MpGenerator( mpg );
        }
    }
}
