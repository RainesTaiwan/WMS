window.undefined = window.undefined;
Wee = {};
Wee.register = {};
//------------------------------------------------------------------------------------------------------------------------------------
Wee.apply = function(o, c, defaults){
    if(defaults){
        Wee.apply(o, defaults);
    }
    if(o && c && typeof c == 'object'){
        for(var p in c){
            o[p] = c[p];
        }
    }
    return o;
};
//------------------------------------------------------------------------------------------------------------------------------------
( function(){
	
	var QUID_AlphaNumericArray = new Array( '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' );
	var QUID_AlphaArray = new Array( 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' );
	
	function createQUID()
	{
		return createAlphaNumericText( 32 );
	}
	function createAlphaNumericText(length)
	{
		// alert( "FUNCTION createAlphaNumericText() BEGIN" );
		var str = "";
		var c = '\0';
		// ------------------------------------------------------------------------------------------------------------------
		c = QUID_AlphaArray[randomInt( QUID_AlphaArray.length )];
		str += c;
		// ------------------------------------------------------------------------------------------------------------------
		for( var i = 0; i < length - 1; i++ ) {
			c = QUID_AlphaNumericArray[randomInt( QUID_AlphaNumericArray.length )];
			str += c;
		} // for i
		// ------------------------------------------------------------------------------------------------------------------
		return str;
	}
	
	function randomInt(max_int)
	{
		var ran_unrounded = Math.random() * max_int;
		var ran_number = Math.floor( ran_unrounded );
		return ran_number;
	}
	var toString = Object.prototype.toString,
        ua = navigator.userAgent.toLowerCase(),
        check = function(r){
            return r.test(ua);
        },
        DOC = document,
        docMode = document.documentMode,
        isOpera = check(/opera/),
        isOpera10_5 = isOpera && check(/version\/10\.5/),
        isChrome = check(/\bchrome\b/),
        isWebKit = check(/webkit/),
        isSafari = !isChrome && check(/safari/),
        isSafari2 = isSafari && check(/applewebkit\/4/), // unique to Safari 2
        isSafari3 = isSafari && check(/version\/3/),
        isSafari4 = isSafari && check(/version\/4/),
        isSafari5_0 = isSafari && check(/version\/5\.0/),
        isSafari5 = isSafari && check(/version\/5/),
        isIE = !isOpera && check(/msie/),
        isIE7 = isIE && ((check(/msie 7/) && docMode != 8 && docMode != 9 && docMode != 10) || docMode == 7),
        isIE8 = isIE && ((check(/msie 8/) && docMode != 7 && docMode != 9 && docMode != 10) || docMode == 8),
        isIE9 = isIE && ((check(/msie 9/) && docMode != 7 && docMode != 8 && docMode != 10) || docMode == 9),
        isIE10 = isIE && ((check(/msie 10/) && docMode != 7 && docMode != 8 && docMode != 9) || docMode == 10),
        isIE6 = isIE && check(/msie 6/),
        isGecko = !isWebKit && check(/gecko/),
        isGecko3 = isGecko && check(/rv:1\.9/),
        isGecko4 = isGecko && check(/rv:2\.0/),
        isGecko5 = isGecko && check(/rv:5\./),
        isGecko10 = isGecko && check(/rv:10\./),
        isFF3_0 = isGecko3 && check(/rv:1\.9\.0/),
        isFF3_5 = isGecko3 && check(/rv:1\.9\.1/),
        isFF3_6 = isGecko3 && check(/rv:1\.9\.2/),
        isWindows = check(/windows|win32/),
        isMac = check(/macintosh|mac os x/),
        isLinux = check(/linux/),
        isSecure = /^https/i.test(window.location.protocol);

    // remove css image flicker
    if(isIE6){
        try{
            DOC.execCommand("BackgroundImageCache", false, true);
        }catch(e){}
    }
    
	Wee.apply(Wee, {
		id : function(){
			return  createQUID();
        },
		applyIf : function(o, c){
            if(o){
                for(var p in c){
                    if(!Wee.isDefined(o[p])){
                        o[p] = c[p];
                    }
                }
            }
            return o;
        },
        extend : function(){
            var io = function(o){
                for(var m in o){
                    this[m] = o[m];
                }
            };
            var oc = Object.prototype.constructor;

            return function(sb, sp, overrides){
                if(Wee.isObject(sp)){
                    overrides = sp;
                    sp = sb;
                    sb = overrides.constructor != oc ? overrides.constructor : function(){sp.apply(this, arguments);};
                }
                var F = function(){},
                    sbp,
                    spp = sp.prototype;

                F.prototype = spp;
                sbp = sb.prototype = new F();
                sbp.constructor=sb;
                sb.superclass=spp;
                if(spp.constructor == oc){
                    spp.constructor=sp;
                }
                sb.override = function(o){
                    Wee.override(sb, o);
                };
                sbp.superclass = sbp.supr = (function(){
                    return spp;
                });
                sbp.override = io;
                Wee.override(sb, overrides);
                sb.extend = function(o){return Wee.extend(sb, o);};
                return sb;
            };
        }(),
        override : function(origclass, overrides){
            if(overrides){
                var p = origclass.prototype;
                Wee.apply(p, overrides);
                /*if(Wee.isIE && overrides.hasOwnProperty('toString')){
                    p.toString = overrides.toString;
                }*/
            }
        },
        namespace : function(){
            var o, d;
            Wee.each(arguments, function(v) {
                d = v.split(".");
                o = window[d[0]] = window[d[0]] || {};
                Wee.each(d.slice(1), function(v2){
                    o = o[v2] = o[v2] || {};
                });
            });
            return o;
        },
        urlEncode : function(o, pre){
            var empty,
                buf = [],
                e = encodeURIComponent;

            Wee.iterate(o, function(key, item){
                empty = Wee.isEmpty(item);
                Wee.each(empty ? key : item, function(val){
                    buf.push('&', e(key), '=', (!Wee.isEmpty(val) && (val != key || !empty)) ? (Wee.isDate(val) ? Wee.encode(val).replace(/"/g, '') : e(val)) : '');
                });
            });
            if(!pre){
                buf.shift();
                pre = '';
            }
            return pre + buf.join('');
        },
        urlDecode : function(string, overwrite){
            if(Wee.isEmpty(string)){
                return {};
            }
            var obj = {},
                pairs = string.split('&'),
                d = decodeURIComponent,
                name,
                value;
            Wee.each(pairs, function(pair) {
                pair = pair.split('=');
                name = d(pair[0]);
                value = d(pair[1]);
                obj[name] = overwrite || !obj[name] ? value :
                            [].concat(obj[name]).concat(value);
            });
            return obj;
        },
        urlAppend : function(url, s){
            if(!Wee.isEmpty(s)){
                return url + (url.indexOf('?') === -1 ? '?' : '&') + s;
            }
            return url;
        },
         toArray : function(){
             return isIE ?
                 function(a, i, j, res){
                     res = [];
                     for(var x = 0, len = a.length; x < len; x++) {
                         res.push(a[x]);
                     }
                     return res.slice(i || 0, j || res.length);
                 } :
                 function(a, i, j){
                     return Array.prototype.slice.call(a, i || 0, j || a.length);
                 }
         }(),
        isIterable : function(v){
            
        	if(Wee.isArray(v) || v.callee){
                return true;
            }
            
            if(/NodeList|HTMLCollection/.test(toString.call(v))){
                return true;
            }
            
            return ((typeof v.nextNode != 'undefined' || v.item) && Wee.isNumber(v.length));
        },
        each : function(array, fn, scope){
            if(Wee.isEmpty(array, true)){
                return;
            }
            if(!Wee.isIterable(array) || Wee.isPrimitive(array)){
                array = [array];
            }
            for(var i = 0, len = array.length; i < len; i++){
                if(fn.call(scope || array[i], array[i], i, array) === false){
                    return i;
                };
            }
        },
        iterate : function(obj, fn, scope){
            if(Wee.isEmpty(obj)){
                return;
            }
            if(Wee.isIterable(obj)){
                Wee.each(obj, fn, scope);
                return;
            }else if(Wee.isObject(obj)){
                for(var prop in obj){
                    if(obj.hasOwnProperty(prop)){
                        if(fn.call(scope || obj, prop, obj[prop], obj) === false){
                            return;
                        };
                    }
                }
            }
        },
        getDom : function(el){
            if(!el || !DOC){
                return null;
            }
            return el.dom ? el.dom : (Wee.isString(el) ? DOC.getElementById(el) : el);
        },
        isEmpty : function(v, allowBlank){
            return v === null || v === undefined || ((Wee.isArray(v) && !v.length)) || (!allowBlank ? v === '' : false);
        },
        isArray : function(v){
            return toString.apply(v) === '[object Array]';
        },
        isDate : function(v){
            return toString.apply(v) === '[object Date]';
        },
        isObject : function(v){
            return !!v && Object.prototype.toString.call(v) === '[object Object]';
        },
        isPrimitive : function(v){
            return Wee.isString(v) || Wee.isNumber(v) || Wee.isBoolean(v);
        },
        isFunction : function(v){
            return toString.apply(v) === '[object Function]';
        },
        isNumber : function(v){
            return typeof v === 'number' && isFinite(v);
        },
        isString : function(v){
            return typeof v === 'string';
        },
        isBoolean : function(v){
            return typeof v === 'boolean';
        },
        isElement : function(v) {
            return !!v && v.tagName;
        },
        isDefined : function(v){
            return typeof v !== 'undefined';
        }
	});
})();
//------------------------------------------------------------------------------------------------------------------------------------
Wee.namespace( "Wee.util","Wee.dm", "Wee.wr", "Wee.pm");
//------------------------------------------------------------------------------------------------------------------------------------
Wee.apply(Function.prototype, {
    createInterceptor : function(fcn, scope){
        var method = this;
        return !Wee.isFunction(fcn) ?
                this :
                function() {
                    var me = this,
                        args = arguments;
                    fcn.target = me;
                    fcn.method = method;
                    return (fcn.apply(scope || me || window, args) !== false) ?
                            method.apply(me || window, args) :
                            null;
                };
    },
    createCallback : function(/*args...*/){
        var args = arguments,
            method = this;
        return function() {
            return method.apply(window, args);
        };
    },
    createDelegate : function(obj, args, appendArgs){
        var method = this;
        return function() {
            var callArgs = args || arguments;
            if (appendArgs === true){
                callArgs = Array.prototype.slice.call(arguments, 0);
                callArgs = callArgs.concat(args);
            }else if (Wee.isNumber(appendArgs)){
                callArgs = Array.prototype.slice.call(arguments, 0); // copy arguments first
                var applyArgs = [appendArgs, 0].concat(args); // create method call params
                Array.prototype.splice.apply(callArgs, applyArgs); // splice them in
            }
            return method.apply(obj || window, callArgs);
        };
    },

    defer : function(millis, obj, args, appendArgs){
        var fn = this.createDelegate(obj, args, appendArgs);
        if(millis > 0){
            return setTimeout(fn, millis);
        }
        fn();
        return 0;
    }
});
//------------------------------------------------------------------------------------------------------------------------------------
Wee.applyIf(String, {
    format : function(format){
        var args = Wee.toArray(arguments, 1);
        return format.replace(/\{(\d+)\}/g, function(m, i){
            return args[i];
        });
    },
    humpToConnector : function(str, conj){  
		  var index = str.search(/[A-Z]/);  
		  if (index >= 0) {  
		   var c = str.charAt(index);  
		   return String.humpToConnector(str.substring(0, index) + conj  
		       + c.toLowerCase() + str.substring(index + 1), conj);  
		  } else {  
		   	return str;  
		  }  
 	}  
});
//------------------------------------------------------------------------------------------------------------------------------------
Wee.applyIf(Array.prototype, {
    indexOf : function(o, from){
        var len = this.length;
        from = from || 0;
        from += (from < 0) ? len : 0;
        for (; from < len; ++from){
            if(this[from] === o){
                return from;
            }
        }
        return -1;
    },
    remove : function(o){
        var index = this.indexOf(o);
        if(index != -1){
            this.splice(index, 1);
        }
        return this;
    },
     insert : function (index,o){  
	  	if(index <= 0){  
            return this.unshift(o);//插入到首元素  
        }  
        if(index >= this.length){  
            return this.push(o);//插入到末元素  
        }  
        var sub = this.slice(index, this.length);  
        this.length = index;  
        this.push(o);  
        for(var i = 0; i < sub.length; i++){  
         	this.push(sub[i]);  
        }  
        return this;  
	 },  
	 removeAt : function(index){  
        if(index != -1){  
            this.splice(index, 1);  
        }  
        return this;  
    }  
});
//------------------------------------------------------------------------------------------------------------------------------------

(function(){

	var EXTUTIL = Wee.util,
	    TOARRAY = Wee.toArray,
	    EACH = Wee.each,
	    ISOBJECT = Wee.isObject,
	    TRUE = true,
	    FALSE = false;
	
	EXTUTIL.Observable = function(){
	    var me = this, e = me.events;
	    if(me.listeners){
	        me.on(me.listeners);
	        delete me.listeners;
	    }
	    me.events = e || {};
	};

	EXTUTIL.Observable.prototype = {
	    // private
	    filterOptRe : /^(?:scope|delay|buffer|single)$/,
	    fireEvent : function(){
	        var a = TOARRAY(arguments),
	            ename = a[0].toLowerCase(),
	            me = this,
	            ret = TRUE,
	            ce = me.events[ename],
	            q,
	            c;
	        if (me.eventsSuspended === TRUE) {
	            if (q = me.eventQueue) {
	                q.push(a);
	            }
	        }
	        else if(ISOBJECT(ce) && ce.bubble){
	            if(ce.fire.apply(ce, a.slice(1)) === FALSE) {
	                return FALSE;
	            }
	            c = me.getBubbleTarget && me.getBubbleTarget();
	            if(c && c.enableBubble) {
	                if(!c.events[ename] || !Wee.isObject(c.events[ename]) || !c.events[ename].bubble) {
	                    c.enableBubble(ename);
	                }
	                return c.fireEvent.apply(c, a);
	            }
	        }
	        else {
	            if (ISOBJECT(ce)) {
	                a.shift();
	                ret = ce.fire.apply(ce, a);
	            }
	        }
	        return ret;
	    },
	    addListener : function(eventName, fn, scope, o){
	        var me = this,
	            e,
	            oe,
	            isF,
	        ce;
	        if (ISOBJECT(eventName)) {
	            o = eventName;
	            for (e in o){
	                oe = o[e];
	                if (!me.filterOptRe.test(e)) {
	                    me.addListener(e, oe.fn || oe, oe.scope || o.scope, oe.fn ? oe : o);
	                }
	            }
	        } else {
	            eventName = eventName.toLowerCase();
	            ce = me.events[eventName] || TRUE;
	            if (Wee.isBoolean(ce)) {
	                me.events[eventName] = ce = new EXTUTIL.Event(me, eventName);
	            }
	            ce.addListener(fn, scope, ISOBJECT(o) ? o : {});
	        }
	    },
	    removeListener : function(eventName, fn, scope){
	        var ce = this.events[eventName.toLowerCase()];
	        if (ISOBJECT(ce)) {
	            ce.removeListener(fn, scope);
	        }
	    },
	    purgeListeners : function(){
	        var events = this.events,
	            evt,
	            key;
	        for(key in events){
	            evt = events[key];
	            if(ISOBJECT(evt)){
	                evt.clearListeners();
	            }
	        }
	    },
	    addEvents : function(o){
	        var me = this;
	        me.events = me.events || {};
	        if (Wee.isString(o)) {
	            var a = arguments,
	                i = a.length;
	            while(i--) {
	                me.events[a[i]] = me.events[a[i]] || TRUE;
	            }
	        } else {
	            Wee.applyIf(me.events, o);
	        }
	    },
	    hasListener : function(eventName){
	        var e = this.events[eventName];
	        return ISOBJECT(e) && e.listeners.length > 0;
	    },
	    suspendEvents : function(queueSuspended){
	        this.eventsSuspended = TRUE;
	        if(queueSuspended && !this.eventQueue){
	            this.eventQueue = [];
	        }
	    },
	    resumeEvents : function(){
	        var me = this,
	            queued = me.eventQueue || [];
	        me.eventsSuspended = FALSE;
	        delete me.eventQueue;
	        EACH(queued, function(e) {
	            me.fireEvent.apply(me, e);
	        });
	    }
	};

	var OBSERVABLE = EXTUTIL.Observable.prototype;
	
	OBSERVABLE.on = OBSERVABLE.addListener;
	
	OBSERVABLE.un = OBSERVABLE.removeListener;
	
	EXTUTIL.Observable.releaseCapture = function(o){
	    o.fireEvent = OBSERVABLE.fireEvent;
	};

	function createTargeted(h, o, scope){
	    return function(){
	        if(o.target == arguments[0]){
	            h.apply(scope, TOARRAY(arguments));
	        }
	    };
	};

	function createBuffered(h, o, l, scope){
	    l.task = new EXTUTIL.DelayedTask();
	    return function(){
	        l.task.delay(o.buffer, h, scope, TOARRAY(arguments));
	    };
	};

	function createSingle(h, e, fn, scope){
	    return function(){
	        e.removeListener(fn, scope);
	        return h.apply(scope, arguments);
	    };
	};

	function createDelayed(h, o, l, scope){
	    return function(){
	        var task = new EXTUTIL.DelayedTask();
	        if(!l.tasks) {
	            l.tasks = [];
	        }
	        l.tasks.push(task);
	        task.delay(o.delay || 10, h, scope, TOARRAY(arguments));
	    };
	};

	EXTUTIL.Event = function(obj, name){
	    this.name = name;
	    this.obj = obj;
	    this.listeners = [];
	};

	EXTUTIL.Event.prototype = {
	    addListener : function(fn, scope, options){
	        var me = this,
	            l;
	        scope = scope || me.obj;
	        if(!me.isListening(fn, scope)){
	            l = me.createListener(fn, scope, options);
	            if(me.firing){ // if we are currently firing this event, don't disturb the listener loop
	                me.listeners = me.listeners.slice(0);
	            }
	            me.listeners.push(l);
	        }
	    },
	    createListener: function(fn, scope, o){
	        o = o || {}, scope = scope || this.obj;
	        var l = {
	            fn: fn,
	            scope: scope,
	            options: o
	        }, h = fn;
	        if(o.target){
	            h = createTargeted(h, o, scope);
	        }
	        if(o.delay){
	            h = createDelayed(h, o, l, scope);
	        }
	        if(o.single){
	            h = createSingle(h, this, fn, scope);
	        }
	        if(o.buffer){
	            h = createBuffered(h, o, l, scope);
	        }
	        l.fireFn = h;
	        return l;
	    },
	    findListener : function(fn, scope){
	        var list = this.listeners,
	            i = list.length,
	            l,
	            s;
	        while(i--) {
	            l = list[i];
	            if(l) {
	                s = l.scope;
	                if(l.fn == fn && (s == scope || s == this.obj)){
	                    return i;
	                }
	            }
	        }
	        return -1;
	    },
	    isListening : function(fn, scope){
	        return this.findListener(fn, scope) != -1;
	    },
	    removeListener : function(fn, scope){
	        var index,
	            l,
	            k,
	            me = this,
	            ret = FALSE;
	        if((index = me.findListener(fn, scope)) != -1){
	            if (me.firing) {
	                me.listeners = me.listeners.slice(0);
	            }
	            l = me.listeners[index];
	            if(l.task) {
	                l.task.cancel();
	                delete l.task;
	            }
	            k = l.tasks && l.tasks.length;
	            if(k) {
	                while(k--) {
	                    l.tasks[k].cancel();
	                }
	                delete l.tasks;
	            }
	            me.listeners.splice(index, 1);
	            ret = TRUE;
	        }
	        return ret;
	    },
	    // Iterate to stop any buffered/delayed events
	    clearListeners : function(){
	        var me = this,
	            l = me.listeners,
	            i = l.length;
	        while(i--) {
	            me.removeListener(l[i].fn, l[i].scope);
	        }
	    },
	    fire : function(){
	        var me = this,
	            args = TOARRAY(arguments),
	            listeners = me.listeners,
	            len = listeners.length,
	            i = 0,
	            l;
	
	        if(len > 0){
	            me.firing = TRUE;
	            for (; i < len; i++) {
	                l = listeners[i];
	                if(l && l.fireFn.apply(l.scope || me.obj || window, args) === FALSE) {
	                    return (me.firing = FALSE);
	                }
	            }
	        }
	        me.firing = FALSE;
	        return TRUE;
	    }
	};
})();