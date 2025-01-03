package com.sap.ewm.sys.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import org.hibernate.validator.constraints.NotBlank;

import java.time.LocalDateTime;

/**
 * <p>
 * 菜單許可權表
 * </p>
 *
 * @author syngna
 * @since 2017-11-08
 */
public class SysMenu extends Model<SysMenu> {

	private static final long serialVersionUID = 1L;
	/**
	 * 菜單ID
	 */
	@TableId(value = "menu_id", type = IdType.AUTO)
	private Integer menuId;
	/**
	 * 菜單名稱
	 */
	@NotBlank(message = "菜單名稱不能為空")
	private String name;
	/**
	 * 菜單許可權標識
	 */
	private String permission;
	/**
	 * 父菜單ID
	 */
	private Integer parentId;
	/**
	 * 圖示
	 */
	private String icon;
	/**
	 * 前端路由標識路徑，預設和 comment 保持一致
	 * 過期
	 */
	private String path;
	/**
	 * 排序值
	 */
	private Integer sort;
	/**
	 * 菜單型別 （0菜單 1按鈕）
	 */
	private String type;
	/**
	 * 路由緩衝
	 */
	private String keepAlive;
	/**
	 * 建立時間
	 */
	private LocalDateTime createTime;
	/**
	 * 更新時間
	 */
	private LocalDateTime updateTime;
	/**
	 * 0--正常 1--刪除
	 */
	@TableLogic
	private String delFlag;

	public Integer getMenuId() {
		return menuId;
	}

	public void setMenuId(Integer menuId) {
		this.menuId = menuId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getKeepAlive() {
		return keepAlive;
	}

	public void setKeepAlive(String keepAlive) {
		this.keepAlive = keepAlive;
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
