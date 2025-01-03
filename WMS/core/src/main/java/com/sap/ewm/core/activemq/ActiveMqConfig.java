package com.sap.ewm.core.activemq;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @program: wms
 * @description:
 * @author: syngna
 * @create: 2020-08-08 11:54
 */
@Component
@ConditionalOnProperty(prefix = "spring.activemq" , name = {"brokerUrl" , "user" , "password", "brokerName"}, matchIfMissing = false)
public class ActiveMqConfig {

    @Value("${spring.activemq.brokerUrl}")
    private String brokerUrl;

    @Value("${spring.activemq.user}")
    private String user;

    @Value("${spring.activemq.password}")
    private String password;

    @Value("${spring.activemq.brokerName}")
    private String brokerName;

    private static String amqBrokerUrl;

    private static String amqBrokerName;

    @PostConstruct
    public void postConstruct() {
        amqBrokerUrl = this.brokerUrl;
        amqBrokerName = this.brokerName;
    }

    public static String getAmqBrokerUrl() {
        return amqBrokerUrl;
    }

    public static String getAmqBrokerName() {
        return amqBrokerName;
    }
}