sap.ui.define([
	"sap/ui/core/mvc/Controller",
	"jquery.sap.global"
], function(Controller, jQuery) {
	"use strict";
	return Controller.extend("com.sap.ewm.app.AboutPage", {
		onInit: function () {
			var oRouter = sap.ui.core.UIComponent.getRouterFor(this);
			oRouter.getRoute("Home").attachPatternMatched(this.onObjectMatched, this);
		},

		onLocaleChanged: function (oEvent) {
			var locale = oEvent.getParameter("item").getKey();
			var oStorage = jQuery.sap.storage(jQuery.sap.storage.Type.local);
			oStorage.put("locale", locale);
			sap.ui.getCore().getConfiguration().setLanguage(locale);
		},

		onObjectMatched: function (oEvent) {
            
        }
	});
});
