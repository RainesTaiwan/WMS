/**
 *  分頁表格
 */
sap.ui.define([
    'sap/ui/core/library',
    'sap/ui/table/Table',
    'sap/ui/model/Sorter',
    'sap/m/Button',
    'sap/m/OverflowToolbar'
], function (
    coreLibrary,
    Table,
    Sorter,
    Button,
    OverflowToolbar
) {
    var PaginatorTable = Table.extend("com.sap.ewm.control.PaginatorTable", {

        metadata: {

            properties: {
                pageSize: {type: "int", group: "Behavior", defaultValue: 20}
            },

            events: {}
        },

        renderer: "sap.ui.table.TableRenderer",
    });

    PaginatorTable.prototype.init = function () {
        if (sap.ui.table.Table.prototype.init) {
            sap.ui.table.Table.prototype.init.apply(this, arguments);
        }
        this._filters = [];
        this._sorters = [];
        this.attachFilter(oEvent => {
            this._doColumnFilter(oEvent);
        });

        this.attachSort(oEvent => {
            this._doColumnSort(oEvent);
        });

        this._pageModel = new sap.ui.model.json.JSONModel();
        this._initPageModel();
        var pageFooter = new OverflowToolbar({
            content: [
                new sap.m.ToolbarSpacer(),
                new sap.m.OverflowToolbarButton({
                    icon: "sap-icon://media-rewind",
                    type: "Transparent",
                    press: jQuery.proxy(this._firstPage, this)
                }),
                new sap.m.OverflowToolbarButton({
                    icon: "sap-icon://close-command-field",
                    type: "Transparent",
                    press: jQuery.proxy(this._previousPage, this)
                }),
                new sap.m.Input({
                    width: "3em",
                    value: "{/currentPage}",
                    change: jQuery.proxy(this._currentPageChange, this)
                }),
                new sap.m.Label({
                    text: '共' + "{/totalPage}" + '頁'
                }),
                new sap.m.OverflowToolbarButton({
                    icon: "sap-icon://open-command-field",
                    type: "Transparent",
                    press: jQuery.proxy(this._nextPage, this)
                }),
                new sap.m.OverflowToolbarButton({
                    icon: "sap-icon://media-forward",
                    type: "Transparent",
                    press: jQuery.proxy(this._lastPage, this)
                }),
                new sap.m.ToolbarSpacer(),
                new sap.m.Label({
                    text: '{/range}' + '條，' + '共' + '{/totalCount}' + '條'
                })
            ]
        });
        pageFooter.setModel(this._pageModel);
        this.setFooter(pageFooter);
    };

    PaginatorTable.prototype.load = function (loadObj) {
        var url = loadObj["url"];
        if(!loadObj["param"]) {
            loadObj["param"] = {};
        }
        var param = loadObj["param"];
        var requestMethod = loadObj["method"] || "GET";
        var resultPath = loadObj["resultPath"];
        this._loadObj = loadObj;
        if(!param["page"]) {
            param["page"] = 1;
            this._pageModel.setProperty("/currentPage", 1);
        }
        param["rows"] = this.getPageSize();

        var queryParam = {};
        jQuery.extend(queryParam, param);
        // 後臺過濾
        if(this._filters && this._filters.length > 0) {
            for(var i = 0; i < this._filters.length; i++) {
                var oFilter = this._filters[i];
                queryParam[oFilter["sPath"]] = oFilter["oValue1"];
            }
        }
        // 後臺排序
        if(this._sorters && this._sorters.length > 0) {
            var sorterKeys = [];
            for(var i = 0; i < this._sorters.length; i++) {
                var oSorter = this._sorters[i];
                if(sorterKeys.indexOf(oSorter["sPath"]) != -1) {
                    break;
                }
                if(oSorter["bDescending"]) {
                    if(queryParam["descx"]) {
                        queryParam["descx"] += ",";
                    } else {
                        queryParam["descx"] = "";
                    }
                    queryParam["descx"] += oSorter["sPath"];
                } else {
                    if(queryParam["ascx"]) {
                        queryParam["ascx"] += ",";
                    } else {
                        queryParam["ascx"] = "";
                    }
                    queryParam["ascx"] += oSorter["sPath"];
                }
                sorterKeys.push(oSorter["sPath"]);
            }
        }
        this.setBusy(true);
        util.CustomUtil.restfulApiRequest(url, requestMethod, queryParam, respData => {
            this.setBusy(false);
            var oBinding = this.getBinding("rows"),
                oBindingInfo = this.getBindingInfo("rows"),
                oModel = oBinding.getModel();

            if(respData["error"] || respData["header"]["code"] != 0) {
                sap.m.MessageToast.show(respData["message"] || respData["header"]["message"]);
                oModel.setProperty(oBindingInfo.path, []);
                this._initPageModel();
                return;
            }

            if (!resultPath) resultPath = "value";
            var rangeFrom = (this._pageModel.getProperty("/currentPage") - 1) * this.getPageSize() + 1;
            var rangeTo = this._pageModel.getProperty("/currentPage") * this.getPageSize();
            if(rangeTo > Number(respData[resultPath]["total"])) {
                rangeTo = respData[resultPath]["total"];
            }
            this._pageModel.setProperty("/totalCount", respData[resultPath]["total"]);
            this._pageModel.setProperty("/totalPage", respData[resultPath]["pages"]);
            this._pageModel.setProperty("/range", rangeFrom + "-" + rangeTo );
            var rootNodeArray = resultPath.split(".");
            var tempData;
            for (var i = 0; i < rootNodeArray.length; i++) {
                var node = rootNodeArray[i];
                if (i === 0) {
                    tempData = respData[node];
                    continue;
                }
                tempData = tempData[node];
            }
            oModel.setProperty(oBindingInfo.path, tempData["records"] || []);
        }, (errorCode, errorMsg) => {
            this.setBusy(false);
            util.CustomUtil.errorCallbackShow(errorCode, errorMsg);
        }, true );
    };

    PaginatorTable.prototype.loadAppendParam = function (param) {
        Object.assign(this._loadObj["param"], param);
        this.load(this._loadObj);
    };

    PaginatorTable.prototype.clearData = function() {
        var oBinding = this.getBinding("rows"),
            oBindingInfo = this.getBindingInfo("rows"),
            oModel = oBinding.getModel();
        oModel.setProperty(oBindingInfo.path, []);
        this._loadObj = null;
        this._initPageModel();
    };

    // TODO MODIFY
    PaginatorTable.prototype.clearAllFilters = function() {
        this._filters = [];
        var aColumns = this.getColumns();
        for (var i = 0, l = aColumns.length; i < l; i++) {
            var aColumn = aColumns[i];
            aColumn.setProperty("filtered", false, true);
            aColumn.setProperty("filterValue", "", true);
            var oMenu = aColumn.getMenu();
            if (aColumn._bMenuIsColumnMenu) {
                // update column menu input field
                oMenu._setFilterValue("");
            }
            aColumn.getMenu()._setFilterState(coreLibrary.ValueState.None);
            aColumn._updateIcons();
        }
    };

    // TODO ADD
    PaginatorTable.prototype.clearAllSorters = function(oEvent) {
        this._sorters = [];
        var aColumns = this.getColumns();

        // 重置未排序的欄位
        for (var i = 0, l = aColumns.length; i < l; i++) {
            aColumns[i].setProperty("sorted", false, true);
            aColumns[i].setProperty("sortOrder", coreLibrary.SortOrder.Ascending, true);
            aColumns[i]._updateIcons(true);
        }
    };

    PaginatorTable.prototype._initPageModel = function() {
        this._pageModel.setData({"totalCount": 0, "currentPage": 1, "totalPage": 0});
    };

    PaginatorTable.prototype._currentPageChange = function (oEvent) {
        var newPage = oEvent.getParameter("newValue"),
            currentPage = this._pageModel.getProperty("/currentPage"),
            totalCount = this._pageModel.getProperty("/totalCount");
        if (newPage > 0 && newPage <= Math.ceil(totalCount / this.getPageSize())) {
            currentPage = oEvent.getParameter("newValue");
            this._loadObj["param"]["page"] = currentPage;
            this.load(this._loadObj);
        } else {
            this._pageModel.setProperty("/currentPage", this._loadObj["param"]["page"]);
        }
    };

    PaginatorTable.prototype.reload = function () {
        this.load(this._loadObj);
    };

    PaginatorTable.prototype._previousPage = function (oEvent) {
        var currentPage = this._pageModel.getProperty("/currentPage");
        if (currentPage > 1) {
            currentPage--;
            this._pageModel.setProperty("/currentPage", currentPage);

            this._loadObj["param"]["page"] = currentPage;
            this.load(this._loadObj);
        }
    };

    PaginatorTable.prototype._nextPage = function (oEvent) {
        var currentPage = this._pageModel.getProperty("/currentPage"),
            totalCount = this._pageModel.getProperty("/totalCount");
        if (currentPage < Math.ceil(totalCount / this.getPageSize())) {
            currentPage++;
            this._pageModel.setProperty("/currentPage", currentPage);

            this._loadObj["param"]["page"] = currentPage;
            this.load(this._loadObj);
        }
    };

    PaginatorTable.prototype._firstPage = function (oEvent) {
        var currentPage = this._pageModel.getProperty("/currentPage");
        if(currentPage > 1 && this._loadObj) {
            this._pageModel.setProperty("/currentPage", 1);
            this._loadObj["param"]["page"] = 1;
            this.load(this._loadObj);
        }
    };

    PaginatorTable.prototype._lastPage = function (oEvent) {
        var currentPage = this._pageModel.getProperty("/currentPage"),
            totalPage = this._pageModel.getProperty("/totalPage"),
            totalCount = this._pageModel.getProperty("/totalCount");
        if (currentPage < Math.ceil(totalCount / this.getPageSize())) {
            currentPage = totalPage;
            this._pageModel.setProperty("/currentPage", currentPage);

            this._loadObj["param"]["page"] = currentPage;
            this.load(this._loadObj);
        }
    };

    PaginatorTable.prototype._doColumnFilter = function(oEvent) {
        var sValue = oEvent.getParameter("value");
        var column = oEvent.getParameter("column");
        oEvent.bPreventDefault = true;
        column.setProperty("filtered", !!sValue, true);
        column.setProperty("filterValue", sValue, true);
        var oMenu = column.getMenu();
        if (column._bMenuIsColumnMenu) {
            // update column menu input field
            oMenu._setFilterValue(sValue);
        }
        var aCols = this.getColumns();
        this._filters = [];
        for (var i = 0, l = aCols.length; i < l; i++) {
            var oCol = aCols[i],
                oFilter;

            oMenu = oCol.getMenu();
            try {
                oFilter = oCol._getFilter();
                if (oCol._bMenuIsColumnMenu) {
                    oMenu._setFilterState(coreLibrary.ValueState.None);
                }
            } catch (e) {
                if (oCol._bMenuIsColumnMenu) {
                    oMenu._setFilterState(coreLibrary.ValueState.Error);
                }
                continue;
            }
            if (oFilter) {
                this._filters.push(oFilter);
            }
        }
        this.load(this._loadObj);
        column._updateIcons();
    };

    PaginatorTable.prototype._doColumnSort = function(oEvent) {
        var column = oEvent.getParameter("column");
        var sortOrder = oEvent.getParameter("sortOrder");
        oEvent.bPreventDefault = true;
        var aSortedCols = this.getSortedColumns();
        var aColumns = this.getColumns();

        // 重置未排序的欄位
        for (var i = 0, l = aColumns.length; i < l; i++) {
            if (aSortedCols.indexOf(aColumns[i]) < 0) {
                aColumns[i].setProperty("sorted", false, true);
                aColumns[i].setProperty("sortOrder", coreLibrary.SortOrder.Ascending, true);
                aColumns[i]._updateIcons(true);
                delete aColumns[i]._oSorter;
            }
        }

        column.setProperty("sorted", true, true);
        column.setProperty("sortOrder", sortOrder, true);
        column._oSorter = new Sorter(column.getSortProperty(), column.getSortOrder() === coreLibrary.SortOrder.Descending);

        this._sorters = [];
        for (var i = 0, l = aSortedCols.length; i < l; i++) {
            aSortedCols[i]._updateIcons(true);
            this._sorters.push(aSortedCols[i]._oSorter);
        }
        this.load(this._loadObj);

        this._resetColumnHeaderHeights();
        this._updateRowHeights(this._collectRowHeights(true), true);

        var oBinding = this.getBinding("rows");
        if (oBinding) {
            if (column._updateTableAnalyticalInfo) {
                column._updateTableAnalyticalInfo(true);
            }
        }
    };

    return PaginatorTable;
});
