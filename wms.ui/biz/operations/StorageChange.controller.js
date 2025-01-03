/**
 * Created by zero on 2018/12/21.
 * 棧板綁定解綁
 */
sap.ui.define([
    'jquery.sap.global',
    'com/sap/ewm/core/BaseController',
    'sap/ui/model/json/JSONModel',
    "sap/m/MessageToast",
    'sap/ui/model/resource/ResourceModel',
    'com/sap/ewm/util/CustomUtil',
    'com/sap/ewm/util/StringUtil'
], function(jQuery, Controller, JSONModel, MessageToast) {
    "use strict";

    return Controller.extend("com.sap.ewm.biz.operations.StorageChange", {

        /**
         * 初始化執行方法
         * @param oEvent
         */
        onInit : function() {
            this.attachPatternMatched("storageChange", this.onRouteMatched);
        },

        /**
         * 每次打開頁面執行
         * url匹配時呼叫
         * @param oEvent
         * @private
         */
        onRouteMatched: function() {

            var me = this, oView = me.getView();
            oView.setModel( new sap.ui.model.json.JSONModel({}) );
        },

        /**
         * change storage confirm
         */
        doConfirm: function(){
            var me = this, oView = me.getView(), oModel = me.getModel();
            var param = {
                carrier: jQuery.trim(oModel.getProperty("/carrier")),
                sourceStorageBin: jQuery.trim(oModel.getProperty("/storageBin")),
                targetStorageBin: jQuery.trim(oModel.getProperty("/targetStorageBin"))
            };
            sap.ui.core.BusyIndicator.show(0);
            util.CustomUtil.restfulApiRequest("handling-unit/storage-change", "GET", param, result => {
                sap.ui.core.BusyIndicator.hide();
                if (result["header"]["code"] != 0) {
                    me.onMessageError(result["header"]["message"]);
                    return;
                }
                MessageToast.show("send message to wcs success");

                util.CustomUtil.setFieldFocus(oView, "carrierNoField", 300);
            }, util.CustomUtil.errorCallbackShow, true);
        }
    });

})