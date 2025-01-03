sap.ui.define([
	"com/sap/ewm/core/BaseController",
    "sap/ui/model/json/JSONModel",
	"sap/m/MessageToast"
], function(BaseController, JSONModel, MessageToast) {
	"use strict";
	return BaseController.extend("com.sap.ewm.sys.SysRoleMaintain", {

		onInit: function () {
            this.attachPatternMatched("sysRoleMaintain", this.onRouteMatched);
		},
        onRetrieve : function () {
            this.loadData(true);
        },

        /**
         * 載入檢索數據
         */
        loadData: function(clear) {
            var me = this,oView = me.getView();

            if(clear) {
                util.CustomUtil.clearMsgStrip(oView);
            }

            var roleList = oView.byId("roleList");

            roleList.load({
                url: "role/page",
                method: "GET",
                param: {}
            });
        },

        onSave: function(oEvent) {
            var me = this;
            var editModel = this.getModel("edit");
            var row = editModel.getProperty("/row");
            var create = editModel.getProperty("/create");
            if(create) {
                row.dsType = "3";
                row.dsScope = "2";
                new SysRoleWebService(this.getWebApi()).insert(
                    row,
                    (result) => {
                        if(result != null && result.header.code == 0) {
                            MessageToast.show("儲存成功");
                            me.loadData();
                        } else {
                            me.onMessageError(result.header.message);
                        }
                    });
            }
            else {
                var roleList = this.getView().byId("roleList");
                new SysRoleWebService(this.getWebApi()).update(
                    row,
                    (result) => {
                        if(result != null && result.header.code == 0) {
                            MessageToast.show("更新成功");
                            roleList.reload();
                        } else {
                            me.onMessageError(result.header.message);
                        }
                    });
            }
            this.onShowOneColumn();
       },

        onClose: function(oEvent) {
            this.onShowOneColumn();
        },

        onCreate: function(oEvent) {
            var editModel = this.getModel("edit");
            editModel.setProperty("/row", {});
            editModel.setProperty("/create", true);
            this.byId("fcLayout").toMidColumnPage(this.createId("editPage"));
            this.byId("fcLayout").setLayout("TwoColumnsBeginExpanded");
            this.byId("createButton").setEnabled(false);
            this.byId("editButton").setEnabled(false);
        },

        onEdit: function(oEvent) {

            var row = oEvent.getParameters("row").row.getBindingContext();
            var editModel = this.getModel("edit");
            editModel.setProperty("/row", row.getObject());
            editModel.setProperty("/create", false);

            this.byId("fcLayout").toMidColumnPage(this.createId("editPage"));
            this.byId("fcLayout").setLayout("TwoColumnsBeginExpanded");

            this.byId("createButton").setEnabled(false);
            this.byId("editButton").setEnabled(false);
        },

        onPermissionEdit: function(oEvent) {
            var me = this;
            var permissionTree = this.byId("permissionTree");
            var row = oEvent.getParameters("row").row.getBindingContext();
            var editModel = this.getModel("edit");
            var roleId = row.getProperty("roleId");
            editModel.setProperty("/roleId", roleId);
            permissionTree.setBusy(true);
            new SysMenuWebService(this.getWebApi()).getRoleTree(
                roleId,
                (result) => {
                    var selectedIdList = result.value;
                    new SysMenuWebService(this.getWebApi()).getTree(
                        (result) => {
                            permissionTree.setBusy(false);
                            var treeData = result.value;
                            me.processTreeData(selectedIdList, treeData);
                            permissionTree.expandToLevel(2);
                            editModel.setProperty("/root", treeData);
                        }
                    );
             });

            this.byId("fcLayout").toMidColumnPage(this.createId("permissionPage"));
            this.byId("fcLayout").setLayout("TwoColumnsBeginExpanded");

            this.byId("createButton").setEnabled(false);
            this.byId("editButton").setEnabled(false);
        },

        onPermissionSave: function(oEvent) {
            var me = this;
            var permissionTree = this.byId("permissionTree");
            var roleId = this.getModel("edit").getProperty("/roleId");
            var selectedIds = [];
            var selectedContexts = permissionTree.getSelectedContexts();
            for(var i = 0; i < selectedContexts.length; i++) {
                selectedIds.push(selectedContexts[i].getProperty("id"));
            }
            var param = {roleId: roleId, menuIds: selectedIds};
            new SysRoleWebService(this.getWebApi()).saveRoleMenus(
                param,
                (result) => {
                    if(result != null && result.header.code == 0) {
                        MessageToast.show("操作成功");
                    } else {
                        me.onMessageError(result.header.message);
                    }
                }
            );

            this.byId("fcLayout").setLayout("OneColumn");
            this.byId("createButton").setEnabled(true);
            this.byId("editButton").setEnabled(true);
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
                        new SysRoleWebService(me.getWebApi()).delete(
                            row.getProperty("roleId"),
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
                this.getModel("edit").setData({});
            } else {
                this.setModel({}, "edit");
            }
            this.onShowOneColumn();
        },

        onShowOneColumn: function() {
            this.byId("fcLayout").setLayout("OneColumn");
            this.byId("createButton").setEnabled(true);
            this.byId("editButton").setEnabled(true);
        }
	});
});
