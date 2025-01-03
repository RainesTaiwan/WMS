sap.ui.define([
    "com/sap/ewm/core/BaseController",
    "sap/ui/model/json/JSONModel",
    "sap/m/MessageToast"
], function (BaseController, JSONModel, MessageToast) {
    "use strict";
    return BaseController.extend("com.sap.ewm.biz.master.StorageBinTypeMaintain", {

        onInit: function () {
            this.attachPatternMatched("storageBinTypeMaintain", this.onRouteMatched);
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

            var storageBinTypeList = oView.byId("storageBinTypeList");

                storageBinTypeList.load({
                url: "storage-bin-type/page",
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
            param["mixItem"] = param["mixItemState"] ? "Y" : "N";
            delete param["mixItemState"];
            util.CustomUtil.restfulApiRequest("storage-bin-type", method, JSON.stringify(param), result => {
                if (result["header"]["code"] != 0) {
                    me.onMessageError(result["header"]["message"]);
                    return;
                }
                MessageToast.show("儲存貨格型別數據儲存成功");
                me.onShowOneColumn();
                me.loadData(false);
            }, util.CustomUtil.errorCallbackShow, true);
        },

        onValidateBeforeSave: function() {
            var me = this, oView = me.getView();
            var aInputs = [
                oView.byId("storageBinTypeField"),
                oView.byId("descriptionField"),
                oView.byId("widthField"),
                oView.byId("lengthField"),
                oView.byId("heightField"),
                oView.byId("maxWeightField"),
                oView.byId("mixItemField")
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
            editModel.setProperty("/row", {"mixItemState": true});
            editModel.setProperty("/create", true);
            util.CustomUtil.setFieldFocus(oView, "storageBinTypeField", 300);
            this.onShowTowColumn();
        },

        onEdit: function (oEvent) {
            var row = oEvent.getParameters("row").row.getBindingContext();
            var editModel = this.getModel("edit");
            var obj = jQuery.extend({}, row.getObject());
            obj["mixItemState"] = obj["mixItem"] == "N" ? false : true;
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
                        util.CustomUtil.restfulApiRequest("storage-bin-type/" + row.getProperty("handle"), "DELETE", {}, result => {
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
            var initData = {"storageBinTypeList": []};
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
            this.byId("storageBinTypeList").loadAppendParam({"globalQuery" : sQuery});
        },

        clearAllFilters: function(oEvent) {
            this.byId("storageBinTypeList").clearAllFilters();
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
        }
    });
});
