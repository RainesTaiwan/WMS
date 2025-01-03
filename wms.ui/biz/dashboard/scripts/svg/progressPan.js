Wee.wr.ProgressPan= function( paper,config )
{
	var progressPanConfig = {
		ratio:0.5,
		label:"",
		imgBorder:0,
		bgAttr:{
			src:"../images/progressPan/round3.png",
			ratio:0.5013
		},
		circleRatio:0.05,
		circleAttr:{
			fill:"r(0.5, 0.5)#fff-#000",
			stroke:"black"
		},
		needleAttr:{
			fill:"grey",
			stroke:"black"
		},
		backAttr:{
			//fill: "black",
			stroke: "#A9A9A9",
			"stroke-width":1,
			cursor: "pointer",
			"fill-opacity":.01
		},
		per:.50,
		perLast:0,
		textAnchor:"middle",
		textColor:"#05EB37",
		textRatio:2
	};

	Wee.wr.ProgressPan.superclass.constructor.call( this, paper, Wee.apply( {}, config, progressPanConfig) );
	this.bg = new Wee.wr.ImageIcon( this.paper,this.bgAttr );
	this.needle = this.paper.path( "M0,0L1,1").attr( this.needleAttr ).hide();
	this.circle = this.paper.circle( 0,0,1 ).attr( this.circleAttr ).hide();
	this.text = this.paper.text( 0, 0, "" ).hide();

	this.setAttr( Wee.apply( {}, this.backAttr, progressPanConfig.backAttr )  );
};

Wee.extend( Wee.wr.ProgressPan,Wee.wr.Widget);

Wee.wr.ProgressPan.prototype.drawAction = function()
{
	var me = this;
	this.bg.attr({x:this.x + me.imgBorder,y:this.y + me.imgBorder,width:this.width - 2* me.imgBorder,height:(Math.floor(this.height)*0.9) - 2* me.imgBorder});
	this.bg.drawImageIcon();
	this.drawNeedle();
	this.drawText();

	this.drawWidget();
};

Wee.wr.ProgressPan.prototype.drawText = function()
{
	var _rate = ( this.per*100 ).toFixed(2).toString() + "%" ;
	if( this.label )_rate = this.label + "  " + _rate;
	var _textAttr =
	{
		x : this.cx,
		y : ( Math.floor( this.cy + this.r + 10 ) ),//與圓離開一個字元的高度
		"text-anchor" : this.textAnchor,
		"fill" : this.textColor,
		"stroke" : "none",
		"font-size" : ( Math.floor( this.textRatio*this.r ) ),
		text: _rate
	};
	//-------------------------------------------------------------------------------------------
	this.text.attr(_textAttr).show();
};

Wee.wr.ProgressPan.prototype.drawNeedle = function()
{
	var _cx = Math.floor( this.bg.iconX + this.bg.iconWidth/2 );
	var _cy = Math.floor( this.bg.iconY + this.bg.iconHeight );
	var _r =  Math.floor( this.bg.iconWidth*this.circleRatio );
	this.cx = _cx;
	this.cy = _cy;
	this.r = _r;
	//-------------------------------------------------------------------------------------------
	var _n1_x = _cx, _n1_y = Math.floor( _cy - 0.5*_r ),
		_n2_x = Math.floor( this.bg.iconX ),
		_n2_y = _cy,
		_n3_x = _n1_x,
		_n3_y = Math.floor( _cy + 0.5*_r );
	//-------------------------------------------------------------------------------------------
	var _needlePathStr= "M" + _n1_x +","+ _n1_y +"L" + _n2_x + "," +_n2_y+"L"+_n3_x+","+_n3_y+"z";
	var _cirAttr = {
		cx:_cx,
		cy:_cy,
		r:_r
	};
	//-------------------------------------------------------------------------------------------
	this.needle.attr({path:_needlePathStr}).show();
	this.circle.attr(_cirAttr).show();
	this.setNeedle();
}

Wee.wr.ProgressPan.prototype.setPer = function( per )
{
	this.perLast = this.per;
	this.per = per;
	this.setNeedle();
}

Wee.wr.ProgressPan.prototype.setPercentage = function( per )
{
	var me = this;
	var _rate = per.replace("%","");
	_rate = _rate/100;
	me.setPer( _rate );
}


Wee.wr.ProgressPan.prototype.setPerInit = function( per )
{
	this.per = per;
}

Wee.wr.ProgressPan.prototype.setNeedle = function()
{
	var _r_cx = this.cx;
	var _r_cy = this.cy;
	var _angle = Math.floor(this.per * 180);
	var tStr = "r"+_angle+","+_r_cx+","+_r_cy;
	var _transformTime = 10 * ( _angle - ( Math.floor( this.perLast * 180 ) ) )//10ms轉1度
	this.needle.animate( { transform : tStr }, _transformTime );
	this.text.hide();
	this.drawText();
}

Wee.wr.ProgressPan.prototype.loadDataAction = function( data )
{
	if(data.id == this.id)
	{
		this.setPer(data.per);
	}
};
