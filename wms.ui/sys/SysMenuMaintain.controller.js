sap.ui.define([
	"com/sap/ewm/core/BaseController",
    'sap/ui/core/Fragment',
    "sap/ui/model/json/JSONModel",
	"sap/m/MessageToast"
], function(BaseController, Fragment, JSONModel, MessageToast) {
	"use strict";
	return BaseController.extend("com.sap.ewm.sys.SysMenuMaintain", {

		onInit: function () {
		    var me = this;
		    var parentMenuField = this.byId("parentMenuField");
            parentMenuField.addEventDelegate({
                onclick: function(e) {
                    me.popoverParentMenu(e);
                }
            });
            this.attachPatternMatched("sysMenuMaintain", this.onRouteMatched);
		},
        onRetrieve : function () {
            this.loadData(true);
        },

        /**
         * 載入檢索數據
         */
        loadData: function(clear) {
            var me = this, menuTree = this.byId("menuTree"), oModel = this.getModel();
            new SysMenuWebService(this.getWebApi()).getTree(result => {
                if( result["header"]["code"] != 0 ) {
                    this.onMessageError(result["header"]["message"]);
                    oModel.setProperty("/menuTree", []);
                    return;
                }
                var resultData = result["value"];
                if( !resultData ) {
                    oModel.setProperty("/menuTree", []);
                    return;
                }
                me.processRetrieveData(resultData, null);
                oModel.setProperty("/menuTree", resultData);
                menuTree.expandToLevel(1);
                if( clear === true ) this.clearMsgStrip();
            });
        },

        processRetrieveData: function(data, parentLabel) {
            var me = this;
            if(!data) {
                return null;
            }
            for(var i = 0; i < data.length; i++) {
                var record = data[i];
                record["parentLabel"] = parentLabel;
                parentLabel = record["label"];
                if(record["children"] && record["children"].length > 0) {
                    me.processRetrieveData(record["children"], parentLabel);
                }
            }
        },

        menuTypeFormat: function(value) {
            return "0" === value ? "選單" : "按鈕";
        },

        onSave: function(oEvent) {
            var me = this;
            var editModel = this.getModel("edit");
            var create = editModel.getProperty("/create");
            var parentId = editModel.getProperty("/parentId");
            var row = {
                icon: editModel.getProperty("/icon"),
                keepAlive: 'N',
                parentId: parentId ? parentId : "-1",
                name: editModel.getProperty("/name"),
                path: editModel.getProperty("/path"),
                permission: editModel.getProperty("/permission"),
                sort: editModel.getProperty("/sort"),
                type: editModel.getProperty("/parentMenu") == "menu" ? "0" : "1"
            };

            if(create) {
                new SysMenuWebService(this.getWebApi()).insert(
                    row,
                    (result) => {
                        if(result != null && result.header.code == 0) {
                            MessageToast.show("儲存成功");
                            editModel.setProperty("/name", "");
                            editModel.setProperty("/permission", "");
                            me.loadData();
                        } else {
                            me.onMessageError(result.header.message);
                        }
                    });
            }
            else {
                row.menuId = editModel.getProperty("/menuId");
                new SysMenuWebService(this.getWebApi()).update(
                    row,
                    (result) => {
                        if(result != null && result.header.code == 0) {
                            MessageToast.show("更新成功");
                            me.loadData();
                            this.byId("fcLayout").setLayout("OneColumn");
                            this.byId("createButton").setEnabled(true);
                        } else {
                            me.onMessageError(result.header.message);
                        }
                    });
            }
       },

        onClose: function(oEvent) {
            this.byId("fcLayout").setLayout("OneColumn");
            this.byId("createButton").setEnabled(true);
        },

        onDelete: function(oEvent) {
            var me = this;
            var row = oEvent.getSource().getBindingContext();
            var dialog = new sap.m.Dialog({
                title: 'Confirm',
                content: new sap.m.Text({ text: '確認刪除？' }),
                type: 'Message',
                beginButton: new sap.m.Button({
                    text: 'Submit',
                    press: function () {
                        new SysMenuWebService(me.getWebApi()).delete(
                            row.getProperty("id"),
                            (result) => {
                                if(result != null && result.header.code == 0) {
                                    MessageToast.show("刪除成功");
                                    me.loadData();
                                } else {
                                    me.onMessageError(result.header.message);
                                }
                            });
                        dialog.close();
                    }
                }),
                endButton: new sap.m.Button({
                    text: 'Cancel',
                    press: function () {
                        dialog.close();
                    }
                }),
                afterClose: function() {
                    dialog.destroy();
                }
            });

            dialog.open();
        },

        onCreate: function(oEvent) {
            var row = oEvent.getSource().getBindingContext();
            var editModel = this.getModel("edit");
            editModel.setData({"parentMenu": "menu", "sort": 999});
            if(row != null) {
                editModel.setProperty("/parentId", row.getProperty("id"));
                var parentMenuField = this.byId("parentMenuField");
                var selectedId = row.getProperty("id");
                var selectedText = row.getProperty("label");
                //parentMenuField.setValue(selectedId);
                parentMenuField.setSelectedItem(new sap.ui.core.Item({key:selectedId, text:selectedText}));
            }
            editModel.setProperty("/create", true);
            this.byId("fcLayout").toMidColumnPage(this.createId("editPage"));
            this.byId("fcLayout").setLayout("TwoColumnsBeginExpanded");
            this.byId("createButton").setEnabled(false);
        },

        onEdit: function(oEvent) {

            var row = oEvent.getSource().getBindingContext();
            var editModel = this.getModel("edit");
            editModel.setProperty("/menuId", row.getProperty("id"));
            editModel.setProperty("/parentMenu", row.getProperty("type") == "0" ? "menu" : "button");
            editModel.setProperty("/parentId", row.getProperty("parentId"));
            editModel.setProperty("/path", row.getProperty("path"));
            editModel.setProperty("/name", row.getProperty("name"));
            editModel.setProperty("/icon", row.getProperty("icon"));
            editModel.setProperty("/permission", row.getProperty("permission"));
            editModel.setProperty("/sort", row.getProperty("sort"));
            editModel.setProperty("/create", false);

            var parentMenuField = this.byId("parentMenuField");
            var parentId = row.getProperty("parentId");
            var parentText = row.getProperty("parentLabel");
            //parentMenuField.setValue(selectedId);
            parentMenuField.setSelectedItem(new sap.ui.core.Item({key:parentId, text:parentText}));

            this.byId("fcLayout").toMidColumnPage(this.createId("editPage"));
            this.byId("fcLayout").setLayout("TwoColumnsBeginExpanded");

            this.byId("createButton").setEnabled(false);
        },

        treeSelectionChange: function(oEvent) {
            var me = this;
            var selected = oEvent.getParameter("selected");
            var context = oEvent.getParameter("listItem").getItemNodeContext();
            var children = context.children;
            me.selectItem(selected, children);
        },

        selectItem: function(selected, children) {
            var me = this, oModel = this.getModel("edit");

            if(children != null && children.length > 0) {
                for(var i = 0; i < children.length; i++) {
                    var child = children[i];
                    var path = child.context.getPath();
                    oModel.setProperty(path + "/selected", selected);
                    if(child.children != null && child.children.length > 0) {
                            me.selectItem(selected, child.children);
                    }
                }
            }
        },

        processTreeData: function(selectedIdList, data) {
            var me = this;
            if(!data) {
                return null;
            }
            if(selectedIdList == null) {
                selectedIdList = [];
            }
            for(var i = 0; i < data.length; i++) {
                var record = data[i];
                record["nodes"] = record["children"];
                var idx = selectedIdList.indexOf(record["id"]);
                if(idx > -1) {
                    record.selected = true;
                    selectedIdList.splice(idx, 1);
                }
                if(record["children"] && record["children"].length > 0) {
                    me.processTreeData(selectedIdList, record["children"]);
                }
                delete record["children"];
            }
        },

        popoverParentMenu: function (oEvent) {
            var me = this;
            var oInput = oEvent.srcControl || oEvent.getSource();

            // create popover
            if (!this._oPopover) {
                Fragment.load({
                    id: "popoverParentMenu",
                    name: "com.sap.ewm.sys.fragment.MenuTreeSelectPopover",
                    controller: this
                }).then(function(oPopover){
                    this._oPopover = oPopover;
                    this.getView().addDependent(this._oPopover);
                    this._oPopover.openBy(oInput);
                    this._oPopover.setModel(new sap.ui.model.json.JSONModel({}));
                    this.setPopoverMenuTreeData(this._oPopover);
                }.bind(this));
            } else {
                this._oPopover.openBy(oInput);
                this.setPopoverMenuTreeData(this._oPopover);
            }
        },

        setPopoverMenuTreeData: function (popover) {
            var me = this;
            new SysMenuWebService(this.getWebApi()).getTree(
                (result) => {
                    // permissionTree.setBusy(false);
                    var treeData = result.value;
                    me.processTreeData([], treeData);
                    // permissionTree.expandToLevel(2);
                    popover.getModel().setProperty("/root", treeData);
                }
            );
        },
        onParentMenuSelect: function (oEvent) {
            var tree = oEvent.getSource();
            var selectedContexts = tree.getSelectedContexts();
            var selectedContext = null;
            if(selectedContexts && selectedContexts.length > 0) {
                selectedContext = selectedContexts[0];
            }
            if(selectedContext !== null) {
                var parentMenuField = this.byId("parentMenuField");
                var selectedId = selectedContext.getProperty("id");
                var selectedText = selectedContext.getProperty("label");
                //parentMenuField.setValue(selectedId);
                //this.byId("parentMenuField").setTextFormatter(selectedContext.getProperty("label"));
                parentMenuField.setSelectedItem(new sap.ui.core.Item({key:selectedId, text:selectedText}));
                this._oPopover.close();
            }
        },

        onRouteMatched: function (oEvent) {
            var me = this;
            me.onClear();
            me.loadData();
        },

        onClear: function(oEvent) {
            var initData = {"roleList": []};
            if(this.getModel() != null) {
                this.getModel().setData(initData);
            } else {
                this.setModel(initData);
            }
            if(this.getModel("edit") != null) {
                this.getModel("edit").setData({"parentMenu": "menu", "sort": 999});
            } else {
                this.setModel({"parentMenu": "menu", "sort": 999}, "edit");
            }
            this.onShowOneColumn();
        },

        onShowOneColumn: function() {
            this.byId("fcLayout").setLayout("OneColumn");
            this.byId("createButton").setEnabled(true);
        }
	});
});
