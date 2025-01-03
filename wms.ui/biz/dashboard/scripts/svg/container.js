Wee.wr.Container = function( paper,config )
{
	var containerConfig = {
		containerFlag:true,
		containers:[]
	};
	Wee.wr.Container.superclass.constructor.call( this, paper, Wee.apply({}, config, containerConfig ) );
	this.widgets = {};
	//this.initWidgets();
};
Wee.extend( Wee.wr.Container,Wee.wr.Widget );

Wee.wr.Container.prototype.initWidgets = function()
{
	if( this.data ){
		for( var i = 0; i < this.data.length; i++ )
		{
			var _config = this.data[i];
			var _widget = this.getNewWidget( _config );
			if( _widget != null )
			this.addWidget( _widget );
		}
	}
};


Wee.wr.Container.prototype.remove = function()
{
	for( var id in this.widgets ){
		var _widget = this.widgets[id];
		if( _widget ){
			this.widgets[id] = null;
			_widget.remove();
		}
	}
	this.border.remove();
};

Wee.wr.Container.prototype.removeAllWidget = function(  )
{
	for( var id in this.widgets ){
		var _widget = this.widgets[id];
		if( _widget ){
			this.widgets[id] = null;
			_widget.remove();
		}
	}
};

Wee.wr.Container.prototype.addWidget = function( widget )
{
	var me = this;
	this.widgets[ widget.id ] = widget;
	if( widget.containerFlag && widget.containerFlag ){
		me.containers.push( widget );
	}
	return widget;
};

Wee.wr.Container.prototype.removeWidget = function( widget )
{
	this.widgets[ widget.id ] = null;
};

Wee.wr.Container.prototype.hide = function()
{
	for( var id in this.widgets ){
		var _widget = this.widgets[id];
		if( _widget ){
			_widget.hide();
		}
	}
	this.border.hide();
};

Wee.wr.Container.prototype.toFront = function()
{
	this.border.toFront();
	for( var id in this.widgets ){
		var _widget = this.widgets[id];
		if( _widget ){
			_widget.toFront();
		}
	}
};

Wee.wr.Container.prototype.getNewWidget = function( config )
{
	var _widget = null;
	//-----------------------------------------------------------------------------------------
	_widget = wrUtil.createWidget( this.paper, config );
	//-----------------------------------------------------------------------------------------
	return _widget
}

Wee.wr.Container.prototype.loadData = function( data )
{
	var me = this;
	if( !data || data.length == 0 ){
		console.log( "Container -- load data:" +  data );
		return;
	}
	var _childData = [];
	for( var i = 0; i < data.length; i++ ){
		var _obj = data[i];
		if( !_obj.id ){
			console.log( "Container -- Data does not contain id:" +  data );
			continue;
		}
		var _widget = me.widgets[ _obj["id"] ];
		if( !_widget ){
			//console.log( "Container -- Widget is not exist!" +  _obj );
			_childData.push( _obj );
			continue;
		}
		_widget.loadData( _obj );
	}
	//------------------------------------------------------------------------------------------------------------------
	if( _childData.length == 0 )return;
	//------------------------------------------------------------------------------------------------------------------
	if( me.containers && me.containers.length > 0 ){
		for( var j = 0; j < me.containers.length; j++ ){
			var _container = me.containers[j];
			_container.loadData( _childData );
		}
	}
}

Wee.wr.Container.prototype.getWidget = function( id )
{
   var me = this;
   if( me.widgets[ id ] )return me.widgets[ id ];
	else return ;
}
//-----------------------------------------------------------------------------

Wee.wr.TableContainer = function( paper,config )
{
	var tableContainer = {
		rows:40,
		cols:132,
		containerFlag:true,
		borderWidth:0,
		borderHeight:0,
		waterMarkText:'',
		waterMarkAttr:{
            fill:"#E94520",
            "opacity": 0.5,
			"fill-opacity":0.5,
            "font-weight":"normal",
            "text-anchor":"middle",
            stroke:"#E94520",
            "font-size": 20
		}
	};
	Wee.wr.TableContainer.superclass.constructor.call( this, paper, Wee.apply({}, config, tableContainer ) );
	this.initWidgets();
};
Wee.extend( Wee.wr.TableContainer, Wee.wr.Container );


Wee.wr.TableContainer.prototype.initWidgets = function()
{
	if( this.data ){
		for( var i = 0; i < this.data.length; i++ ){
			var _widgetConfig = this.data[i];
			var _widget = this.createWidget( _widgetConfig );
			this.addWidget( _widget );
		}
	}
};

Wee.wr.TableContainer.prototype.createWidget = function( config )
{
	var _widget = null;
	//-----------------------------------------------------------------------------------------
	_widget = wrUtil.createWidget( this.paper, config );
	//-----------------------------------------------------------------------------------------
	return _widget;
};

Wee.wr.TableContainer.prototype.drawAction = function()
{
	var me = this;
	this.colWidth = this.width/this.cols;
	this.rowHeight = this.height/this.rows;

	for( var id in this.widgets ){
		var _widget = this.widgets[id];
		if( _widget ){
			_widget.attr({
				x:Math.ceil( this.x + ( _widget.col - 1 ) * this.colWidth + this.borderWidth ),
				y:Math.ceil( this.y + ( _widget.row - 1 ) * this.rowHeight + this.borderHeight ),
				width:Math.floor( _widget.sizeX * this.colWidth - 2*this.borderWidth ),
				height:Math.floor( _widget.sizeY * this.rowHeight - 2*this.borderHeight )
			});
			_widget.drawAction();
		}
	}
    this.drawWidget();
	if( this.waterMarkText != '' ){
		if( this.waterMark ) {
            this.waterMark.attr({x: me.x + me.width/2, y: me.y + me.height/2});
		}else{
			this.waterMark = this.paper.text( this.x + this.width/2,this.y + this.height/2,this.waterMarkText );
		}
        this.waterMark.attr( this.waterMarkAttr ).show();
	}

};

Wee.wr.TableContainer.prototype.markToFront = function()
{
	if( this.waterMark )this.waterMark.toFront();
}
