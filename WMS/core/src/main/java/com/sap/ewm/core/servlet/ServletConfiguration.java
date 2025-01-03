package com.sap.ewm.core.servlet;

import org.springframework.boot.context.embedded.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServletConfiguration {

    @Bean
    public ServletRegistrationBean fileServiceServlet(){
        return new ServletRegistrationBean(new FileServlet(),"/fileservice/*");
    }
}
