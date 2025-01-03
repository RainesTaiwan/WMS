/*
 * ! SAPUI5

		(c) Copyright 2009-2019 SAP SE. All rights reserved
	
 */
sap.ui.define([],function(){"use strict";var D={localToUtc:function(d){return new Date(d.valueOf()-d.getTimezoneOffset()*60000);},utcToLocal:function(d){var o=d.getTimezoneOffset(),r=new Date(d.valueOf()+o*60000),R;R=r.getTimezoneOffset();if(o!==R){r=new Date(d.valueOf()+R*60000);}return r;},adaptPrecision:function(d,p){var m=d.getMilliseconds(),r;if(isNaN(p)||p>=3||m===0){return d;}if(p===0){m=0;}else if(p===1){m=Math.floor(m/100)*100;}else if(p===2){m=Math.floor(m/10)*10;}r=new Date(d);r.setMilliseconds(m);return r;},isDate:function(d,u){if(u){return d.getUTCHours()===0&&d.getUTCMinutes()===0&&d.getUTCSeconds()===0&&d.getUTCMilliseconds()===0;}else{return d.getHours()===0&&d.getMinutes()===0&&d.getSeconds()===0&&d.getMilliseconds()===0;}},normalizeDate:function(d,u){var r;if(this.isDate(d,u)){return d;}r=new Date(d);if(u){r.setUTCHours(0,0,0,0);}else{r.setHours(0,0,0,0);}return r;}};return D;});
