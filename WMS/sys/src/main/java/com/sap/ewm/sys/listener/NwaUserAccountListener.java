package com.sap.ewm.sys.listener;

import com.sap.security.api.UMException;
import com.sap.security.api.UserAccountListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @program: mesext
 * @description: netweaver使用者賬號監聽
 * @author: syngna
 * @create: 2020-06-15 20:58
 */
@Service
public class NwaUserAccountListener implements UserAccountListener {

    private final Logger logger = LoggerFactory.getLogger(NwaUserAccountListener.class);

    @Override
    public void userAccountAdded(String s) throws UMException {
        /*IUserAccount userAccount =UMFactory.getUserAccountFactory().getUserAccount(s);
        Boolean userAccountLocked = userAccount.isUserAccountLocked();
        if(userAccountLocked) {
            SysUser existsUser = sysUserService.getOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, userAccount.getLogonUid()), false);
            if(existsUser != null) {
                existsUser.setUpdateTime(LocalDateTime.now());
                existsUser.setLockFlag(CommonConstants.Y);
                sysUserService.updateById(existsUser);
                logger.info("user account updated to lock: " + s);
            }
        }*/
    }

    @Override
    public void userAccountRemoved(String s) throws UMException {
        logger.info("user account removed: " + s);
    }

    @Override
    public void userAccountLogOut(String s) throws UMException {
        logger.info("user account logout: " + s);
    }
}