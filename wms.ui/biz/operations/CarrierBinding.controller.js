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

    return Controller.extend("com.sap.ewm.biz.operations.CarrierBinding", {

        /**
         * 初始化執行方法
         * @param oEvent
         */
        onInit : function() {
            this.attachPatternMatched("carrierBind", this.onRouteMatched);
        },

        /**
         * 每次打開頁面執行
         * url匹配時呼叫
         * @param oEvent
         * @private
         */
        onRouteMatched: function() {

            var me = this, oView = me.getView();
            oView.setModel( new sap.ui.model.json.JSONModel({carrierBindList:[]}) );
            new StatusWebService(this.getWebApi()).groupList(
                "INVENTORY",
                (result) => {
                    if (result != null && result.header.code == 0) {
                        me.getModel().setProperty("/statusList", result.value);
                    } else {
                        me.onMessageError(result.header.message);
                    }
                }
            );
        },

        doRetrieve: function(){
            var me = this;
            me.loadData();
        },

        /**
         * 載入檢索數據
         */
        loadData: function (clear) {
            var me = this, oView = me.getView();
            var batchNo = jQuery.trim(this.getModel().getProperty("/batchNo"));
            if(util.StringUtil.isBlank(batchNo)) {
                me.showMsgStrip("Warning", "批次編號不能為空");
            }
            if (clear) {
                util.CustomUtil.clearMsgStrip(oView);
            }
            util.CustomUtil.restfulApiRequest("handling-unit", "GET", {batchNo: batchNo}, result => {
                if (result["header"]["code"] != 0) {
                    me.onMessageError(result["header"]["message"]);
                    return;
                }
                if (result["value"]) {
                    me.getModel().setProperty("/carrierBindList", result["value"]);
                }
            }, util.CustomUtil.errorCallbackShow, true);

        },

        onBatchNoFieldChange: function(oEvent) {
            var me = this, oView = me.getView();
            var value = oEvent.getParameter("value");
            me.doSetStatus(value);
            me.doRetrieve();
        },

        onBatchNoFieldConfirm: function(oEvent) {
            var me = this;
            var status = oEvent.getParameter("contexts")[0].getProperty("status");
            me.getModel().setProperty("/status", status);
            me.doRetrieve();
        },

        doSetStatus: function(batchNo) {
            var me = this;
            util.CustomUtil.restfulApiRequest("inventory/InventoryBO:" + batchNo, "GET", {}, result => {
                if (result["header"]["code"] != 0) {
                    me.onMessageError(result["header"]["message"]);
                    return;
                }
                if (result["value"] && result["value"]["status"]) {
                    me.getModel().setProperty("/status", result["value"]["status"]);
                }
            }, util.CustomUtil.errorCallbackShow, true);
        },

        /**
         * 棧板綁定
         */
        doBind: function(){
            var me = this, oView = me.getView(), oModel = me.getModel();
            var param = {
                batchNo: jQuery.trim(oModel.getProperty("/batchNo")),
                carrier: jQuery.trim(oModel.getProperty("/carrier")),
                status: jQuery.trim(oModel.getProperty("/status")),
                qty: jQuery.trim(oModel.getProperty("/qty"))
            };
            sap.ui.core.BusyIndicator.show(0);
            util.CustomUtil.restfulApiRequest("handling-unit/carrier-bind", "POST", JSON.stringify(param), result => {
                sap.ui.core.BusyIndicator.hide();
                if (result["header"]["code"] != 0) {
                    me.onMessageError(result["header"]["message"]);
                    return;
                }
                MessageToast.show("綁定成功");

                util.CustomUtil.setFieldFocus(oView, "carrierNoField", 300);
                oModel.setProperty("/carrier", "");
                me.loadData(false);
            }, util.CustomUtil.errorCallbackShow, true);
        },

        /**
         * 棧板解綁
         */
        doUnBind: function(){
            let me = this, oView = me.getView();
            let carrierBindList = oView.byId("carrierBindList");
            let selectedIndices = carrierBindList.getSelectedIndices();
            if(!selectedIndices || selectedIndices.length == 0) {
                me.showMsgStrip("Warning", "請選擇需要解綁的數據");
                return;
            }
            let handles = [];
            for( let i = 0; i < selectedIndices.length; i++ ) {
                let context = carrierBindList.getContextByIndex(selectedIndices[i]);
                handles.push(context.getProperty("handle"));
            }
            if( handles.length > 0 ) {
                sap.ui.core.BusyIndicator.show(0);
                util.CustomUtil.restfulApiRequest( "handling-unit/carrier-unbind", "POST", JSON.stringify(handles), respData => {
                    sap.ui.core.BusyIndicator.hide();
                    if( respData["header"]["code"] != 0 ) {
                        me.onMessageError(respData["header"]["message"]);
                        return;
                    }
                    me.loadData(false);
                    me.showMsgStrip("Success", "解綁成功");
                    carrierBindList.clearSelection();
                }, util.CustomUtil.errorCallbackShow, true );
            }
        },

        itemBoFormatter: function(value) {
            return value ? value.split(":")[1] : "";
        },

        statusFormatter: function(value) {
            var me = this;
            var statusList = me.getModel().getProperty("/statusList");
            if(statusList == null) {
                return value;
            }
            for(var i = 0; i < statusList.length; i++) {
                var item = statusList[i];
                if(item.status == value) {
                    return item.description;
                }
            }
        }
    });

})