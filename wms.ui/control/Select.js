/**
 * Created by Administrator on 2017/11/22.
 */
jQuery.sap.declare("com.sap.ewm.control.Select");
jQuery.sap.require("sap.m.Select");

sap.m.Select.extend("com.sap.ewm.control.Select", {
    metadata : {
        properties : {
            "arrowUpChange" : {type : "boolean", group : "Behavior", defaultValue : false},
            "arrowDownChange" : {type : "boolean", group : "Behavior", defaultValue : false}
        },
        events : {
            "beforePickerOpen" : {},
            "afterPickerClose" : {}
        }
    },

    init: function() {
        if (sap.m.Select.prototype.init) {   // check whether superclass has an init() method
            sap.m.Select.prototype.init.apply(this, arguments);  // call super.init()
        }
    },

    renderer: "sap.m.SelectRenderer",

    onBeforeRendering: function() {

        if (sap.m.Select.prototype.onBeforeRendering) {
            sap.m.Select.prototype.onBeforeRendering.apply(this, arguments);
        }
    },

    onAfterRendering: function() {

        if (sap.m.Select.prototype.onAfterRendering) {
            sap.m.Select.prototype.onAfterRendering.apply(this, arguments);
        }
    },

    onBeforeOpen: function(oEvent) {
        if (sap.m.Select.prototype.onBeforeOpen) {
            sap.m.Select.prototype.onBeforeOpen.apply(this, [oEvent]);
        }
        this.fireBeforePickerOpen(oEvent);
    },

    onAfterClose: function(oEvent) {
        if (sap.m.Select.prototype.onAfterClose) {
            sap.m.Select.prototype.onAfterClose.apply(this, [oEvent]);
        }
        this.fireAfterPickerClose(oEvent);
    },

    exit: function() {
        if (sap.m.Select.prototype.exit) {
            sap.m.Select.prototype.exit.apply(this, arguments);
        }
    },

    onsapdown: function(oEvent) {
        if( this.getArrowDownChange() && sap.m.Select.prototype.onsapdown) {
            sap.m.Select.prototype.onsapdown.apply(this, [oEvent]);
        }
    },

    onsapup: function(oEvent) {
        if( this.getArrowUpChange() && sap.m.Select.prototype.onsapup) {
            sap.m.Select.prototype.onsapup.apply(this, [oEvent]);
        }
    }
});