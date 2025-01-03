/*!
 * OpenUI5
 * (c) Copyright 2009-2019 SAP SE or an SAP affiliate company.
 * Licensed under the Apache License, Version 2.0 - see LICENSE.txt.
 */
sap.ui.define(["sap/base/Log","sap/ui/core/CalendarType","sap/ui/core/format/DateFormat","sap/ui/model/FormatException","sap/ui/model/ParseException","sap/ui/model/ValidateException","sap/ui/model/odata/type/ODataType","sap/ui/thirdparty/jquery"],function(L,C,D,F,P,V,O,q){"use strict";function g(t){return sap.ui.getCore().getLibraryResourceBundle().getText("EnterTime",[t.formatValue("23:59:58","string")]);}function a(t){var f;if(!t.oUiFormat){f=q.extend({strictParsing:true},t.oFormatOptions);f.UTC=true;t.oUiFormat=D.getTimeInstance(f);}return t.oUiFormat;}function s(t,c){var n,p;t.oConstraints=undefined;if(c){n=c.nullable;p=c.precision;if(n===false){t.oConstraints={nullable:false};}else if(n!==undefined&&n!==true){L.warning("Illegal nullable: "+n,null,t.getName());}if(p===Math.floor(p)&&p>0&&p<=12){t.oConstraints=t.oConstraints||{};t.oConstraints.precision=p;}else if(p!==undefined&&p!==0){L.warning("Illegal precision: "+p,null,t.getName());}}}var T=O.extend("sap.ui.model.odata.type.TimeOfDay",{constructor:function(f,c){O.apply(this,arguments);this.oModelFormat=undefined;this.rTimeOfDay=undefined;this.oUiFormat=undefined;s(this,c);this.oFormatOptions=f;}});T.prototype._handleLocalizationChange=function(){this.oUiFormat=null;};T.prototype._resetModelFormatter=function(){this.oModelFormat=undefined;};T.prototype.formatValue=function(v,t){var d,i,p;if(v===undefined||v===null){return null;}p=this.getPrimitiveType(t);switch(p){case"any":return v;case"object":case"string":i=v.indexOf(".");if(i>=0){v=v.slice(0,i+4);}d=this.getModelFormat().parse(v);if(d){if(p==="object"){return new Date(1970,0,1,d.getUTCHours(),d.getUTCMinutes(),d.getUTCSeconds());}return a(this).format(d);}throw new F("Illegal "+this.getName()+" value: "+v);default:throw new F("Don't know how to format "+this.getName()+" to "+t);}};T.prototype.getModelFormat=function(){var p="HH:mm:ss",i;if(!this.oModelFormat){i=this.oConstraints&&this.oConstraints.precision;if(i){p+="."+"".padEnd(i,"S");}this.oModelFormat=D.getTimeInstance({calendarType:C.Gregorian,pattern:p,strictParsing:true,UTC:true});}return this.oModelFormat;};T.prototype.getName=function(){return"sap.ui.model.odata.type.TimeOfDay";};T.prototype.parseValue=function(v,S){var d;if(v===""||v===null){return null;}switch(this.getPrimitiveType(S)){case"object":return this.getModelFormat().format(v,false);case"string":d=a(this).parse(v);if(!d){throw new P(g(this));}return this.getModelFormat().format(d);default:throw new P("Don't know how to parse "+this.getName()+" from "+S);}};T.prototype.validateValue=function(v){var p;if(v===null){if(this.oConstraints&&this.oConstraints.nullable===false){throw new V(g(this));}return;}if(!this.rTimeOfDay){p=this.oConstraints&&this.oConstraints.precision;this.rTimeOfDay=new RegExp("^(?:[01]\\d|2[0-3]):[0-5]\\d(?::[0-5]\\d"+(p?"(\\.\\d{1,"+p+"})?":"")+")?$");}if(!this.rTimeOfDay.test(v)){throw new V("Illegal sap.ui.model.odata.type.TimeOfDay value: "+v);}};return T;});
