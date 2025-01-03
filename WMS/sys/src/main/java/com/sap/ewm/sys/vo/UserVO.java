
package com.sap.ewm.sys.vo;

import com.sap.ewm.sys.model.SysRole;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author syngna
 * @date 2017/10/29
 */
public class UserVO implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 主鍵ID
	 */
	private Integer userId;
	/**
	 * 使用者名稱
	 */
	private String username;
	/**
	 * firstName
	 */
	private String firstName;
	/**
	 * lastName
	 */
	private String lastName;
	/**
	 * 密碼
	 */
	private String password;
	/**
	 * 隨機鹽
	 */
	private String salt;
	/**
	 * 建立時間
	 */
	private LocalDateTime createTime;
	/**
	 * 修改時間
	 */
	private LocalDateTime updateTime;
	/**
	 * 0-正常，1-刪除
	 */
	private String delFlag;
	/**
	 * 鎖定標記
	 */
	private String lockFlag;
	/**
	 * 手機號
	 */
	private String phone;
	/**
	 * 頭像
	 */
	private String avatar;
	/**
	 * 部門ID
	 */
	private Integer deptId;
	/**
	 * 租戶ID
	 */
	private Integer tenantId;
	/**
	 * 部門名稱
	 */
	private String deptName;
	/**
	 * 角色列表
	 */
	private List<SysRole> roleList;

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public LocalDateTime getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getLockFlag() {
		return lockFlag;
	}

	public void setLockFlag(String lockFlag) {
		this.lockFlag = lockFlag;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Integer getDeptId() {
		return deptId;
	}

	public void setDeptId(Integer deptId) {
		this.deptId = deptId;
	}

	public Integer getTenantId() {
		return tenantId;
	}

	public void setTenantId(Integer tenantId) {
		this.tenantId = tenantId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public List<SysRole> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<SysRole> roleList) {
		this.roleList = roleList;
	}
}
