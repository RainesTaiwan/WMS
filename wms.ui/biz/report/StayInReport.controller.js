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

    return Controller.extend("com.sap.ewm.biz.report.StayInReport", {

        /**
         * 初始化執行方法
         * @param oEvent
         */
        onInit : function() {
            this.attachPatternMatched("stayInReport", this.onRouteMatched);
        },

        /**
         * 每次打開頁面執行
         * url匹配時呼叫
         * @param oEvent
         * @private
         */
        onRouteMatched: function() {
            var me = this, oView = me.getView();
            oView.setModel( new sap.ui.model.json.JSONModel({storageTree:{}}) );
        },

        doRetrieve: function(){
            var me = this;
            me.loadData();
        },

        /**
         * 載入檢索數據
         */
        loadData: function (clear) {
            var me = this, oView = me.getView(), oModel = oView.getModel();
            let storageTree = this.byId("storageTree");
            let item = jQuery.trim(this.getModel().getProperty("/item"));
            let batchNo = jQuery.trim(this.getModel().getProperty("/batchNo"));
            let carrier = jQuery.trim(this.getModel().getProperty("/carrier"));
            if (clear) {
                util.CustomUtil.clearMsgStrip(oView);
            }

            let params = {item, batchNo, carrier};
            util.CustomUtil.restfulApiRequest("/report/stayIn/inventory/list", "POST", JSON.stringify(params), result => {
                if (result["header"]["code"] != 0) {
                    me.onMessageError(result["header"]["message"]);
                    return;
                }

                let resultData = result["value"];
                if( !resultData ) {
                    oModel.setProperty("/storageTree", []);
                    return;
                }
                oModel.setProperty("/storageTree", {'children':resultData});
                storageTree.expandToLevel(1);
                if( clear === true ) this.clearMsgStrip();
            }, util.CustomUtil.errorCallbackShow, true);

        },

        handleFormatter: function(value) {
            return value ? value.split(":")[1] : "";
        },
        
        doClear: function () {
            let me = this, oView = me.getView();
            oView.setModel( new sap.ui.model.json.JSONModel({storageTree:{}}) );
        }
    });

})