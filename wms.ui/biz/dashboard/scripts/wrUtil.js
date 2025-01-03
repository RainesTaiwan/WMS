Date.prototype.Format = function (fmt) { //author: meizz
	var o = {
		"M+": this.getMonth() + 1, //月份
		"d+": this.getDate(), //日
		"h+": this.getHours(), //小時
		"m+": this.getMinutes(), //分
		"s+": this.getSeconds(), //秒
		"q+": Math.floor((this.getMonth() + 3) / 3), //季度
		"S": this.getMilliseconds() //毫秒
	};
	if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for (var k in o)
		if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
}

var wrUtil = {
	strMatchCount:function( mainStr, subStr )
	{
		var count = 0;
	    var offset = 0;
	    do
	    {
	        offset = mainStr.indexOf(subStr, offset);
	        if(offset != -1)
	        {
	            count++;
	            offset += subStr.length;
	        }
	    }while(offset != -1)
	    return count;
	},
	maxTextLineLength:function( mainStr, subStr )
	{
	 	var offset = 0;
	    var count=0;
	    var maxTextLineLength=0;
	    var textDistance=0
	    var position1=0;
	    var position2=0;
	    var lastOffset=0;
	    var lastSubStrlength=0;
	    do
	    {
	        offset = mainStr.indexOf(subStr, offset);

	        if(offset != -1)
	        {
	            lastOffset=offset;
	            position2=offset-1;
	            textDistance=position2-position1+1;
	            position1=position2+subStr.length+1;
	            offset += subStr.length;

	            count++;
	            if(textDistance>maxTextLineLength)
	            {
	        		maxTextLineLength=textDistance;
	            }
	        }
	    }while(offset != -1)
	    lastSubStrlength= mainStr.length-lastOffset-subStr.length;
	    if(lastSubStrlength>maxTextLineLength)
	    {
	    maxTextLineLength=lastSubStrlength;
	    }

	    if(count==0)
	    {maxTextLineLength=mainStr.length}
	    return maxTextLineLength;
	},
	calTextLength : function(str)
	{
		var len = str.match(/[^ -~]/g) == null ? str.length : str.length + str.match(/[^ -~]/g).length;
		return len

	},
	coverTime:function( date ){
        var month = date.getMonth() + 1;
        var strDate = date.getDate();
        if (month >= 1 && month <= 9) {
            month = "0" + month;
        }
        if (strDate >= 0 && strDate <= 9) {
            strDate = "0" + strDate;
        }
        var week = this.convertDay( date.getDay() );
        var strHour = date.getHours();
        if (strHour >= 0 && strHour <= 9) {
            strHour = "0" + strHour;
        }
		var strMinute = date.getMinutes();
        if (strMinute >= 0 && strMinute <= 9) {
            strMinute = "0" + strMinute;
        }
        var strSecond = date.getSeconds();
        if (strSecond >= 0 && strSecond <= 9) {
            strSecond = "0" + strSecond;
        }
        var currentDate = date.getFullYear() + "-" + month + "-" + strDate + " " + week
            + " " + strHour + ":" + strMinute + ":" + strSecond;
        return currentDate;
	},
//------------myDate.getDay()返回值轉換
	convertDay:function(day)
    {
    	var _day;
    	switch(day)
    	{
    	case 0:
    	_day ="星期天";
    	break;
    	case 1:
    	_day ="星期一";
    	break;
    	case 2:
    	_day ="星期二";
    	break;
    	case 3:
    	_day ="星期三";
    	break;
    	case 4:
    	_day ="星期四";
    	break;
    	case 5:
    	_day ="星期五";
    	break;
    	case 6:
    	_day ="星期六";
    	break;
    	}
    	return _day;

    },

    dateTransform:function(date)
    {
	var _date=date.replace(" ",":").replace(/\:/g,"-").split("-");

	var da = new Date(_date[0],_date[1]-1,_date[2],_date[3],_date[4],_date[5]);
	return da;

    },
    getRandomRGB:function()
    {
    function randomColor( ) {
    var rand = Math.floor(Math.random( ) * 0xFFFFFF).toString(16);
    if(rand.length == 6){
        return rand;
    }else{
        return randomColor();
    }
    }
    var randRGB = "#ffffff";
    randRGB = "#"+randomColor();
    return randRGB;

    },
	isEmpty: function(value, allowEmptyString) {
        return (value === null) || (value === undefined) || (!allowEmptyString ? value === '' : false) || (wrUtil.isArray(value) && value.length === 0);
    },
    isArray: ('isArray' in Array) ? Array.isArray : function(value) {
        return toString.call(value) === '[object Array]';
    },
	createWidget:function( paper, config )
	{
		var _widget;
		if( config.xtype == "tableContainer" )
		{
			_widget = new Wee.wr.TableContainer( paper,config);
		}
		else if( config.xtype == "multTranship" )
		{
			_widget = new Wee.wr.MultTranship( paper,config);
		}
		else if( config.xtype == "singleTranship" )
		{
			_widget = new Wee.wr.SingleTranship( paper,config);
		}
		else if( config.xtype == "multDisplayBoard" )
		{
			_widget = new Wee.wr.MultDisplayBoard( paper,config);
		}
		else if( config.xtype =="signaEquipment" )
		{
			_widget = new Wee.wr.SignaEquipment( paper,config);
		}
		else if( config.xtype == "headingArea" )
		{
			_widget = new Wee.wr.HeadingArea( paper,config);
		}
		else if( config.xtype == "cncTranship" )
		{
			_widget = new Wee.wr.CNCTranship( paper,config);
		}
		else if( config.xtype == "progressPan" )
		{
			_widget = new Wee.wr.ProgressPan( paper,config);
		}
		else if( config.xtype == "textLabel" )
		{
			_widget = new Wee.wr.TextLabel( paper,config);
		}
		else if( config.xtype == "oeeDisplay" )
		{
			_widget = new Wee.wr.OeeDisplay( paper,config);
		}
		else if( config.xtype == "legend" )
		{
			_widget = new Wee.wr.Legend( paper,config);
		}
		else if( config.xtype == "oeePanel" )
		{
			_widget = new Wee.wr.OeePanel( paper,config);
		}else if( config.xtype == "columnField" )
		{
			_widget = new Wee.wr.ColumnField( paper,config);
		}else if( config.xtype == "equipLabel" )
		{
			_widget = new Wee.wr.EquipLabel( paper,config);
		}else if( config.xtype == "pieChart" )
		{
			_widget = new Wee.wr.PieChart( paper,config);
		}else if( config.xtype == "barChart" )
		{
			_widget = new Wee.wr.BarChart( paper,config);
		}else if( config.xtype == "subAGVPath" )
		{
			_widget = new Wee.wr.SubAGVPath( paper,config);
		}
		else if( config.xtype == "flowLinePath" )
		{
			_widget = new Wee.wr.FlowLinePath( paper,config);
		}
		else if( config.xtype == "bootDynamic" )
		{
			_widget = new Wee.wr.BootDynamic( paper,config);
		}
		else if( config.xtype == "threeDContainer" )
		{
			_widget = new Wee.wr.ThreeDContainer( paper,config);
		}
		else if( config.xtype == "equipContainer" )
		{
			_widget = new Wee.wr.EquipContainer( paper,config);
		}
		else if( config.xtype == "groupLablesContainer" )
		{
			_widget = new Wee.wr.GroupLablesContainer( paper,config);
		}
		else if( config.xtype == "top5NcContainer" )
		{
			_widget = new Wee.wr.Top5NcContainer( paper,config);
		}
		else if( config.xtype == "ngRecord" )
		{
			_widget = new Wee.wr.NGRecord( paper,config);
		}
		else if( config.xtype == "dealContainer" )
		{
			_widget = new Wee.wr.DealContainer( paper,config);
		}
		else if( config.xtype == "needDealContainer" )
		{
			_widget = new Wee.wr.NeedDealContainer( paper,config);
		}
		else if( config.xtype == "signaLamp" )
		{
			_widget = new Wee.wr.SignaLamp( paper,config);
		}
		else if( config.xtype == "agvEquip" )
		{
			_widget = new Wee.wr.AgvEquip( paper,config);
		}else if( config.xtype == "planToAchieve" )
		{
			_widget = new Wee.wr.PlanToAchieve( paper,config);
		}else if( config.xtype == "lightEquip" )
		{
			_widget = new Wee.wr.LightEquip( paper,config);
		}else if( config.xtype == "completeRate" ){
            _widget = new Wee.wr.ComleteRate( paper,config);
		}else if( config.xtype == "widget" ){
            _widget = new Wee.wr.Widget( paper,config);
        }else if( config.xtype =="simpleEquip" )
        {
            _widget = new Wee.wr.SimpleEquip( paper,config);
        }else {
			console.log( "wrUtil[Xypte: "+ config.xtype + "不存在！]" );
		}

		return _widget
	}
};

