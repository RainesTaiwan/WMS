sap.ui.define([
    "sap/ui/core/UIComponent",
    "sap/ui/model/json/JSONModel",
    "sap/ui/model/resource/ResourceModel",
    "jquery.sap.global",
    'com/sap/ewm/util/CustomUtil'
], function (UIComponent, JSONModel, ResourceModel, jQuery) {
    "use strict";
    return UIComponent.extend("com.sap.ewm.Component", {
        metadata : {
            manifest: "json"
        },
        init : function () {
            //Get Storage object to use
            var locale = util.CustomUtil.getCookie("lang");
            if(!locale) {
                var oStorage = jQuery.sap.storage(jQuery.sap.storage.Type.local);
                locale = oStorage.get("locale") || "en_US";
                oStorage.put("locale", locale);
            }

            UIComponent.prototype.init.apply(this, arguments);
            // locale
            sap.ui.getCore().getConfiguration().setLanguage(locale);
            // routing
            this.getRouter().initialize();
        },

        getContentDensityClass : function() {
            if (!this._sContentDensityClass) {
                if (!sap.ui.Device.support.touch) {
                    this._sContentDensityClass = "sapUiSizeCompact";
                } else {
                    this._sContentDensityClass = "sapUiSizeCozy";
                }
            }
            return this._sContentDensityClass;
        }
    });
});
