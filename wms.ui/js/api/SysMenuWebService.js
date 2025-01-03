/**
 */
var SysMenuWebService = function(webapi) {
    this.url = webapi + "/menu/";
};

/**
 */
SysMenuWebService.prototype = new AbstractWebService();

/**
 */
SysMenuWebService.prototype.insert = function(dto, handler) {
    this.jsonPost(this.url,dto, handler);
}

/**
 */
SysMenuWebService.prototype.update = function(dto, handler) {
    this.jsonPut(this.url, dto, handler);
}

/**
 */
SysMenuWebService.prototype.delete = function(menuId, handler) {
    this.jsonDelete(this.url + menuId, {}, handler);
}

/**
 */
SysMenuWebService.prototype.getUserMenu = function(handler) {
    this.jsonGet(this.url, handler);
}

/**
 */
SysMenuWebService.prototype.getTree = function(handler) {
    this.jsonGet(this.url + "tree?lazy=false", handler);
}

/**
 */
SysMenuWebService.prototype.getRoleTree = function(roleId, handler) {
    this.jsonGet(this.url + "tree/" + roleId, handler);
}
// export default EquipWebService;
