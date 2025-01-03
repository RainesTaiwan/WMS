/**
 * Created by Administrator on 2017/10/23.
 */
sap.ui.define([
    'jquery.sap.global',
    'sap/ui/core/Fragment',
    "sap/ui/base/Object",
    'sap/ui/model/Filter',
    'sap/ui/model/json/JSONModel'
], function (jQuery, Fragment, Object, Filter, JSONModel) {
    "use strict";

    return Object.extend("com.sap.ewm.common.dialog.ExtInputValueHelpDialog", {

        _selectHandler: null,

        _dialogId: null,

        _pageMode: false,

        show: function (sInput, defaultParams) {
            var me = this;
            me._dialogId = sInput.getId() + "--extInputValueHelpDialog";
            var url = sInput.getBrowseUrl();
            var inputValue = sInput.getValue();
            var columns = sInput.getBrowseColumns();
            var keyColumn = sInput.getBrowseKeyColumn();
            this._pageMode = sInput.getBrowsePageMode();
            var isMulti = sInput.getMetadata().getName() === "sap.m.MultiInput" ? true : false;
            var rootNode = sInput.getBrowseResultPath();
            // create value help dialog
            if (!this._valueHelpDialog) {
                this._valueHelpDialog = sap.ui.xmlfragment(
                    me._dialogId,
                    this._pageMode ? "com.sap.ewm.common.dialog.ExtInputValueServerHelpDialog" : "com.sap.ewm.common.dialog.ExtInputValueHelpDialog",
                    this
                );
            }
            var sContentDensityClass;
            if (!sap.ui.Device.support.touch) {
                sContentDensityClass = "sapUiSizeCompact";
            } else {
                sContentDensityClass = "sapUiSizeCozy";
            }
            jQuery.sap.syncStyleClass(sContentDensityClass, sInput, this._valueHelpDialog);

            var extInputValueHelpDialogGrid = sap.ui.core.Fragment.byId(me._dialogId, "extInputValueHelpDialogGrid");
            extInputValueHelpDialogGrid.setSelectionMode(isMulti ? "MultiToggle" : "Single");
            extInputValueHelpDialogGrid.setSelectionBehavior(isMulti ? "Row" : "RowOnly");

            var oBundle = sInput.getModel("i18n").getResourceBundle();
            var selectText = oBundle.getText("請選擇");
            this._valueHelpDialog.setModel(sInput.getModel("i18n"), "i18n");
            this._valueHelpDialog.setTitle(selectText + oBundle.getText(sInput.getBrowseTitle()));
            //extInputValueHelpDialogGrid.destroyColumns();
            if (columns.length == 0 && extInputValueHelpDialogGrid.getColumns().length == 0 && keyColumn) {
                extInputValueHelpDialogGrid.addColumn(new sap.ui.table.Column(
                    {
                        label: keyColumn,
                        filterProperty: keyColumn,
                        sortProperty: keyColumn,
                        template: new sap.m.Text({
                            text: "{" + keyColumn + "}"
                        })
                    }
                ));
            } else {
                for (var i = 0; i < columns.length; i++) {
                    extInputValueHelpDialogGrid.addColumn(columns[i]);
                }
            }

            if (!rootNode) rootNode = "value";
            var rootNodeArray = rootNode.split(".");
            var oModel = new sap.ui.model.json.JSONModel();
            oModel.setProperty("/confirmButtonVisible", false);
            if(sInput.getDefaultParams() && !jQuery.isEmptyObject(sInput.getDefaultParams())) {
                jQuery.extend(defaultParams, sInput.getDefaultParams());
            }
            if(this._pageMode) {
                this.clearAllFilters();
                extInputValueHelpDialogGrid.load({
                    url: url,
                    method: "GET",
                    param: defaultParams
                });
            } else {
                extInputValueHelpDialogGrid.setBusy(true);
                var successAction = function (respData) {
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

                    if (isMulti) {
                        //TODO for default select
                        if (data) {
                            var records = data["records"];
                            for (var i = 0; i < records.length; i++) {
                                var record = records[i];
                                var values = inputValue ? [] : inputValue.split(",");
                                for (var j = 0; j < values.length; j++) {
                                    var value = values[j];
                                    if (value && value.length > 0) {
                                        values[j] = util.CustomUtil.trimStr(value);
                                    }
                                }
                                if (jQuery.inArray(record[me.callBackKey], values) > -1) {
                                    extInputValueHelpDialogGrid.addSelectionInterval(i, i);
                                }
                            }
                        }
                        oModel.setProperty("/confirmButtonVisible", true);
                    } else {
                        oModel.setProperty("/confirmButtonVisible", false);
                        var searchField = sap.ui.core.Fragment.byId(me._dialogId, "searchField");
                        searchField.setValue(inputValue);
                        searchField.onSearch();
                    }

                    extInputValueHelpDialogGrid.setBusy(false);
                };
                var errorAction = function (errorCode, errorMsg) {
                    extInputValueHelpDialogGrid.setBusy(false);
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
            }

            if (oModel) {
                this._valueHelpDialog.setModel(oModel);
                var searchField = sap.ui.core.Fragment.byId(me._dialogId, "searchField");
                searchField.setValue(inputValue);
            }

            // open value help dialog filtered by the input value
            this.clearMsgArea();
            if(!this._pageMode) {
                this.clearAllFilters();
            }
            this._valueHelpDialog.open();
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
        },
        onTableCellClick: function (evt) {
            var me = this;
            var ctx = evt.getParameter("rowBindingContext");
            var extInputValueHelpDialogGrid = sap.ui.core.Fragment.byId(me._dialogId, "extInputValueHelpDialogGrid");
            if (extInputValueHelpDialogGrid.getSelectionMode() !== "Single") {
                return;
            }
            if (!ctx) return;
            if (this._selectHandler) {
                this._selectHandler([ctx]);
            }
            this._valueHelpDialog.close();
        },
        onConfirm: function (evt) {
            var me = this;
            var extInputValueHelpDialogGrid = sap.ui.core.Fragment.byId(me._dialogId, "extInputValueHelpDialogGrid");
            var oSelectedIndices = extInputValueHelpDialogGrid.getSelectedIndices();
            var aContexts = [];
            for (var i = 0; i < oSelectedIndices.length; i++) {
                var selectedContext = extInputValueHelpDialogGrid.getContextByIndex(oSelectedIndices[i]);
                aContexts.push(selectedContext);
            }
            if (this._selectHandler) {
                this._selectHandler([aContexts]);
            }
            this._valueHelpDialog.close();
        },
        onClose: function (evt) {
            this._valueHelpDialog.close();
        },
        showMsgStrip: function (aTypes, text) {
            var me = this;
            var oMs = sap.ui.getCore().byId("msgStrip");

            if (oMs) {
                oMs.destroy();
            }
            this._generateMsgStrip(aTypes, text);
        },
        _generateMsgStrip: function (aTypes, text) {
            var me = this;
            var oVC = sap.ui.core.Fragment.byId(me._dialogId, "errorMsgArea"),
                oMsgStrip = new sap.m.MessageStrip("msgStrip", {
                    text: text,
                    showCloseButton: false,
                    showIcon: true,
                    type: aTypes
                });

            oVC.addItem(oMsgStrip);
        },
        clearMsgArea: function () {
            var me = this;
            var oVC = sap.ui.core.Fragment.byId(me._dialogId, "errorMsgArea");
            if (oVC) oVC.removeAllItems();
        },
        onGridFilter: function () {
            this.clearMsgArea();
        },

        filterChange: function (oEvent) {
            var sQuery = oEvent.getSource().getValue();
            if(this._pageMode) {
                return;
            }
            this._oGlobalFilter = null;
            if (sQuery && sQuery.length > 0) {
                this.globalFilters = [];
                this.initFilters(sQuery);
                this._oGlobalFilter = new sap.ui.model.Filter(this.globalFilters, false)
            }
            this._filter();
        },
        filterGlobally: function (oEvent) {
            var me = this, sQuery = oEvent.getParameter("query");
            if(this._pageMode) {
                var extInputValueHelpDialogGrid = sap.ui.core.Fragment.byId(me._dialogId, "extInputValueHelpDialogGrid");
                extInputValueHelpDialogGrid.loadAppendParam({"globalQuery" : sQuery});
                return;
            }
            this._oGlobalFilter = null;

            if (sQuery) {
                this.globalFilters = [];
                this.initFilters(sQuery);
                this._oGlobalFilter = new sap.ui.model.Filter(this.globalFilters, false)
            }

            this._filter();
        },

        _filter: function () {
            var me = this;
            var oFilter = null;

            if (this._oGlobalFilter) {
                oFilter = new sap.ui.model.Filter([this._oGlobalFilter], true);
            } else if (this._oGlobalFilter) {
                oFilter = this._oGlobalFilter;
            }

            sap.ui.core.Fragment.byId(me._dialogId, "extInputValueHelpDialogGrid").getBinding("rows").filter(oFilter, "Application");
        },

        initFilters: function (sQuery) {
            var me = this;
            var oTable = sap.ui.core.Fragment.byId(me._dialogId, "extInputValueHelpDialogGrid");
            var aColumns = oTable.getColumns();
            for (var i = 0; i < aColumns.length; i++) {
                var columnBinding = aColumns[i].getTemplate().getBindingPath("text");
                if (columnBinding) {
                    this.globalFilters.push(new sap.ui.model.Filter(columnBinding, sap.ui.model.FilterOperator.Contains, sQuery));
                }
            }
        },

        clearAllFilters: function () {
            var me = this;
            var oTable = sap.ui.core.Fragment.byId(me._dialogId, "extInputValueHelpDialogGrid");

            if(this._pageMode) {
                oTable.clearAllFilters();
                return;
            }
            var aColumns = oTable.getColumns();
            for (var i = 0; i < aColumns.length; i++) {
                oTable.filter(aColumns[i], null);
            }
        },

        clearAllSorters: function () {
            var me = this;
            var oTable = sap.ui.core.Fragment.byId(me._dialogId, "extInputValueHelpDialogGrid");
            oTable.clearAllSorters();
        }
    });

});
