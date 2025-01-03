jQuery.sap.declare("util.TableUtil");
jQuery.sap.require("util.Model");
jQuery.sap.require("util.CommonKey");
jQuery.sap.require("util.StringUtil");

util.TableUtil = {

    doTableFieldNext : function(e, _table) {
        var me = this;
        var selectedTd = e.currentTarget.closest("td");
        var oItemNavigation = _table._oItemNavigation || _table._getItemNavigation();
        var selectedIndex = me.getSelectedIndexBySelectedTd( selectedTd, _table);
        var iRowCount = oItemNavigation.aItemDomRefs.length / oItemNavigation.iColumns,
            iRow = Math.floor(selectedIndex / oItemNavigation.iColumns),
            iCol = selectedIndex % oItemNavigation.iColumns;

        switch( e.keyCode ) {
            case jQuery.sap.KeyCodes.ARROW_UP :
                if( me.isTableFirstRow(selectedTd, _table) ) {
                    if (_table._scrollPrev) {
                        _table._scrollPrev();
                    } else {
                        if (_table.getFirstVisibleRow() > 0) {
                            _table.setFirstVisibleRow(_table.getFirstVisibleRow() - 1);
                        }
                    }
                    _table._performUpdateRows();
                }
                selectedIndex = me.getTableUpFocusableIndex(selectedIndex, _table);
                break;
            case jQuery.sap.KeyCodes.ARROW_DOWN :
                if( me.isTableLastRow(selectedTd, _table) ) {
                    if( _table._scrollNext ) {
                        _table._scrollNext();
                    } else {
                        if (_table.getFirstVisibleRow() < _table._getRowCount() - _table.getVisibleRowCount()) {
                            _table.setFirstVisibleRow(Math.min(_table.getFirstVisibleRow() + 1, _table._getRowCount() - _table.getVisibleRowCount()));
                        }
                    }
                    _table._performUpdateRows();
                }
                selectedIndex = me.getTableDownFocusableIndex(selectedIndex, _table);
                break;
            case jQuery.sap.KeyCodes.ARROW_LEFT :
                if( me.isTableFirstCell(selectedTd, _table) ) {
                    if (_table._scrollPrev) {
                        _table._scrollPrev();
                    } else {
                        if (_table.getFirstVisibleRow() > 0) {
                            _table.setFirstVisibleRow(_table.getFirstVisibleRow() - 1);
                        }
                    }
                    _table._performUpdateRows();
                    selectedIndex = me.getTableDownFocusableIndex(selectedIndex, _table);
                }
                selectedIndex = me.getTableLeftFocusableIndex(selectedIndex, _table);
                break;
            case jQuery.sap.KeyCodes.ARROW_RIGHT :
            case jQuery.sap.KeyCodes.TAB :
                if( me.isTableLastCell(selectedTd, _table) ) {
                    if( _table._scrollNext ) {
                        _table._scrollNext();
                    } else {
                        if (_table.getFirstVisibleRow() < _table._getRowCount() - _table.getVisibleRowCount()) {
                            _table.setFirstVisibleRow(Math.min(_table.getFirstVisibleRow() + 1, _table._getRowCount() - _table.getVisibleRowCount()));
                        }
                    }
                    _table._performUpdateRows();
                    selectedIndex = me.getTableUpFocusableIndex(selectedIndex, _table);
                }
                selectedIndex = me.getTableRightFocusableIndex(selectedIndex, _table);
                break;
        }
        jQuery.sap.delayedCall(100, me, me.setTableFieldFocus, [selectedIndex, _table]);
    },
    setTableFieldFocus: function(selectedIndex, _table) {
        var me = this;
        var oItemNavigation = _table._oItemNavigation || _table._getItemNavigation();
        var aItemDomRef = oItemNavigation.aItemDomRefs[selectedIndex];
        var sapFocusableFields = $(aItemDomRef).find(":sapFocusable");
        if( sapFocusableFields && sapFocusableFields[0] ) sapFocusableFields[0].focus();
    },
    getSelectedIndexBySelectedTd: function( selectedTd, _table) {
        var me = this;
        var oItemNavigation = _table._oItemNavigation || _table._getItemNavigation();
        var selectedIndex = -1;
        //var s = e.currentTarget.closest("td");
        for (var i = 0; i < oItemNavigation.aItemDomRefs.length; i++) {
            var o = oItemNavigation.aItemDomRefs[i];
            if (jQuery.sap.containsOrEquals(o, selectedTd)) {
                if (o === selectedTd) {
                    selectedIndex = i;
                    break;
                }
            }
        }
        return selectedIndex;
    },
    getTableUpFocusableIndex: function(selectedIndex,_table) {
        var me = this;
        var oItemNavigation = _table._oItemNavigation || _table._getItemNavigation();
        var iRowCount = oItemNavigation.aItemDomRefs.length / oItemNavigation.iColumns,
            iRow = Math.floor(selectedIndex / oItemNavigation.iColumns),
            iCol = selectedIndex % oItemNavigation.iColumns;

        if (iRow  > 1) {
            selectedIndex = selectedIndex - oItemNavigation.iColumns;
        } else {
            return selectedIndex;
        }

        var aItemDomRef = oItemNavigation.aItemDomRefs[selectedIndex];
        var sapFocusableFields = $(aItemDomRef).find(":sapFocusable");
        if( sapFocusableFields && sapFocusableFields[0] ) {
            return selectedIndex;
        }
        if(selectedIndex < oItemNavigation.iColumns) {
            return selectedIndex;
        }
        return me.getTableUpFocusableIndex(selectedIndex,_table);
    },
    getTableDownFocusableIndex: function(selectedIndex,_table) {
        var me = this;
        var oItemNavigation = _table._oItemNavigation || _table._getItemNavigation();
        var iRowCount = oItemNavigation.aItemDomRefs.length / oItemNavigation.iColumns,
            iRow = Math.floor(selectedIndex / oItemNavigation.iColumns),
            iCol = selectedIndex % oItemNavigation.iColumns;

        if (iRow < iRowCount - 1) {
            selectedIndex = selectedIndex + oItemNavigation.iColumns;
        } else {
            return selectedIndex;
        }
        var aItemDomRef = oItemNavigation.aItemDomRefs[selectedIndex];
        var sapFocusableFields = $(aItemDomRef).find(":sapFocusable");
        if( sapFocusableFields && sapFocusableFields[0] ) {
            return selectedIndex;
        }
        if(selectedIndex >= oItemNavigation.aItemDomRefs.length - oItemNavigation.iColumns) {
            return selectedIndex;
        }
        return me.getTableDownFocusableIndex(selectedIndex,_table);
    },
    getTableLeftFocusableIndex: function(selectedIndex,_table) {
        var me = this;
        var oItemNavigation = _table._oItemNavigation || _table._getItemNavigation();

        selectedIndex = selectedIndex - 1;
        var aItemDomRef = oItemNavigation.aItemDomRefs[selectedIndex];
        var sapFocusableFields = $(aItemDomRef).find(":sapFocusable");
        if( sapFocusableFields && sapFocusableFields[0] ) {
            return selectedIndex;
        }
        if(selectedIndex <= 0) {
            return selectedIndex;
        }
        return me.getTableLeftFocusableIndex(selectedIndex,_table);
    },
    getTableRightFocusableIndex: function(selectedIndex,_table) {
        var me = this;
        var oItemNavigation = _table._oItemNavigation || _table._getItemNavigation();

        selectedIndex = selectedIndex + 1;

        var aItemDomRef = oItemNavigation.aItemDomRefs[selectedIndex];
        var sapFocusableFields = $(aItemDomRef).find(":sapFocusable");
        if( sapFocusableFields && sapFocusableFields[0] ) {
            return selectedIndex;
        }
        if(selectedIndex >= oItemNavigation.aItemDomRefs.length) {
            return selectedIndex;
        }
        return me.getTableRightFocusableIndex(selectedIndex,_table);
    },
    isTableLastRow: function(s, _table) {
        var oItemNavigation = _table._oItemNavigation || _table._getItemNavigation();
        for (var i = 0; i < oItemNavigation.aItemDomRefs.length; i++) {
            var o = oItemNavigation.aItemDomRefs[i];
            if (jQuery.sap.containsOrEquals(o, s)) {
                if (o === s) {
                    var selectedIndex = -1;
                    var r = oItemNavigation.aItemDomRefs.length / oItemNavigation.iColumns, R = Math.floor(i / oItemNavigation.iColumns), c = i % oItemNavigation.iColumns;
                    if (R < r - 1) {
                        selectedIndex = i + oItemNavigation.iColumns;
                    }
                }
                return !oItemNavigation.aItemDomRefs[selectedIndex] ? true : false;
            }
        }
    },
    isTableFirstRow: function(s, _table) {
        var oItemNavigation = _table._oItemNavigation || _table._getItemNavigation();
        for (var i = 0; i < oItemNavigation.aItemDomRefs.length; i++) {
            var o = oItemNavigation.aItemDomRefs[i];
            if (jQuery.sap.containsOrEquals(o, s)) {
                if (o === s) {
                    var iRow = Math.floor(i / oItemNavigation.iColumns);
                    if( iRow === 1 ) {
                        return true;
                    }
                }
                return false;
            }
        }
    },
    isTableFirstCell: function(s, _table) {
        var oItemNavigation = _table._oItemNavigation || _table._getItemNavigation();
        for (var i = 0; i < oItemNavigation.aItemDomRefs.length; i++) {
            var o = oItemNavigation.aItemDomRefs[i];
            if (jQuery.sap.containsOrEquals(o, s)) {
                if (o === s) {
                    var iRow = Math.floor(i / oItemNavigation.iColumns);
                    var iCol = i % oItemNavigation.iColumns;
                    if( i === oItemNavigation.iColumns ) {
                        return true;
                    } else if( iRow === 1 ) {
                        for( var j = iCol - 1; j >= 0; j-- ) {
                            var aItemDomRef = oItemNavigation.aItemDomRefs[j + oItemNavigation.iColumns];
                            if( !aItemDomRef ) return true;
                            var sapFocusableFields = $(aItemDomRef).find(":sapFocusable");
                            if( sapFocusableFields && sapFocusableFields[0] ) {
                                return false;
                            }
                        }
                        return true;
                    }
                }
                return false;
            }
        }
    },
    isTableLastCell: function(s, _table) {
        var oItemNavigation = _table._oItemNavigation || _table._getItemNavigation();
        var iRowCount = oItemNavigation.aItemDomRefs.length / oItemNavigation.iColumns;
        for (var i = 0; i < oItemNavigation.aItemDomRefs.length; i++) {
            var o = oItemNavigation.aItemDomRefs[i];
            if (jQuery.sap.containsOrEquals(o, s)) {
                if (o === s) {
                    var iRow = Math.floor(i / oItemNavigation.iColumns);
                    var iCol = i % oItemNavigation.iColumns;
                    if( i === oItemNavigation.aItemDomRefs.length - 1 ) {
                        return true;
                    } else if( iRow === iRowCount - 1 ) {
                        var index = i;
                        for( var j = iCol + 1; j < oItemNavigation.iColumns; j++ ) {
                            index++;
                            var aItemDomRef = oItemNavigation.aItemDomRefs[index];
                            if( !aItemDomRef ) return true;
                            var sapFocusableFields = $(aItemDomRef).find(":sapFocusable");
                            if( sapFocusableFields && sapFocusableFields[0] ) {
                                return false;
                            }
                        }
                        return true;
                    }
                }
                return false;
            }
        }
    }
}