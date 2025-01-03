(function (global, factory) {
    typeof exports === 'object' && typeof module !== 'undefined' ? factory(exports) :
    typeof define === 'function' && define.amd ? define(['exports'], factory) :
    (factory((global.pie = global.pie || {})));
}(this, (function (exports) { 'use strict';

    /**
     *
     */
    var AbstractWebService = function() {};

    /**
     *
     */
    AbstractWebService.prototype.jsonPost = function(apiUrl, apiRequest, responseCallback) {
        var xhr = new XMLHttpRequest();
        xhr.open("POST", apiUrl, true);
        xhr.setRequestHeader('Content-type', 'application/json; cruns=utf-8');
        xhr.onload = function() {
            if (responseCallback) {
                if (xhr.responseText) {
                    responseCallback(JSON.parse(xhr.responseText));
                } else {
                    responseCallback(null);
                }
            }
        };
        if (apiRequest) {
            xhr.send(JSON.stringify(apiRequest));
        } else {
            xhr.send();
        }
    };

    /**
     *
     */
    AbstractWebService.prototype.jsonPut = function(apiUrl, apiRequest, responseCallback) {
        var xhr = new XMLHttpRequest();
        xhr.open("PUT", apiUrl, true);
        xhr.setRequestHeader('Content-type', 'application/json; cruns=utf-8');
        xhr.onload = function() {
            if (responseCallback) {
                if (xhr.responseText) {
                    responseCallback(JSON.parse(xhr.responseText));
                } else {
                    responseCallback(null);
                }
            }
        };
        if (apiRequest) {
            xhr.send(JSON.stringify(apiRequest));
        } else {
            xhr.send();
        }
    };


    /**
     *
     */
    AbstractWebService.prototype.jsonDelete = function(apiUrl, apiRequest, responseCallback) {
        var xhr = new XMLHttpRequest();
        xhr.open("DELETE", apiUrl, true);
        xhr.setRequestHeader('Content-type', 'application/json; cruns=utf-8');
        xhr.onload = function() {
            if (responseCallback) {
                if (xhr.responseText) {
                    responseCallback(JSON.parse(xhr.responseText));
                } else {
                    responseCallback(null);
                }
            }
        };
        if (apiRequest) {
            xhr.send(JSON.stringify(apiRequest));
        } else {
            xhr.send();
        }
    };

    /**
     *
     */
    AbstractWebService.prototype.jsonGet = function(apiUrl, responseCallback) {
        var xhr = new XMLHttpRequest();
        xhr.open("GET", apiUrl, true);
        xhr.setRequestHeader('Content-type', 'application/json; cruns=utf-8');
        xhr.onload = function() {
            if (responseCallback) {
                if (xhr.responseText) {
                    responseCallback(JSON.parse(xhr.responseText));
                } else {
                    responseCallback(null);
                }
            }
        };
        xhr.send();
    };

    /**
     */
    var EquipWebService = function(webapi) {
        this.webapi = webapi;
        this.url = webapi + "/equips/";
    };

    /**
     */
    EquipWebService.prototype = new AbstractWebService();

    /**
     */
    EquipWebService.prototype.queryAll = function(handler) {
        this.jsonGet(this.webapi + "/equips?includeChambers=true", handler);
    };

    /**
     */
    EquipWebService.prototype.queryParams = function(equipId, chamberId, handler) {
        this.jsonGet(this.url + equipId + "/chambers/" + chamberId + "/params", handler);
    };

    /**
     */
    EquipWebService.prototype.queryRoutes = function(equipId, chamberId, request, handler) {
        this.jsonPost(this.url + equipId + "/chambers/" + chamberId + "/routes", request, handler);
    };

    /**
     */
    EquipWebService.prototype.inspect = function(equipId, chamberId, request, handler) {
        this.jsonPost(this.url + equipId + "/chambers/" + chamberId + "/_inspect", request, handler);
    };

    /**
     */
    var InspAlgorithmWebService = function(webapi) {
        this.webapi = webapi;
        this.url = webapi + "/inspAlgorithms/";
    };

    /**
     */
    InspAlgorithmWebService.prototype = new AbstractWebService();

    /**
     */
    InspAlgorithmWebService.prototype.queryAll = function(handler) {
        this.jsonGet(this.webapi + "/inspAlgorithms", handler);
    };

    /**
     */
    InspAlgorithmWebService.prototype.queryParams = function(id, handler) {
        this.jsonGet(this.url + id, handler);
    };

    /**
     */
    var InspRuleWebService = function(webapi) {
        this.webapi = webapi;
        this.url = webapi + "/inspRules/";
    };

    /**
     */
    InspRuleWebService.prototype = new AbstractWebService();

    /**
     */
    InspRuleWebService.prototype.insert = function(request, handler) {
        this.jsonPost(this.webapi + "/inspRules", request, handler);
    };

    /**
     */
    InspRuleWebService.prototype.update = function(request, handler) {
        this.jsonPut(this.webapi + "/inspRules", request, handler);
    };

    /**
     */
    InspRuleWebService.prototype.queryAll = function(handler) {
        this.jsonGet(this.webapi + "/inspRules", handler);
    };


    /**
     */
    InspRuleWebService.prototype.queryOne = function(id, rev, handler) {
        this.jsonGet(this.url + id + "/" + rev, handler);
    };

    /**
     */
    InspRuleWebService.prototype.queryOneDetail = function(id, rev, handler) {
        this.jsonGet(this.url + id + "/" + rev + "/detail", handler);
    };

    /**
     */
    InspRuleWebService.prototype.inspect = function(id, rev, request, handler) {
        this.jsonPost(this.url + id + "/" + rev + "/_inspect", null, handler);
    };

    /**
     */
    InspRuleWebService.prototype.createRule = function(inspRule, handler) {
        this.jsonPost(this.url, inspRule, handler);
    };

    var LotWebService = function(webapi) {
        this.webapi = webapi;
        this.url = webapi + "/lots/";
    };

    LotWebService.prototype = new AbstractWebService();

    LotWebService.prototype.queryLotRoutes = function(lotId, request, handler) {
        this.jsonPost(this.url + lotId + "/routes", request, handler);
    };

    LotWebService.prototype.queryWaferRoutes = function(lotId, waferId, request, handler) {
        this.jsonPost(this.url + lotId + "/wafers/" + waferId + "/routes", request, handler);
    };

    var TrackRunWebService = function(webapi) {
        this.webapi = webapi;
        this.url = webapi + "/trackruns/";
    };

    TrackRunWebService.prototype = new AbstractWebService();

    /**
     * @param equipId
     * @param chamberId
     * @param request { tracksRun:[], paramNames:[] }
     * @param handler callback(result)
     */
    TrackRunWebService.prototype.runs4dyg = function(equipId, chamberId, request, handler) {
        this.jsonPost(this.url + equipId + "/" + chamberId + "/runs4dyg", request, handler);
    };


    /**
     * @param equipId
     * @param chamberId
     * @param request { tracksRun:[], algorithm:'', windowScript:'', valueScript:'' }
     * @param handler callback(result)
     */
    TrackRunWebService.prototype.inspect4dyg = function(equipId, chamberId, request, handler) {
        this.jsonPost(this.url + equipId + "/" + chamberId + "/inspect4dyg", request, handler);
    };

    /**
     * @param equipId
     * @param chamberId
     * @param trackRun
     * @param handler callback(result)
     */
    TrackRunWebService.prototype.queryRaws = function(equipId, chamberId, trackRun, handler) {
        this.jsonGet(this.url + equipId + "/" + chamberId + "/runs/" + trackRun, handler);
    };

    exports.EquipWebService = EquipWebService;
    exports.InspAlgorithmWebService = InspAlgorithmWebService;
    exports.InspRuleWebService = InspRuleWebService;
    exports.LotWebService = LotWebService;
    exports.TrackRunWebService = TrackRunWebService;

    Object.defineProperty(exports, '__esModule', { value: true });

})));
