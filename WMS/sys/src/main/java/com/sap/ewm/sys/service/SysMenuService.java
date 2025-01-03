
package com.sap.ewm.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sap.ewm.sys.model.SysMenu;
import com.sap.ewm.sys.vo.MenuVO;

import java.util.List;

/**
 * <p>
 * 菜單許可權表 服務類
 * </p>
 *
 * @author syngna
 * @since 2017-10-29
 */
public interface SysMenuService extends IService<SysMenu> {
	/**
	 * 通過角色編號查詢URL 許可權
	 *
	 * @param roleId 角色ID
	 * @return 菜單列表
	 */
	List<MenuVO> findMenuByRoleId(Integer roleId);

	/**
	 * 級聯刪除菜單
	 *
	 * @param id 菜單ID
	 * @return 成功、失敗
	 */
	Boolean removeMenuById(Integer id);

	/**
	 * 更新菜單資訊
	 *
	 * @param sysMenu 菜單資訊
	 * @return 成功、失敗
	 */
	Boolean updateMenuById(SysMenu sysMenu);
}
