package com.fw.wcs.sys.configs;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsMessagingTemplate;


@Configuration
public class ActiveMqConfig {

    @Primary
    @Bean(name = "wmsConnectionFactory")
    public ActiveMQConnectionFactory wmsConnectionFactory(
            @Value("${spring.activemq.wms.brokerUrl}") String brokerUrl,
            @Value("${spring.activemq.wms.username}") String username,
            @Value("${spring.activemq.wms.password}") String password)
    {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setBrokerURL(brokerUrl);
        factory.setUserName(username);
        factory.setPassword(password);
        return factory;
    }

    @Bean(name = "wmsActivemqTemplate")
    public JmsMessagingTemplate wmsActivemqTemplate(
            @Qualifier("wmsConnectionFactory") ActiveMQConnectionFactory connectionFactory) {
        JmsMessagingTemplate template = new JmsMessagingTemplate(connectionFactory);
        template.getJmsTemplate().setReceiveTimeout(10000);
        return template;
    }

    @Bean(name = "wmsFactory")
    public JmsListenerContainerFactory wmsFactory(
            @Qualifier("wmsConnectionFactory") ActiveMQConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }
}
