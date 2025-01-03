
package com.sap.ewm.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sap.ewm.sys.mapper.SysUserRoleMapper;
import com.sap.ewm.sys.model.SysUserRole;
import com.sap.ewm.sys.service.SysUserRoleService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 使用者角色表 服務實現類
 * </p>
 *
 * @author syngna
 * @since 2017-10-29
 */
@Service
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleMapper, SysUserRole> implements SysUserRoleService {

	/**
	 * 根據使用者Id刪除該使用者的角色關係
	 *
	 * @param userId 使用者ID
	 * @return boolean
	 * @author Jarvish
	 * @date 2017年12月7日 16:31:38
	 */
	@Override
	public Boolean deleteByUserId(Integer userId) {
		return baseMapper.deleteByUserId(userId);
	}
}
