
package com.sap.ewm.sys.model;

import com.baomidou.mybatisplus.extension.activerecord.Model;

/**
 * <p>
 * 使用者角色表
 * </p>
 *
 * @author syngna
 * @since 2017-10-29
 */
public class SysUserRole extends Model<SysUserRole> {

	private static final long serialVersionUID = 1L;
	/**
	 * 使用者ID
	 */
	private Integer userId;
	/**
	 * 角色ID
	 */
	private Integer roleId;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}
}
