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

    return Object.extend("com.sap.ewm.common.dialog.ColumnFilterValueHelpDialog", {
        callbackFunction:null,
        /**
         * 開啟彈出框視窗
         * @param oEvent
         * @param oView
         * @param url
         * @param queryParam
         * @param rootNode
         * @param columns
         * @param oModel
         * @param callbackFunction 回Call函式接收選中行數據為參數
         */
        open : function (oEvent, oView, url, queryParam, rootNode, columns, oModel, callbackFunction) {
            var me = this;
            var sInput = oEvent.getSource();
            var sInputValue = sInput.getValue();

            this.oView = oView;
            this.inputId = sInput.getId();

            me.callbackFunction = callbackFunction;
            var  logicNo, columnsStr, valueHelpTitle;
            var customDatas = sInput.getCustomData();
            if( customDatas ) {
                for( var i = 0; i < customDatas.length; i++ ) {
                    var customData = customDatas[i];
                    switch( customData.getKey().toUpperCase() ) {
                        case 'valueHelpFilterKey'.toUpperCase() :
                            this.filterKey = customData.getValue();
                            break;
                        case 'callBackKey'.toUpperCase() :
                            this.callBackKey = customData.getValue();
                            break;
                        case 'logicNo'.toUpperCase() :
                            logicNo = customData.getValue();
                            break;
                        case 'columns'.toUpperCase() :
                            columnsStr = customData.getValue();
                            break;
                        case 'valueHelpTitle'.toUpperCase() :
                            valueHelpTitle = customData.getValue();
                            break;
                    }
                }
            }
            // create value help dialog
            if (!this._valueHelpDialog) {
                this._valueHelpDialog = sap.ui.xmlfragment(
                    "columnFilterValueHelpDialog",
                    "com.sap.ewm.common.dialog.ColumnFilterValueHelpDialog",
                    this
                );
                jQuery.sap.syncStyleClass(oView.getController().getOwnerComponent().getContentDensityClass(), oView, this._valueHelpDialog);
                oView.addDependent(this._valueHelpDialog);
            }
            var oBundle = oView.getModel("i18n").getResourceBundle();
            var selectText = oBundle.getText("common.Select");
            this._valueHelpDialog.setTitle( selectText + oBundle.getText(valueHelpTitle) );

            var columnFilterValueHelpDialogGrid = sap.ui.core.Fragment.byId("columnFilterValueHelpDialog", "columnFilterValueHelpDialogGrid");
            columnFilterValueHelpDialogGrid.setSelectionMode(sInput.getMetadata().getName() === "sap.m.MultiInput" ? "MultiToggle" : "Single");
            columnFilterValueHelpDialogGrid.setSelectionBehavior(sInput.getMetadata().getName() === "sap.m.MultiInput" ? "Row" : "RowOnly");

                /**
             * 	columns
             * eg:[{
		   	  width: "8em",
		   	  styleClass:"",
		   	  label: new sap.m.Text({ text: "shopOrder" }),
		   	  template:new sap.m.Text({
            	        	text: "{SHOP_ORDER}"
            	        })
		     }]
             or columnsStr in input custom data: SHOP_ORDER|shopOrder|8em
             */
            columnFilterValueHelpDialogGrid.destroyColumns();
            if( columns && columns.length > 0 ) {
                for( var i = 0; i < columns.length; i++ ) {
                    columnFilterValueHelpDialogGrid.addColumn(new sap.ui.table.Column(
                        columns[i]
                    ));
                }
            } else if( columnsStr ) {
                var splitData = columnsStr.split(";");
                if( splitData ) {
                    columns = [];
                    for( var i = 0; i < splitData.length; i++ ) {
                        var columnsSplitData = splitData[i].split("|");
                        var columnObj = {};
                        columnObj.label = columnsSplitData[1] ? "{i18n>" + columnsSplitData[1] + "}" : "{i18n>" + columnsSplitData[0] + "}";
                        if( columnsSplitData[2] ) {
                            columnObj.width = columnsSplitData[2];
                        }
                        columnObj.template = new sap.m.Text({
                            text: "{"+columnsSplitData[0]+"}"
                        });
                        columnObj.filterProperty = columnsSplitData[0];
                        //columnObj.filterProperty = columnsSplitData[0];
                        columnFilterValueHelpDialogGrid.addColumn(new sap.ui.table.Column(
                            columnObj
                        ));
                    }
                }
            } else {
                columnFilterValueHelpDialogGrid.addColumn(new sap.ui.table.Column(
                    {
                        label: this.filterKey,
                        filterProperty: columnsSplitData[0],
                        template:new sap.m.Text({
                            text: "{" + this.filterKey + "}"
                        })
                    }
                ));
            }
            //set model
            if( !oModel ) {

                columnFilterValueHelpDialogGrid.setBusy(true);

                if( !rootNode ) rootNode = "value";
                var rootNodeArray = rootNode.split(".");
                var oModel = new sap.ui.model.json.JSONModel();
                var successAction = function(respData) {
                    var tempData;
                    for( var i = 0; i <  rootNodeArray.length; i++ ) {
                        var node = rootNodeArray[i];
                        if( i === 0 ) {
                            tempData = respData[node];
                            continue;
                        }
                        tempData = tempData[node];
                    }
                    var data = {"records": me.processData( tempData )};
                    oModel.setData(data);

                    if( sInput.getMetadata().getName() === "sap.m.MultiInput" ) {
                        //TODO for default select
                        if( data ) {
                             var records = data["records"];
                             for( var i = 0; i < records.length; i++ ) {
                                 var record = records[i];
                                 var values = sInputValue.split(",");
                                 for( var j = 0; j < values.length; j++ ) {
                                     var value = values[j];
                                     if( value && value.length > 0 ){
                                         values[j] = util.CustomUtil.trimStr(value);
                                     }
                                 }
                                 if( jQuery.inArray(record[me.callBackKey], values) > -1 ) {
                                    columnFilterValueHelpDialogGrid.addSelectionInterval(i, i);
                                 }
                             }
                         }
                        oModel.setProperty("/confirmButtonVisible", true);
                    } else {
                        oModel.setProperty("/confirmButtonVisible", false);
                        var searchField = sap.ui.core.Fragment.byId("columnFilterValueHelpDialog", "searchField");
                        searchField.setValue(sInputValue);
                        searchField.onSearch();
                    }

                    columnFilterValueHelpDialogGrid.setBusy(false);
                };
                var errorAction = function(errorCode, errorMsg) {
                    columnFilterValueHelpDialogGrid.setBusy(false);
                    util.CustomUtil.errorCallbackShow(errorCode, errorMsg);
                };
                util.CustomUtil.restfulApiRequest(url, "GET", queryParam, successAction, errorAction, true );
            }

            if (oModel) {
                this._valueHelpDialog.setModel(oModel);
                var searchField = sap.ui.core.Fragment.byId("columnFilterValueHelpDialog", "searchField");
                searchField.setValue(sInputValue);
            }

            // open value help dialog filtered by the input value
            this.clearMsgArea();
            this.clearAllFilters();
            this._valueHelpDialog.open();
        },

        openPost : function (oEvent, oView, url, queryParam, rootNode, columns, oModel) {
            var me = this;
            var sInput = oEvent.getSource();
            var sInputValue = sInput.getValue();

            this.oView = oView;
            this.inputId = sInput.getId();

            var  logicNo, columnsStr, valueHelpTitle;
            var customDatas = sInput.getCustomData();
            if( customDatas ) {
                for( var i = 0; i < customDatas.length; i++ ) {
                    var customData = customDatas[i];
                    switch( customData.getKey().toUpperCase() ) {
                        case 'valueHelpFilterKey'.toUpperCase() :
                            this.filterKey = customData.getValue();
                            break;
                        case 'callBackKey'.toUpperCase() :
                            this.callBackKey = customData.getValue();
                            break;
                        case 'logicNo'.toUpperCase() :
                            logicNo = customData.getValue();
                            break;
                        case 'columns'.toUpperCase() :
                            columnsStr = customData.getValue();
                            break;
                        case 'valueHelpTitle'.toUpperCase() :
                            valueHelpTitle = customData.getValue();
                            break;
                    }
                }
            }
            // create value help dialog
            if (!this._valueHelpDialog) {
                this._valueHelpDialog = sap.ui.xmlfragment(
                    "columnFilterValueHelpDialog",
                    "com.sap.ewm.common.dialog.ColumnFilterValueHelpDialog",
                    this
                );
                jQuery.sap.syncStyleClass(oView.getController().getOwnerComponent().getContentDensityClass(), oView, this._valueHelpDialog);
                oView.addDependent(this._valueHelpDialog);
            }
            var oBundle = oView.getModel("i18n").getResourceBundle();
            var selectText = oBundle.getText("common.Select");
            this._valueHelpDialog.setTitle( selectText + oBundle.getText(valueHelpTitle) );

            var columnFilterValueHelpDialogGrid = sap.ui.core.Fragment.byId("columnFilterValueHelpDialog", "columnFilterValueHelpDialogGrid");
            columnFilterValueHelpDialogGrid.setSelectionMode(sInput.getMetadata().getName() === "sap.m.MultiInput" ? "MultiToggle" : "Single");
            columnFilterValueHelpDialogGrid.setSelectionBehavior(sInput.getMetadata().getName() === "sap.m.MultiInput" ? "Row" : "RowOnly");

            /**
             * 	columns
             * eg:[{
		   	  width: "8em",
		   	  styleClass:"",
		   	  label: new sap.m.Text({ text: "shopOrder" }),
		   	  template:new sap.m.Text({
            	        	text: "{SHOP_ORDER}"
            	        })
		     }]
             or columnsStr in input custom data: SHOP_ORDER|shopOrder|8em
             */
            columnFilterValueHelpDialogGrid.destroyColumns();
            if( columns && columns.length > 0 ) {
                for( var i = 0; i < columns.length; i++ ) {
                    columnFilterValueHelpDialogGrid.addColumn(new sap.ui.table.Column(
                        columns[i]
                    ));
                }
            } else if( columnsStr ) {
                var splitData = columnsStr.split(";");
                if( splitData ) {
                    columns = [];
                    for( var i = 0; i < splitData.length; i++ ) {
                        var columnsSplitData = splitData[i].split("|");
                        var columnObj = {};
                        columnObj.label = columnsSplitData[1] ? "{i18n>" + columnsSplitData[1] + "}" : "{i18n>" + columnsSplitData[0] + "}";
                        if( columnsSplitData[2] ) {
                            columnObj.width = columnsSplitData[2];
                        }
                        columnObj.template = new sap.m.Text({
                            text: "{"+columnsSplitData[0]+"}"
                        });
                        columnObj.filterProperty = columnsSplitData[0];
                        //columnObj.filterProperty = columnsSplitData[0];
                        columnFilterValueHelpDialogGrid.addColumn(new sap.ui.table.Column(
                            columnObj
                        ));
                    }
                }
            } else {
                columnFilterValueHelpDialogGrid.addColumn(new sap.ui.table.Column(
                    {
                        label: this.filterKey,
                        filterProperty: this.filterKey,
                        template:new sap.m.Text({
                            text: "{" + this.filterKey + "}"
                        })
                    }
                ));
            }
            //set model
            if( !oModel ) {

                columnFilterValueHelpDialogGrid.setBusy(true);

                if( !rootNode ) rootNode = "value";
                var rootNodeArray = rootNode.split(".");
                var oModel = new sap.ui.model.json.JSONModel();
                var successAction = function(respData) {
                    var tempData;
                    for( var i = 0; i <  rootNodeArray.length; i++ ) {
                        var node = rootNodeArray[i];
                        if( i === 0 ) {
                            tempData = respData[node];
                            continue;
                        }
                        tempData = tempData[node];
                    }
                    var data = {"records": me.processData( tempData )};
                    oModel.setData(data);

                    if( sInput.getMetadata().getName() === "sap.m.MultiInput" ) {
                        //TODO for default select
                        if( data ) {
                            var records = data["records"];
                            for( var i = 0; i < records.length; i++ ) {
                                var record = records[i];
                                var values = sInputValue.split(",");
                                for( var j = 0; j < values.length; j++ ) {
                                    var value = values[j];
                                    if( value && value.length > 0 ){
                                        values[j] = util.CustomUtil.trimStr(value);
                                    }
                                }
                                if( jQuery.inArray(record[me.callBackKey], values) > -1 ) {
                                    columnFilterValueHelpDialogGrid.addSelectionInterval(i, i);
                                }
                            }
                        }
                        oModel.setProperty("/confirmButtonVisible", true);
                    } else {
                        oModel.setProperty("/confirmButtonVisible", false);
                        var searchField = sap.ui.core.Fragment.byId("columnFilterValueHelpDialog", "searchField");
                        searchField.setValue(sInputValue);
                        searchField.onSearch();
                    }

                    columnFilterValueHelpDialogGrid.setBusy(false);
                };
                var errorAction = function(errorCode, errorMsg) {
                    columnFilterValueHelpDialogGrid.setBusy(false);
                    util.CustomUtil.errorCallbackShow(errorCode, errorMsg);
                };
                util.CustomUtil.restfulApiRequest(url, "POST", queryParam, successAction, errorAction, true );
            }

            if (oModel) {
                this._valueHelpDialog.setModel(oModel);
            }

            // open value help dialog filtered by the input value
            this.clearMsgArea();
            this.clearAllFilters();
            this._valueHelpDialog.open();
        },

        /**
         * 將數據轉為String,避免filter報錯
         * @param data
         */
        processData: function( data ) {
            if( !data ) return [];
            data.forEach(function( record ){
                for( var key in record ) {
                    var value = record[key];
                    if (value == null) {
                        continue;
                    }
                    if( typeof value != "string" ) record[key] = value.toString();
                }
            });
            return data;
        },
        onTableCellClick: function(evt) {
            var me = this;
            var productInput = this.oView.byId(this.inputId);
            if( productInput == null ) productInput = sap.ui.getCore().byId(this.inputId);
            if( productInput.getMetadata().getName() !== "sap.m.MultiInput" ) {
                var ctx = evt.getParameter("rowBindingContext");
                if( !ctx ) return;
                var path = ctx.getPath();
                var selectedRecord = ctx.getProperty(path);
                productInput.setValue( selectedRecord[me.callBackKey] );
                productInput.fireEvent( "liveChange" );
                if(this.callbackFunction) {
                    this.callbackFunction.call( this, productInput, [ctx] );
                }
                var oCon = me.oView.getController();
                if( oCon && oCon.valueHelpCallBack ) {
                    oCon.valueHelpCallBack.call( oCon, productInput, [ctx] );
                }
                me._valueHelpDialog.close();
                productInput.focus();
                //productInput.selectText(0, productInput.getValue().length);
            }
        },
        onConfirm : function (evt) {
            var me = this;
            var columnFilterValueHelpDialogGrid = sap.ui.core.Fragment.byId("columnFilterValueHelpDialog", "columnFilterValueHelpDialogGrid");
            var oSelectedIndices = columnFilterValueHelpDialogGrid.getSelectedIndices();
            var aContexts = [];
            for( var i = 0; i < oSelectedIndices.length; i++ ) {
                var selectedContext = columnFilterValueHelpDialogGrid.getContextByIndex(oSelectedIndices[i]);
                aContexts.push(selectedContext);
            }
            var productInput = this.oView.byId(this.inputId);
            if( productInput == null ) productInput = sap.ui.getCore().byId(this.inputId);
            /*if (aContexts && aContexts.length) {*/
                var tokens = [];
                if( productInput.getMetadata().getName() === "sap.m.MultiInput" ){
                    //aContexts.map(function(oContext) {
                    //    var value = oContext.getObject()[me.callBackKey];
                    //    tokens.push(new sap.m.Token({key: value, text: value}));
                    //});
                    //productInput.setTokens(tokens);
                    productInput.setValue(aContexts.map(function(oContext) {
                        return value = oContext.getObject()[me.callBackKey];
                    }).join(","));
                } else {
                    var value = aContexts.map(function(oContext) {
                        return value = oContext.getObject()[me.callBackKey];
                    }).join(",");
                    productInput.setValue(value);
                }
            /*} else {
                this.showMsgStrip("Warning", "請先選擇！");
                return;
            }*/

            var oCon = this.oView.getController();
            if( oCon && oCon.valueHelpCallBack ) {
                oCon.valueHelpCallBack.call( oCon, productInput, aContexts );
            }
            //if( evt.getSource().getBinding("items") ) evt.getSource().getBinding("items").filter([]);
            this._valueHelpDialog.close();
            productInput.fireEvent( "liveChange" );
            productInput.focus();
            //productInput.selectText(0, productInput.getValue().length);
        },
        onClose : function( evt ) {
            this._valueHelpDialog.close();
        },
        showMsgStrip: function(aTypes, text) {
            var oMs = sap.ui.getCore().byId("msgStrip");

            if (oMs) {
                oMs.destroy();
            }
            this._generateMsgStrip(aTypes, text);
        },
        _generateMsgStrip: function(aTypes, text) {
            var oVC =  sap.ui.core.Fragment.byId("columnFilterValueHelpDialog", "errorMsgArea"),
                oMsgStrip = new sap.m.MessageStrip("msgStrip", {
                    text: text,
                    showCloseButton: false,
                    showIcon: true,
                    type: aTypes
                });

            oVC.addItem(oMsgStrip);
        },
        clearMsgArea: function() {
            var oVC =  sap.ui.core.Fragment.byId("columnFilterValueHelpDialog", "errorMsgArea");
            if( oVC ) oVC.removeAllItems();
        },
        onGridFilter: function() {
            this.clearMsgArea();
        },

        filterChange : function(oEvent) {
        var sQuery = oEvent.getSource().getValue();
          this._oGlobalFilter = null;
            if (sQuery && sQuery.length > 0) {
                this.globalFilters = [];
                this.initFilters(sQuery);
                this._oGlobalFilter = new sap.ui.model.Filter(this.globalFilters, false)
            }
          this._filter();
       },
        filterGlobally : function(oEvent) {
            var sQuery = oEvent.getParameter("query");
            this._oGlobalFilter = null;

            if (sQuery) {
                this.globalFilters = [];
                this.initFilters(sQuery);
                this._oGlobalFilter = new sap.ui.model.Filter(this.globalFilters, false)
            }

            this._filter();
        },

        _filter : function () {
            var oFilter = null;

            if (this._oGlobalFilter) {
                oFilter = new sap.ui.model.Filter([this._oGlobalFilter], true);
            } else if (this._oGlobalFilter) {
                oFilter = this._oGlobalFilter;
            }

            sap.ui.core.Fragment.byId("columnFilterValueHelpDialog", "columnFilterValueHelpDialogGrid").getBinding("rows").filter(oFilter, "Application");
        },

        initFilters: function(sQuery) {
            var oTable = sap.ui.core.Fragment.byId("columnFilterValueHelpDialog", "columnFilterValueHelpDialogGrid");
            var aColumns = oTable.getColumns();
            for (var i=0; i<aColumns.length; i++) {
                var columnBinding = aColumns[i].getTemplate().getBindingPath("text");
                if( columnBinding ) {
                    this.globalFilters.push(new sap.ui.model.Filter(columnBinding, sap.ui.model.FilterOperator.Contains, sQuery));
                }
            }
        },

        clearAllFilters : function() {
            var oTable = sap.ui.core.Fragment.byId("columnFilterValueHelpDialog", "columnFilterValueHelpDialogGrid");

            var aColumns = oTable.getColumns();
            for (var i=0; i<aColumns.length; i++) {
                oTable.filter(aColumns[i], null);
            }
        }
    });

});
