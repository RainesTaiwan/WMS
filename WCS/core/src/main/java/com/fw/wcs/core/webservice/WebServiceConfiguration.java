package com.fw.wcs.core.webservice;

import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebServiceConfiguration {

    @Bean
    public ServletRegistrationBean webServiceServlet(){
        return new ServletRegistrationBean(new CXFServlet(),"/webservice/*");//發佈服務名稱
    }

    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus()
    {
        return  new SpringBus();
    }

    /*@Bean
    public Endpoint endpoint() {
        JaxWsServerFactoryBean ss;
        JaxWsProxyFactoryBean aa;
        EndpointImpl endpoint = new EndpointImpl( springBus(), pickFeedService() );
        endpoint.publish("/pickItem"); //顯示要發佈的名稱
        return endpoint;
    }*/
}
