Wee.wr.Listener = function()
{
	Wee.wr.Listener.superclass.constructor.call( this );
	this.addEvents({
        "mouseover":true,
        "mouseout":true
    });
};
Wee.extend(Wee.wr.Listener, Wee.util.Observable);

var WRListener = new Wee.wr.Listener();


Wee.wr.Register = function()
{
	Wee.wr.Register.superclass.constructor.call( this );
	this.register = {};
};
Wee.extend(Wee.wr.Register, Wee.util.Observable);

Wee.wr.Register.prototype.add = function( id, obj )
{
	this.register[id] = obj;
};

Wee.wr.Register.prototype.remove = function( id )
{
	delete this.register[id];
};

Wee.wr.Register.prototype.get = function( id )
{
	return this.register[id];
};

var WRRegister = new Wee.wr.Register();


var WRListener = new Wee.wr.Listener();

var wrConfig = {

};

//----------------------------------------------------------------------------------------------------------------------------
Wee.wr.Widget = function( paper, config, groupSet )
{
	var widgetConfig = {
		x:0,
		y:0,
		height:40,
		width: 200,
		r: 0,
		borderDef:{
			//fill: "#122d50",
			stroke: "#ADADAD",
			//stroke: "red",
			"stroke-width":0,
			cursor: "pointer",
			"fill-opacity":.0001
		},
		angleAttr:{
			//fill: "#00FFFF",
			stroke: "#00FFFF",
			"stroke-width":3
		},
		fillColor: "",
        gradualColor: "",//是否從中間的漸變色
		borderColor: "",
		borderStroke: 0,
		fillOpacity: 0,
        angleWidth: 0,
		angleColor: '',
		angle:0,
		angleLength:10,
        angleRadius:0
	};
	Wee.wr.Widget.superclass.constructor.call( this );
	Wee.apply(this, config, widgetConfig );
	var me = this;
	if( !this.id )this.id = Wee.id();
	WRRegister.add( this.id, me );
	this.paper = paper;
	if( groupSet )this.groupSet = groupSet;
	//this.bgImg = this.paper.image(this.bgImgUrl,0,0,1,1).hide();
	this.border = this.paper.rect( 0,0,30,30,this.angleRadius ).attr( this.borderDef ).hide();
	if( this.groupSet )this.groupSet.push( this.border );
	this.initWidgetAttr();

};
Wee.extend( Wee.wr.Widget, Wee.util.Observable );

Wee.wr.Widget.prototype.remove = function()
{
	this.removeWidget();
};

Wee.wr.Widget.prototype.removeWidget = function()
{
	this.border.remove();
	WRRegister.remove( this.id );
};

Wee.wr.Widget.prototype.drawAction = function()
{
	this.drawWidget();
};

Wee.wr.Widget.prototype.initWidgetAttr = function()
{
    if( this.fillOpacity != 0 ) {
        this.border.attr({"fill-opacity": this.fillOpacity});
    }
    if( this.borderColor && this.borderColor != ''){
        this.border.attr({"stroke":this.borderColor});
    }
    if( this.borderStroke ){
        this.border.attr({"stroke-width":this.borderStroke});
    }
    if( this.fillColor != "" ){
        this.border.attr({"fill":this.fillColor});
    }
}

Wee.wr.Widget.prototype.drawWidget = function()
{
	this.border.attr({x:this.x ,y:this.y ,width:this.width,height:this.height });
	this.border.show();
	if( this.angle == 1 ){
		if( this.angleWidth != 0 )this.angleAttr["stroke-width"] = this.angleWidth;
		if( this.angleColor != '')this.angleAttr["stroke"] = this.angleColor;

		var _x = this.x, _y = this.y, _r = this.angleLength;
		var strPath = "M" + _x + " " + ( _y + _r ) + "L" + _x + " "+ _y + "L"+ (_x + _r ) +" "+ _y;
		var lt = this.paper.path( strPath ).attr( this.angleAttr).show();
		//--------------------------------------------------------------------------------------------------------------
		_x = this.x + this.width,_y = this.y;
		strPath = "M" + (_x - _r) + " " + _y + "L" + _x + " "+ _y + "L"+ _x +" "+ (_y + _r);
		var rt = this.paper.path( strPath ).attr( this.angleAttr).show();
		//--------------------------------------------------------------------------------------------------------------
		_x = this.x + this.width,_y = this.y + this.height;
		strPath = "M" + _x + " " + ( _y - _r) + "L" + _x + " "+ _y + "L"+ (_x - _r) +" "+ _y;
		var lb = this.paper.path( strPath ).attr( this.angleAttr).show();
		//--------------------------------------------------------------------------------------------------------------
		_y = this.y + this.height,_x = this.x;
		strPath = "M" + _x + " " + ( _y - _r ) + "L" + _x + " "+ _y + "L"+ (_x + _r ) +" "+ _y;
		var rb = this.paper.path( strPath ).attr( this.angleAttr).show();
		if( this.groupSet ){
            this.groupSet.push( lt );
			this.groupSet.push( rt );
            this.groupSet.push( lb );
            this.groupSet.push( rb );
        }
	}
	if( this.gradualColor != "" ){
		if( this.gradualCircle ){
            this.gradualCircle.attr({cx: this.x + this.width/2, cy:this.y + this.height/2, r: _r, fill: this.gradualColor }).show();
		}else{
			var _r = this.width >= this.height ? this.height / 2 : this.width / 2;
            this.gradualCircle = this.paper.circle( this.x + this.width/2, this.y + this.height/2, _r ).attr({ fill: this.gradualColor });
            this.gradualCircle.show();
		}
        if( this.groupSet ){
            this.groupSet.push( this.gradualCircle );
		}
	}
};

Wee.wr.Widget.prototype.hide = function()
{
	this.hideWidget();
};


Wee.wr.Widget.prototype.hideWidget = function()
{
	this.border.hide();
};


Wee.wr.Widget.prototype.getId = function()
{
	return this.id;
};

Wee.wr.Widget.prototype.toFront= function()
{
	this.border.toFront();
};

Wee.wr.Widget.prototype.setBorderDef= function(obj)
{
	this.borderDef = obj;
};

Wee.wr.Widget.prototype.setAttr = function( attr )
{
	this.border.attr( attr );
};

Wee.wr.Widget.prototype.setAngleAttr = function( attr )
{
	this.angle = 1;
	this.angleAttr = Wee.apply( {}, this.angleAttr , attr )
};

Wee.wr.Widget.prototype.setBackgroundImg = function()
{

};

Wee.wr.Widget.prototype.changeWidgetPosition = function( param, ms )
{
	var me = this;
	/*this.border.attr( { x: param.x, y: param.y } ); */
	this.border.animate({ x: param.x, y: param.y },ms);
}

Wee.wr.Widget.prototype.widgetAnimatePath = function( param, ms )
{
	var me = this;
	this.border.animate( param,ms );
}


Wee.wr.Widget.prototype.attr = function( obj )
{
	if( obj ){
		this.x = obj.x || this.x;
		this.y = obj.y || this.y;
		this.width = obj.width || this.width;
		this.height = obj.height || this.heigth;
	}else{
		return {
			x:this.x,
			y:this.y,
			width:this.width,
			height:this.height
		};

	}
};

Wee.wr.Widget.prototype.loadData = function( data )
{
};

Wee.wr.Widget.prototype.setProperty = function( config )
{
    Wee.apply(this, config );
};
