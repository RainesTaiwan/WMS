package com.sap.ewm.sys.vo;

import java.util.List;

/**
 * @program: ewm
 * @description:
 * @author: syngna
 * @create: 2020-06-24 16:33
 */
public class RoleMenuVO {

    private Integer roleId;

    private List<String> menuIds;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public List<String> getMenuIds() {
        return menuIds;
    }

    public void setMenuIds(List<String> menuIds) {
        this.menuIds = menuIds;
    }
}