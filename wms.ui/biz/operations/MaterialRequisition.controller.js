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

    return Controller.extend("com.sap.ewm.biz.operations.MaterialRequisition", {

        /**
         * 初始化執行方法
         * @param oEvent
         */
        onInit : function() {
            this.attachPatternMatched("materialRequisition", this.onRouteMatched);
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
            util.CustomUtil.setFieldFocus(oView, "itemField", 300);
        },

        /**
         * 棧板綁定
         */
        doConfirm: function(){
            var me = this, oView = me.getView(), oModel = me.getModel();
            if(!me.onValidateBeforeSave()) {
                return;
            }
            var param = {
                item: jQuery.trim(oModel.getProperty("/item")),
                qty: jQuery.trim(oModel.getProperty("/qty"))
            };
            sap.ui.core.BusyIndicator.show(0);
            util.CustomUtil.restfulApiRequest("material-requisition/requisition-confirm", "GET", param, result => {
                sap.ui.core.BusyIndicator.hide();
                if (result["header"]["code"] != 0) {
                    me.onMessageError(result["header"]["message"]);
                    return;
                }
                MessageToast.show("任務已發送給WCS執行");
                me.getModel().setData({});
                util.CustomUtil.setFieldFocus(oView, "itemField", 300);

            }, util.CustomUtil.errorCallbackShow, true);
        },

        onValidateBeforeSave: function() {
            var me = this, oView = me.getView();
            var aInputs = [
                oView.byId("itemField"),
                oView.byId("qtyField")
            ];
            return me.commonInputFieldsValidate(aInputs);
        }
    });
})