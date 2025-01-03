package com.sap.ewm.sys.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sap.ewm.sys.constants.CommonConstants;
import com.sap.ewm.sys.model.SysUser;
import com.sap.ewm.sys.service.NwaUserService;
import com.sap.ewm.sys.service.SysUserService;
import com.sap.security.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @program: ewm
 * @description:
 * @author: syngna
 * @create: 2020-06-22 16:19
 */
@Service
public class NwaUserServiceImpl implements NwaUserService {

    @Autowired
    private SysUserService sysUserService;

    @Override
    public void getNwaAllUsers() throws UMException {
        LocalDateTime now = LocalDateTime.now();
        IUserFactory userFactory = UMFactory.getUserFactory();
        ISearchResult searchResult = userFactory.searchUsers(userFactory.getUserSearchFilter());
        // 更新使用者資訊
        if(searchResult != null) {
            while (searchResult.hasNext()) {
                String userId = (String)searchResult.next();
                IUser user = userFactory.getUser(userId);
                IUserAccount userAccount = UMFactory.getUserAccountFactory().getUserAccount(CommonConstants.NWA_USER_ACCOUNT_ID_PREFIX + user.getName());
                SysUser existsUser = sysUserService.getOne(Wrappers.<SysUser>lambdaQuery().eq(SysUser::getUsername, user.getName()), false);
                if(existsUser != null) {
                    existsUser.setFirstName(user.getFirstName());
                    existsUser.setLastName(user.getLastName());
                    existsUser.setPhone(user.getTelephone());
                    existsUser.setUpdateTime(now);
                    existsUser.setLockFlag(userAccount.isUserAccountLocked() ? CommonConstants.Y : CommonConstants.N);
                    sysUserService.updateById(existsUser);
                } else {
                    SysUser sysUser = new SysUser();
                    sysUser.setUsername(user.getName());
                    sysUser.setFirstName(user.getFirstName());
                    sysUser.setLastName(user.getLastName());
                    sysUser.setPhone(user.getTelephone());
                    sysUser.setCreateTime(now);
                    sysUser.setUpdateTime(now);
                    sysUser.setLockFlag(userAccount.isUserAccountLocked() ? CommonConstants.Y : CommonConstants.N);
                    sysUser.setDelFlag(CommonConstants.STATUS_NORMAL);
                    sysUserService.save(sysUser);
                }
            }
        }
        // 刪除NWA沒有的數據
        sysUserService.remove(Wrappers.<SysUser>lambdaQuery().lt(SysUser::getUpdateTime, now));
    }
}