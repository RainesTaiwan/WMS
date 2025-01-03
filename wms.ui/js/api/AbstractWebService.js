/**
 *
 */
var AbstractWebService = function() {}

/**
 *
 */
AbstractWebService.prototype.jsonPost = function(apiUrl, apiRequest, responseCallback) {
    var xhr = new XMLHttpRequest();
    xhr.open("POST", apiUrl, true);
    xhr.setRequestHeader('Content-type', 'application/json; cruns=utf-8');
    xhr.onload = function() {
        if(responseCallback) {
            if(xhr.responseText) {
                var respJson = JSON.parse(xhr.responseText);
                if(respJson.status == 403) {
                    util.CustomUtil.errorCallbackShow(respJson.status, respJson.message);
                    return;
                }
                responseCallback(respJson);
            } else {
                responseCallback(null);
            }
        }
    }
    if(apiRequest) {
        xhr.send(JSON.stringify(apiRequest));
    } else {
        xhr.send();
    }
}

/**
 *
 */
AbstractWebService.prototype.jsonPut = function(apiUrl, apiRequest, responseCallback) {
    var xhr = new XMLHttpRequest();
    xhr.open("PUT", apiUrl, true);
    xhr.setRequestHeader('Content-type', 'application/json; cruns=utf-8');
    xhr.onload = function() {
        if(responseCallback) {
            if(xhr.responseText) {
                var respJson = JSON.parse(xhr.responseText);
                if(respJson.status == 403) {
                    util.CustomUtil.errorCallbackShow(respJson.status, respJson.message);
                    return;
                }
                responseCallback(respJson);
            } else {
                responseCallback(null);
            }
        }
    }
    if(apiRequest) {
        xhr.send(JSON.stringify(apiRequest));
    } else {
        xhr.send();
    }
}


/**
 *
 */
AbstractWebService.prototype.jsonDelete = function(apiUrl, apiRequest, responseCallback) {
    var xhr = new XMLHttpRequest();
    xhr.open("DELETE", apiUrl, true);
    xhr.setRequestHeader('Content-type', 'application/json; cruns=utf-8');
    xhr.onload = function() {
        if(responseCallback) {
            if(xhr.responseText) {
                var respJson = JSON.parse(xhr.responseText);
                if(respJson.status == 403) {
                    util.CustomUtil.errorCallbackShow(respJson.status, respJson.message);
                    return;
                }
                responseCallback(respJson);
            } else {
                responseCallback(null);
            }
        }
    }
    if(apiRequest) {
        xhr.send(JSON.stringify(apiRequest));
    } else {
        xhr.send();
    }
}

/**
 *
 */
AbstractWebService.prototype.jsonGet = function(apiUrl, responseCallback) {
    var xhr = new XMLHttpRequest();
    xhr.open("GET", apiUrl, true);
    xhr.setRequestHeader('Content-type', 'application/json; cruns=utf-8');
    xhr.onload = function() {
        if(responseCallback) {
            if(xhr.responseText) {
                var respJson = JSON.parse(xhr.responseText);
                if(respJson.status == 403) {
                    util.CustomUtil.errorCallbackShow(respJson.status, respJson.message);
                    return;
                }
                responseCallback(respJson);
            } else {
                responseCallback(null);
            }
        }
    }
    xhr.send();
}

// export default TrackRunWebService;
