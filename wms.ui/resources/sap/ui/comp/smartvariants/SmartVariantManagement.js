/*
 * ! SAPUI5

		(c) Copyright 2009-2019 SAP SE. All rights reserved
	
 */
sap.ui.define(['sap/ui/thirdparty/jquery','sap/ui/comp/library','./SmartVariantManagementAdapter','sap/ui/comp/variants/VariantItem','sap/ui/comp/variants/VariantManagement','sap/ui/comp/odata/MetadataAnalyser','sap/base/Log','sap/base/util/merge','sap/ui/base/SyncPromise'],function(q,l,S,V,a,M,L,m,b){"use strict";var C=l.smartvariants.ChangeHandlerType;var F;var c;var d;var e=a.extend("sap.ui.comp.smartvariants.SmartVariantManagement",{metadata:{library:"sap.ui.comp",designtime:"sap/ui/comp/designtime/smartvariants/SmartVariantManagement.designtime",properties:{persistencyKey:{type:"string",group:"Misc",defaultValue:null},entitySet:{type:"string",group:"Misc",defaultValue:null}},aggregations:{personalizableControls:{type:"sap.ui.comp.smartvariants.PersonalizableInfo",multiple:true,singularName:"personalizableControl"}},events:{initialise:{},save:{parameters:{tile:{type:"boolean"}}},afterSave:{}}},renderer:function(r,o){a.getMetadata().getRenderer().render(r,o);}});e.prototype.init=function(){a.prototype.init.apply(this);this._bIsInitialized=false;this._oStandardVariant=null;this._oMetadataPromise=null;this._oControlPromise=null;this._oPersoControl=null;this._sAppStandardVariantKey=null;this._oSelectionVariantHandler={};this._oAppStdContent=null;this._aPersonalizableControls=[];if(this.setLifecycleSupport){this.setLifecycleSupport(true);}this._setBackwardCompatibility(false);this._oAdapter=null;this._bApplyingUIState=false;this.setSupportExecuteOnSelectOnSandardVariant(true);this._loadFlex();};e.prototype.propagateProperties=function(){a.prototype.propagateProperties.apply(this,arguments);this._initializeMetadata();};e.prototype._initializeMetadata=function(){var o=this.getModel();if(o&&!this._oMetadataPromise){this._oMetadataPromise=new Promise(function(r,f){if(!this.getEntitySet()){r();}else{o.getMetaModel().loaded().then(function(){this._onMetadataInitialised();r();}.bind(this));}}.bind(this));}};e.prototype._onMetadataInitialised=function(){var o,E=this.getEntitySet();o=new M(this.getModel());if(o&&E){this._oAdapter=new S({selectionPresentationVariants:o.getSelectionPresentationVariantAnnotationList(E)});}};e.prototype.applySettings=function(s){if(!s||!s.hasOwnProperty("useFavorites")){this.setUseFavorites(true);}a.prototype.applySettings.apply(this,arguments);};e.prototype._createControlWrapper=function(o){var f=null;var g=sap.ui.getCore().byId(o.getControl());if(g){f={control:g,type:o.getType(),dataSource:o.getDataSource(),keyName:o.getKeyName(),loaded:q.Deferred()};}return f;};e.prototype._getControlWrapper=function(o){var f=this._getAllPersonalizableControls();if(f&&f.length){for(var i=0;i<f.length;i++){if(f[i].control===o){return f[i];}}}return null;};e.prototype.addPersonalizableControl=function(o){var f,s=o.getControl();var g=sap.ui.getCore().byId(s);if(!g){L.error("couldn't obtain the control with the id="+s);return this;}this.addAggregation("personalizableControls",o,true);f=this._createControlWrapper(o);if(f){this._aPersonalizableControls.push(f);}if(this.isPageVariant()){return this;}this.setPersControler(g);return this;};e.prototype.getTransportSelection=function(){if(c){return Promise.resolve(c._getTransportSelection());}else{return this._loadFlex().then(function(){return c._getTransportSelection();});}};e.prototype._loadFlex=function(){var r=function(){return new Promise(function(R){sap.ui.require(["sap/ui/fl/apply/api/SmartVariantManagementApplyAPI","sap/ui/fl/write/api/SmartVariantManagementWriteAPI","sap/ui/fl/apply/api/FlexRuntimeInfoAPI"],function(f,g,h){F=f;c=g;d=h;R();});});};if(!this._oFlLibrary){if(!this._oPersistencyPromise){this._oPersistencyPromise=new Promise(function(R,f){this._fResolvePersistencyPromise=R;this._fRejectPersistencyPromise=f;}.bind(this));}this._oFlLibrary=new Promise(function(R){sap.ui.getCore().loadLibrary('sap.ui.fl',{async:true}).then(function(){r().then(R);});});return this._oFlLibrary;}else{return r();}};e.prototype.setPersControler=function(o){if(F&&c&&d){this._setPersControler(o);}else{this._loadFlex().then(function(){this._setPersControler(o);}.bind(this));}};e.prototype._setPersControler=function(o){if(!this._oPersoControl){if(d.isFlexSupported({element:o})){this._oPersoControl=o;this._handleGetChanges(o);}}};e.prototype.setPersistencyKey=function(k){this.setProperty("persistencyKey",k);this.setPersControler(this);return this;};e.prototype.isPageVariant=function(){if(this.getPersistencyKey()){return true;}return false;};e.prototype._getAdapter=function(){return this._oAdapter;};e.prototype._handleGetChanges=function(o){if(o&&F){this._oControlPromise=new Promise(function(r,f){F.loadChanges({control:this._oPersoControl}).then(function(v){this._fResolvePersistencyPromise();r(v);}.bind(this),function(g){this._fRejectPersistencyPromise(g);f(g);}.bind(this));}.bind(this));}};e.prototype.getVariantContent=function(o,k){var p,f=this._getChangeContent(k);if(f&&this.isPageVariant()){p=this._getControlPersKey(o);if(p){f=this._retrieveContent(f,p);}}return f;};e.prototype._getChangeContent=function(k){var o=null;if(k===this.STANDARDVARIANTKEY){o=this.getStandardVariant();}else{o=this._getChange(k);if(o){o=o.getContent();if(o){o=m({},o);}}}return o;};e.prototype._getChange=function(i){return F?F.getChangeById({control:this._oPersoControl,id:i}):null;};e.prototype._getAllPersonalizableControls=function(){return this._aPersonalizableControls;};e.prototype.removeAllPersonalizableControls=function(){this.removeAllAggregation("personalizableControls");this._aPersonalizableControls=[];};e.prototype.removePersonalizableControl=function(o){var p=this.removeAggregation("personalizableControls",o);if(p){this._aPersonalizableControls.some(function(P,i){if(P.control.getId()===p.getControl()){this._aPersonalizableControls.splice(i,1);return true;}return false;}.bind(this));}return p;};e.prototype.removePersonalizableControlById=function(o){var p=this.getAggregation("personalizableControls");if(p){p.some(function(P,i){if(P.getControl()===o.getId()){this.removePersonalizableControl(P);return true;}return false;}.bind(this));}};e.prototype._createVariantEntries=function(v){var n=null;var s,f=null;var o,g;var h=[];var i=[];this.removeAllVariantItems();if(v){for(n in v){if(n){o=v[n];if(o.isVariant()){g=new V({key:o.getId(),global:!o.isUserDependent(),executeOnSelection:this._getExecuteOnSelection(o),lifecycleTransportId:o.getRequest(),lifecyclePackage:o.getPackage(),namespace:o.getNamespace(),readOnly:this._isReadOnly(o),labelReadOnly:o.isLabelReadOnly(),author:this._getLRepUser(o)});g.setText(o.getText("variantName"));if(this._hasStoredStandardVariant(o)){f=o.getId();}this.addVariantItem(g);h.push(o.getId());}else{if((o.getChangeType()===C.addFavorite)||(o.getChangeType()===C.removeFavorite)){i.push(o);}}}}}if(this._oPersoControl){s=this._getDefaultVariantKey();if(s){this.setInitialSelectionKey(s);}var j=F.isApplicationVariant({control:this._oPersoControl});if(j){this._setIndustrySolutionMode(j);j=F.isVendorLayer();this._setVendorLayer(j);}if(this._getIndustrySolutionMode()){if(f){this._sAppStandardVariantKey=f;this.setStandardVariantKey(f);}}if(F&&F.isVariantDownport()){this._enableManualVariantKey(true);}}this._aFavoriteChanges=i;this.applyDefaultFavorites(h);return h;};e.prototype.applyDefaultFavorites=function(v,s){if(this._aFavoriteChanges&&(this._aFavoriteChanges.length>0)){this._applyFavorites(this._aFavoriteChanges);}else{if(!s){this._applyDefaultFavorites(v);}else{this._applyDefaultFavoritesForSelectionVariants(v);}}};e.prototype._applyDefaultFavoritesForSelectionVariants=function(v){v.forEach(function(s){var o=this.getItemByKey(s);if(o){this._setFavorite(s);}}.bind(this));};e.prototype._applyDefaultFavorites=function(v){if(!this._sAppStandardVariantKey){this.setStandardFavorite(true);this._setFavorite(this.STANDARDVARIANTKEY);}v.forEach(function(s){var o=this._getChange(s);var f=this.getItemByKey(s);if(o&&f){if(!this._isReadOnly(o)){this._setFavorite(s);}else if(o.getLayer()==="VENDOR"){this._setFavorite(s);}}}.bind(this));};e.prototype._applyFavorites=function(f){f.forEach(function(o){var v,g=o.getContent();if(g&&g.key){if(g.key===this.STANDARDVARIANTKEY){this.setStandardFavorite(g.visible);}else{v=this.getItemByKey(g.key);if(v){v.setFavorite(g.visible);}}}}.bind(this));};e.prototype._addFavorites=function(f){var A=f.filter(function(o){return o.visible===true;});var r=f.filter(function(o){return o.visible===false;});this._createFavoriteTypeChanges(A,r);};e.prototype._createFavoriteTypeChanges=function(A,r){if(!A.length&&!r.length){return;}this._createFavoriteChanges(A,C.addFavorite);this._createFavoriteChanges(r,C.removeFavorite);};e.prototype._createFavoriteChanges=function(f,s){if(!c||!f.length){return;}if(!s){throw new Error("sChangeType should be filled");}f.forEach(function(o){c.add({control:this._oPersoControl,changeSpecificData:{type:s,content:o,isUserDependent:true}});}.bind(this));};e.prototype._isReadOnly=function(o){var r=o.isReadOnly();if(r&&!o.isChangeFromOtherSystem()){var u=this._getUser();if(u){return!(u.getId().toUpperCase()===o.getOwnerId().toUpperCase());}}return r;};e.prototype._getUser=function(){var u=null;if(sap.ushell&&sap.ushell.Container&&sap.ushell.Container.getUser){u=sap.ushell.Container.getUser();}return u;};e.prototype.getVariantsInfo=function(f){if(!f){L.error("'getVariantsInfo' failed. Expecting callBack not passed.");return;}var n=null;var v;var g=[];var t=this;try{if(F){F.loadChanges({control:this._oPersoControl}).then(function(i){if(i){for(n in i){if(n){v=i[n];if(v.isVariant()){g.push({key:v.getId(),text:v.getText("variantName")});}}}}f(g);},function(i){var E="'getChanges' failed:";if(i&&i.message){E+=(' '+i.message);}t._setErrorValueState(t.oResourceBundle.getText("VARIANT_MANAGEMENT_READ_FAILED"),E);f(g);});}}catch(h){this._setErrorValueState(this.oResourceBundle.getText("VARIANT_MANAGEMENT_READ_FAILED"),"'getChanges' throws an exception");}};e.prototype.getCurrentVariantId=function(){var k=this._getCurrentVariantId();if(k===this.STANDARDVARIANTKEY){k="";}return k;};e.prototype._getCurrentVariantId=function(){var k=this.getSelectionKey();return k;};e.prototype.setCurrentVariantId=function(v,D){var i=this._determineVariantId(v);if(this._oPersoControl){if(!this._oStandardVariant){this._setSelectionByKey(i);}else{this._applyCurrentVariantId(i,D);}}};e.prototype._applyCurrentVariantId=function(v,D){var o;if(this._oPersoControl){o=this._getChangeContent(v);if(o){this._setSelectionByKey(v);if(!D){if(this.isPageVariant()){this._applyVariants(o,"SET_VM_ID");}else{this._applyVariant(this._oPersoControl,o,"SET_VM_ID");}}}}};e.prototype._determineVariantId=function(v){var i=v;if(!i){i=this.getStandardVariantKey();}else{if(!this.getItemByKey(i)){i=this.getStandardVariantKey();}}return i;};e.prototype.initialise=function(f,p){var o,E;try{if(p&&f){o=this._getControlWrapper(p);if(!o){L.error("initialise on an unknown control.");return;}if(o.bInitialized){L.error("initialise on "+p.getId()+" already executed");return;}o.fInitCallback=f;}else if(!this.isPageVariant()){o=this._getControlWrapper(this._oPersoControl);}if(this._oPersistencyPromise){this._oPersistencyPromise.then(function(){if(this._oControlPromise&&this._oPersoControl&&o){Promise.all([this._oMetadataPromise,this._oControlPromise,F.isVariantSharingEnabled()]).then(function(v){this._dataReceived(v[1],v[2],o);}.bind(this),function(h){E="'getChanges' failed:";if(h&&h.message){E+=(' '+h.messages);}this._errorHandling(E,f,p);}.bind(this),function(h){if(h&&h.message){E=h.message;}else{E="accessing either flexibility functionality or odata metadata.";}this._errorHandling("'initialise' failed: "+E,f,p);}.bind(this));}else{this._errorHandling("'initialise' no personalizable component available",f,p);}}.bind(this),function(h){if(h&&h.message){E=h.message;}else{E="accessing the flexibility functionality.";}this._errorHandling("'initialise' failed: "+E,f,p);}.bind(this));}else{this._errorHandling("'initialise' no '_oPersistencyPromise'  available",f,p);}}catch(g){this._errorHandling("'getChanges' throws an exception",f,p);}};e.prototype._errorHandling=function(E,f,p){var g={variantKeys:[]};this._setErrorValueState(this.oResourceBundle.getText("VARIANT_MANAGEMENT_READ_FAILED"),E);if(f&&p){f.call(p);}else{this.fireEvent("initialise",g);}if(p.variantsInitialized){p.variantsInitialized();}};e.prototype._dataReceived=function(v,i,o){var D,k,f,p={variantKeys:[]};var A=this._getAdapter();if(this._bIsBeingDestroyed){return;}if(!this._bIsInitialized){this.setShowShare(i);this._bIsInitialized=true;p.variantKeys=this._createVariantEntries(v);if(A){A.createSelectionPresentationVariants(this);}f=this._getExecuteOnSelectOnStandardVariant();if(f!==null){this._executeOnSelectForStandardVariantByUser(f);}k=this._getDefaultVariantKey();if(k){D=this._getChangeContent(k);if(D){this.setDefaultVariantKey(k);this.setInitialSelectionKey(k);}}if(this._sAppStandardVariantKey){var g=this._getChange(this._sAppStandardVariantKey);if(g){g=g.getContent();if(g){g=m({},g);}}this._oAppStdContent=g;}}this._initialize(p,o);};e.prototype._initialize=function(p,o){var k,f=null,i=this.isPageVariant();if(this._oAppStdContent){if((o.type==="table")||(o.type==="chart")){if(i){this._applyControlVariant(o.control,this._oAppStdContent,"STANDARD",true);}else{this._applyVariant(o.control,this._oAppStdContent,"STANDARD",true);}}}if(o.fInitCallback){o.fInitCallback.call(o.control);delete o.fInitCallback;o.bInitialized=true;}else{this.fireEvent("initialise",p);}k=this.getSelectionKey();if(k&&(k!==this.getStandardVariantKey())){f=this._getChangeContent(k);}else if(this._oAppStdContent){f=this._oAppStdContent;if((o.type==="table")||(o.type==="chart")){f=null;}}var r;if(this._sAppStandardVariantKey){r=this._updateStandardVariant(o,this._oAppStdContent);}else{r=this._setStandardVariant(o);}b.resolve(r).then(function(r){o.loaded.resolve();if(f){if(i){this._applyControlVariant(o.control,f,"INIT",true);}else{this._applyVariant(o.control,f,"INIT",true);}}if(o.control.variantsInitialized){o.control.variantsInitialized();}if((this._getCurrentVariantId()===this.getStandardVariantKey())&&this.getExecuteOnSelectForStandardVariant()){if(o.control.search){o.control.search();}}if(!this.getEnabled()){this.setEnabled(true);}}.bind(this)).catch(function(g){L.error("'_initialize' throws an exception:"+g.message);}).unwrap();};e.prototype.setInitialState=function(){var k=this._getDefaultVariantKey()||this.getStandardVariantKey();if(k){this.setInitialSelectionKey(k);this._triggerSelectVariant(k,"INIT_STATE");}};e.prototype._updateVariant=function(v){if(this._isIndustrySolutionModeAndVendorLayer()||(v.key!==this.getStandardVariantKey())){if(v){var o=this._getChange(v.key);if(o){try{if((v.lifecycleTransportId!==null)&&(v.lifecycleTransportId!==undefined)){o.setRequest(v.lifecycleTransportId);}return b.resolve(this._fetchContentAsync()).then(function(g){if(g){var i=this.getItemByKey(v.key);if(i){g.executeOnSelection=i.getExecuteOnSelection();}if(g.standardvariant!==undefined){delete g.standardvariant;}if(this._isIndustrySolutionModeAndVendorLayer()&&(v.key===this.getStandardVariantKey())){g.standardvariant=true;}o.setContent(g);if(v.def===true){this._setDefaultVariantKey(v.key);}}this._save(false);this.setEnabled(true);}.bind(this)).catch(function(f){L.error("'_updateVariant' throws an exception:"+f.message);}).unwrap();}catch(f){L.error("'_updateVariant' throws an exception");}}}}};e.prototype._createChangeHeader=function(){if(this.isPageVariant()){return{type:"page",dataService:"n/a"};}var f=this._getAllPersonalizableControls();if(f&&f.length>0){return{type:f[0].type,dataService:f[0].dataSource};}};e.prototype._newVariant=function(v){var i,o,I=false;if(v&&c){var t=this._createChangeHeader();var u=!v.global;var p="";if((v.lifecyclePackage!==null)&&(v.lifecyclePackage!==undefined)){p=v.lifecyclePackage;}var T="";if((v.lifecycleTransportId!==null)&&(v.lifecycleTransportId!==undefined)){T=v.lifecycleTransportId;}i=this._isVariantDownport()?v.key:null;if(this._isIndustrySolutionModeAndVendorLayer()&&(this.getStandardVariantKey()===this.STANDARDVARIANTKEY)){if((T||p)&&(v.name===this.oResourceBundle.getText("VARIANT_MANAGEMENT_STANDARD"))){this.setStandardVariantKey(i);I=true;}}return b.resolve(this._fetchContentAsync()).then(function(f){if(f){if(v.exe){f.executeOnSelection=v.exe;}if(v.tile){f.tile=v.tile;}if(f.standardvariant!==undefined){delete f.standardvariant;}if(I){f.standardvariant=true;}}var P={type:t.type,ODataService:t.dataSource,texts:{variantName:v.name},content:f,isVariant:true,packageName:p,isUserDependent:u,id:i};i=c.add({control:this._oPersoControl,changeSpecificData:P});if(v.implicit&&!this.verifyVariantKey(v.key)){v.key=this.createVariantEntry(v);}this.replaceKey(v.key,i);this.setInitialSelectionKey(i);if(this._isIndustrySolutionModeAndVendorLayer()&&((v.key===this.STANDARDVARIANTKEY)||this._isVariantDownport())){this.setStandardVariantKey(i);}o=this._getChange(i);if(o){o.setRequest(T);var g=this.getItemByKey(i);if(g){g.setNamespace(o.getNamespace());}}if(v.def===true){this._setDefaultVariantKey(i);}this._setFavorite(i);this._save(true);this.setEnabled(true);}.bind(this)).catch(function(f){L.error("'_newVariant' throws an exception:"+f.message);}).unwrap();}};e.prototype._setFavorite=function(i){var I=this.getItemByKey(i);if(I){I.setFavorite(true);}this._addFavorites([{key:i,visible:true}]);};e.prototype._fetchContent=function(){var o,p,f,g={};var h=this._getAllPersonalizableControls();for(var i=0;i<h.length;i++){o=h[i];if(o&&o.control&&o.control.fetchVariant){f=o.control.fetchVariant();if(f){f=m({},f);if(this.isPageVariant()){p=this._getControlPersKey(o);if(p){g=this._assignContent(g,f,p);}else{L.error("no persistancy key retrieved");}}else{g=f;break;}}}}return g;};e.prototype._fetchContentAsync=function(){var o,p,f,g={},h=[],j=[];var k=this._getAllPersonalizableControls();for(var i=0;i<k.length;i++){o=k[i];if(o&&o.control&&o.control.fetchVariant){f=o.control.fetchVariant();if(f){if(f&&f instanceof Promise){this.setEnabled(false);h.push(f);j.push(o);continue;}f=m({},f);if(this.isPageVariant()){p=this._getControlPersKey(o);if(p){g=this._assignContent(g,f,p);}else{L.error("no persistancy key retrieved");}}else{g=f;break;}}}}if(h.length>0){var r=null;var P=new Promise(function(n,s){r=n;});Promise.all(h).then(function(n){for(var i=0;i<n.length;i++){f=m({},n[i]);if(this.isPageVariant()){p=this._getControlPersKey(j[i]);if(p){g=this._assignContent(g,f,p);}else{L.error("no persistancy key retrieved");}}else{g=f;break;}}r(g);}.bind(this));return P;}else{return g;}};e.prototype._getControlPersKey=function(o){var p=null;if(o.keyName){if(o.keyName==="id"){p=o.control.getId();}else{p=o.control.getProperty(o.keyName);}}else{var f=this._getControlWrapper(o);if(f&&f.keyName){if(f.keyName==="id"){p=f.control.getId();}else{p=f.control.getProperty(f.keyName);}}}return p;};e.prototype._appendLifecycleInformation=function(v,i){var t;var I=this.getItemByKey(i);if(I){t=I.getLifecycleTransportId();if(t===null||t===undefined){t="";}if(v){v.setRequest(t);}}};e.prototype._renameVariant=function(v){if(v.key!==this.getStandardVariantKey()){if(v){var o=this._getChange(v.key);if(o){o.setText("variantName",v.name);this._appendLifecycleInformation(o,v.key);}}}};e.prototype._deleteVariants=function(v){var i;if(v&&v.length){var s=this._getDefaultVariantKey();for(i=0;i<v.length;i++){if(v[i]===this.getStandardVariantKey()){if(!this._isIndustrySolutionModeAndVendorLayer()){continue;}else{this.setStandardVariantKey(this.STANDARDVARIANTKEY);}}var o=this._getChange(v[i]);if(o){o.markForDeletion();if(s&&s===v[i]){this._setDefaultVariantKey("");}this._appendLifecycleInformation(o,v[i]);}}}};e.prototype._getDefaultVariantKey=function(){var D="";if(F){D=F.getDefaultVariantId({control:this._oPersoControl});}return D;};e.prototype._setDefaultVariantKey=function(v){if(c){c.setDefaultVariantId({control:this._oPersoControl,defaultVariantId:v});}};e.prototype._getExecuteOnSelectOnStandardVariant=function(){var E=null;if(F){E=F.getExecuteOnSelect({control:this._oPersoControl});}return E;};e.prototype._setExecuteOnSelectOnStandardVariant=function(f){if(c){c.setExecuteOnSelect({control:this._oPersoControl,executeOnSelect:f});}};e.prototype._isVariantDownport=function(){var D=false;if(F){D=F.isVariantDownport();}return D;};e.prototype._getExecuteOnSelection=function(v){var o;if(v){o=v.getContent();if(o&&(o.executeOnSelection!==undefined)){return o.executeOnSelection;}}return false;};e.prototype._hasStoredStandardVariant=function(v){var o;if(v){o=v.getContent();if(o&&o.standardvariant){return o.standardvariant;}}return false;};e.prototype._setExecuteOnSelections=function(v){var i;if(v&&v.length){for(i=0;i<v.length;i++){if(v[i].key===this.STANDARDVARIANTKEY){this._setExecuteOnSelectOnStandardVariant(v[i].exe);continue;}var o=this._getChange(v[i].key);if(o){var j=o.getContent();if(j){j.executeOnSelection=v[i].exe;o.setContent(j);}this._appendLifecycleInformation(o,v[i].key);}}}};e.prototype._save=function(n,i){var t=this;if(c){try{c.save({control:this._oPersoControl}).then(function(){if(!i){if(n){t._updateUser();}t.fireEvent("afterSave");}},function(g){var E="'_save' failed:";if(g&&g.message){E+=(' '+g.message);}t._setErrorValueState(t.oResourceBundle.getText("VARIANT_MANAGEMENT_SAVE_FAILED"),E);});}catch(f){this._setErrorValueState(this.oResourceBundle.getText("VARIANT_MANAGEMENT_SAVE_FAILED"),"'_save' throws an exception");}}};e.prototype._updateUser=function(){var i=this.getInitialSelectionKey();var u=this._getLRepUser(this._getChange(i));if(u){this._assignUser(i,u);}};e.prototype._getLRepUser=function(o){var u=null;if(o&&o.getDefinition()&&o.getDefinition().support){u=o.getDefinition().support.user?o.getDefinition().support.user:"";}return u;};e.prototype.fireSave=function(v){var E={};if(v){if(v.hasOwnProperty("tile")){E.tile=v.tile;}if(v.overwrite){if(this._isIndustrySolutionModeAndVendorLayer()||(v.key!==this.getStandardVariantKey())){this.fireEvent("save",E);if(v.key===this.STANDARDVARIANTKEY){this._newVariant(v);}else{this._updateVariant(v);}}}else{this.fireEvent("save",E);this._newVariant(v);}}};e.prototype.fireManage=function(v){var i;if(v){if(v.renamed){for(i=0;i<v.renamed.length;i++){this._renameVariant(v.renamed[i]);}}if(v.deleted){this._deleteVariants(v.deleted);}if(v.exe){this._setExecuteOnSelections(v.exe);}if(v.def){var D=this._getDefaultVariantKey();if(D!==v.def){this._setDefaultVariantKey(v.def);}}if(v.fav&&(v.fav.length>0)){this._addFavorites(v.fav);}if((v.deleted&&v.deleted.length>0)||(v.renamed&&v.renamed.length>0)||(v.exe&&v.exe.length>0)||v.def){this._save();}else if(v.fav&&(v.fav.length>0)){this._save(false,true);}this.fireEvent("manage",v);}};e.prototype.fireSelect=function(v,s){if(this._oPersoControl&&v&&v.key){this._triggerSelectVariant(v.key,s);this.fireEvent("select",v);}};e.prototype._selectVariant=function(v,s){this.fireSelect({key:v},s);};e.prototype._checkForSelectionHandler=function(v){var h=null,H=Object.keys(this._oSelectionVariantHandler);if(H.length>-1){H.some(function(k){if(v.indexOf(k)===0){h=this._oSelectionVariantHandler[k];return true;}return false;}.bind(this));}return h;};e.prototype._triggerSelectVariant=function(v,s){var o,h=this._checkForSelectionHandler(v);if(this._getAdapter()&&(v.substring(0,1)==="#")){this._applyUiState(v,s);return;}if(h){o=this._triggerSpecialSelectVariant(v,s,h);}else{o=this._triggerGeneralSelectVariant(v,s);}if(o){if(this.isPageVariant()){this._applyVariants(o,s);}else{this._applyVariant(this._oPersoControl,o,s);}}};e.prototype._triggerSpecialSelectVariant=function(v,s,h){return h.callback.call(h.handler,v,s);};e.prototype._triggerGeneralSelectVariant=function(v,s){var o=this._getChangeContent(v);if(o){o=m({},o);if((v===this.STANDARDVARIANTKEY)&&this.getExecuteOnSelectForStandardVariant()){o.executeOnSelection=this.getExecuteOnSelectForStandardVariant();}}return o;};e.prototype.currentVariantSetModified=function(f){if(!this._bApplyingUIState){a.prototype.currentVariantSetModified.apply(this,arguments);}};e.prototype._applyControlUiState=function(o,f){if(o&&f){o.loaded.then(function(){this._bApplyingUIState=true;if(o.control.setUiStateAsVariant){o.control.setUiStateAsVariant(f);}this._bApplyingUIState=false;}.bind(this));}};e.prototype._applyUiState=function(v,s){var i,A=this._getAdapter(),o=null,f=this._getAllPersonalizableControls();if(A){o=A.getUiState(v);for(i=0;i<f.length;i++){if(f[i]&&f[i].control&&f[i].loaded){this._applyControlUiState(f[i],o,s);}}}};e.prototype._applyControlWrapperVariants=function(o,f,s){var t=this;if(o){o.loaded.then(function(){t._applyControlVariant(o.control,f,s);});}};e.prototype._applyVariants=function(o,s){var i,f=this._getAllPersonalizableControls();for(i=0;i<f.length;i++){if(f[i]&&f[i].control&&f[i].loaded){this._applyControlWrapperVariants(f[i],o,s);}}};e.prototype._setStandardVariant=function(o){var f=o.control;if(f){if(f.fireBeforeVariantSave){f.fireBeforeVariantSave(a.STANDARD_NAME);}return this._assignStandardVariantAsync(o);}return null;};e.prototype._retrieveContent=function(o,p){var r=o;if(this.isPageVariant()&&o){r=o[p];if(!r&&(p===this.getPersistencyKey())&&this._aPersonalizableControls&&this._aPersonalizableControls.length===1){r=o;}}return r;};e.prototype._assignContent=function(t,o,p){if(this.isPageVariant()){if(!((p===this.getPersistencyKey())&&this._aPersonalizableControls&&this._aPersonalizableControls.length===1)){t[p]=o;}else{t=o;}}else{t=o;}return t;};e.prototype._updateStandardVariant=function(o,f){if(o.control){var g=f;if(this.isPageVariant()){var p=this._getControlPersKey(o);if(p){g=this._retrieveContent(f,p);}}return this._assignStandardVariantForControl(o,g);}return f;};e.prototype._assignStandardVariantAsync=function(o){var s=null;if(o.control){if(o.control.fetchVariant){s=o.control.fetchVariant();}if(s instanceof Promise){this.setEnabled(false);}return b.resolve(s).then(function(s){return this._assignStandardVariantForControl(o,s);}.bind(this)).catch(function(f){L.error("'_assignStandardVariant' throws an exception: "+f.message);}).unwrap();}return null;};e.prototype._assignStandardVariantForControl=function(o,s){var f=s;if(o){if(this.isPageVariant()){var p=this._getControlPersKey(o.control);if(p){if(!this._oStandardVariant){this._oStandardVariant={};}this._oStandardVariant=this._assignContent(this._oStandardVariant,f,p);}}else{this._oStandardVariant=f;}}return this._oStandardVariant;};e.prototype.getStandardVariant=function(o){var p,f,g=null;if(this._oStandardVariant){if(!o){g=this._oStandardVariant;}else{if(this.isPageVariant()){f=this._getControlWrapper(o);if(f){p=this._getControlPersKey(o);if(p){g=this._retrieveContent(this._oStandardVariant,p);}}}else{if((o===this._oPersoControl)){g=this._oStandardVariant;}}}}return g;};e.prototype._applyVariant=function(o,f,s,i){if(o&&o.applyVariant){o.applyVariant(f,s,i);}};e.prototype._applyControlVariant=function(o,f,s,i){var g,p;p=this._getControlPersKey(o);if(p){g=this._retrieveContent(f,p);if(g){g.executeOnSelection=f.executeOnSelection;this._applyVariant(o,g,s,i);}}};e.prototype.registerSelectionVariantHandler=function(h,k){this._oSelectionVariantHandler[k]=h;};e.prototype.unregisterSelectionVariantHandler=function(h){var E=null;if(!this._oSelectionVariantHandler){return;}if(typeof h==='string'){E=h;}else{Object.keys(this._oSelectionVariantHandler).some(function(k){if(this._oSelectionVariantHandler[k].handler===h){E=k;return true;}return false;}.bind(this));}if(E){delete this._oSelectionVariantHandler[E];}};e.prototype._setErrorValueState=function(t,s){this.setInErrorState(true);if(s){L.error(s);}};e.prototype.exit=function(){a.prototype.exit.apply(this,arguments);this._aPersonalizableControls=null;this._fResolvePersistencyPromise=null;this._fRejectPersistencyPromise=null;this._oMetadataPromise=null;this._oControlPromise=null;this._oFlLibrary=null;this._oPersistencyPromise=null;this._oPersoControl=null;this._oAppStdContent=null;this._sAppStandardVariantKey=null;this._oSelectionVariantHandler=null;this._aFavoriteChanges=null;if(this._oAdapter){this._oAdapter.destroy();this._oAdapter=null;}};return e;},true);