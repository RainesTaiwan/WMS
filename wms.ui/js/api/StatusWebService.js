/**
 */
var StatusWebService = function(webapi) {
    this.url = webapi + "/status/";
};

StatusWebService.prototype = new AbstractWebService();

/**
 * 根據狀態分組獲取狀態列表
 */
StatusWebService.prototype.groupList = function(group, handler) {
    this.jsonGet(this.url + "group/list?group=" + group, handler);
}
