

package com.sap.ewm.sys.dto;

import com.sap.ewm.sys.model.SysUser;

import java.io.Serializable;

/**
 * @author syngna
 * @date 2017/11/11
 */
public class UserInfo implements Serializable {
	/**
	 * 使用者基本資訊
	 */
	private SysUser sysUser;
	/**
	 * 許可權標識集合
	 */
	private String[] permissions;
	/**
	 * 角色集合
	 */
	private Integer[] roles;

	public SysUser getSysUser() {
		return sysUser;
	}

	public void setSysUser(SysUser sysUser) {
		this.sysUser = sysUser;
	}

	public String[] getPermissions() {
		return permissions;
	}

	public void setPermissions(String[] permissions) {
		this.permissions = permissions;
	}

	public Integer[] getRoles() {
		return roles;
	}

	public void setRoles(Integer[] roles) {
		this.roles = roles;
	}
}
