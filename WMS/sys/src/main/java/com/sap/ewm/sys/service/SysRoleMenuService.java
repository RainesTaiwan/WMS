
package com.sap.ewm.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sap.ewm.sys.model.SysRoleMenu;

/**
 * <p>
 * 角色菜單表 服務類
 * </p>
 *
 * @author syngna
 * @since 2017-10-29
 */
public interface SysRoleMenuService extends IService<SysRoleMenu> {

	/**
	 * 更新角色菜單
	 *
	 * @param role
	 * @param roleId  角色
	 * @param menuIds 菜單ID拼成的字串，每個id之間根據逗號分隔
	 * @return
	 */
	Boolean saveRoleMenus(String role, Integer roleId, String menuIds);
}
