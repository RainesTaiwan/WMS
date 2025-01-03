Wee.wr.ImageIcon = function( paper,config )
{
	var imageIcon = {
		startX:0,
		startY:0,
		x:0,
		y:0,
		width:100,
		height:100,
		srcPath:"images/",
		imageName:"",
		imageType:"png",
		src:"",
		stateCtlNum:0,
		pictureArry:[],
		interval:500,
		animateFlag:0,
		statusSrc:{}
	};
	Wee.wr.ImageIcon.superclass.constructor.call( this, paper, Wee.apply({}, config, imageIcon ) );

	this.icon = paper.image( "",this.x,this.y,this.width,this.height ).hide();
	/*this.addEvents({
		"click":true,
		"moveIn":true,
		"moveOut":true
	});*/
	var me = this;
	this.icon.mouseover( function( e ){
		WRListener.fireEvent("mouseover", me );
	});
};
Wee.extend( Wee.wr.ImageIcon,Wee.wr.Widget);

Wee.wr.ImageIcon.prototype.drawAction = function()
{
	this.drawImageIcon();
};

Wee.wr.ImageIcon.prototype.toFront = function()
{
	this.icon.toFront();
};

Wee.wr.ImageIcon.prototype.drawImageIcon = function()
{
	var me = this, _iconWidth = this.width ,_iconHeight = this.height,_imageX = 0,_imageY = 0,_iconAttr;
	if( !this.ratio ){
		this.ratio = this.height/this.width;
	}
	//-------------------------------------------------------------------------------------------
	if( this.iconWidth ){
		if( this.iconWidth < this.width ){
			_iconWidth = this.iconWidth;
			_imageX = Math.floor( (this.width - this.iconWidth)/2 );
		}
	}
	if( this.iconHeight ){
		if( this.iconHeight < this.height ){
			_iconHeight = this.iconHeight;
			_imageY = Math.floor( (this.height - this.iconHeight)/2 );
		}
	}
	//-------------------------------------------------------------------------------------------
	if( !this.iconHeight || !this.iconWidth ){
		if( this.iconHeight )
		{
			_iconWidth = Math.floor( _iconHeight / this.ratio );
			_imageX = Math.floor( (this.width - _iconWidth)/2 );
		}else if( this.iconWidth ){
			_iconHeight = Math.floor( _iconWidth * this.ratio );
			_imageY = Math.floor( (this.height - _iconHeight)/2 );
		}else{
			if( _iconHeight >= Math.floor( _iconWidth * this.ratio ) ){
				_iconHeight = Math.floor( _iconWidth * this.ratio );
				_imageY = Math.floor( (this.height - _iconHeight)/2 );
			}else{
				_iconWidth = Math.floor( _iconHeight / this.ratio );
				_imageX = Math.floor( (this.width - _iconWidth)/2 );
			}
		}
	}
	//-------------------------------------------------------------------------------------------
	_iconAttr = {
		x: ( this.x + _imageX),
		y: ( this.y + _imageY),
		width: _iconWidth,
		height: _iconHeight,
		src: this.src
	};
	this.icon.attr( _iconAttr ).show();
	//-------------------------------------------------------------------------------------------
	this.iconX = this.x + _imageX;
	this.iconY = this.y + _imageY;
	this.iconWidth = _iconWidth;
	this.iconHeight = _iconHeight;

	if(this.animateFlag != 0)
	{
			this.animate();
	}
	//-------------------------------------------------------------------------------------------
	this.drawWidget();


};

Wee.wr.ImageIcon.prototype.changStatus = function(  )
{
   	var me = this;
   	var _src = me.statusSrc[ me.status ];
   	if( _src ){
   		var _iconAttr = {
   				x: me.iconX,
   				y: me.iconY,
   				width: me.iconWidth,
   				height: me.iconHeight,
   				src: _src
   			};
   		me.icon.attr( _iconAttr ).show();
   	}

};


Wee.wr.ImageIcon.prototype.animate = function()
{
   	var that=this;
	that.icon.attr({src:that.pictureArry[that.stateCtlNum]}).show();
	that.stateCtlNum=(that.stateCtlNum+1)%(that.pictureArry.length);
	setTimeout( function(){ that.animate(); },that.interval );

};


Wee.wr.ImageIcon.prototype.loadData = function( obj )
{
   	var me = this;
   	for( key in obj ){
   		me[key] = obj[key];
   	}
   	me.changStatus();
};
