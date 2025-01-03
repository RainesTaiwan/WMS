
package com.sap.ewm.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sap.ewm.sys.model.SysRole;

import java.util.List;

/**
 * <p>
 * 服務類
 * </p>
 *
 * @author syngna
 * @since 2017-10-29
 */
public interface SysRoleService extends IService<SysRole> {

	/**
	 * 通過使用者ID，查詢角色資訊
	 *
	 * @param userId
	 * @return
	 */
	List<SysRole> findRolesByUserId(Integer userId);

	/**
	 * 通過角色ID，刪除角色
	 *
	 * @param id
	 * @return
	 */
	Boolean removeRoleById(Integer id);
}
