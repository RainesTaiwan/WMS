sap.ui.define([
    "com/sap/ewm/core/BaseController",
    "sap/ui/model/json/JSONModel",
    "sap/m/MessageToast"
], function (BaseController, JSONModel, MessageToast) {
    "use strict";
    return BaseController.extend("com.sap.ewm.biz.master.StorageLocationMaintain", {

        onInit: function () {
            var me = this;
            me.attachPatternMatched("storageLocationMaintain", this.onRouteMatched);
            me.setModel({}, "list");
            util.CustomUtil.restfulApiRequest("storage-location-type", "GET", {}, result => {
                if (result["header"]["code"] != 0) {
                    me.onMessageError(result["header"]["message"]);
                    return;
                }
                me.getModel("list").setProperty("/storageLocationTypeList", result.value);
            }, util.CustomUtil.errorCallbackShow, true);

            util.CustomUtil.restfulApiRequest("warehouse", "GET", {}, result => {
                if (result["header"]["code"] != 0) {
                    me.onMessageError(result["header"]["message"]);
                    return;
                }
                me.getModel("list").setProperty("/warehouseList", result.value);
            }, util.CustomUtil.errorCallbackShow, true);
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

            var storageLocationList = oView.byId("storageLocationList");

            storageLocationList.load({
                url: "storage-location/page",
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
            if (!me.onValidateBeforeSave()) {
                return;
            }
            var editModel = this.getModel("edit");
            var create = editModel.getProperty("/create");
            var method = create ? "POST" : "PUT";
            var param = editModel.getProperty("/row");
            util.CustomUtil.restfulApiRequest("storage-location", method, JSON.stringify(param), result => {
                if (result["header"]["code"] != 0) {
                    me.onMessageError(result["header"]["message"]);
                    return;
                }
                MessageToast.show("儲存位置數據儲存成功");
                me.onShowOneColumn();
                me.loadData(false);
            }, util.CustomUtil.errorCallbackShow, true);
        },

        onValidateBeforeSave: function () {
            var me = this, oView = me.getView();
            var aInputs = [
                oView.byId("storageLocationField"),
                oView.byId("descriptionField"),
                oView.byId("storageLocationTypeField"),
                oView.byId("warehouseField")
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
            editModel.setProperty("/row", {"storageLocationType":"1001", "warehouse": "1001"});
            editModel.setProperty("/create", true);
            util.CustomUtil.setFieldFocus(oView, "storageLocationField", 300);
            this.onShowTowColumn();
        },

        onEdit: function (oEvent) {
            var row = oEvent.getParameters("row").row.getBindingContext();
            var editModel = this.getModel("edit");
            var obj = jQuery.extend({}, row.getObject());
            obj["storageLocationType"] = this.storageLocationTypeBoFormatter(obj["storageLocationTypeBo"]);
            obj["warehouse"] = this.storageLocationTypeBoFormatter(obj["warehouseBo"]);
            editModel.setProperty("/row", obj);
            editModel.setProperty("/create", false);
            this.onShowTowColumn();
        },

        onDelete: function (oEvent) {
            var me = this, row = oEvent.getParameters("row").row.getBindingContext();
            var oDialog = new sap.m.Dialog({
                title: me.getI18N().getText("g.Confirm"),
                type: 'Message',
                content: new sap.m.Text({text: me.getI18N().getText("g.DeleteConfirm")}),
                beginButton: new sap.m.Button({
                    type: sap.m.ButtonType.Emphasized,
                    text: me.getI18N().getText("g.Confirm"),
                    press: function () {
                        util.CustomUtil.restfulApiRequest("storage-location/" + row.getProperty("handle"), "DELETE", {}, result => {
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
            var initData = {"storageLocationList": []};
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

        filterGlobally: function (oEvent) {
            var sQuery = oEvent.getParameter("query");
            this.byId("storageLocationList").loadAppendParam({"globalQuery": sQuery});
        },

        clearAllFilters: function (oEvent) {
            this.byId("storageLocationList").clearAllFilters();
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

        storageLocationTypeFormatter: function (value) {
            var me = this;
            value = me.storageLocationTypeBoFormatter(value);
            var storageLocationTypeList = me.getModel("list").getProperty("/storageLocationTypeList");
            if (storageLocationTypeList == null) {
                return value;
            }
            for (var i = 0; i < storageLocationTypeList.length; i++) {
                var item = storageLocationTypeList[i];
                if (item.storageLocationType == value) {
                    return item.description;
                }
            }
        },

        storageLocationTypeBoFormatter: function (value) {
            return value ? value.split(":")[1] : "";
        },

        warehouseBoFormatter: function (value) {
            return value ? value.split(":")[1] : "";
        },

        onComboFieldChange: function(oEvent) {
            oEvent.getSource().setValueState(sap.ui.core.ValueState.None);
        }
    });
});
