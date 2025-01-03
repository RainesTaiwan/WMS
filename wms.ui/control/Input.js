/**
 * Created by Administrator on 2017/11/22.
 */
jQuery.sap.declare("com.sap.ewm.control.Input");
jQuery.sap.require("com.sap.ewm.control.InputRenderer");
jQuery.sap.require("sap.m.Input");
jQuery.sap.require("com.sap.ewm.common.dialog.ExtInputValueHelpDialog");
sap.m.Input.extend("com.sap.ewm.control.Input", {
    metadata: {
        properties: {
            "showBrowse": {type: "boolean", group: "Behavior", defaultValue: false},
            "showClear": {type: "boolean", group: "Behavior", defaultValue: false},
            "upperCase": {type: "boolean", group: "Behavior", defaultValue: false},
            "focusSelected": {type: "boolean", group: "Behavior", defaultValue: false},//added by for field focus and select all
            "trim": {type: "boolean", group: "Behavior", defaultValue: true},
            "paramFields": {type: "string", group: "Misc", defaultValue: null},
            "browseType": {type: "string", group: "Misc", defaultValue: null},
            "browsePageMode": {type: "boolean", group: "Behavior", defaultValue: false},
            "browseResultPath": {type: "string", group: "Misc", defaultValue: null},
            "browseUrl": {type: "string", group: "Misc", defaultValue: null},
            "browseKeyColumn": {type: "string", group: "Misc", defaultValue: null},
            "browseTitle": {type: "string", group: "Misc", defaultValue: null},
            "defaultParams": {type: "object", group: "Misc", defaultValue: null}
        },
        aggregations: {
            browseColumns: {type: "sap.ui.table.Column", multiple: true, singularName: "browseColumn"},
            browseStandardColumnListItems: {type : "sap.ui.core.Control", multiple : true, singularName : "item", bindable : "bindable"},
            browseStandardColumns : {type : "sap.m.Column", multiple : true, singularName : "column", dnd : {draggable : true, droppable : true, layout : "Horizontal"} }
        },
        events: {
            "browseTap": {},
            "clearTap": {},
            "browseConfirm": {},
            "prepareBrowseDefaultParams": {}
        }
    },

    init: function () {
        var me = this;
        if (sap.m.Input.prototype.init) {   // check whether superclass has an init() method
            sap.m.Input.prototype.init.apply(this, arguments);  // call super.init()
        }

        this.attachValueHelpRequest(oEvent => {
            if (!this.getEditable()) {
                return;
            }
            if(!this.getBrowseUrl()) {
                return;
            }
            if (!this._browseDialog) {
                if(this.getBrowseType() == "Standard") {
                    this._browseDialog = new com.sap.ewm.common.dialog.StandardValueHelpDialog();
                } else {
                    this._browseDialog = new com.sap.ewm.common.dialog.ExtInputValueHelpDialog();
                }
            }
            var defaultParams = {};
            this.firePrepareBrowseDefaultParams({defaultParams: defaultParams});
            this._browseDialog.onSelect(contexts => {
                if (!contexts || contexts.length == 0) {
                    return;
                }
                var _value = contexts.map(function (oContext) {
                    var record = oContext.getObject();
                    var keyColumn = me.getBrowseKeyColumn();
                    if (!keyColumn) {
                        keyColumn = Object.keys(record)[0];
                    }
                    return record[keyColumn];
                }).join(",");
                this.setValue(_value);
                this.fireLiveChange();
                this.fireBrowseConfirm({contexts: contexts});
            }).show(this, defaultParams);
        });
    },

    renderer: "com.sap.ewm.control.InputRenderer",

    onBeforeRendering: function () {

        if (sap.m.Input.prototype.onBeforeRendering) {
            sap.m.Input.prototype.onBeforeRendering.apply(this, arguments);
        }

        jQuery.sap.byId(this.getId() + "-inner").unbind("keydown", this._inputKeyDown);
        jQuery.sap.byId(this.getId() + "__clrb").unbind("click", this._inputFieldClick);
    },

    onAfterRendering: function () {

        if (sap.m.Input.prototype.onAfterRendering) {
            sap.m.Input.prototype.onAfterRendering.apply(this, arguments);
        }

        // set up for managing clear button
        var that = this;
        jQuery.sap.byId(this.getId() + "-inner").bind("keydown", jQuery.proxy(this._inputKeyDown, this));
        jQuery.sap.byId(this.getId() + "-inner").bind("focus", jQuery.proxy(this._inputFocus, this));
        jQuery.sap.byId(this.getId() + "__clrb").bind("click", jQuery.proxy(this._inputFieldClick, this));

        // show clear button if text exists
        var bShowClear = true;
        if (util.StringUtil.isBlank(this.getValue())) {
            bShowClear = false;
        }
        this._setClearVisibility(bShowClear);
    },

    exit: function () {

        jQuery.sap.byId(this.getId() + "-inner").unbind("keydown", this._inputKeyDown);
        jQuery.sap.byId(this.getId() + "-inner").unbind("focus", this._inputFocus);
        jQuery.sap.byId(this.getId() + "__clrb").unbind("click", this._inputFieldClick);

        if (this._oClearIcon) {
            this._oClearIcon.destroy();
            this._oClearIcon = null;
        }

        if (this._oBrowseIcon) {
            this._oBrowseIcon.destroy();
            this._oBrowseIcon = null;
        }

        if (sap.m.Input.prototype.exit) {
            sap.m.Input.prototype.exit.apply(this, arguments);
        }
        if (this._browseDialog) {
            this._browseDialog.destroy();
            if(this._browseDialog._valueHelpDialog) {
                this._browseDialog._valueHelpDialog.destroy();
            }
        }
    },

    setValue: function (sValue) {
        var sUpdateValue = sValue;
        if (this.getUpperCase() && !util.StringUtil.isBlank(sValue)) {
            sUpdateValue = sValue.toUpperCase();
        }
        if (this.getTrim()) {
            sUpdateValue = jQuery.trim(sUpdateValue);
        }
        if (sap.m.Input.prototype.setValue) {
            sap.m.Input.prototype.setValue.apply(this, [sUpdateValue]);
        }
        if (this.getEditable()) {
            if (util.StringUtil.isBlank(sUpdateValue)) {
                this._setClearVisibility(false);
            } else {
                this._setClearVisibility(true);
            }
        } else {
            this._setClearVisibility(false);
            this._setBrowseVisibility(false);
        }
        $(this).trigger("input");
        this.fireLiveChange();
    },

    getValue: function () {
        var sValue = undefined;
        if (sap.m.Input.prototype.getValue) {
            sValue = sap.m.Input.prototype.getValue.apply(this);
        }
        if (this.getUpperCase() && !util.StringUtil.isBlank(sValue)) {
            sValue = sValue.toUpperCase();
        }
        if (this.getTrim()) {
            sValue = jQuery.trim(sValue);
        }
        return sValue;
    },

    oninput: function (oEvent) {
        this._clearValueState();
        if (sap.m.Input.prototype.oninput) {
            sap.m.Input.prototype.oninput.apply(this, [oEvent]);
        }
    },

    setEnabled: function (bEnabled) {
        if (sap.m.Input.prototype.setEnabled) {
            sap.m.Input.prototype.setEnabled.apply(this, [bEnabled]);
        }

        var bShowClear = bEnabled;
        if (bShowClear) {
            if (util.StringUtil.isBlank(this.getValue())) {
                bShowClear = false;
            }
        }

        if (this._oClearIcon) {
            this._setClearVisibility(bShowClear);
        }
        if (this._oBrowseIcon) {
            this._setBrowseVisibility(bEnabled);
        }
    },

    setEditable: function (bEditable) {
        if (sap.m.Input.prototype.setEditable) {
            sap.m.Input.prototype.setEditable.apply(this, [bEditable]);
        }
        if (this._oClearIcon) {
            this._setClearVisibility(bEditable);
        }
        if (this._oBrowseIcon) {
            this._setBrowseVisibility(bEditable);
        }
    },

    setVisible: function (bVisible) {
        if (sap.m.Input.prototype.setVisible) {
            sap.m.Input.prototype.setVisible.apply(this, [bVisible]);
        }
        this._setClearVisibility(bVisible);
        this._setBrowseVisibility(bVisible);
    },

    _inputKeyDown: function (oEvent) {
        if (this._oClearIcon) {
            var ival = jQuery.sap.delayedCall(32, this, this._setFieldVisibility, [oEvent]);
        }
    },

    _inputFocus: function (oEvent) {
        var sValue = undefined;
        if (sap.m.Input.prototype.getValue) {
            sValue = sap.m.Input.prototype.getValue.apply(this);
        }
        if (this.getFocusSelected() && !util.StringUtil.isBlank(sValue)) {
            sap.m.Input.prototype.selectText.apply(this, [0, sValue.length]);
        }
    },

    _setFieldVisibility: function (oEvent) {

        if (this._oClearIcon) {

            // get value from dom
            var sValue = undefined;
            var oDomInput = jQuery.sap.domById(this.getId() + "-inner");
            if (oDomInput) {
                sValue = oDomInput.value
            }
            if (util.StringUtil.isBlank(sValue)) {
                this._setClearVisibility(false);
            } else {
                this._setClearVisibility(true);
            }
        }
    },

    _inputFieldClick: function (oEvent) {
        this._clearTap(this);
        if (this._oClearIcon) {
            var oInputControl = jQuery("#" + this.getId() + "-inner");
            var sValue = oInputControl.val();
            if (util.StringUtil.isBlank(sValue)) {
                this._setClearVisibility(false);
            }
        }
    },

    _setClearVisibility: function (bShow) {
        if (!bShow) {
            jQuery.sap.byId(this.getId() + "__clrb").hide();
            $("#" + this.getId() + "__clrb").css('visibility','hidden');
            this.bClearVisible = false;
        } else {
            jQuery.sap.byId(this.getId() + "__clrb").show();
            $("#" + this.getId() + "__clrb").css('visibility','visible');
            this.bClearVisible = true;
        }
    },

    _setBrowseVisibility: function (bShow) {
        if (!bShow) {
            jQuery.sap.byId(this.getId() + "__brwseb").hide();
            jQuery.sap.byId(this.getId() + "__brwse").hide();
            this.bBrowseVisible = false;
        } else {
            jQuery.sap.byId(this.getId() + "__brwse").show();
            jQuery.sap.byId(this.getId() + "__brwseb").show();
            this.bBrowseVisible = true;
        }
    },

    _getClearIcon: function () {
        var that = this;

        if (!this._oClearIcon) {

            this._oClearIcon = sap.ui.core.IconPool.createControlByURI({
                id: this.getId() + "__clrb",
                src: sap.ui.core.IconPool.getIconURI("decline"),
                tap: [that._clearTap, that]
            }, sap.m.Image);

            this._oClearIcon.addStyleClass("sapMInputClearIcon");
        }
        return this._oClearIcon;
    },

    _getBrowseIcon: function () {
        var that = this;

        if (!this._oBrowseIcon) {

            this._oBrowseIcon = sap.ui.core.IconPool.createControlByURI({
                id: this.getId() + "__brwse",
                src: sap.ui.core.IconPool.getIconURI("search"),
                tap: [that._browseTap, that]
            }, sap.m.Image);

            this._oBrowseIcon.addStyleClass("sapMInputBrowseIcon");
        }
        return this._oBrowseIcon;
    },

    _clearTap: function (oEvent) {
        this.setValue("");
        var elem = sap.ui.getCore().byId(this.getId());
        if (elem) {
            elem.focus();
        }
        this.fireClearTap();
    },

    _browseTap: function (oEvent) {
        if (this._oBrowseIcon) {
            this.fireBrowseTap();
        }
    },

    _clearValueState: function () {
        var valueState = sap.m.Input.prototype.getValueState.apply(this);
        if (valueState === sap.ui.core.ValueState.Error) {
            sap.m.Input.prototype.setValueState.apply(this, [sap.ui.core.ValueState.None]);
            sap.m.Input.prototype.setValueStateText.apply(this, [""]);
        }
    }
});
