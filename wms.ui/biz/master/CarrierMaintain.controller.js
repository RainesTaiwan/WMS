sap.ui.define([
    "com/sap/ewm/core/BaseController",
    "sap/ui/model/json/JSONModel",
    "sap/m/MessageToast"
], function (BaseController, JSONModel, MessageToast) {
    "use strict";
    return BaseController.extend("com.sap.ewm.biz.master.CarrierMaintain", {

        onInit: function () {
            this.attachPatternMatched("carrierMaintain", this.onRouteMatched);
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

            var carrierList = oView.byId("carrierList");

            carrierList.load({
                url: "carrier/page",
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
            util.CustomUtil.restfulApiRequest("carrier", method, JSON.stringify(param), result => {
                if (result["header"]["code"] != 0) {
                    me.onMessageError(result["header"]["message"]);
                    return;
                }
                MessageToast.show("棧板數據儲存成功");
                me.onShowOneColumn();
                me.loadData(false);
            }, util.CustomUtil.errorCallbackShow, true);
        },

        onValidateBeforeSave: function() {
            var me = this, oView = me.getView();
            var aInputs = [
                oView.byId("carrierField"),
                oView.byId("descriptionField"),
                oView.byId("carrierTypeField")
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
            editModel.setProperty("/row", {});
            editModel.setProperty("/create", true);
            this.onShowTowColumn();
        },

        onEdit: function (oEvent) {
            var row = oEvent.getParameters("row").row.getBindingContext();
            var editModel = this.getModel("edit");
            var obj = row.getObject();
            obj["carrierType"] = this.carrierTypeFormatter(obj["carrierTypeBo"]);
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
                        util.CustomUtil.restfulApiRequest("carrier/" + row.getProperty("handle"), "DELETE", {}, result => {
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
            me.loadData();
        },

        onClear: function (oEvent) {
            var me = this;
            var initData = {"carrierList}": []};
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
            this.byId("carrierList").loadAppendParam({"globalQuery" : sQuery});
        },

        clearAllFilters: function(oEvent) {
            this.byId("carrierList").clearAllFilters();
            this.onRetrieve();
        },

        onShowOneColumn: function () {
            this.byId("fcLayout").setLayout("OneColumn");
            this.byId("createButton").setEnabled(true);
            this.byId("editButton").setEnabled(true);
        },

        onShowTowColumn: function () {
            this.byId("fcLayout").setLayout("TwoColumnsBeginExpanded");
            this.byId("createButton").setEnabled(false);
            this.byId("editButton").setEnabled(false);
        },

        carrierTypeFormatter: function(value) {
            return value ? value.split(":")[1] : "";
        }
    });
});
