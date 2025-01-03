sap.ui.define([
    "sap/ui/core/mvc/Controller",
    "sap/ui/core/routing/History",
    "sap/m/MessageToast",
    "sap/m/Button",
    "sap/m/Dialog",
    "sap/m/Text",
    'com/sap/ewm/util/DateUtil',
], function(Controller, History, MessageToast, Button, Dialog, Text) {
    "use strict";
    return Controller.extend("com.sap.ewm.core.BaseController", {

        /**
         * Convenience method for accessing the router.
         * @public
         * @returns {sap.ui.core.routing.Router} the router for this component
         */
        getRouter: function() {
            return sap.ui.core.UIComponent.getRouterFor(this);
        },

        getI18N: function(sName) {
            if(sName) {
                return this.getModel(sName).getResourceBundle();
            } else {
                return this.getModel("i18n").getResourceBundle();
            }
        },

        /**
         * Convenience method for getting the view model by name.
         * @public
         * @param {string} sName the model name
         * @returns {sap.ui.model.Model} the model instance
         */
        getModel: function(sName) {
            return this.getView().getModel(sName);
        },

        /**
         * Convenience method for setting the view model.
         * @public
         * @param {object} oModel the model instance
         * @param {string} sName the model name
         * @returns {sap.ui.mvc.View} the view instance
         */
        setModel: function(oModel, sName) {
            return this.getView().setModel(new sap.ui.model.json.JSONModel(oModel), sName);
        },

        /**
         * Convenience method for getting the configuration from manifest.
         * @public
         * @returns the configuration.
         */
        getConfig: function() {
            return this.getOwnerComponent().getManifestEntry("sap.ui5").config;
        },

        /**
         * Convenience method for getting the configuration from manifest.
         * @public
         * @param {string} sName the route name.
         * @param {function} handle Callback of attachPatternMatched.
         */
        attachPatternMatched: function(sName, handle) {
            var oRouter = sap.ui.core.UIComponent.getRouterFor(this);
            oRouter.getRoute(sName).attachPatternMatched(handle, this);
        },

        /**
         * Convenience method for getting the root url of web biz.
         * @public
         * @returns {string} the url.
         */
        getWebApi: function() {
            var config = this.getOwnerComponent().getManifestEntry("sap.ui5").config;
            var webapi = config.webapi;
            return webapi || window.location.origin + config.domain;
        },

        navTo: function(sName, key, value) {
            var oRouter = sap.ui.core.UIComponent.getRouterFor(this);
            if(key) {
                var request = {};
                request[key] = value;
                oRouter.navTo(sName, request);
            } else {
                oRouter.navTo(sName);
            }
        },

        navBack: function(sName) {
            if(!sName) {
                sName = "Home";
            }
            var sPreviousHash = History.getInstance().getPreviousHash();
            if(sPreviousHash !== undefined) {
                window.history.go(-1);
            } else {
                this.getOwnerComponent()
                    .getRouter()
                    .navTo(sName);
            }
        },

        showDialog: function(message) {
            var self = this;
            return new Promise(function(approved, reject) {
                var dialog = new Dialog({
                    title: 'Confirm',
                    type: 'Message',
                    content: new Text({ text: message }),
                    beginButton: new Button({
                        text: 'OK',
                        press: function() {
                            approved.call(self);
                            dialog.close();
                        }
                    }),
                    endButton: new Button({
                        text: 'Cancel',
                        press: function() {
                            reject.call(self);
                            dialog.close();
                        }
                    }),
                    afterClose: function() {
                        dialog.destroy();
                    }
                });

                dialog.open();
            });
            return promise;
        },

        showConfirmDialog: function(message) {
            var ok = false;
            var dialog = new Dialog({
                title: 'Confirm',
                type: 'Message',
                content: new Text({ text: message }),
                beginButton: new Button({
                    text: 'OK',
                    press: function() {
                        ok = true;
                        dialog.close();
                    }
                }),
                endButton: new Button({
                    text: 'Cancel',
                    press: function() {
                        ok = false;
                        dialog.close();
                    }
                }),
                afterClose: function() {
                    dialog.destroy();
                }
            });

            dialog.open();
            return ok;
        },

        onNavBack: function(oEvent) {
            if(this.getDefaultNavBack !== undefined) {
                this.navBack(this.getDefaultNavBack());
            } else {
                this.navBack("Home");
            }
        },

        assign: function(model, path, data) {
            Object.keys(data).forEach((k) => {
                model.setProperty(path + k, data[k]);
            });
        },

        getTableSelected: function(id, message) {
            if(!message) {
                message = "select one";
            }

            var table = this.byId(id);
            var index = table.getSelectedIndex();
            if(index < 0) {
                MessageToast.show(message);
                return null;
            } else {
                return table.getContextByIndex(index).getObject();
            }
        },

        yesNoFormatter: function(value) {
            if(!value) {
                return "";
            }
            return this.getI18N().getText("g." + value);
        },

        dateFormatter: function(value) {
            if(util.StringUtil.isBlank(value)) {
                return value;
            }
            return util.DateUtil.format("yyyy-MM-dd", new Date(value));
        },

        dateTimeFormatter: function(value) {
            if(util.StringUtil.isBlank(value)) {
                return value;
            }
            return util.DateUtil.format("yyyy-MM-dd hh:mm:ss", new Date(value));
        },

        dateTimeFormatterWithoutSecond: function(value) {
            if(util.StringUtil.isBlank(value)) {
                return value;
            }
            return util.DateUtil.format("yyyy-MM-dd hh:mm", new Date(value));
        },

        getPermissionList: function() {
            return util.Model.getData(util.ModelKey.PermissionList);
        },

        hasPermission: function(permissionCode) {
            var permissionList = this.getPermissionList();
            if(permissionList == null) {
                return false;
            }
            return permissionList.indexOf(permissionCode) > -1 ? true : false;
        },
        onPositiveIntNumberValidate: function( oEvent ) {
            var field = oEvent.getSource();
            this.positiveIntNumberFieldValidate(field, field.getValue());

        },
        onPositiveFloatNumberValidate: function( oEvent ) {
            var field = oEvent.getSource();
            this.positiveFloatNumberFieldValidate(field, field.getValue());
        },
        /**
         * 驗證正正數欄位
         * @param field 需要驗證的欄位
         * @param value 欄位值
         */
        positiveIntNumberFieldValidate: function( field, value ) {
            var me = this;
            var reg = new RegExp("^[0-9]*$");
            if(!reg.test(value)){
                field.setValueState(sap.ui.core.ValueState.Error);
                field.setValueStateText( me.getI18n("common.PositiveIntNumberTip"));
                field.openValueStateMessage();
                return false;
            } else {
                field.setValueState(sap.ui.core.ValueState.None);
                field.setValueStateText("");
                field.closeValueStateMessage();
                return true;
            }
        },
        /**
         * 驗證正浮點數欄位
         * @param field 需要驗證的欄位
         * @param value 欄位值
         */
        positiveFloatNumberFieldValidate: function( field, value ) {
            var me = this;
            var reg = new RegExp("^(([0-9]+\.[0-9]*[1-9][0-9]*)|([0-9]*[1-9][0-9]*\.[0-9]+)|([0-9]*[1-9][0-9]*))$");
            if (!util.StringUtil.isBlank(value) && !reg.test(value)) {
                field.setValueState(sap.ui.core.ValueState.Error);
                field.setValueStateText(me.getI18n("common.QuantityTip"));
                field.openValueStateMessage();
            } else {
                field.setValueState(sap.ui.core.ValueState.None);
                field.setValueStateText("");
                field.closeValueStateMessage();
            }
        },
        /**
         * 輸入框基礎驗證
         * @param inputFields
         * @returns {boolean}
         */
        commonInputFieldsValidate: function(inputFields) {
            var me = this;
            var validate = true;
            var focused = false;
            for( var i = 0; i < inputFields.length; i++ ) {
                var validateField = inputFields[i];
                if(!validateField.getValue) {
                    continue;
                }
                var fieldValue = jQuery.trim(validateField.getValue());
                if( validateField.getVisible() && validateField.getRequired() && util.StringUtil.isBlank(fieldValue) ) {
                    validateField.setValueState(sap.ui.core.ValueState.Error);
                    validateField.setValueStateText(me.getI18N().getText("g.EmptyField"));
                    if(!focused) {
                        validateField.focus();
                        focused = true;
                    }
                }
                if( validateField.getValueState() == "Error" ) {
                    validate = false;
                    validateField.openValueStateMessage();
                    if(!focused) {
                        validateField.focus();
                        focused = true;
                    }
                }
            }
            if( !validate ) {
                return false;
            }
            return true;
        },

        showMsgStrip: function(aTypes, text) {
            var oView = this.getView();
            var oMs = sap.ui.getCore().byId("msgStrip");

            if (oMs) {
                oMs.destroy();
            }
            this._generateMsgStrip(oView, aTypes, text);
            var viewContent = oView.getContent();
            if( viewContent && viewContent[0] && viewContent[0].scrollTo ) {
                viewContent[0].scrollTo(0);
            }
        },
        _generateMsgStrip: function(oView, aTypes, text) {
            var oVC =  oView.byId("msgArea"),
                oMsgStrip = new sap.m.MessageStrip("msgStrip", {
                    text: text,
                    showCloseButton: true,
                    showIcon: true,
                    type: aTypes
                });

            oVC.addItem(oMsgStrip);
        },
        clearMsgStrip: function(oView) {
            var oVC =  oView.byId("msgArea");
            if( oVC ) oVC.removeAllItems();
        },

        onMessageError: function (message) {
            var dialog = new sap.m.Dialog({
                title: 'Error',
                type: 'Message',
                state: 'Error',
                content: new sap.m.Text({
                    text: message
                }),
                beginButton: new sap.m.Button({
                    text: 'OK',
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
        trimHandle: function(value) {
            return value ? value.split(":")[1] : "";
        }
    });

});
