package com.sap.ewm.core.aspect;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.ewm.core.utils.ObjectMapperUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class ParamBindAspect {

    @Pointcut("@annotation(com.sap.ewm.core.annotation.ParamBind)")
    public void paramBind(){}

    @Around("paramBind()")
    public Object doBefore(ProceedingJoinPoint joinPoint) throws Throwable {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );
        objectMapper.setDateFormat( new ObjectMapperUtil.DynamicDateFormat() );

        Object[] args = joinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String[] paramNames = methodSignature.getParameterNames();
        Type[] paramTypes = methodSignature.getMethod().getGenericParameterTypes();
        try {
            ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = sra.getRequest();
            Map<String, Object> paramMap = objectMapper.readValue(request.getInputStream(), HashMap.class);

            for (int i = 0; i < paramTypes.length; i++) {
                if( paramMap.containsKey( paramNames[i] ) ){
                    JavaType javaType = objectMapper.constructType( paramTypes[i] );
                    args[i] = ObjectMapperUtil.convertValue( paramMap.get(paramNames[i]), javaType );
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return joinPoint.proceed(args);
    }
}
