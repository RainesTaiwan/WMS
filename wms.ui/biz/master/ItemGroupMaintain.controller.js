sap.ui.define([
    "com/sap/ewm/core/BaseController",
    "sap/ui/model/json/JSONModel",
    "sap/m/MessageToast"
], function (BaseController, JSONModel, MessageToast) {
    "use strict";
    return BaseController.extend("com.sap.ewm.biz.master.ItemGroupMaintain", {

        onInit: function () {
            var me = this;
            me.attachPatternMatched("itemGroupMaintain", this.onRouteMatched);
        },

        /**
         * 檢索
         */
        onRetrieve: function () {
            this.loadData(true);
        },

        /**
         * 載入檢索數據
         */
        loadData: function (clear) {
            var me = this, oView = me.getView();

            if (clear) {
                util.CustomUtil.clearMsgStrip(oView);
            }

            var itemGroupList = oView.byId("itemGroupList");

                itemGroupList.load({
                url: "item-group/page",
                method: "GET",
                param: {}
            });
        },

        /**
         * 儲存
         * @param oEvent
         */
        onSave: function (oEvent) {
            var me = this;
            if(!me.onValidateBeforeSave()) {
                return;
            }
            var editModel = this.getModel("edit");
            var create = editModel.getProperty("/create");
            var method = create ? "POST" : "PUT";
            var param = editModel.getProperty("/row");
            util.CustomUtil.restfulApiRequest("item-group", method, JSON.stringify(param), result => {
                if (result["header"]["code"] != 0) {
                    me.onMessageError(result["header"]["message"]);
                    return;
                }
                MessageToast.show("物料組數據儲存成功");
                me.onShowOneColumn();
                me.loadData(false);
            }, util.CustomUtil.errorCallbackShow, true);
        },

        onValidateBeforeSave: function() {
            var me = this, oView = me.getView();
            var aInputs = [
                oView.byId("itemGroupField"),
                oView.byId("descriptionField"),
                oView.byId("consumptionModeField")
            ];
            return me.commonInputFieldsValidate(aInputs);
        },

        /**
         * Form關閉
         * @param oEvent
         */
        onClose: function (oEvent) {
            this.onShowOneColumn();
        },

        onCreate: function (oEvent) {
            var me = this, oView = this.getView();
            var editModel = this.getModel("edit");
            editModel.setProperty("/row", {consumptionMode: "FIFO"});
            editModel.setProperty("/create", true);
            util.CustomUtil.setFieldFocus(oView, "itemGroupField", 300);
            this.onShowTowColumn();
        },

        onEdit: function (oEvent) {
            var row = oEvent.getParameters("row").row.getBindingContext();
            var editModel = this.getModel("edit");
            var obj = jQuery.extend({}, row.getObject());
            editModel.setProperty("/row", obj);
            editModel.setProperty("/create", false);
            this.onShowTowColumn();
        },

        onDelete: function (oEvent) {
            var me = this, row = oEvent.getParameters("row").row.getBindingContext();
            var oDialog = new sap.m.Dialog({
                title: me.getI18N().getText("g.Confirm"),
                type: 'Message',
                content: new sap.m.Text({ text: me.getI18N().getText("g.DeleteConfirm") }),
                beginButton: new sap.m.Button({
                    type: sap.m.ButtonType.Emphasized,
                    text: me.getI18N().getText("g.Confirm"),
                    press: function () {
                        util.CustomUtil.restfulApiRequest("item-group/" + row.getProperty("handle"), "DELETE", {}, result => {
                            if (result["header"]["code"] != 0) {
                                me.onMessageError(result["header"]["message"]);
                                return;
                            }
                            MessageToast.show("刪除成功");
                            me.loadData(false);
                        }, util.CustomUtil.errorCallbackShow, true);
                        oDialog.close();
                    }
                }),
                endButton: new sap.m.Button({
                    text: me.getI18N().getText("g.Cancel"),
                    press: function () {
                        oDialog.close();
                    }
                }),
                afterClose: function () {
                    oDialog.destroy();
                }
            });
            oDialog.open();
        },

        selectItem: function (selected, children) {
            var me = this, oModel = this.getModel("edit");

            if (children != null && children.length > 0) {
                for (var i = 0; i < children.length; i++) {
                    var child = children[i];
                    var path = child.context.getPath();
                    oModel.setProperty(path + "/selected", selected);
                    if (child.children != null && child.children.length > 0) {
                        me.selectItem(selected, child.children);
                    }
                }
            }
        },

        onRouteMatched: function (oEvent) {
            var me = this;
            me.onClear();
            if(!me.getModel("list")) {
                me.setModel({}, "list");
                util.CustomUtil.restfulApiRequest("consumption-mode", "GET", {}, result => {
                    if (result["header"]["code"] != 0) {
                        me.onMessageError(result["header"]["message"]);
                        return;
                    }
                    me.getModel("list").setProperty("/consumptionModeList", result.value);
                    me.loadData();
                }, util.CustomUtil.errorCallbackShow, true);
            } else {
                me.loadData();
            }
        },

        onClear: function (oEvent) {
            var me = this;
            var initData = {"itemGroupList": []};
            if (this.getModel() != null) {
                this.getModel().setData(initData);
            } else {
                this.setModel(initData);
            }
            if (this.getModel("edit") != null) {
                this.getModel("edit").setData({});
            } else {
                this.setModel({}, "edit");
            }
            this.onShowOneColumn();
        },

        filterGlobally: function(oEvent) {
            var sQuery = oEvent.getParameter("query");
            this.byId("itemGroupList").loadAppendParam({"globalQuery" : sQuery});
        },

        clearAllFilters: function(oEvent) {
            this.byId("itemGroupList").clearAllFilters();
            this.onRetrieve();
        },

        onShowOneColumn: function () {
            this.byId("fcLayout").setLayout("OneColumn");
            this.byId("createButton").setEnabled(true);
            this.byId("retrieveButton").setEnabled(true);
        },

        onShowTowColumn: function () {
            this.byId("fcLayout").setLayout("TwoColumnsBeginExpanded");
            this.byId("createButton").setEnabled(false);
            this.byId("retrieveButton").setEnabled(false);
        },

        consumptionModeFormatter: function(value) {
            var me = this;
            if(!value) {
                return "";
            }
            var consumptionModeList = me.getModel("list").getProperty("/consumptionModeList");
            if(consumptionModeList == null) {
                return value;
            }
            for(var i = 0; i < consumptionModeList.length; i++) {
                var item = consumptionModeList[i];
                if(item.consumptionMode == value) {
                    return item.description;
                }
            }
        }
    });
});
