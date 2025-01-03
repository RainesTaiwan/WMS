package com.sap.ewm.console.listener;

import com.sap.ewm.core.security.common.CommonMethods;
import com.sap.ewm.sys.listener.NwaPrincipaListener;
import com.sap.ewm.sys.listener.NwaUserAccountListener;
import com.sap.ewm.sys.listener.NwaUserListener;
import com.sap.ewm.sys.service.NwaUserService;
import com.sap.security.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @program: mesext
 * @description: 專案啟動后監聽
 * @author: syngna
 * @create: 2020-06-16 11:55
 */

@Component
public class StartListener implements ApplicationListener<ApplicationReadyEvent> {

    private final Logger logger = LoggerFactory.getLogger(StartListener.class);

    @Autowired
    NwaUserListener nwaUserListener;

    @Autowired
    NwaUserAccountListener nwaUserAccountListener;

    @Autowired
    NwaPrincipaListener nwaPrincipaListener;

    @Autowired
    NwaUserService nwaUserService;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        logger.info("專案啟動成功");
        if(CommonMethods.isLocal()) {
            return;
        }
        logger.info("add user listeners...");
        IUserFactory userFactory = UMFactory.getUserFactory();
        IUserAccountFactory userAccountFactory = UMFactory.getUserAccountFactory();
        IPrincipalFactory principalFactory = UMFactory.getPrincipalFactory();
        // 註冊新增使用者監聽
        userFactory.registerListener(nwaUserListener, UserListener.INFORM_ON_USER_ADDED);
        // 註冊刪除使用者監聽
        userFactory.registerListener(nwaUserListener, UserListener.INFORM_ON_USER_REMOVED);
        // 註冊註銷監聽
        userAccountFactory.registerListener(nwaUserAccountListener, UserAccountListener.INFORM_ON_USERACCOUNT_ADDED);
        userAccountFactory.registerListener(nwaUserAccountListener, UserAccountListener.INFORM_ON_USERACCOUNT_LOGOUT);

        principalFactory.registerListener(nwaPrincipaListener, NwaPrincipaListener.INFORM_ON_OBJECT_ADDED);
        principalFactory.registerListener(nwaPrincipaListener, NwaPrincipaListener.INFORM_ON_OBJECT_ASSIGNED);
        principalFactory.registerListener(nwaPrincipaListener, NwaPrincipaListener.INFORM_ON_OBJECT_EDITED);
        principalFactory.registerListener(nwaPrincipaListener, NwaPrincipaListener.INFORM_ON_OBJECT_REMOVED);
        principalFactory.registerListener(nwaPrincipaListener, NwaPrincipaListener.INFORM_ON_OBJECT_UNASSIGNED);

        logger.info("add user listeners end");

        new Thread(() -> {
            try {
                nwaUserService.getNwaAllUsers();
            } catch (Exception e) {
                logger.error("", e.getMessage());
            }
        }).start();
    }
}