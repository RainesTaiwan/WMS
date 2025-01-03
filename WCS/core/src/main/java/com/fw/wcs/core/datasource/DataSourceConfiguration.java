package com.fw.wcs.core.datasource;

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DynamicDataSourceProperties;
import com.fw.wcs.core.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.Map;

/**
 * @author Ervin Chen
 * @date 2020/1/2 10:32
 */

@Configuration
public class DataSourceConfiguration {

    @Autowired
    DynamicDataSourceProperties dynamicDataSourceProperties;

    @PostConstruct
    public void init(){
        DynamicRoutingDataSource dynamicRoutingDataSource = SpringUtil.getBean("dataSource");
        Map<String, DataSourceProperty> dataSourcePropertiesMap = dynamicDataSourceProperties.getDatasource();
        for (Map.Entry<String, DataSourceProperty> item : dataSourcePropertiesMap.entrySet()) {
            DataSourceProperty dataSourceProperty = item.getValue();
            String pollName = dataSourceProperty.getPollName();
            if (pollName == null || "".equals(pollName)) {
                pollName = item.getKey();
            }
            dataSourceProperty.setPollName(pollName);
            String jndiName = dataSourceProperty.getJndiName();
            if (jndiName != null && !jndiName.isEmpty()) {
                try {
                    DataSource dataSource = (DataSource) new InitialContext().lookup(jndiName);
                    dynamicRoutingDataSource.addDataSource( pollName, dataSource );
                } catch (NamingException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
