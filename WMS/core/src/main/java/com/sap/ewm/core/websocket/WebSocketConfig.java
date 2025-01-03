package com.sap.ewm.core.websocket;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.websocket.WebSocketHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Configuration
@ConditionalOnProperty( prefix = "websocket", name={ "server.port", "contextPath" }, matchIfMissing = false )
public class WebSocketConfig {

    @Value("${websocket.server.port}")
    private int serverPort;

    @Value("${websocket.contextPath}")
    private String contextPath;

    private Server server;

    @Autowired
    private WebSocketHandler webSocketServer;

    @PostConstruct
    public void startServer(){
        try {
            // Creating Jetty Server on 8081 port
            server = new Server( serverPort );

            // Registerring WebSocketServer in Jetty server
            //WebSocketServer chatWebSocketHandler = new WebSocketServer();
            webSocketServer.setHandler(new DefaultHandler());

            ContextHandler contextHandler = new ContextHandler();
            contextHandler.setContextPath( contextPath );
            contextHandler.setHandler( webSocketServer );

            server.setHandler(webSocketServer);
            // Starting jetty
            server.start();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void destroy(){
        try{
            if( server != null ){
                server.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
