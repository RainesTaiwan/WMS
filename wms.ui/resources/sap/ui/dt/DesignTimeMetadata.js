/*!
 * OpenUI5
 * (c) Copyright 2009-2019 SAP SE or an SAP affiliate company.
 * Licensed under the Apache License, Version 2.0 - see LICENSE.txt.
 */
sap.ui.define(["sap/ui/thirdparty/jquery","sap/ui/base/ManagedObject","sap/ui/dt/ElementUtil","sap/ui/dt/DOMUtil","sap/base/util/merge"],function(q,M,E,D,m){"use strict";var a=M.extend("sap.ui.dt.DesignTimeMetadata",{metadata:{library:"sap.ui.dt",properties:{data:{type:"any",defaultValue:{}}}}});a.prototype.setData=function(d){this.setProperty("data",m({},this.getDefaultData(),d));return this;};a.prototype.getDefaultData=function(){return{ignore:false,domRef:undefined};};a.prototype.isIgnored=function(e){var i=this.getData().ignore;if(!i||(i&&typeof i==="function"&&!i(e))){return false;}return true;};a.prototype.markedAsNotAdaptable=function(){var A=this.getData().actions;return A==="not-adaptable";};a.prototype.getDomRef=function(){return this.getData().domRef;};a.prototype.getAssociatedDomRef=function(e,d,A){if(e){var o=E.getDomRef(e);var b=[];b.push(e);if(A){b.push(A);}if(typeof(d)==="function"){var r=d.apply(null,b);return r?q(r):r;}else if(o&&typeof(d)==="string"){return D.getDomRefForCSSSelector(o,d);}}};a.prototype.getAction=function(A,e){var d=this.getData();if(d.actions&&d.actions[A]){var v=d.actions[A];if(typeof(v)==="function"){v=v(e);}if(typeof(v)==="string"){return{changeType:v};}return v;}};a.prototype.getLibraryText=function(e,k,A){var o=e.getMetadata();return this._lookForLibraryTextInHierarchy(o,k,A);};a.prototype._lookForLibraryTextInHierarchy=function(o,k,A){var l;var p;var r;l=o.getLibraryName();r=this._getTextFromLibrary(l,k,A);if(!r){p=o.getParent();if(p&&p.getLibraryName){r=this._lookForLibraryTextInHierarchy(p,k,A);}else{r=k;}}return r;};a.prototype._getTextFromLibrary=function(l,k,A){var L=sap.ui.getCore().getLibraryResourceBundle(l+".designtime");if(L&&L.hasText(k)){return L.getText(k,A);}L=sap.ui.getCore().getLibraryResourceBundle(l);if(L&&L.hasText(k)){return L.getText(k,A);}};a.prototype.getLabel=function(){var l=this.getData().getLabel;return typeof l==="function"?l.apply(this,arguments):undefined;};a.prototype.getControllerExtensionTemplate=function(){return this.getData().controllerExtensionTemplate;};return a;});
