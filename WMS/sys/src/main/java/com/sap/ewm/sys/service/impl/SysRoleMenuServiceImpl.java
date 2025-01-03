
package com.sap.ewm.sys.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sap.ewm.sys.constants.CacheConstants;
import com.sap.ewm.sys.mapper.SysRoleMenuMapper;
import com.sap.ewm.sys.model.SysRoleMenu;
import com.sap.ewm.sys.service.SysRoleMenuService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 角色菜單表 服務實現類
 * </p>
 *
 * @author syngna
 * @since 2017-10-29
 */
@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {

	/**
	 * @param role
	 * @param roleId  角色
	 * @param menuIds 菜單ID拼成的字串，每個id之間根據逗號分隔
	 * @return
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	@CacheEvict(value = CacheConstants.MENU_DETAILS, key = "#roleId + '_menu'")
	public Boolean saveRoleMenus(String role, Integer roleId, String menuIds) {
		this.remove(Wrappers.<SysRoleMenu>query().lambda()
				.eq(SysRoleMenu::getRoleId, roleId));

		if (StrUtil.isBlank(menuIds)) {
			return Boolean.TRUE;
		}
		List<SysRoleMenu> roleMenuList = Arrays
				.stream(menuIds.split(","))
				.map(menuId -> {
					SysRoleMenu roleMenu = new SysRoleMenu();
					roleMenu.setRoleId(roleId);
					roleMenu.setMenuId(Integer.valueOf(menuId));
					return roleMenu;
				}).collect(Collectors.toList());

		return this.saveBatch(roleMenuList);
	}
}
