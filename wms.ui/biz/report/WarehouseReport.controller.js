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

    return Controller.extend("com.sap.ewm.biz.report.WarehouseReport", {

        /**
         * 初始化執行方法
         * @param oEvent
         */
        onInit : function() {
            this.attachPatternMatched("warehouseReport", this.onRouteMatched);
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
            let warehouse = jQuery.trim(this.getModel().getProperty("/warehouse"));
            let storageLocation = jQuery.trim(this.getModel().getProperty("/storageLocation"));
            let storageBin = jQuery.trim(this.getModel().getProperty("/storageBin"));
            let item = jQuery.trim(this.getModel().getProperty("/item"));
            let batchNo = jQuery.trim(this.getModel().getProperty("/batchNo"));
            let carrier = jQuery.trim(this.getModel().getProperty("/carrier"));
            if (clear) {
                util.CustomUtil.clearMsgStrip(oView);
            }

            let params = {warehouse, storageLocation, storageBin, item, batchNo, carrier};
            util.CustomUtil.restfulApiRequest("/report/warehouse/inventory/list", "POST", JSON.stringify(params), result => {
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

        statusFormatter: function(value){
            var result = "";
            switch (value) {
                case "IDLE":
                    result = "Idle";
                    break;
                case "IN_USE":
                    result = "In Use";
                    break;
                case "WAIT_IN_STORAGE":
                    result = "Wait In Storage";
                    break;
                case "WAIT_OUT_STORAGE":
                    result = "Wait Out Storage";
                    break;
                case "IN_STORAGE":
                    result = "In Storage";
                    break;
                case "OUT_STORAGE":
                    result = "Out Storage";
                    break;
            }
            return result;
        },

        inventoryStatusFormatter: function(value) {
            return value == "AVAILABLE" ? "Available" : value == "RESTRICT" ? "Restrict" : "";
        },
        
        doClear: function () {
            let me = this, oView = me.getView();
            oView.setModel( new sap.ui.model.json.JSONModel({storageTree:{}}) );
        }
    });

})