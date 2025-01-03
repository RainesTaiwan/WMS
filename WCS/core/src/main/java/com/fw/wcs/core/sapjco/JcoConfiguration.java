package com.fw.wcs.core.sapjco;

import com.sap.conn.jco.ext.DestinationDataProvider;
import com.sap.conn.jco.ext.Environment;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Properties;

@Configuration
@ConditionalOnProperty( prefix = "jco", name = { "ashost", "sysnr", "client", "user", "passwd" }, matchIfMissing = false )
public class JcoConfiguration {

    @Bean
    @ConfigurationProperties("jco")
    public Properties jcoProperties(){
        Properties properties = new Properties();
        return properties;
    }

    @Bean
    public JcoClient jcoClient(){
        JcoClient jcoClient = new JcoClient( jcoProperties().getProperty( "destination" ) );
        return jcoClient;
    }

    @PostConstruct
    public void registerDestinationDataProvider(){
        if( !Environment.isDestinationDataProviderRegistered() ){
            CustomDestinationDataProvider provider = new CustomDestinationDataProvider();
            Properties properties = new Properties();
            properties.setProperty( DestinationDataProvider.JCO_ASHOST, jcoProperties().get( "ashost" ).toString() );
            properties.setProperty( DestinationDataProvider.JCO_SYSNR, jcoProperties().get("sysnr").toString() );
            properties.setProperty( DestinationDataProvider.JCO_CLIENT, jcoProperties().get( "client" ).toString() );
            properties.setProperty( DestinationDataProvider.JCO_USER, jcoProperties().get( "user" ).toString() );
            properties.setProperty( DestinationDataProvider.JCO_PASSWD, jcoProperties().get( "passwd" ).toString() );
            properties.setProperty( DestinationDataProvider.JCO_LANG, jcoProperties().get( "lang" ).toString() );
            properties.setProperty( DestinationDataProvider.JCO_PEAK_LIMIT, jcoProperties().get( "peak_limit" ).toString() );
            properties.setProperty( DestinationDataProvider.JCO_POOL_CAPACITY, jcoProperties().get( "pool_capacity" ).toString() );
            properties.setProperty( DestinationDataProvider.JCO_MAX_GET_TIME, jcoProperties().get( "max_get_client_time" ).toString() );
            provider.addDestinationProperties( jcoProperties().getProperty( "destination" ), properties );
            Environment.registerDestinationDataProvider( provider );
        }
    }
}
