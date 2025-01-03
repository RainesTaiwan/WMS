sap.ui.define([
    "com/sap/ewm/core/BaseController",
    "sap/ui/model/json/JSONModel",
    "sap/m/MessageToast"
], function (BaseController, JSONModel, MessageToast) {
    "use strict";
    return BaseController.extend("com.sap.ewm.biz.master.ItemMaintain", {

        onInit: function () {
            var me = this;
            this.attachPatternMatched("itemMaintain", this.onRouteMatched);
            me.setModel({}, "list");
            util.CustomUtil.restfulApiRequest("consumption-mode", "GET", {}, result => {
                if (result["header"]["code"] != 0) {
                    me.onMessageError(result["header"]["message"]);
                    return;
                }
                me.getModel("list").setProperty("/consumptionModeList", result.value);
            }, util.CustomUtil.errorCallbackShow, true);

            util.CustomUtil.restfulApiRequest("item-type", "GET", {}, result => {
                if (result["header"]["code"] != 0) {
                    me.onMessageError(result["header"]["message"]);
                    return;
                }
                me.getModel("list").setProperty("/itemTypeList", result.value);
            }, util.CustomUtil.errorCallbackShow, true);

            util.CustomUtil.restfulApiRequest("measure-unit", "GET", {}, result => {
                if (result["header"]["code"] != 0) {
                    me.onMessageError(result["header"]["message"]);
                    return;
                }
                me.getModel("list").setProperty("/measureUnitList", result.value);
            }, util.CustomUtil.errorCallbackShow, true);

            util.CustomUtil.restfulApiRequest("item-group", "GET", {}, result => {
                if (result["header"]["code"] != 0) {
                    me.onMessageError(result["header"]["message"]);
                    return;
                }
                me.getModel("list").setProperty("/itemGroupList", result.value);
            }, util.CustomUtil.errorCallbackShow, true);

            new StatusWebService(this.getWebApi()).groupList(
                "ITEM",
                (result) => {
                    if (result != null && result.header.code == 0) {
                        me.getModel("list").setProperty("/statusList", result.value);
                        me.loadData();
                    } else {
                        me.onMessageError(result.header.message);
                    }
                }
            );
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

            var itemList = oView.byId("itemList");

                itemList.load({
                url: "item/page",
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
            var param = jQuery.extend({}, editModel.getProperty("/row"));
            param["mixItem"] = param["mixItemState"] ? "Y" : "N";
            param["inspectionRequired"] = param["inspectionRequiredState"] ? "Y" : "N";
            delete param["mixItemState"];
            delete param["inspectionRequiredState"];
            util.CustomUtil.restfulApiRequest("item", method, JSON.stringify(param), result => {
                if (result["header"]["code"] != 0) {
                    me.onMessageError(result["header"]["message"]);
                    return;
                }
                MessageToast.show("物料數據儲存成功");
                me.onShowOneColumn();
                me.loadData(false);
            }, util.CustomUtil.errorCallbackShow, true);
        },

        onValidateBeforeSave: function() {
            var me = this, oView = me.getView();
            var aInputs = [
                oView.byId("itemField"),
                oView.byId("descriptionField"),
                oView.byId("mixItemField"),
                oView.byId("inspectionRequiredField"),
                oView.byId("itemStatusField"),
                oView.byId("itemTypeField"),
                oView.byId("consumptionModeField"),
                oView.byId("unitOfMeasureField")
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
            editModel.setProperty("/row", {"itemStatus": "AVAILABLE", "itemType": "1001", "consumptionMode": "FIFO", "mixItemState": true, "inspectionRequiredState": false});
            editModel.setProperty("/create", true);
            util.CustomUtil.setFieldFocus(oView, "itemField", 300);
            this.onShowTowColumn();
        },

        onEdit: function (oEvent) {
            var row = oEvent.getParameters("row").row.getBindingContext();
            var editModel = this.getModel("edit");
            var obj = jQuery.extend({}, row.getObject());
            obj["mixItemState"] = obj["mixItem"] == "N" ? false : true;
            obj["inspectionRequiredState"] = obj["inspectionRequired"] == "N" ? false : true;
            obj["itemType"] = this.itemTypeBoFormatter(obj["itemTypeBo"]);
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
                        util.CustomUtil.restfulApiRequest("item/" + row.getProperty("handle"), "DELETE", {}, result => {
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
            var initData = {"itemList": []};
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
            this.byId("itemList").loadAppendParam({"globalQuery" : sQuery});
        },

        clearAllFilters: function(oEvent) {
            this.byId("itemList").clearAllFilters();
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

        statusFormatter: function(value) {
            var me = this;
            var statusList = me.getModel("list").getProperty("/statusList");
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

        consumptionModeFormatter: function(value) {
            var me = this;
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
        },

        itemTypeFormatter: function(value) {
            var me = this;
            value = me.itemTypeBoFormatter(value);
            var itemTypeList = me.getModel("list").getProperty("/itemTypeList");
            if(itemTypeList == null) {
                return value;
            }
            for(var i = 0; i < itemTypeList.length; i++) {
                var item = itemTypeList[i];
                if(item.itemType == value) {
                    return item.description;
                }
            }
        },

        measureUnitFormatter: function(value) {
            var me = this;
            var measureUnitList = me.getModel("list").getProperty("/measureUnitList");
            if(measureUnitList == null) {
                return value;
            }
            for(var i = 0; i < measureUnitList.length; i++) {
                var item = measureUnitList[i];
                if(item.measureUnit == value) {
                    return item.description;
                }
            }
        },

        itemTypeBoFormatter: function(value) {
            return value ? value.split(":")[1] : "";
        },

        onComboFieldChange: function(oEvent) {
            oEvent.getSource().setValueState(sap.ui.core.ValueState.None);
        }
    });
});
