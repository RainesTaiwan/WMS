package com.sap.ewm.core.utils;

import cn.hutool.core.util.StrUtil;
import com.sap.ewm.core.activemq.ActiveMqConfig;
import org.apache.activemq.broker.jmx.BrokerViewMBean;
import org.apache.activemq.broker.jmx.QueueViewMBean;
import org.apache.activemq.broker.jmx.TopicViewMBean;

import javax.management.MBeanServerConnection;
import javax.management.MBeanServerInvocationHandler;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.IOException;
import java.util.function.Function;

/**
 * @author Ervin Chen
 * @date 2020/4/13 15:47
 */
public class ActiveMQUtil {
    private static final String jmxServiceUrl = "service:jmx:rmi:///jndi/rmi://%s:11099/jmxrmi";
    private static final String broker = "org.apache.activemq:brokerName=%s,type=Broker";
    private static MBeanServerConnection connection = null;
    private static BrokerViewMBean mBean = null;

    public static long getQueueSize(String queueName) {
        return getQueueProperty(queueName, QueueViewMBean::getQueueSize, 0l);
    }

    public static long getQueueConsumerCount(String queueName) {
        return getQueueProperty(queueName, QueueViewMBean::getConsumerCount, 0l);
    }

    public static long getQueueEnqueueCount(String queueName) {
        return getQueueProperty(queueName, QueueViewMBean::getEnqueueCount, 0l);
    }

    public static long getQueueDequeueCount(String queueName) {
        return getQueueProperty(queueName, QueueViewMBean::getDequeueCount, 0l);
    }

    public static long getTopicSize(String topicName) {
        return getTopicProperty(topicName, TopicViewMBean::getQueueSize, 0l);
    }

    public static long getTopicConsumerCount(String topicName) {
        return getTopicProperty(topicName, TopicViewMBean::getConsumerCount, 0l);
    }

    public static long getTopicEnqueueCount(String topicName) {
        return getTopicProperty(topicName, TopicViewMBean::getEnqueueCount, 0l);
    }

    public static long getTopicDequeueCount(String topicName) {
        return getTopicProperty(topicName, TopicViewMBean::getDequeueCount, 0l);
    }

    private static BrokerViewMBean getBrokerViewMBean() {
        if (mBean != null) {
            return mBean;
        }
        try {
            MBeanServerConnection connection = getConnection();
            ObjectName name = new ObjectName(String.format(broker, StrUtil.isBlank(ActiveMqConfig.getAmqBrokerName()) ? "localhost" : ActiveMqConfig.getAmqBrokerName()));
            mBean = MBeanServerInvocationHandler.newProxyInstance(connection, name, BrokerViewMBean.class, true);
        } catch (IOException e) {
            //log.error("ActiveMQUtil.getAllQueueSize",e);
        } catch (MalformedObjectNameException e) {
            //log.error("ActiveMQUtil.getAllQueueSize",e);
        }
        return mBean;
    }


    private static <R> R getQueueProperty(String queue, Function<QueueViewMBean, R> function, R defaultValue) {
        BrokerViewMBean mBean = getBrokerViewMBean();
        if (mBean != null) {
            for (ObjectName queueName : mBean.getQueues()) {
                QueueViewMBean queueMBean = getQueueMBean(queueName);
                if (queue.equals(queueMBean.getName())) {
                    return function.apply(queueMBean);
                }
            }
        }
        return defaultValue;
    }

    private static <R> R getTopicProperty(String queue, Function<TopicViewMBean, R> function, R defaultValue) {
        BrokerViewMBean mBean = getBrokerViewMBean();
        if (mBean != null) {
            for (ObjectName topicName : mBean.getTopics()) {
                TopicViewMBean topicMBean = getTopicMBean(topicName);
                if (queue.equals(topicMBean.getName())) {
                    return function.apply(topicMBean);
                }
            }
        }
        return defaultValue;
    }

    private static QueueViewMBean getQueueMBean(ObjectName objectName) {
        QueueViewMBean queueMBean = MBeanServerInvocationHandler.newProxyInstance(connection, objectName, QueueViewMBean.class, true);
        return queueMBean;
    }

    private static TopicViewMBean getTopicMBean(ObjectName objectName) {
        TopicViewMBean topicMBean = MBeanServerInvocationHandler.newProxyInstance(connection, objectName, TopicViewMBean.class, true);
        return topicMBean;
    }

    private static MBeanServerConnection getConnection() throws IOException {
        if (connection != null) {
            return connection;
        }
        String brokerUrl = ActiveMqConfig.getAmqBrokerUrl();
        String ip = StrUtil.isBlank(brokerUrl) ? "localhost" : StringUtils.extract(brokerUrl, "//", ":");
        JMXServiceURL url = new JMXServiceURL(String.format(jmxServiceUrl, ip));
        JMXConnector connector = JMXConnectorFactory.connect(url);
        connector.connect();
        connection = connector.getMBeanServerConnection();
        return connection;
    }

    public static void main(String[] args) {
        Long queueSize = getTopicEnqueueCount("123");
        System.out.println(queueSize);
    }
}
