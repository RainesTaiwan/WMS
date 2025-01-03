
package com.sap.ewm.sys.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sap.ewm.sys.model.SysUser;

import java.util.List;

/**
 * @author syngna
 * @date 2017/11/5
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO extends SysUser {
	/**
	 * 角色ID
	 */
	private List<Integer> role;

	public List<Integer> getRole() {
		return role;
	}

	public void setRole(List<Integer> role) {
		this.role = role;
	}
}
