sap.ui.define([
	"com/sap/ewm/core/BaseController",
    "sap/ui/model/json/JSONModel",
	"sap/m/MessageToast",
    "util/DateUtil"
], function(BaseController, JSONModel, MessageToast) {
	"use strict";
	return BaseController.extend("com.sap.ewm.sys.SysUserMaintain", {

        _row: null,

		onInit: function () {
            this.attachPatternMatched("sysUserMaintain", this.onRouteMatched);
            /*var permissionList = this.getPermissionList();
            if(permissionList != null) {
                var permissionObj = {};
                for(var i = 0; i < permissionList.length; i++) {
                    permissionObj[permissionList[i]] = true;
                }
            }
            this.setModel(permissionObj, "permission");*/
		},

        onSave: function(oEvent) {
            var userList = this.getView().byId("userList");
            var editModel = this.getModel("edit");
            var row = editModel.getProperty("/row");
            row.role = editModel.getProperty("/row/roles");
            new SysUserWebService(this.getWebApi()).update(
                row,
                (result) => {
                    if(result != null && result.header.code == 0) {
                        MessageToast.show("更新成功");
                        userList.reload();
                    } else {
                        me.onMessageError(result.header.message);
                    }
            });

            this.onShowOneColumn();
        },

        onClose: function(oEvent) {
            this.onShowOneColumn();
        },

        onClear: function(oEvent) {
            var initData = {"userList": []};
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
        onRetrieve : function () {

            var me = this, oView = me.getView();
            util.CustomUtil.clearMsgStrip(oView);
            me.loadData(true);
            util.CustomUtil.setFieldFocus(oView, "usernameField");
        },
        /**
         * 載入檢索數據
         */
        loadData: function(clear) {
            var me = this,oView = me.getView(), oModel = this.getModel();
            var userList = oView.byId("userList");

            var username = jQuery.trim(oModel.getProperty("/username"));

            var param = {};
            if(!util.StringUtil.isBlank(username)) {
                param["username"] = username;
            }
            userList.load({
                url: "user/page",
                method: "GET",
                param: param
            });
        },

        roleFormatter: function(value) {
            var me = this;
            var result = [];
            if(value && value.length > 0) {
                for(var i = 0; i < value.length; i++) {
                    result.push(value[i]["roleId"])
                }
            }
            return result;
        },

        onEdit: function(oEvent) {
            var row = oEvent.getParameters("row").row.getBindingContext();
            var role = row.getProperty("roleList");
            var tokens = [];
            if(role && role.length > 0) {
                for(var i = 0; i < role.length; i++) {
                    // tokens.push(new sap.m.Token({text: role[i].roleName, key: role[i].roleId}));
                    tokens.push(role[i].roleId);
                }
            }
            var rowData = row.getObject();
            rowData.roles = tokens;
            rowData.fullName = jQuery.trim(row.getProperty("lastName")) + jQuery.trim(row.getProperty("firstName"));
            var editModel = this.getModel("edit");
            editModel.setProperty("/row", rowData);
            //this.getModel("view").setProperty("/selected", row);
            this.byId("fcLayout").setLayout("TwoColumnsBeginExpanded");
            this.byId("retrieveButton").setEnabled(false);
            this.byId("clearButton").setEnabled(false);
        },

        onRemovePermissionCache: function(oEvent) {
            var row = oEvent.getParameters("row").row.getBindingContext();
            var username = row.getProperty("username");
            new SysUserWebService(this.getWebApi()).removePermissionCache(username, result =>{
                if(result != null && result.header.code == 0) {
                    MessageToast.show("使用者" + username + "清除許可權快取成功");
                } else {
                    me.onMessageError(result.header.message);
                }
            });
        },

		onRouteMatched: function (oEvent) {
            var me = this;
            me.onClear();
            var oModel = me.getModel();
            new SysRoleWebService(this.getWebApi()).list(
                (result) => {
                    oModel.setProperty("/roleList", result.value);
                }
            );
        },

        onShowOneColumn: function() {
            this.byId("fcLayout").setLayout("OneColumn");
            this.byId("retrieveButton").setEnabled(true);
            this.byId("clearButton").setEnabled(true);
        }
	});
});
