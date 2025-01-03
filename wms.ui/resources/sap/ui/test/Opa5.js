/*!
 * OpenUI5
 * (c) Copyright 2009-2019 SAP SE or an SAP affiliate company.
 * Licensed under the Apache License, Version 2.0 - see LICENSE.txt.
 */
sap.ui.define(['sap/ui/test/Opa','sap/ui/test/OpaPlugin','sap/ui/test/PageObjectFactory','sap/ui/base/Object','sap/ui/test/launchers/iFrameLauncher','sap/ui/test/launchers/componentLauncher','sap/ui/core/routing/HashChanger','sap/ui/test/matchers/Matcher','sap/ui/test/matchers/AggregationFilled','sap/ui/test/matchers/PropertyStrictEquals','sap/ui/test/pipelines/ActionPipeline','sap/ui/test/_ParameterValidator','sap/ui/test/_OpaLogger','sap/ui/thirdparty/URI','sap/ui/base/EventProvider','sap/ui/qunit/QUnitUtils','sap/ui/test/autowaiter/_autoWaiter','sap/ui/dom/includeStylesheet','sap/ui/thirdparty/jquery','sap/ui/test/_OpaUriParameterParser'],function(O,a,P,U,f,c,H,M,A,b,d,_,e,g,E,Q,h,i,$,j){"use strict";var l=e.getLogger("sap.ui.test.Opa5"),p=new a(),o=new d(),F="OpaFrame",v=new _({errorPrefix:"sap.ui.test.Opa5#waitFor"}),C=["visible","enabled","viewNamespace","viewName","viewId","fragmentId","autoWait"].concat(O._aConfigValuesForWaitFor),k=["check","error","success"].concat(O._aConfigValuesForWaitFor),m=[],n=new E();var q=j._getAppParams();var r=new g().search(true);var s=U.extend("sap.ui.test.Opa5",$.extend({},O.prototype,{constructor:function(){O.apply(this,arguments);}}));function S(){var x=this;var y={};var z=["source","timeout","autoWait","width","height"];if(arguments.length===1&&$.isPlainObject(arguments[0])){y=arguments[0];}else{var V=arguments;z.forEach(function(I,J){y[I]=V[J];});}if(y.source&&typeof y.source!=="string"){y.source=y.source.toString();}var B=new g(y.source?y.source:'');B.search($.extend(B.search(true),O.config.appParams));var D=w();D.success=function(){u({source:B.toString(),width:y.width||O.config.frameWidth,height:y.height||O.config.frameHeight});};this.waitFor(D);var G=w();G.check=f.hasLaunched;G.timeout=y.timeout||80;G.errorMessage="unable to load the IFrame with the url: "+y.source;x.waitFor(G);var L=w();L.success=function(){x._loadExtensions(f.getWindow());};this.waitFor(L);var W=w();W.autoWait=y.autoWait||false;W.timeout=y.timeout||80;return this.waitFor(W);}s.prototype.iStartMyUIComponent=function iStartMyUIComponent(x){var y=this;var z=false;x=x||{};var B=w();B.success=function(){var I=new g();I.search($.extend(I.search(true),O.config.appParams));window.history.replaceState({},"",I.toString());};this.waitFor(B);var D=w();D.success=function(){var I=sap.ui.require.toUrl("sap/ui/test/OpaCss")+".css";i(I);H.getInstance().setHash(x.hash||"");c.start(x.componentConfig).then(function(){z=true;});};this.waitFor(D);var G=w();G.errorMessage="Unable to load the component with the name: "+x.name;G.check=function(){return z;};if(x.timeout){G.timeout=x.timeout;}y.waitFor(G);var L=w();L.success=function(){y._loadExtensions(window);};this.waitFor(L);var W=w();W.autoWait=x.autoWait||false;W.timeout=x.timeout||80;return this.waitFor(W);};s.prototype.iTeardownMyUIComponent=function iTeardownMyUIComponent(){var x=w();x.success=function(){c.teardown();};var y=w();y.success=function(){var z=new g();z.search(r);window.history.replaceState({},"",z.toString());};return $.when(this.waitFor(x),this.waitFor(y));};s.prototype.iTeardownMyApp=function(){var x=this;var y=w();y.success=function(){x._unloadExtensions(s.getWindow());};var z=w();z.success=function(){if(f.hasLaunched()){this.iTeardownMyAppFrame();}else if(c.hasLaunched()){this.iTeardownMyUIComponent();}else{var B="A teardown was called but there was nothing to tear down use iStartMyComponent or iStartMyAppInAFrame";l.error(B,"Opa");throw new Error(B);}}.bind(this);return $.when(this.waitFor(y),this.waitFor(z));};s.iStartMyAppInAFrame=S;s.prototype.iStartMyAppInAFrame=S;function t(){var W=w();W.success=function(){f.teardown();};return this.waitFor(W);}s.iTeardownMyAppFrame=t;s.prototype.iTeardownMyAppFrame=t;s.prototype.hasAppStartedInAFrame=function(){return f.hasLaunched();};s.prototype.hasUIComponentStarted=function(){return c.hasLaunched();};s.prototype.hasAppStarted=function(){return f.hasLaunched()||c.hasLaunched();};s.prototype.waitFor=function(x){var y=x.actions,z=O._createFilteredConfig(C),B;x=$.extend({},z,x);x.actions=y;v.validate({validationInfo:s._validationInfo,inputToValidate:x});var D=x.check,G=null,I=x.success,R,J;B=O._createFilteredOptions(k,x);B.check=function(){var K=!!x.actions||x.autoWait;var L=s._getAutoWaiter();L.extendConfig(x.autoWait);if(K&&L.hasToWait()){return false;}var p=s.getPlugin();var N=$.extend({},x,{interactable:K});R=p._getFilteredControls(N,G);if(f.hasLaunched()&&$.isArray(R)){var T=[];R.forEach(function(V){T.push(V);});R=T;}if(R===a.FILTER_FOUND_NO_CONTROLS){l.debug("Matchers found no controls so check function will be skipped");return false;}if(D){return this._executeCheck(D,R);}return true;};B.success=function(){var W=O._getWaitForCounter();if(y&&(R||!J)){o.process({actions:y,control:R});}if(!I){return;}var K=[];if(R){K.push(R);}if(W.get()===0){l.timestamp("opa.waitFor.success");l.debug("Execute success handler");I.apply(this,K);return;}var L=w();if($.isPlainObject(x.autoWait)){L.autoWait=$.extend({},x.autoWait);}else{L.autoWait=x.autoWait;}L.success=function(){I.apply(this,K);};this.waitFor(L);};return O.prototype.waitFor.call(this,B);};s.getPlugin=function(){return f.getPlugin()||p;};s.getJQuery=function(){return f.getJQuery()||$;};s.getWindow=function(){return f.getWindow()||window;};s.getUtils=function(){return f.getUtils()||Q;};s.getHashChanger=function(){return f.getHashChanger()||H.getInstance();};s._getAutoWaiter=function(){return f._getAutoWaiter()||h;};s.extendConfig=function(x){O.extendConfig(x);O.extendConfig({appParams:q});s._getAutoWaiter().extendConfig(x.autoWait);};s.resetConfig=function(){O.resetConfig();O.extendConfig({viewNamespace:"",arrangements:new s(),actions:new s(),assertions:new s(),visible:true,enabled:undefined,autoWait:false,_stackDropCount:1});O.extendConfig({appParams:q});};s.getTestLibConfig=function(T){return O.config.testLibs&&O.config.testLibs[T]?O.config.testLibs[T]:{};};s.emptyQueue=O.emptyQueue;s.stopQueue=O.stopQueue;s.getContext=O.getContext;s.matchers={};s.matchers.Matcher=M;s.matchers.AggregationFilled=A;s.matchers.PropertyStrictEquals=b;s.createPageObjects=function(x){return P.create(x,s);};s.prototype._executeCheck=function(x,y){var z=[];y&&z.push(y);l.debug("Executing OPA check function on controls "+y);l.debug("Check function is:\n"+x);var R=x.apply(this,z);l.debug("Result of check function is: "+R||"not defined or null");return R;};s.prototype.iWaitForPromise=function(x){var y={viewName:null,controlType:null,id:null,searchOpenDialogs:false,autoWait:false};return O.prototype._schedulePromiseOnFlow.call(this,x,y);};s.resetConfig();function u(x){var I=sap.ui.require.toUrl("sap/ui/test/OpaCss")+".css";i(I);var y=$.extend({},x,{frameId:F,opaLogLevel:O.config.logLevel});return f.launch(y);}function w(){return{viewName:null,controlType:null,id:null,searchOpenDialogs:false,autoWait:false};}$(function(){if($("#"+F).length){u();}$("body").addClass("sapUiBody");$("html").height("100%");});s._validationInfo=$.extend({_stack:"string",viewName:"string",viewNamespace:"string",viewId:"string",fragmentId:"string",visible:"bool",enabled:"bool",matchers:"any",actions:"any",id:"any",controlType:"any",searchOpenDialogs:"bool",autoWait:"any"},O._validationInfo);s._getEventProvider=function(){return n;};s.prototype._loadExtensions=function(x){var y=this;var z=O.config.extensions?O.config.extensions:[];var B=$.when($.map(z,function(D){var G;var I=$.Deferred();x.sap.ui.require([D],function(J){G=new J();G.name=D;y._executeExtensionOnAfterInit(G,x).done(function(){s._getEventProvider().fireEvent('onExtensionAfterInit',{extension:G,appWindow:x});y._addExtension(G);I.resolve();}).fail(function(K){l.error(new Error("Error during extension init: "+K),"Opa");I.resolve();});});return I.promise();}));return this.iWaitForPromise(B);};s.prototype._unloadExtensions=function(x){var y=this;var z=$.when($.map(this._getExtensions(),function(B){var D=$.Deferred();s._getEventProvider().fireEvent('onExtensionBeforeExit',{extension:B});y._executeExtensionOnBeforeExit(B,x).done(function(){D.resolve();}).fail(function(G){l.error(new Error("Error during extension init: "+G),"Opa");D.resolve();});return D.promise();}));this.iWaitForPromise(z);};s.prototype._addExtension=function(x){m.push(x);};s.prototype._getExtensions=function(){return m;};s.prototype._executeExtensionOnAfterInit=function(x,y){var D=$.Deferred();var z=x.onAfterInit;if(z){z.bind(y)().done(function(){D.resolve();}).fail(function(B){D.reject(new Error("Error while waiting for extension: "+x.name+" to init, details: "+B));});}else{D.resolve();}return D.promise();};s.prototype._executeExtensionOnBeforeExit=function(x,y){var D=$.Deferred();var z=x.onBeforeExit;if(z){z.bind(y)().done(function(){D.resolve();}).fail(function(B){D.reject(new Error("Error while waiting for extension: "+x.name+" to exit, details: "+B));});}else{D.resolve();}return D.promise();};return s;});