package com.fw.wcs.core.aspect;

import com.alibaba.fastjson.JSONObject;
import com.fw.wcs.core.utils.CommonMethods;
import com.fw.wcs.core.utils.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 *  controller操作攔截記錄日誌
 * @author Syngna
 * @since 2020-01-13
 */
@Aspect
@Configuration
public class ControllerLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(ControllerLogAspect.class);

    //切點表達式（*代表所有，第一個*表示返回型別為所有型別，第二個*表示包名，第三個*表示方法名）
    @Pointcut("execution(public * com.fw.wcs.*.controller..*(..))")
    public void log() {

    }

    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String user = CommonMethods.getUser();
        JSONObject jsonObj = new JSONObject();
        jsonObj.put("url", request.getRequestURI());
        jsonObj.put("method", request.getMethod());
        //jsonObj.put("ip", request.getRemoteAddr());
        jsonObj.put("classMethod", joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());

        //參數
        Enumeration<String> paramter = request.getParameterNames();
        if (paramter != null) {
            String paramName = "";
            String paramValue = "";
            while (paramter.hasMoreElements()) {
                String str = paramter.nextElement();
                if (StringUtils.notBlank(paramName)) {
                    paramName += ",";
                    paramValue += ",";
                }
                paramName = paramName + str;
                paramValue = paramValue + request.getParameter(str);
            }
            paramName = "paramName[" + paramName + "],";
            paramValue = "paramValue[" + paramValue + "]";
            jsonObj.put("params", paramName + paramValue);
        }
        logger.info("controller.request.invoked by {}: {}", user, jsonObj);
    }

    @Around("log()")
    public Object doLog(ProceedingJoinPoint joinPoint) throws Throwable {
        String user = CommonMethods.getUser();
        //記錄起始時間
        long begin = System.currentTimeMillis();
        Object result = "";
        /** 執行目標方法 */
        try {
            result = joinPoint.proceed();
        } catch (Exception e) {
            logger.error("controller.request.invoked by {} error, errorMessage: {}", user, e.getMessage());
        } finally {
            /** 記錄操作時間 */
            long took = System.currentTimeMillis() - begin;
            logger.info("controller.request.invoked by {}: {}耗時{}毫秒", user, joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName(), took);
        }
        return result;
    }

    @After("log()")
    public void doAfter(JoinPoint joinPoint) {

    }
}