function ajaxService(service, callback, params )
{
	 $.ajax( {
		type : "POST",
		url : ""+service,
		success : callback,
		data: params,
        dataType: "json",
		error : ajaxError
	} );
}

function ajaxGetService(service, callback, params )
{
    $.ajax( {
        type : "GET",
        url : ""+service,
        success : callback,
        data: params,
        dataType: "json",
        error : ajaxError
    } );
}

function ajaxError( jqXHR )
{
    console.log("Ajax Fail: ", jqXHR.status, jqXHR.statusText);
}


var wrWebSocketUtil = {
    wsUrl:"ws://localhost:8081/websocket?",
    websocket:null,
    msgListeners:new Map(),
    lockReconnect: false,  //避免ws重複連線

    createWebSocket: function(queryString) {
        var me = this;
        var wsUrl = me.wsUrl + queryString;
        try{
            if( me.websocket ){
                return me.websocket;
            }
            //判斷目前瀏覽器是否支援WebSocket
            if ('WebSocket' in window) {
                me.websocket = new WebSocket(wsUrl);
            }
            else {
                alert('目前瀏覽器 Not support websocket')
            }
            me.initEventHandle(queryString);
        }catch(e){
            me.reconnect(queryString);
            console.log(e);
        }
        return me.websocket;
    },

    initEventHandle: function (wsUrl) {
        var me = this;
        me.heartCheck = {
            timeout: 1000,        //1分鐘發一次心跳
            timeoutObj: null,
            serverTimeoutObj: null,
            reset: function(){
                clearTimeout(this.timeoutObj);
                clearTimeout(this.serverTimeoutObj);
                return this;
            },
            start: function(){
                var self = this;
                this.timeoutObj = setTimeout(function(){
                    //這裡發送一個心跳，後端收到后，返回一個心跳訊息，
                    //onmessage拿到返回的心跳就說明連線正常
                    me.websocket.send("ping");
                    // console.log("ping");
                    self.serverTimeoutObj = setTimeout(function(){//如果超過一定時間還沒重置，說明後端主動斷開了
                        me.websocket.close();     //如果onclose會執行reconnect，我們執行ws.close()就行了.如果直接執行reconnect 會觸發onclose導致重連兩次
                    }, self.timeout)
                }, this.timeout)
            }
        };

        //連線發生錯誤的回撥方法
        me.websocket.onerror = function (event) {
            console.log(event);
            me.reconnect(wsUrl);
            console.log("WebSocket連線發生錯誤");
        };
        //連線成功建立的回撥方法
        me.websocket.onopen = function () {
            me.heartCheck.reset().start();      //心跳檢測重置
            console.log("WebSocket連線成功");
        };
        //接收到訊息的回撥方法
        me.websocket.onmessage = function (event) {
            console.log(event.data);
            me.heartCheck.reset().start();      //心跳檢測重置
            if(event.data!='pong'){
                me._msgReceivedAtion(event)
            }
        };
        //連線關閉的回撥方法
        me.websocket.onclose = function (event) {
            console.log(event);
            me.reconnect(wsUrl);
            console.log("WebSocket連線關閉");
        };

        window.onbeforeunload = function () {
            me.closeWebSocket();
        };
    },

    //關閉WebSocket連線
    closeWebSocket: function() {
        var me = this;
        me.websocket.close();
    },

    reconnect: function(url) {
        var me = this;
        me.websocket = null;
        me.msgListeners = new Map();
        if(me.lockReconnect) return;
        me.lockReconnect = true;
        setTimeout(function () {     //沒連線上會一直重連，設定延遲避免請求過多
            me.createWebSocket(url);
            me.lockReconnect = false;
        }, 2000);
    },

    /*initWebsocket:function( channel ){
        var me = this;
        if( me.websocket )
            return me.websocket;
        //判斷目前瀏覽器是否支援WebSocke
        if ( 'WebSocket' in window ) {
            me.websocket = new WebSocket( me.wsUrl +  "channel=" + channel );
            window.websocket = me.websocket;
        }else {
            console.log( "目前瀏覽器 Not support websocket" );
        }
        //連線關閉的回撥方法
        me.websocket.onclose = me._closeAction.bind( me );
        //連線發生錯誤的回撥方法
        me.websocket.onerror = me._errorAction.bind( me );
        //接收到訊息的回撥方法
        me.websocket.onmessage = me._msgReceivedAtion.bind( me );
        //監聽視窗關閉事件，當視窗關閉時，主動去關閉websocket連線，防止連線還沒斷開就關閉視窗，server端會拋異常。
        window.onbeforeunload = function () {
            if( me.websocket != null ){
                me.websocket.close();
            }
        }
        //websocket斷線重連
        window.setInterval( function(){
            if( window.websocket.readyState == 3 ){
                me.initWebsocket( channel );
                console.log( "WebSocket重連" );
            }
        }, 5000 );
        //---------------------------------------------------------------------------------------------------------
        return me.websocket;
    },
    _closeAction:function(){
        var me = this;
        me.websocket = null;
        me.msgListeners = new Map();
        console.log( "WebSocket連線關閉" );
        //window.location.reload();
    },
    _errorAction:function()
    {
        console.log( "WebSocket連線發生錯誤" );
    },
    */
    _msgReceivedAtion:function( event ){
        var me = this;
        var jsonData = $.parseJSON( event.data  );
        if(me.msgListeners.has( jsonData["messageType"] ) ){
            var callback = me.msgListeners.get( jsonData["messageType"] );
            me.msgListeners.delete( jsonData["messageType"] );
            callback( event.data );
        }else{
            me.receiveAction( event.data );
        }
    },
    sendAndReceive:function( messageType, msg, callback )
    {
        var me = this;
        if( !me.websocket )me.getWebSocket();
        me.msgListeners.set( messageType, callback );
        me.websocket.send( msg );
    },
    receiveAction:null
};
