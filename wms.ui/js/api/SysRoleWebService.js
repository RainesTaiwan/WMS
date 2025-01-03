/**
 */
var SysRoleWebService = function(webapi) {
    this.url = webapi + "/role/";
};

/**
 */
SysRoleWebService.prototype = new AbstractWebService();

/**
 * 獲取角色資訊
 */
SysRoleWebService.prototype.list = function(handler) {
    this.jsonGet(this.url + "list", handler);
}

/**
 */
SysRoleWebService.prototype.insert = function(dto, handler) {
    this.jsonPost(this.url, dto, handler);
}

/**
 */
SysRoleWebService.prototype.update = function(dto, handler) {
    this.jsonPut(this.url, dto, handler);
}

SysRoleWebService.prototype.delete = function(roleId, handler) {
    this.jsonDelete(this.url + roleId, {}, handler);
}

/**
 */
SysRoleWebService.prototype.saveRoleMenus = function(dto, handler) {
    this.jsonPut(this.url + "menu", dto, handler);
}
// export default EquipWebService;
