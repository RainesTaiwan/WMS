/**
 * Created by Administrator.
 */
jQuery.sap.declare("com.sap.ewm.control.Label");
jQuery.sap.require("sap.m.Label");

sap.m.Label.extend("com.sap.ewm.control.Label", {
    metadata : {
        properties : {
        },
        events : {
            "mouseenter" : {},
            "mouseleave" :{}
        }
    },

    init: function() {
        if (sap.m.Label.prototype.init) {   // check whether superclass has an init() method
            sap.m.Label.prototype.init.apply(this, arguments);  // call super.init()
        }
    },

    renderer: "sap.m.LabelRenderer",

    onBeforeRendering: function() {

        if (sap.m.Label.prototype.onBeforeRendering) {
            sap.m.Label.prototype.onBeforeRendering.apply(this, arguments);
        }

        jQuery.sap.byId(this.getId()).unbind("mouseenter", this._mouseenter);
        jQuery.sap.byId(this.getId()).unbind("mouseleave", this._mouseleave);
    },

    onAfterRendering: function() {

        if (sap.m.Label.prototype.onAfterRendering) {
            sap.m.Label.prototype.onAfterRendering.apply(this, arguments);
        }

        // set up for managing clear button
        var that = this;
        jQuery.sap.byId(this.getId()).bind("mouseenter", jQuery.proxy(this._mouseenter, this));
        jQuery.sap.byId(this.getId()).bind("mouseleave", jQuery.proxy(this._mouseleave, this));
    },

    exit: function() {

        jQuery.sap.byId(this.getId()).unbind("mouseenter", this._mouseenter);
        jQuery.sap.byId(this.getId()).unbind("mouseleave", this._mouseleave);

        if (sap.m.Label.prototype.exit) {
            sap.m.Label.prototype.exit.apply(this, arguments);
        }
    },

    _mouseenter: function (oEvent) {
        this.fireMouseenter(oEvent);
    },

    _mouseleave: function( oEvent ) {
        this.fireMouseleave(oEvent);
    }
});