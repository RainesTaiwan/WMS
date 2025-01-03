package com.sap.ewm.sys.listener;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sap.ewm.sys.constants.CommonConstants;
import com.sap.ewm.sys.model.SysUser;
import com.sap.ewm.sys.service.SysUserService;
import com.sap.security.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @program: mesext
 * @description: netweaver使用者監聽
 * @author: syngna
 * @create: 2020-06-15 20:58
 */
@Service
public class NwaPrincipaListener implements PrincipalListener {

    private final Logger logger = LoggerFactory.getLogger(NwaPrincipaListener.class);

    @Autowired
    private SysUserService sysUserService;

    @Override
    public void objectAdded(String s) throws UMException {
        logger.info("objectAdded: " + s);
        logger.info("user add start: " + s);
        LocalDateTime now = LocalDateTime.now();
        // 建立使用者
        if(s.startsWith(CommonConstants.NWA_USER_ID_PREFIX)) {
            IUser user = UMFactory.getUserFactory().getUser(s);
            SysUser sysUser = new SysUser();
            SysUser existsUser = sysUserService.getOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, user.getName()), false);
            if(existsUser != null) {
                existsUser.setUpdateTime(now);
                existsUser.setFirstName(user.getFirstName());
                existsUser.setLastName(user.getLastName());
                existsUser.setPhone(user.getTelephone());
                existsUser.setLockFlag(CommonConstants.N);
                existsUser.setDelFlag(CommonConstants.STATUS_NORMAL);
                sysUserService.updateById(existsUser);
            } else {
                sysUser.setUsername(user.getName());
                sysUser.setFirstName(user.getFirstName());
                sysUser.setLastName(user.getLastName());
                sysUser.setPhone(user.getTelephone());
                sysUser.setCreateTime(now);
                sysUser.setUpdateTime(now);
                // sysUser.setDeptId(user.getDepartment());
                sysUser.setLockFlag(CommonConstants.N);
                sysUser.setDelFlag(CommonConstants.STATUS_NORMAL);
                sysUserService.save(sysUser);
            }
            logger.info("user add complete: " + s);
            // 建立使用者賬號，修改鎖定狀態
        } else if(s.startsWith(CommonConstants.NWA_USER_ACCOUNT_ID_PREFIX)) {
            IUserAccount user = UMFactory.getUserAccountFactory().getUserAccount(s);
            SysUser existsUser = sysUserService.getOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, user.getLogonUid()), false);
            if(existsUser != null) {
                existsUser.setLockFlag(user.isUserAccountLocked() ? CommonConstants.Y : CommonConstants.N);
                existsUser.setUpdateTime(now);
                sysUserService.updateById(existsUser);
            }
            logger.info("modify user account lock status : " + s + "->" + user.isUserAccountLocked());
        }
    }

    @Override
    public void objectRemoved(String s) throws UMException {
        logger.info("objectRemoved: " + s);
    }

    @Override
    public void objectEdited(String s) throws UMException {
        logger.info("objectEdited: " + s);
        LocalDateTime now = LocalDateTime.now();
        if(s.startsWith(CommonConstants.NWA_USER_ID_PREFIX)) {
            IUser user = UMFactory.getUserFactory().getUser(s);
            SysUser existsUser = sysUserService.getOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, user.getName()), false);
            if(existsUser != null) {
                existsUser.setFirstName(user.getFirstName());
                existsUser.setLastName(user.getLastName());
                existsUser.setPhone(user.getTelephone());
                existsUser.setUpdateTime(now);
                sysUserService.updateById(existsUser);
                logger.info("user updated: " + existsUser.getUsername());
            }
        } else if(s.startsWith(CommonConstants.NWA_USER_ACCOUNT_ID_PREFIX)) {
            IUserAccount userAccount = UMFactory.getUserAccountFactory().getUserAccount(s);
            if(userAccount != null) {
                SysUser existsUser = sysUserService.getOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, userAccount.getLogonUid()), false);
                if(existsUser != null) {
                    Boolean locked = userAccount.isUserAccountLocked();
                    existsUser.setLockFlag(locked != null && locked ? CommonConstants.Y : CommonConstants.N);
                    sysUserService.updateById(existsUser);
                    logger.info("modify user account lock status : " + s + "->" + userAccount.isUserAccountLocked());
                }
                Boolean locked = userAccount.isUserAccountLocked();
                existsUser.setLockFlag(locked != null && locked ? CommonConstants.Y : CommonConstants.N);
            }
        }
    }

    @Override
    public void objectAssigned(String s, String s1) throws UMException {
        logger.info("objectAssigned: " + s);
    }

    @Override
    public void objectUnAssigned(String s, String s1) throws UMException {
        logger.info("objectUnAssigned: " + s);
    }
}