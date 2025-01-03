sap.ui.define([
    "com/sap/ewm/core/BaseController",
    "sap/ui/model/json/JSONModel",
    "sap/m/MessageToast"
], function (BaseController, JSONModel, MessageToast) {
    "use strict";
    return BaseController.extend("com.sap.ewm.operations.InventoryReceive", {

        onInit: function () {
            this.attachPatternMatched("inventoryReceive", this.onRouteMatched);
        },
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

            var inventoryList = oView.byId("inventoryList");

            inventoryList.load({
                url: "inventory/page",
                method: "GET",
                param: {}
            });
        },

        onSave: function (oEvent) {
            var me = this;
            if(!me.onValidateBeforeSave()) {
                return;
            }
            var editModel = this.getModel("edit");
            var create = editModel.getProperty("/create");
            var method = create ? "POST" : "PUT";
            var param = editModel.getProperty("/row");
            util.CustomUtil.restfulApiRequest("inventory", method, JSON.stringify(param), result => {
                if (result["header"]["code"] != 0) {
                    me.onMessageError(result["header"]["message"]);
                    return;
                }
                MessageToast.show("庫存接收成功");
                me.onShowOneColumn();
                me.loadData(false);
            }, util.CustomUtil.errorCallbackShow, true);
        },

        onValidateBeforeSave: function() {
            var me = this, oView = me.getView();
            var aInputs = [
                oView.byId("batchNoField"),
                oView.byId("itemField"),
                oView.byId("qtyOnHandField"),
                oView.byId("statusField"),
                oView.byId("productionDateField"),
                oView.byId("validDurationsField")
            ];
            return me.commonInputFieldsValidate(aInputs);
        },

        onClose: function (oEvent) {
            this.onShowOneColumn();
        },

        onCreate: function (oEvent) {
            var me = this, oView = this.getView();
            var editModel = this.getModel("edit");
            editModel.setProperty("/row", {"status": "AVAILABLE", "timeUnit": "DAY"});
            editModel.setProperty("/create", true);
            util.CustomUtil.setFieldFocus(oView, "batchNoField", 300);
           // this.getModel("edit").setData({"status": "AVAILABLE", "timeUnit": "DAY"});
            this.onShowTowColumn();
        },

        onEdit: function (oEvent) {

            var row = oEvent.getParameters("row").row.getBindingContext();
            var editModel = this.getModel("edit");
            editModel.setProperty("/row", row.getObject());
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
                        util.CustomUtil.restfulApiRequest("inventory/" + row.getProperty("handle"), "DELETE", {}, result => {
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
        },

        onClear: function (oEvent) {
            var me = this;
            var initData = {"inventoryList": []};
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
            new StatusWebService(this.getWebApi()).groupList(
                "INVENTORY",
                (result) => {
                    if (result != null && result.header.code == 0) {
                        me.getModel().setProperty("/statusList", result.value);
                        me.loadData();
                    } else {
                        me.onMessageError(result.header.message);
                    }
                }
            );
            this.onShowOneColumn();
        },

        onItemBrowseConfirm: function(oEvent) {
            var me = this;
            var item = oEvent.getParameter("contexts")[0].getProperty("item");
            me.doSetUnitOfMeasure(item);
        },

        onItemChange: function (oEvent) {
            var me = this;
            var itemValue = oEvent.getParameter("value");
            me.doSetUnitOfMeasure(itemValue);
        },

        doSetUnitOfMeasure: function(itemValue) {
            var me = this;
            util.CustomUtil.restfulApiRequest("item/ItemBO:" + itemValue, "GET", {}, result => {
                if (result["header"]["code"] != 0) {
                    me.onMessageError(result["header"]["message"]);
                    return;
                }
                if (result["value"] && result["value"]["unitOfMeasure"]) {
                    me.getModel("edit").setProperty("/row/unitOfMeasure", result["value"]["unitOfMeasure"]);
                } else {
                    me.getModel("edit").setProperty("/row/unitOfMeasure", "");
                }
            }, util.CustomUtil.errorCallbackShow, true);
        },

        onStatusFieldChange: function(oEvent) {
            oEvent.getSource().setValueState(sap.ui.core.ValueState.None);
        },

        onProductionDateFieldChange: function(oEvent) {
            oEvent.getSource().setValueState(sap.ui.core.ValueState.None);
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
        },

        filterGlobally: function(oEvent) {
            var sQuery = oEvent.getParameter("query");
            this.byId("inventoryList").loadAppendParam({"globalQuery" : sQuery});
        },

        clearAllFilters: function(oEvent) {
            this.byId("inventoryList").clearAllFilters();
            this.onRetrieve();
        },

        onShowOneColumn: function () {
            this.byId("fcLayout").setLayout("OneColumn");
            // this.byId("createButton").setEnabled(true);
            // this.byId("editButton").setEnabled(true);
        },

        onShowTowColumn: function () {
            this.byId("fcLayout").setLayout("TwoColumnsBeginExpanded");
            // this.byId("createButton").setEnabled(false);
            // this.byId("editButton").setEnabled(false);
        }
    });
});
