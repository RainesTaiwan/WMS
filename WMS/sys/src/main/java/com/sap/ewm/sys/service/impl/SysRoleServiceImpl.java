
package com.sap.ewm.sys.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sap.ewm.sys.mapper.SysRoleMapper;
import com.sap.ewm.sys.model.SysRole;
import com.sap.ewm.sys.model.SysRoleMenu;
import com.sap.ewm.sys.model.SysUserRole;
import com.sap.ewm.sys.service.SysRoleMenuService;
import com.sap.ewm.sys.service.SysRoleService;
import com.sap.ewm.sys.service.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <p>
 * 服務實現類
 * </p>
 *
 * @author syngna
 * @since 2017-10-29
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

	@Autowired
	private SysRoleMenuService sysRoleMenuService;

	@Autowired
	private SysUserRoleService sysUserRoleService;

	/**
	 * 通過使用者ID，查詢角色資訊
	 *
	 * @param userId
	 * @return
	 */
	@Override
	public List findRolesByUserId(Integer userId) {
		return baseMapper.listRolesByUserId(userId);
	}

	/**
	 * 通過角色ID，刪除角色,並清空角色菜單快取
	 *
	 * @param id
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public Boolean removeRoleById(Integer id) {
		sysUserRoleService.remove(Wrappers
				.<SysUserRole>update().lambda()
				.eq(SysUserRole::getRoleId, id));

		sysRoleMenuService.remove(Wrappers
			.<SysRoleMenu>update().lambda()
			.eq(SysRoleMenu::getRoleId, id));
		return this.removeById(id);
	}
}
