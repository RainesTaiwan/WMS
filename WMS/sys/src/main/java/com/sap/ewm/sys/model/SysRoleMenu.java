package com.sap.ewm.sys.model;


import com.baomidou.mybatisplus.extension.activerecord.Model;

/**
 * <p>
 * 角色菜單表
 * </p>
 *
 * @author syngna
 * @since 2017-10-29
 */
public class SysRoleMenu extends Model<SysRoleMenu> {

    private static final long serialVersionUID = 1L;
    /**
     * 角色ID
     */
    private Integer roleId;
    /**
     * 菜單ID
     */
    private Integer menuId;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }
}
