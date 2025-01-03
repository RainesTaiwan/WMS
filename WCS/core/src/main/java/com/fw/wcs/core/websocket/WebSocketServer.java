package com.fw.wcs.core.websocket;


import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.TypeUtils;
import com.fw.wcs.core.utils.SpringUtil;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketHandler;
import org.reflections.ReflectionUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketServer extends WebSocketHandler {

    private static Logger logger = LoggerFactory.getLogger( WebSocketServer.class );

    /**
     * Active web sockets set
     */
    private static final ConcurrentHashMap<String, Class<? extends WebSocketWrapper>> handleMap = new ConcurrentHashMap<>();

    /**
     * Executing on new connetcion establishment
     *
     * @param request
     * @param protocol
     *            protocol (it can be ws and wss)
     * @return
     */
    @Override
    public WebSocket doWebSocketConnect(HttpServletRequest request, String protocol) {

        String channel = request.getParameter( "channel" );
        WebSocketWrapper wrapper = null;
        try{
            Map<String,String[]> paramMap = request.getParameterMap();
            if( handleMap.containsKey( channel ) ){
                wrapper = handleMap.get( channel ).newInstance();
                for(String key : paramMap.keySet()){
                    Set<Field> fields = ReflectionUtils.getAllFields(handleMap.get( channel ), field -> field.getName().equals(key));
                    if (fields.size() > 0){
                        Field field = fields.iterator().next();
                        field.setAccessible(true);
                        String[] value = paramMap.get(key);
                        if(field.getType().isArray()){
                            field.set(wrapper, TypeUtils.cast(value[0].split(","), field.getGenericType(), ParserConfig.getGlobalInstance()));
                        }else{
                            field.set(wrapper, TypeUtils.cast(value[0], field.getGenericType(), ParserConfig.getGlobalInstance()));
                        }
                    }
                }
                injectDependency( wrapper );
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }

        return wrapper;
    }

    private void injectDependency( WebSocketWrapper webSocket ){
        try{
            Set<Field> fieldList = ReflectionUtils.getAllFields( webSocket.getClass() );
            for( Field field : fieldList ){
                Autowired aw = field.getAnnotation( Autowired.class );
                if( aw != null ){
                    field.setAccessible( true );
                    Object fieldObj = SpringUtil.getBean( field.getType() );
                    field.set( webSocket, fieldObj );
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    static {
        Reflections reflections = new Reflections("com.sap");
        Set<Class<? extends WebSocketWrapper>> classList =  reflections.getSubTypesOf(WebSocketWrapper.class);
        for( Class clazz : classList ){
            if( !Modifier.isAbstract( clazz.getModifiers() ) ){
                com.fw.wcs.core.annotation.WebSocketHandler handler = (com.fw.wcs.core.annotation.WebSocketHandler) clazz.getAnnotation( com.fw.wcs.core.annotation.WebSocketHandler.class );
                if( handler != null && handler.channel() != null ){
                    handleMap.put( handler.channel(), clazz );
                }
            }
        }
    }

}
