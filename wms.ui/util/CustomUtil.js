jQuery.sap.declare("util.CustomUtil");
jQuery.sap.require("util.StringUtil");
jQuery.sap.require("sap.m.MessageBox");

util.CustomUtil = {

    restfulApiRequest : function(servletUrl, requestType, requestData, successCallback, errorCallback, async, thisArg, authObj) {
        var config = com.sap.ewm.Component.getMetadata().getManifestEntry("sap.ui5").config;
        var serverHost = config.webapi;
        if(!serverHost) {
            serverHost = window.location.origin + config.domain;
        }
        servletUrl = serverHost + (servletUrl.indexOf("/") === 0 ? "" : "/") + servletUrl;
        $.getJSON("manifest.json");//added for request new user cookie
        try {
            return this.remoteRequest(servletUrl, requestType, requestData, successCallback, errorCallback, async, thisArg, authObj);
        } catch( e ) {
            alert(e);
        }
    },
   remoteRequest : function(servletUrl, requestType, requestData, successCallback, errorCallback, async, thisArg, authObj) {
       var me = this;
       if (successCallback) {
           jQuery.sap.assert(typeof successCallback === "function", "successCallback must be a function");
       }
       if (errorCallback) {
           jQuery.sap.assert(typeof errorCallback === "function", "errorCallback must be a function");
       }
       var context = thisArg;
       if (context == null || context == undefined) {
           context = this;
       }
       var rtype = requestType;
       if (rtype == null || rtype == undefined) {
           rtype = "GET";
       }

       // this is required to prevent caching of requests - in chrome it can cause requests to return 404 errors
       // (see "http://stackoverflow.com/questions/11463637/prevent-chrome-from-caching-ajax-requests")
       if (rtype.toUpperCase() === "GET") {
           // jQuery.sap.log.debug("remoteRequest(before): servletUrl  = " + servletUrl);
           var currentDate = new Date();
           if (servletUrl.indexOf("?") > 0) {
               servletUrl = servletUrl + "&preventCache='" + currentDate.getTime() + "'";
           } else {
               servletUrl = servletUrl + "?preventCache='" + currentDate.getTime() + "'";
           }
           // jQuery.sap.log.debug("remoteRequest(after): servletUrl  = " + servletUrl);
       }

       if (servletUrl.indexOf("?") > 0) {
           servletUrl = servletUrl + "&USER_ID=" + me.getCookie("USER_ID");
       } else {
           servletUrl = servletUrl + "?USER_ID=" + me.getCookie("USER_ID");
       }
       if( !util.StringUtil.isBlank(me.getCookie("SITE")) ) {
           servletUrl = servletUrl + "&SITE=" + me.getCookie("SITE");
       }
       var returnData = undefined;

       //  util.StringUtil.printTimeStamp("jQuery.ajax - start");
       var ajaxObj = {
           url: servletUrl,            // "/layouter/restws/read",
           async: async ? true : false,
           dataType: "json",
           contentType: "application/json",
           data: requestData,    // {fileName: fileName, filePath: filePath},
           type: rtype.toUpperCase(),
           xhrFields: {
               withCredentials: true
           },
           crossDomain: true,
           success: function(oData, textStatus, resp) {
               //  util.StringUtil.printTimeStamp("jQuery.ajax - success");
               if (successCallback) {
                   returnData = oData;
                   if(returnData.status == 403) {
                       me.errorCallbackShow(returnData.status, returnData.message);
                       return;
                   }
                   successCallback.call(context, oData);
               }
               if( resp.getResponseHeader('logout') === "Y" ) {
                   try {
                       me.logoutBasicAuth();
                   } catch ( e ) {
                       console.log(e);
                   }
               }
           },
           error: function(XMLHttpRequest, textStatus, errorThrown){
               //  util.StringUtil.printTimeStamp("jQuery.ajax - error");
               if (errorCallback) {
                   var jsonValue = "";
                   var responseText = XMLHttpRequest.responseText;
                   if( responseText && util.CustomUtil.isJsonFormat(responseText) ) {
                       jsonValue = jQuery.parseJSON(XMLHttpRequest.responseText);
                   }
                   var errorCode = "";
                   var errorMessage = "";
                   if (jsonValue) {
                       if(jsonValue.status == 403) {
                           errorCallback.call(context, jsonValue.status, jsonValue.message);
                           return;
                       }
                       return successCallback.call(context, jsonValue);
                   } else if( responseText ) {
                       errorCode = textStatus;
                       switch( XMLHttpRequest.status ) {
                           case 401 :
                               errorMessage = "authentication failed!";
                               return successCallback.call(context, {"header":{"code": 1, "message" : errorMessage}});
                               break;
                           default :
                               errorMessage = responseText;
                               break;
                       }
                   } else {
                       errorCode = textStatus;
                       errorMessage = "ajax call failed!";
                   }
                   errorCallback.call(context, errorCode, errorMessage);
               }
           }
       };
       ajaxObj["headers"] = {
           "Accept-Language": util.StringUtil.isBlank(me.getCookie("lang")) ? "zh-CN" : me.getCookie("lang")
       };
       if(authObj) {
           ajaxObj["headers"]["Authorization"] = "Basic " + util.StringUtil.base64Encode(authObj["username"] + ":" + authObj["password"])
       }
       jQuery.ajax(ajaxObj);

       return returnData;
   },
    logoutBasicAuth:function() {
        var me = this;
        if( me.IEVersion() != -1 ) {
            document.execCommand("ClearAuthenticationCache", "false");
        } else {
            var appConfig = util.Model.getData("AppConfig");
            var serverHost = appConfig["serverHost"];
            var serverUrl = serverHost + "/logout/_logout";
            $.ajax({
                type: "GET",
                xhrFields: {
                    withCredentials: true
                },
                dataType: "json",
                contentType: "application/json",
                data : {},
                crossDomain: true,
                url: serverUrl,
                success: function (jsonData) {
                    console.log(jsonData);
                },
                error: function (request, textStatus, errorThrown) {
                    console.log(request.responseText);
                    console.log(textStatus);
                    console.log(errorThrown);
                }
            });
        }
    },
   UpperFirstLetter:function (str)   
   {   
      return str.replace(/\b\w+\b/g, function(word) {   
    	  return word.substring(0,1).toUpperCase( ) +  word.substring(1);   
      });   
   },
   errorCallbackShow:function(errorCode, errorMessage){
       sap.ui.core.BusyIndicator.hide();
	   sap.m.MessageBox.show(
           errorMessage, {
	          icon: sap.m.MessageBox.Icon.INFORMATION,
	          title: errorCode,
	          actions: [sap.m.MessageBox.Action.CLOSE],
	          onClose: function(oAction) {}
	      }
	    );
   },
    showMsgStrip: function( oView, aTypes, text) {
        var oMs = sap.ui.getCore().byId("msgStrip");

        if (oMs) {
            oMs.destroy();
        }
        this._generateMsgStrip(oView, aTypes, text);
        var viewContent = oView.getContent();
        if( viewContent && viewContent[0] && viewContent[0].scrollTo ) {
            viewContent[0].scrollTo(0);
        }
    },
    _generateMsgStrip: function(oView, aTypes, text) {
        var oVC =  oView.byId("msgArea"),
            oMsgStrip = new sap.m.MessageStrip("msgStrip", {
                text: text,
                showCloseButton: true,
                showIcon: true,
                type: aTypes
            });

        oVC.addItem(oMsgStrip);
    },
    clearMsgStrip: function(oView) {
        var oVC =  oView.byId("msgArea");
        if( oVC ) oVC.removeAllItems();
    },
    showFragmentMsgStrip: function( fragmentName, aTypes, text) {
        var oMs = sap.ui.getCore().byId("msgStrip");
        if (oMs) {
            oMs.destroy();
        }
        this._generateFragmentMsgStrip(fragmentName, aTypes, text);
    },
    _generateFragmentMsgStrip: function(fragmentName, aTypes, text) {
        var oVC =  sap.ui.core.Fragment.byId(fragmentName, "msgArea"),
            oMsgStrip = new sap.m.MessageStrip("msgStrip", {
                text: text,
                showCloseButton: true,
                showIcon: true,
                type: aTypes
            });

        oVC.addItem(oMsgStrip);
    },
    clearFragmentStrip: function(fragmentName) {
        var oVC =  sap.ui.core.Fragment.byId(fragmentName, "msgArea");
        if( oVC ) oVC.removeAllItems();
    },
    /*
     * Set the field focus
     */
    setFieldFocus: function (view, focusField, delay) {
        jQuery.sap.delayedCall(delay || 800, this, util.CustomUtil.setFocus, [view, focusField]);
    },
    setFocus: function (view, control) {
        var focusedControl = view.byId(control);
        focusedControl.focus();
        if( focusedControl.getValue() && focusedControl.getValue().length ) {
            focusedControl.selectText(0, focusedControl.getValue().length);
        }
    },
    fieldTab: function( e, oView, tabIndexes ) {
        var keyCode = ('which' in e) ? e.which : e.keyCode;

        if (keyCode == 13) {
            var fieldControl = e.srcControl;
            if (fieldControl.getValueState() === sap.ui.core.ValueState.Error) {
                return;
            }
            for( var i = 0; i < tabIndexes.length; i++ ) {
                if( fieldControl.getId() === oView.createId(tabIndexes[i]) ) {
                    util.CustomUtil.setFieldFocus(oView, i === (tabIndexes.length - 1) ? tabIndexes[i] : tabIndexes[i+1], 100);
                }
            }
        }
    },
    fieldValidate: function( field, msg, msgType ) {
        field.setValueState(msgType);
        field.setValueStateText(msg);
        field.openValueStateMessage();
    },
    trimStr: function(str) {
        return str.replace(/(^\s*)|(\s*$)/g,"");
    },
    isJsonFormat: function( str ) {
        try {
            jQuery.parseJSON(str);
        } catch (e) {
            return false;
        }
        return true;
    },
    getCookie: function(name) {
        var arr,
            reg = new RegExp("(^| )"+name+"=([^;]*)(;|$)");
        if(arr=document.cookie.match(reg)) {
            return decodeURIComponent(arr[2]);
        } else {
            return null;
        }
    },
    setCookie: function(cname,cvalue,exdays){
        var d = new Date();
        d.setTime(d.getTime()+(exdays*24*60*60*1000));
        var expires = "expires="+d.toGMTString();
        var path = "path=/"
        document.cookie = cname+"=" + cvalue + "; " + expires + ";" + path;
    },
    IEVersion: function() {
        var userAgent = navigator.userAgent; //取得瀏覽器的userAgent字串
        var isIE = userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1; //判斷是否IE<11瀏覽器
        var isEdge = userAgent.indexOf("Edge") > -1 && !isIE; //判斷是否IE的Edge瀏覽器
        var isIE11 = userAgent.indexOf('Trident') > -1 && userAgent.indexOf("rv:11.0") > -1;
        if(isIE) {
            var reIE = new RegExp("MSIE (\\d+\\.\\d+);");
            reIE.test(userAgent);
            var fIEVersion = parseFloat(RegExp["$1"]);
            if(fIEVersion == 7) {
                return 7;
            } else if(fIEVersion == 8) {
                return 8;
            } else if(fIEVersion == 9) {
                return 9;
            } else if(fIEVersion == 10) {
                return 10;
            } else {
                return 6;//IE版本<=7
            }
        } else if(isEdge) {
            return 'edge';//edge
        } else if(isIE11) {
            return 11; //IE11
        }else{
            return -1;//不是ie瀏覽器
        }
    },
    /*刪除陣列中的某一個對像
     _arr:陣列
     _key:需刪除對象的key值
     _value:需刪除對象的key對應的value
     */
    removeObjectArray: function(_arr, _key, _value) {
        var length = _arr.length;
        for (var i = 0; i < length; i++) {
            if (_arr[i][_key] == _value) {
                if (i == 0) {
                    _arr.shift(); //刪除並返回陣列的第一個元素
                    return _arr;
                }
                else if (i == length - 1) {
                    _arr.pop();  //刪除並返回陣列的最後一個元素
                    return _arr;
                }
                else {
                    _arr.splice(i, 1); //刪除下標為i的元素
                    return _arr;
                }
            }
        }
    },
    groupBy: function( array , f ) {
        let groups = {};
        array.forEach( function( o ) {
            let group = JSON.stringify( f(o) );
            groups[group] = groups[group] || [];
            groups[group].push( o );
        });
        return Object.keys(groups).map( function( group ) {
            return groups[group];
        });
    },

    /**
     * Redirects to given URL
     *
     * @param {String} sURL Uniform resource locator.
     * @param {Boolean} [bNewWindow] Opens URL in a new browser window
     * @public
     * @name util.IOUtil#redirect
     * @function
     */
    redirect : function (sURL, bNewWindow) {

        jQuery.sap.log.debug("redirect: sURL = " + sURL);

        if (!bNewWindow) {
            window.location.href = sURL;
        } else if (navigator.app && typeof navigator.app.loadUrl == "function") {
            navigator.app.loadUrl(sURL, {openExternal : true}); // PhoneGap
        } else {
            window.open(sURL);
        }
    }
};