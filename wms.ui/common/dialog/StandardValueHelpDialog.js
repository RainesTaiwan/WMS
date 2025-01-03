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

    return Object.extend("com.sap.ewm.common.dialog.StandardValueHelpDialog", {

        _selectHandler: null,

        show : function (sInput, defaultParams) {
            var me = this;
            var sInputValue = sInput.getValue();

            this.inputId = sInput.getId();

            var url = sInput.getBrowseUrl();
            var inputValue = sInput.getValue();
            var columnListItems = sInput.getBrowseStandardColumnListItems();
            var columns = sInput.getBrowseStandardColumns();
            var keyColumn = sInput.getBrowseKeyColumn();
            var isMulti = sInput.getMetadata().getName() === "sap.m.MultiInput" ? true : false;
            var rootNode = sInput.getBrowseResultPath();
            this.filterKey = keyColumn;
            // create value help dialog
            if (!this._valueHelpDialog) {
                this._valueHelpDialog = sap.ui.xmlfragment(
                    "com.sap.ewm.common.dialog.StandardValueHelpDialog",
                    this
                );
                var sContentDensityClass;
                if (!sap.ui.Device.support.touch) {
                    sContentDensityClass = "sapUiSizeCompact";
                } else {
                    sContentDensityClass = "sapUiSizeCozy";
                }
                jQuery.sap.syncStyleClass(sContentDensityClass, sInput, this._valueHelpDialog);

                me._valueHelpDialog.bindAggregation("items", {
                    path : "/records",
                    template : new sap.m.ColumnListItem({
                        type : sap.m.ListType.Navigation,
                        cells: columnListItems
                    })
                });

                var cells = [];
                if (columns.length == 0 && this._valueHelpDialog.getColumns().length == 0 && keyColumn) {
                    this._valueHelpDialog.addColumn(new sap.m.Column(
                        {
                            header: new sap.m.Text({ text: keyColumn }),
                            hAlign: "Center",
                            template:new sap.m.Text({
                                text: "{" + keyColumn + "}"
                            })
                        }
                    ));
                    cells.push(new sap.m.Text({
                        text: "{" + keyColumn + "}"
                    }));
                } else {
                    for (var i = 0; i < columns.length; i++) {
                        this._valueHelpDialog.addColumn(columns[i]);
                    }
                }
            }
            this._valueHelpDialog.setMultiSelect(isMulti);
            var oBundle = sInput.getModel("i18n").getResourceBundle();
            var selectText = oBundle.getText("common.Select");
            this._valueHelpDialog.setModel(sInput.getModel("i18n"), "i18n");
            this._valueHelpDialog.setTitle(selectText + oBundle.getText(sInput.getBrowseTitle()));
            // this._valueHelpDialog.destroyColumns();

            // process data
            if (!rootNode) rootNode = "value";
            var rootNodeArray = rootNode.split(".");
            var oModel = new sap.ui.model.json.JSONModel();
            me._valueHelpDialog.setModel(oModel);
            this._valueHelpDialog.setBusy(true);
            var successAction = function (respData) {
                // create a filter for the binding
                if( me._valueHelpDialog.getBinding("items") ) me._valueHelpDialog.getBinding("items").filter([new Filter(
                    keyColumn,
                    sap.ui.model.FilterOperator.Contains, sInputValue
                )]);

                var tempData;
                for (var i = 0; i < rootNodeArray.length; i++) {
                    var node = rootNodeArray[i];
                    if (i === 0) {
                        tempData = respData[node];
                        continue;
                    }
                    tempData = tempData[node];
                }
                var data = {"records": me.processData(tempData)};
                oModel.setData(data);
                me._valueHelpDialog.setModel(oModel);
                me._valueHelpDialog.setBusy(false);
            };
            var errorAction = function (errorCode, errorMsg) {
                me._valueHelpDialog.setBusy(false);
                util.CustomUtil.errorCallbackShow(errorCode, errorMsg);
            };

            // 新增查詢參數
            var params = defaultParams || {};
            if(sInput.getParamFields() && sInput.getModel()) {

                var paramFields = sInput.getParamFields().split(",");
                for(var i = 0; i < paramFields.length; i++) {
                    params[paramFields[i]] = sInput.getModel().getProperty("/" + paramFields[i]);
                }
            }
            if(sInput.getDefaultParams() && !jQuery.isEmptyObject(sInput.getDefaultParams())) {
                jQuery.extend(params, sInput.getDefaultParams());
            }
            util.CustomUtil.restfulApiRequest(url, "GET", params, successAction, errorAction, true);

            // open value help dialog filtered by the input value
            this._valueHelpDialog.open(sInputValue);
        },

        _handleValueHelpSearch : function (evt) {
            var sValue = evt.getParameter("value");

            var oFilter = new Filter(
                this.filterKey,

                sap.ui.model.FilterOperator.Contains, sValue
            );
            if( evt.getSource().getBinding("items") ) evt.getSource().getBinding("items").filter([oFilter]);
        },

        _handleValueHelpClose : function (evt) {
            var me = this;

            var aContexts = evt.getParameter("selectedContexts");
            if (aContexts && aContexts.length) {
                if (this._selectHandler) {
                    this._selectHandler(aContexts);
                }
            }

            if( evt.getSource().getBinding("items") ) evt.getSource().getBinding("items").filter([]);
        },
        onSelect: function (handler) {
            this._selectHandler = handler;
            return this;
        },
        /**
         * 將數據轉為String,避免filter報錯
         * @param data
         */
        processData: function (data) {
            if (!data) return [];
            data.forEach(function (record) {
                for (var key in record) {
                    var value = record[key];
                    if (value == null) {
                        continue;
                    }
                    if (typeof value != "string") record[key] = value.toString();
                }
            });
            return data;
        }
    });

});
