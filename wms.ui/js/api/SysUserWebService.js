/**
 */
var SysUserWebService = function(webapi) {
    this.url = webapi + "/user/";
};

/**
 */
SysUserWebService.prototype = new AbstractWebService();

/**
 */
SysUserWebService.prototype.update = function(dto, handler) {
    this.jsonPut(this.url, dto, handler);
}

/**
 * 獲取使用者資訊
 */
SysUserWebService.prototype.info = function(handler) {
    this.jsonGet(this.url + "info", handler);
}

SysUserWebService.prototype.removePermissionCache = function(username, handler) {
    this.jsonGet(this.url + "removePermissionCache/" + username, handler);
}

// export default EquipWebService;
