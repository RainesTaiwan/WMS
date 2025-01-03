sap.ui.define([
	"sap/m/MessageToast",
	"sap/ui/core/util/Export",
	"sap/ui/core/util/ExportTypeCSV",
	"sap/ui/model/json/JSONModel"
], function(MessageToast, Export, ExportTypeCSV, JSONModel) {
	"use strict";

	var ExcelExport = {

		/**
		 * Export data to CSV file.
		 * @public
		 * @param {sap.ui.model.Model} oModel
		 * @param {string} rowsPath Path of values in the model.
		 * @param {string} params Parameters exported.
		 * @param {string} fileName File name.
		 */
		save: function(oModel, rowsPath, columnsOrder, fileName) {
			if(fileName == undefined) {
				fileName = "data_export";
			}

			var columns = [];
			for(var i = 0; i < columnsOrder.length; i++) {
				columns.push({
					name: columnsOrder[i],
					template: { content: "{" + columnsOrder[i] + "}" }
				});
			}

			var oExport = new Export({
				"exportType": new ExportTypeCSV({ separatorChar: "," }),
				"models": oModel,
				"rows": { path: rowsPath },
				"columns": columns
			});

			// download exported file
			oExport.saveFile(fileName).catch(function(oError) {
				MessageBox.error("Error when downloading data. Browser might not be supported!\n\n" + oError);
			}).then(function() {
				oExport.destroy();
			});
		}
	};

	return ExcelExport;
}, /* bExport= */ true);
