package com.sap.ewm.sys.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sap.ewm.sys.model.SysMenu;
import com.sap.ewm.sys.vo.MenuVO;

import java.util.List;

/**
 * <p>
 * 菜單許可權表 Mapper 介面
 * </p>
 *
 * @author syngna
 * @since 2017-10-29
 */
public interface SysMenuMapper extends BaseMapper<SysMenu> {

	/**
	 * 通過角色編號查詢菜單
	 *
	 * @param roleId 角色ID
	 * @return
	 */
	List<MenuVO> listMenusByRoleId(Integer roleId);

	/**
	 * 通過角色ID查詢許可權
	 *
	 * @param roleIds Ids
	 * @return
	 */
	List<String> listPermissionsByRoleIds(String roleIds);
}
