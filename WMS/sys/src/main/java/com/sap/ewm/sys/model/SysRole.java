package com.sap.ewm.sys.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author syngna
 * @since 2017-10-29
 */
public class SysRole extends Model<SysRole> {

	private static final long serialVersionUID = 1L;

	@TableId(value = "role_id", type = IdType.AUTO)
	private Integer roleId;

	@NotBlank(message = "角色名稱不能為空")
	private String roleName;

	@NotBlank(message = "角色標識不能為空")
	private String roleCode;

	private String roleDesc;

	@NotNull(message = "數據許可權型別不能為空")
	private Integer dsType;
	/**
	 * 數據許可權作用範圍
	 */
	private String dsScope;
	/**
	 * 建立時間
	 */
	private LocalDateTime createTime;
	/**
	 * 修改時間
	 */
	private LocalDateTime updateTime;
	/**
	 * 刪除標識（0-正常,1-刪除）
	 */
	@TableLogic
	private String delFlag;

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(String roleCode) {
		this.roleCode = roleCode;
	}

	public String getRoleDesc() {
		return roleDesc;
	}

	public void setRoleDesc(String roleDesc) {
		this.roleDesc = roleDesc;
	}

	public Integer getDsType() {
		return dsType;
	}

	public void setDsType(Integer dsType) {
		this.dsType = dsType;
	}

	public String getDsScope() {
		return dsScope;
	}

	public void setDsScope(String dsScope) {
		this.dsScope = dsScope;
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
}
