package com.sap.ewm.core.aspect;

import com.alibaba.fastjson.JSONObject;
import com.sap.ewm.core.exception.BusinessException;
import com.sap.ewm.core.utils.IPUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author syngna
 * @date 2020/7/14 21:19
 */

@Aspect
@Component
public class ActiveMqListenerLogAspect {

    private Logger logger = LoggerFactory.getLogger(ActiveMqListenerLogAspect.class);

    @Autowired
    JmsMessagingTemplate jmsTemplate;

    @Value("${spring.activemq.brokerUrl}")
    String brokerURL;

    @Pointcut("@annotation(org.springframework.jms.annotation.JmsListener) && !execution(* com.sap.ewm.sys.listener.MqLogListener.*(..))")
    public void messageLog() {
    }

    @Around("messageLog()")
    public void doAfterReturning(JoinPoint joinPoint) {
        handleLog(joinPoint, null);
    }

    @AfterThrowing(pointcut = "messageLog()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e) {
        handleLog(joinPoint, e);
    }

    public void handleLog(JoinPoint joinPoint, Exception e) {

        Object[] args = joinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        JmsListener jmsListener = methodSignature.getMethod().getAnnotation(JmsListener.class);

        String queueName = jmsListener.destination();
        String messageBody = (String) args[0];

        JSONObject map = JSONObject.parseObject(messageBody);
        String ip = IPUtil.getIp();
        String result = null;
        String errorMsg = null;
        if (e == null) {
            result = "Y";
        } else {
            result = "N";
            errorMsg = e.getMessage();
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("broker", brokerURL);
        jsonObject.put("queue", queueName);
        jsonObject.put("messageId", map.getString("MESSAGE_ID"));
        jsonObject.put("correlationId", map.getString("CORRELATION_ID"));
        jsonObject.put("messageBody", messageBody != null && messageBody.length() > 2000 ? messageBody.substring(0, 2000) : messageBody);
        jsonObject.put("result", result);
        jsonObject.put("error", errorMsg != null && errorMsg.length() > 2000 ? errorMsg.substring(0, 2000) : errorMsg);
        jsonObject.put("serverId", ip);
        jsonObject.put("messageType", "IN");
        jsonObject.put("createdDateTime", LocalDateTime.now());

        if(joinPoint instanceof ProceedingJoinPoint) {
            try {
                String responseBody = (String)((ProceedingJoinPoint)joinPoint).proceed();
                jsonObject.put("responseBody", responseBody != null && responseBody.length() > 2000 ? responseBody.substring(0, 2000) : responseBody);
            } catch (Throwable throwable) {
                throw BusinessException.build(throwable.toString());
            }
        }

        jmsTemplate.convertAndSend("MQ_LOG", jsonObject.toString());
    }
}
