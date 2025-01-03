sap.ui.define([
	'com/sap/ewm/core/BaseController',
	'sap/ui/Device',
	'jquery.sap.global',
	'util/Model',
    'util/ModelKey'
], function(BaseController, Device, jQuery) {
	"use strict";
	return BaseController.extend("com.sap.ewm.app.App", {

		_bExpanded: true,

        onInit: function() {
			var me = this;
			this.getView().addStyleClass(this.getOwnerComponent().getContentDensityClass());

			// if the app starts on desktop devices with small or meduim screen size, collaps the sid navigation
			if(Device.resize.width <= 1024) {
				this.onSideNavButtonPress();
			}
			Device.media.attachHandler(function(oDevice) {
				if((oDevice.name === "Tablet" && this._bExpanded) || oDevice.name === "Desktop") {
					this.onSideNavButtonPress();
					// set the _bExpanded to false on tablet devices
					// extending and collapsing of side navigation should be done when resizing from
					// desktop to tablet screen sizes)
					this._bExpanded = (oDevice.name === "Desktop");
				}
			}.bind(this));
			// 獲取選單
            new SysMenuWebService(this.getWebApi()).getUserMenu(
                (result) => {
                	if(result.value && result.value.length > 0) {
                		me.generateMenuTree(result.value);
					}
			});

            util.Model.setData(util.ModelKey.PermissionList, []);
            new SysUserWebService(this.getWebApi()).info(
                (result) => {
                    if(result.value) {
                        util.Model.setData(util.ModelKey.PermissionList, result.value.permissions);
                        me.byId("loginUser").setText(result.value.sysUser.username);
                        if(result.value.permissions != null) {
                            var permissionObj = {};
                            for(var i = 0; i < result.value.permissions.length; i++) {
                                permissionObj[result.value.permissions[i]] = true;
                            }
                        }
                        this.setModel(permissionObj, "permission");
                    }
            });
		},
		// NavigationList經測試最多隻能有兩層，後臺配置多層會無法顯示。第二層圖示不會顯示
		generateMenuTree: function(data, parentMenu) {
			var me = this;
            var sysMenu = parentMenu ? parentMenu : this.byId("sys_menu");
            for(var i = 0; i < data.length; i ++) {
                var record = data[i];
                var menu = new sap.tnt.NavigationListItem({
                    text: record.label,
                    icon: record.icon,
                    key: record.path,
                    select: [me.onItemSelected, me]
                });
                sysMenu.addItem(menu);
                if(record.children && record.children.length > 0) {
                	me.generateMenuTree(record.children, menu);
				}
            }
		},

		onItemSelected: function(oEvent) {
			var oItem = oEvent.getParameter('item');
			var sKey = oItem.getKey();

            var children = oItem.getItems();
            if(children != null && children.length > 0) {
                oItem.setExpanded(!oItem.getExpanded());
            	return;
			}
			// if the device is phone, collaps the navigation side of the app to give more space
			if(Device.system.phone) {
				this.onSideNavButtonPress();
			}

			if(sKey.endsWith(".html")) {
				window.open(sKey);
			} else {
                this.getRouter().navTo(sKey);
			}
		},

		onSideNavButtonPress: function() {
			var oToolPage = this.byId("app");
			var bSideExpanded = oToolPage.getSideExpanded();
			this._setToggleButtonTooltip(bSideExpanded);
			oToolPage.setSideExpanded(!oToolPage.getSideExpanded());
		},

		onUserPress: function(oEvent) {
		},

        onLogOut: function () {
            // Get the Query String
            var href = window.location.href;
            var indexOf = href.indexOf('?');
            var query = "";
            if(indexOf > -1) {
                query = href.substr(indexOf);
            }
            // Redirect to the logout.jsp and include the query parameters
            util.CustomUtil.redirect("logout.jsp" + query, false);
        },

		_setToggleButtonTooltip: function(bSideExpanded) {
			var oToggleButton = this.byId('sideButton');
			if(bSideExpanded) {
				oToggleButton.setTooltip('Large Size Navigation');
			} else {
				oToggleButton.setTooltip('Small Size Navigation');
			}
		},

        onLoginUserPress: function (oEvent) {
			var me = this;
            var oPopover = new sap.m.Popover({
                showHeader: false,
                placement: "Bottom",
                content:[
                    new sap.m.Button({
                        text: me.getI18N().getText("g.LogOut"),
                        type: "Transparent",
						press: [me, me.onLogOut]
                    })
                ]
            }).addStyleClass('sapMOTAPopover sapTntToolHeaderPopover');
            oPopover.openBy(oEvent.getSource());
        }
	});
});
