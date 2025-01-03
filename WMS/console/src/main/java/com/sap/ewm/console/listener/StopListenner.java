package com.sap.ewm.console.listener;

import com.sap.ewm.core.security.common.CommonMethods;
import com.sap.ewm.sys.listener.NwaUserAccountListener;
import com.sap.ewm.sys.listener.NwaUserListener;
import com.sap.security.api.PrincipalListener;
import com.sap.security.api.UMFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

/**
 * @program: mesext
 * @description: 專案停止監聽
 * @author: syngna
 * @create: 2020-06-16 11:56
 */

@Component
public class StopListenner implements ApplicationListener<ContextClosedEvent> {

    private final Logger logger = LoggerFactory.getLogger(StopListenner.class);

    @Autowired
    NwaUserListener nwaUserListener;

    @Autowired
    NwaUserAccountListener nwaUserAccountListener;

    @Autowired
    PrincipalListener principalListener;

    // 監聽kill pid     無法監聽 kill -9 pid
    @Override
    public void onApplicationEvent(ContextClosedEvent contextClosedEvent) {
        logger.info("專案將要停止");
        if(CommonMethods.isLocal()) {
            return;
        }
        logger.info("remove user listeners...");
        UMFactory.getUserFactory().unregisterListener(nwaUserListener);
        UMFactory.getUserAccountFactory().unregisterListener(nwaUserAccountListener);
        UMFactory.getPrincipalFactory().unregisterListener(principalListener);
        logger.info("remove user listeners end");
    }
}