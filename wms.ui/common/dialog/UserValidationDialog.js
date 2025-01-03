/**
 * Created by Administrator on 2017/10/23.
 */
sap.ui.define([
    'jquery.sap.global',
    'sap/ui/core/Fragment',
    "sap/ui/base/Object",
    'sap/ui/model/Filter',
    'sap/ui/model/json/JSONModel'
], function(jQuery, Fragment, Object, Filter, JSONModel) {
    "use strict";

    return Object.extend("com.sap.ewm.common.dialog.UserValidationDialog", {

        open : function (oView, validationConfirmCallBack, validationCancelCallBack, reqScope) {

            // create value help dialog
            if (!this._userValidationDialog) {
                this._userValidationDialog = sap.ui.xmlfragment(
                    "UserValidationDialog",
                    "com.sap.ewm.common.dialog.UserValidationDialog",
                    this
                );
                jQuery.sap.syncStyleClass(oView.getController().getOwnerComponent().getContentDensityClass(), oView, this._userValidationDialog);
                oView.addDependent(this._userValidationDialog);
            }
            this._userValidationDialog.setModel(new sap.ui.model.json.JSONModel());
            this.validationConfirmCallBack = validationConfirmCallBack;
            this.validationCancelCallBack = validationCancelCallBack;
            this.reqScope = reqScope;
            this._userValidationDialog.open();
            //sap.ui.core.Fragment.byId("UserValidationDialog", "passwordField").setType("Password");
        },

        onContainConfirm : function (evt) {
            var me = this, oModel = me._userValidationDialog.getModel();
            if( !me.validateRequiredField() ) return;
            me._userValidationDialog.setBusy(true);
            if( me.validationConfirmCallBack ) {
                //me.reqFunction.call(me.reqArgs);
                me.validationConfirmCallBack.call( this.reqScope ? this.reqScope : me, {"username": oModel.getProperty("/username"), "password": oModel.getProperty("/password")} );
            }
            me.destroy();
        },

        onContainClose : function (evt) {
            var me = this;
            if( me.validationCancelCallBack ) {
                me.validationCancelCallBack.call( this.reqScope ? this.reqScope : me );
            }
            me.destroy();
        },
        destroy: function() {
            var me = this;
            if( me._userValidationDialog ) {
                me._userValidationDialog.destroy();
                me._userValidationDialog = null;
            }
        },
        validateRequiredField: function() {
            var me = this;
            var usernameField = sap.ui.core.Fragment.byId("UserValidationDialog", "usernameField");
            var passwordField = sap.ui.core.Fragment.byId("UserValidationDialog", "passwordField");
            var oBundle = me._userValidationDialog.getModel("i18n").getResourceBundle();
            var validate = true;

            if( util.StringUtil.isBlank(usernameField.getValue()) ) {
                util.CustomUtil.fieldValidate( usernameField, oBundle.getText("common.NoCondition"), sap.ui.core.ValueState.Error );
                validate = false;
            }
            if( util.StringUtil.isBlank(passwordField.getValue()) ) {
                util.CustomUtil.fieldValidate( passwordField, oBundle.getText("common.NoCondition"), sap.ui.core.ValueState.Error );
                validate = false;
            }
            return validate;
        }
    });

});