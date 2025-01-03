/*
 * ! SAPUI5

		(c) Copyright 2009-2019 SAP SE. All rights reserved
	
 */
sap.ui.define(['sap/ui/thirdparty/jquery','sap/ui/comp/library','sap/m/library','sap/m/List','sap/m/ResponsivePopover','sap/m/StandardListItem','sap/m/Token','sap/m/Table','sap/m/ColumnListItem','sap/m/Label','./BaseValueListProvider','sap/ui/comp/util/FormatUtil','sap/ui/comp/util/DateTimeUtil','sap/ui/model/json/JSONModel','sap/ui/core/format/DateFormat','sap/ui/Device','sap/ui/comp/smartfilterbar/SmartFilterBar','sap/ui/model/Sorter','sap/base/util/merge'],function(q,l,L,a,R,S,T,b,C,c,B,F,D,J,d,e,f,g,m){"use strict";var P=L.PlacementType;var V=l.valuehelpdialog.ValueHelpRangeOperation;var h=L.ListMode;var j;var k=B.extend("sap.ui.comp.providers.ValueHelpProvider",{constructor:function(p){if(!f){f=sap.ui.require("sap/ui/comp/smartfilterbar/SmartFilterBar");}if(p){this.preventInitialDataFetchInValueHelpDialog=!!p.preventInitialDataFetchInValueHelpDialog;this.sTitle=p.title;this.bSupportMultiselect=!!p.supportMultiSelect;this.bSupportRanges=!!p.supportRanges;this.bIsSingleIntervalRange=!!p.isSingleIntervalRange;this.bIsUnrestrictedFilter=!!p.isUnrestrictedFilter;this.bTakeOverInputValue=(p.takeOverInputValue===false)?false:true;this._sScale=p.scale;this._sPrecision=p.precision;if(this.bIsSingleIntervalRange){this.bSupportRanges=true;}}B.apply(this,arguments);this._onInitialise();}});k.prototype._onInitialise=function(){if(this.oControl.attachValueHelpRequest){this._fVHRequested=function(E){if(!this.bInitialised){return;}this.oControl=E.getSource();this.bForceTriggerDataRetreival=E.getParameter("fromSuggestions");if(this.bTakeOverInputValue||this.bForceTriggerDataRetreival){this.sBasicSearchText=E.getSource().getValue();}this._createValueHelpDialog();}.bind(this);this.oControl.attachValueHelpRequest(this._fVHRequested);}};k.prototype._createValueHelpDialog=function(){if(!this.bCreated){this.bCreated=true;if(!this._oValueHelpDialogClass){sap.ui.require(['sap/ui/comp/valuehelpdialog/ValueHelpDialog'],this._onValueHelpDialogRequired.bind(this));}else{this._onValueHelpDialogRequired(this._oValueHelpDialogClass);}}};k.prototype._getTitle=function(){if(this.sTitle){return this.sTitle;}else if(this.oFilterProvider){return this.oFilterProvider._determineFieldLabel(this._fieldViewMetadata);}return"";};k.prototype._onValueHelpDialogRequired=function(i){this._oValueHelpDialogClass=i;var v=this.oControl.getId()+"-valueHelpDialog";this.oValueHelpDialog=new i(v,{stretch:e.system.phone,basicSearchText:this.sBasicSearchText,supportRangesOnly:this.bIsSingleIntervalRange||!this.oPrimaryValueListAnnotation,supportMultiselect:this.bSupportMultiselect,title:this._getTitle(),supportRanges:this.bSupportRanges,displayFormat:this.sDisplayFormat,ok:this._onOK.bind(this),cancel:this._onCancel.bind(this),afterClose:function(){if(this.oPrimaryValueListAnnotation){this._resolveAnnotationData(this.oPrimaryValueListAnnotation);}this.oValueHelpDialog.destroy();this.bCreated=false;if(this.oControl&&this.oControl.focus&&!e.system.phone){this.oControl.focus();}}.bind(this)});this.oControl.addDependent(this.oValueHelpDialog);this.oValueHelpDialog.suggest(function(o,s){if(this.oPrimaryValueListAnnotation){var n=function(j){o.setShowSuggestion(true);o.setFilterSuggests(false);o._oSuggestProvider=new j({fieldName:s,control:o,model:this.oODataModel,displayFormat:this.sDisplayFormat,resolveInOutParams:false,displayBehaviour:this.sTokenDisplayBehaviour,annotation:this.oPrimaryValueListAnnotation,fieldViewMetadata:this._fieldViewMetadata,maxLength:this._sMaxLength,aggregation:"suggestionRows",typeAheadEnabled:true,enableShowTableSuggestionValueHelp:false});}.bind(this);j=sap.ui.require('sap/ui/comp/providers/ValueListProvider');if(!j){sap.ui.require(['sap/ui/comp/providers/ValueListProvider'],n);}else{n(j);return o._oSuggestProvider;}return null;}}.bind(this));if(this.bIsSingleIntervalRange){this.oValueHelpDialog.setIncludeRangeOperations([V.BT,V.EQ],this._sType);this.oValueHelpDialog.setMaxIncludeRanges(1);this.oValueHelpDialog.setMaxExcludeRanges(0);this._updateInitialInterval();}else if((this._sType==="date"||this._sType==="time"||this._sType==="datetime")&&!this.bIsUnrestrictedFilter){this.oValueHelpDialog.setIncludeRangeOperations([V.EQ],this._sType);this.oValueHelpDialog.setMaxExcludeRanges(0);}if(this.oControl.$()&&this.oControl.$().closest(".sapUiSizeCompact").length>0){this.oValueHelpDialog.addStyleClass("sapUiSizeCompact");}else if(this.oControl.$()&&this.oControl.$().closest(".sapUiSizeCozy").length>0){this.oValueHelpDialog.addStyleClass("sapUiSizeCozy");}else if(q("body").hasClass("sapUiSizeCompact")){this.oValueHelpDialog.addStyleClass("sapUiSizeCompact");}else{this.oValueHelpDialog.addStyleClass("sapUiSizeCozy");}if(this.bSupportRanges){this.oValueHelpDialog.setRangeKeyFields([{label:this._getTitle(),key:this.sFieldName,typeInstance:this._fieldViewMetadata?this._fieldViewMetadata.ui5Type:null,type:this._sType,formatSettings:this._sType==="numc"?{isDigitSequence:true,maxLength:this._sMaxLength}:Object.assign({},this._oDateFormatSettings,{UTC:false}),scale:this._sScale,precision:this._sPrecision,maxLength:this._sMaxLength}]);}if(!(this.bIsSingleIntervalRange||!this.oPrimaryValueListAnnotation)){this.oValueHelpDialog.setModel(this.oODataModel);this._createAdditionalValueHelpControls();this._createCollectiveSearchControls();}if(this.oControl.getTokens){var t=this.oControl.getTokens();if(t){t=this._adaptTokensFromFilterBar(t);this.oValueHelpDialog.setTokens(t);}}this.oValueHelpDialog.open();};k.prototype._adaptTokensFromFilterBar=function(t){var o,r,n,p=t;if(this.oFilterProvider&&t&&this._sType==="time"){p=[];for(var i=0;i<t.length;i++){o=m({},t[i]);r=o.data("range");if(r){r=m({},r);if(r.value1 instanceof Date){n=D.localToUtc(r.value1);r.value1={__edmType:"Edm.Time",ms:n.getTime()};}if(r.value2 instanceof Date){n=D.localToUtc(r.value2);r.value2={__edmType:"Edm.Time",ms:n.getTime()};}o.data("range",r);p.push(o);}}}return p;};k.prototype._updateInitialInterval=function(){var i=this.oControl.getValue(),t,r,v,o,n;if(i){t=new T();r={exclude:false,keyField:this.sFieldName};if(this._sType==="numeric"){v=F.parseFilterNumericIntervalData(i);if(v.length==0){v.push(i);}}else if(this._sType==="datetime"){v=F.parseDateTimeOffsetInterval(i);o=d.getDateTimeInstance(Object.assign({},this._oDateFormatSettings,{UTC:false}));n=o.parse(v[0]);v[0]=n?n:new Date(v[0]);if(v.length===2){n=o.parse(v[1]);v[1]=n?n:new Date(v[1]);}}else{v=i.split("-");}if(v&&v.length===2){r.operation="BT";r.value1=v[0];r.value2=v[1];}else{r.operation="EQ";r.value1=v[0];}t.data("range",r);}if(t){this.oValueHelpDialog.setTokens([t]);}};k.prototype._createCollectiveSearchControls=function(){var p,o,I,i=0,n=0,O,A,r;if(this.additionalAnnotations&&this.additionalAnnotations.length){O=function(E){var s=E.getParameter("listItem"),t;p.close();if(s){t=s.data("_annotation");if(t){this._triggerAnnotationChange(t);}}}.bind(this);o=new a({mode:h.SingleSelectMaster,selectionChange:O});r=sap.ui.getCore().getLibraryResourceBundle("sap.ui.comp");p=new R({placement:P.Bottom,showHeader:true,contentHeight:"30rem",title:r.getText("COLLECTIVE_SEARCH_SELECTION_TITLE"),content:[o],afterClose:function(){this.oValueHelpDialog._rotateSelectionButtonIcon(false);}.bind(this)});I=new S({title:this.oPrimaryValueListAnnotation.valueListTitle});I.data("_annotation",this.oPrimaryValueListAnnotation);o.addItem(I);o.setSelectedItem(I);this.oValueHelpDialog.oSelectionTitle.setText(this.oPrimaryValueListAnnotation.valueListTitle);this.oValueHelpDialog.oSelectionTitle.setTooltip(this.oPrimaryValueListAnnotation.valueListTitle);n=this.additionalAnnotations.length;for(i=0;i<n;i++){A=this.additionalAnnotations[i];I=new S({title:A.valueListTitle});I.data("_annotation",A);o.addItem(I);}this.oValueHelpDialog.oSelectionButton.setVisible(true);this.oValueHelpDialog.oSelectionTitle.setVisible(true);this.oValueHelpDialog.oSelectionButton.attachPress(function(){if(!p.isOpen()){this.oValueHelpDialog._rotateSelectionButtonIcon(true);p.openBy(this.oValueHelpDialog.oSelectionButton);}else{p.close();}}.bind(this));}};k.prototype._triggerAnnotationChange=function(A){this.oValueHelpDialog.oSelectionTitle.setText(A.valueListTitle);this.oValueHelpDialog.oSelectionTitle.setTooltip(A.valueListTitle);this.oValueHelpDialog.resetTableState();this._resolveAnnotationData(A);this._createAdditionalValueHelpControls();};k.prototype._createAdditionalValueHelpControls=function(){var s=null;this.oValueHelpDialog.setKey(this.sKey);this.oValueHelpDialog.setKeys(this._aKeys);this.oValueHelpDialog.setDescriptionKey(this.sDescription);this.oValueHelpDialog.setTokenDisplayBehaviour(this.sTokenDisplayBehaviour);var o=new J();o.setData({cols:this._aCols});this.oValueHelpDialog.setModel(o,"columns");if(this.bSupportBasicSearch){s=this.sKey;}if(this.oSmartFilterBar){this.oSmartFilterBar._setCollectiveSearch(null);this.oSmartFilterBar.destroy();}this.oSmartFilterBar=new f(this.oValueHelpDialog.getId()+"-smartFilterBar",{entitySet:this.sValueListEntitySetName,basicSearchFieldName:s,enableBasicSearch:this.bSupportBasicSearch,advancedMode:true,showGoOnFB:!e.system.phone,expandAdvancedArea:(!this.bForceTriggerDataRetreival&&e.system.desktop),search:this._onFilterBarSearchPressed.bind(this),reset:this._onFilterBarResetPressed.bind(this),filterChange:this._onFilterBarFilterChange.bind(this),initialise:this._onFilterBarInitialise.bind(this)});if(this._oDateFormatSettings){this.oSmartFilterBar.data("dateFormatSettings",this._oDateFormatSettings);}this.oSmartFilterBar.isRunningInValueHelpDialog=true;this.oValueHelpDialog.setFilterBar(this.oSmartFilterBar);};k.prototype._onFilterBarFilterChange=function(){if(!this._bIgnoreFilterChange){this.oValueHelpDialog.getTableAsync().then(function(t){t.setShowOverlay(true);this.oValueHelpDialog.TableStateSearchData();}.bind(this));}};k.prototype._onFilterBarSearchPressed=function(){this._rebindTable();};k.prototype._rebindTable=function(){var n,p,o;n=this.oSmartFilterBar.getFilters();p=this.oSmartFilterBar.getParameters()||{};if(this.aSelect&&this.aSelect.length){p["select"]=this.aSelect.toString();}o={path:"/"+this.sValueListEntitySetName,filters:n,parameters:p,events:{dataReceived:function(E){this.oValueHelpDialog.TableStateDataFilled();var i=E.getSource();this.oValueHelpDialog.getTableAsync().then(function(t){t.setBusy(false);if(i&&this.oValueHelpDialog&&this.oValueHelpDialog.isOpen()){var r=i.getLength();if(r){this.oValueHelpDialog.update();}}}.bind(this));}.bind(this)}};this.oValueHelpDialog.getTableAsync().then(function(t){t.setShowOverlay(false);this.oValueHelpDialog.TableStateDataSearching();t.setBusy(true);if(t instanceof b){var E;if(this.sKey&&this._oMetadataAnalyser){E=this._oMetadataAnalyser.getFieldsByEntitySetName(this.sValueListEntitySetName);for(var i=0;i<E.length;i++){if(E[i].name===this.sKey&&E[i].sortable!==false){o.sorter=new g(this.sKey);break;}}}o.factory=function(I,u){var v=t.getModel("columns").getData().cols;return new C({cells:v.map(function(w){var x=w.template;return new c({text:"{"+x+"}"});})});};t.bindItems(o);}else{var r=t.getColumns();for(var i=0;i<r.length;i++){var s=r[i];s._appDefaults=null;}r=t.getSortedColumns();if(!r||r.length==0){r=t.getColumns();}for(var i=0;i<r.length;i++){var s=r[i];if(s.getSorted()){if(!o.sorter){o.sorter=[];}o.sorter.push(new g(s.getSortProperty(),s.getSortOrder()==="Descending"));}}t.bindRows(o);}}.bind(this));};k.prototype._onFilterBarResetPressed=function(){this._calculateFilterInputData();if(this.oSmartFilterBar){this.oSmartFilterBar.setFilterData(this.mFilterInputData);}};k.prototype._onFilterBarInitialise=function(){var o=null;this._bIgnoreFilterChange=true;this._onFilterBarResetPressed();delete this._bIgnoreFilterChange;if(this.oSmartFilterBar&&this.oSmartFilterBar.getBasicSearchControl){o=this.oSmartFilterBar.getBasicSearchControl();if(o){o.setValue(this.sBasicSearchText);if(e.system.phone&&o.isA("sap.m.SearchField")){o.setShowSearchButton(true);}}}if(!this.preventInitialDataFetchInValueHelpDialog||this.bForceTriggerDataRetreival){this._rebindTable();this.bForceTriggerDataRetreival=false;}};k.prototype._onOK=function(o){var t=o.getParameter("tokens"),r,K,i=0,n=[],p=null,s;this._onCancel();if(this.oControl.isA("sap.m.MultiInput")){this.oControl.setValue("");this.oControl.destroyTokens();this.oControl.setTokens(t);i=t.length;while(i--){p=t[i].data("row");if(p){n.push(p);}}}else{if(t[0]){if(this.bIsSingleIntervalRange){r=t[0].data("range");if(r){if(this._sType==="datetime"){s=d.getDateTimeInstance(Object.assign({},this._oDateFormatSettings,{UTC:false}));if(typeof r.value1==="string"){r.value1=new Date(r.value1);}if(r.operation==="BT"){if(typeof r.value2==="string"){r.value2=new Date(r.value2);}K=s.format(r.value1)+"-"+s.format(r.value2);}else{K=s.format(r.value1);}}else{if(r.operation==="BT"){K=r.value1+"-"+r.value2;}else{K=r.value1;}}}}else{K=t[0].getKey();}p=t[0].data("row");if(p){n.push(p);}t[0].destroy();}this.oControl.setValue(K);this.oControl.fireChange({value:K,validated:true});}this._calculateAndSetFilterOutputData(n);};k.prototype._onCancel=function(){this.oValueHelpDialog.close();this.oValueHelpDialog.setModel(null);};k.prototype.destroy=function(){if(this.oControl&&this.oControl.detachValueHelpRequest){this.oControl.detachValueHelpRequest(this._fVHRequested);this._fVHRequested=null;}B.prototype.destroy.apply(this,arguments);if(this.oValueHelpDialog){this.oValueHelpDialog.destroy();this.oValueHelpDialog=null;}if(this.oSmartFilterBar){this.oSmartFilterBar.destroy();this.oSmartFilterBar=null;}this.sTitle=null;this._oValueHelpDialogClass=null;};return k;},true);
