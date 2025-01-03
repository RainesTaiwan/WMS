
package com.sap.ewm.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sap.ewm.sys.model.SysUserRole;

/**
 * <p>
 * 使用者角色表 服務類
 * </p>
 *
 * @author syngna
 * @since 2017-10-29
 */
public interface SysUserRoleService extends IService<SysUserRole> {

	/**
	 * 根據使用者Id刪除該使用者的角色關係
	 *
	 * @param userId 使用者ID
	 * @return boolean
	 * @author Jarvish
	 * @date 2017年12月7日 16:31:38
	 */
	Boolean deleteByUserId(Integer userId);
}
