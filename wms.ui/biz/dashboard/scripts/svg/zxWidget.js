Wee.wr.TextLabel = function( paper,config )
{
    var defaultTextLableConfig = {
        textRatio:0.4,
        //value:"Test",
        text:"",
        textColor:"",
        textAnchor:"",
        backColor:"",
        borderWidth:0,
        borderColor:"",
        mindleLine:0,//要不要顯示中間線
        textAttr:{
            fill:"white",
            stroke:"white",
            "font-weight":"normal",
            "text-anchor":"middle"
        },
        backAttr:{
            fill: "#C0C0C0",
            stroke: "#C0C0C0",
            "stroke-width":0,
            cursor: "pointer",
            "fill-opacity":.001
        },
        mindleLineAttr:{
            fill: "#FFFFFF",
            stroke: "#FFFFFF",
            "stroke-width":2,
            "stroke-dasharray":"-",
            cursor: "pointer",
            "fill-opacity":1
        }
    };

    Wee.wr.TextLabel.superclass.constructor.call( this, paper, Wee.apply({}, config, defaultTextLableConfig ) );

    //this.showText = this.paper.text(0,0,this.text ).attr(Wee.apply({}, this.textAttr, this.defaultTextLableConfig.textAttr )).hide();
    this.initAttr();
    this.showText = this.paper.text(0,0,this.text ).attr( Wee.apply({}, this.textAttr, defaultTextLableConfig.textAttr) ).hide();
    this.middleLine = this.paper.path( "M0 0").attr( defaultTextLableConfig.mindleLineAttr).hide();
    this.setAttr( Wee.apply({}, this.backAttr, defaultTextLableConfig.backAttr));
};
Wee.extend( Wee.wr.TextLabel, Wee.wr.Widget );

Wee.wr.TextLabel.prototype.initAttr = function()
{
    if( this.textColor ){
        this.textAttr["fill"] = this.textColor;
        this.textAttr["stroke"] = this.textColor;
    }
    if( this.textAnchor ){
        this.textAttr["text-anchor"] = this.textAnchor;
    }
    if( this.backColor ){
        this.backAttr["fill"] = this.backColor;
        this.backAttr["fill-opacity"] = 1;
    }

    if( this.backR ){
        this.backAttr["r"] = this.backR
    }
    if( this.backOpacity ){
        this.backAttr["fill-opacity"] = this.backOpacity;
    }


    if( this.borderWidth ){
        this.backAttr["stroke-width"] = this.borderWidth;
        this.backAttr["stroke"] = this.borderColor || this.backAttr["fill"];
        if( ( this.width -  2*this.borderWidth )<= 0 || ( this.height - 2*this.borderWidth ) <= 0 ){
            console.log( "TextLabel中，BorderWidth的寬度大於給出範圍的長度或寬度" );
            return;
        }
        this.backAttr["x"] = this.x + this.borderWidth;
        this.backAttr["y"] = this.y + this.borderWidth;
        this.backAttr["width"] =  this.width -  2*this.borderWidth ;
        this.backAttr["height"] = this.height - 2*this.borderWidth;
    }

}
Wee.wr.TextLabel.prototype.drawAction = function()
{
    //this.showText.attr( this.textAttr );
    var me = this;
    var titleWordsAttr ={
        x:(this.x + this.width/2 ),
        y:Math.floor(this.y +this.height/2),
        "font-size":this.height*this.textRatio
    };
    if( me.textAttr["text-anchor"] == "start" )
        titleWordsAttr = {
            x: this.x + 10,
            y: Math.floor(this.y +this.height/2),
            "font-size":this.height*this.textRatio
            //text: this.value
        };
    if( this.mindleLine == 1 ){
        var strPath = "M" + ( this.x + 2 ) + "," + (this.y + this.height/2) + "L" + ( this.x + this.width - 4 ) + "," + (this.y + this.height/2);
        this.middleLine.attr( { path: strPath }).show();
    }else{
        this.middleLine.hide();
    }
    this.showText.attr( titleWordsAttr ).show();
    if( this.transform ){
        this.showText.transform( this.transform );
    }
    this.drawWidget();
};
Wee.wr.TextLabel.prototype.remove = function( textStr )
{
    this.showText.remove();
    this.removeWidget();
};
Wee.wr.TextLabel.prototype.setText = function( textStr )
{
    this.showText.attr( {text: textStr } );
};
Wee.wr.TextLabel.prototype.setTextAttr = function( attr )
{
    this.showText.attr( attr );
}
Wee.wr.TextLabel.prototype.setBackAttr = function( attr )
{
    this.setAttr( attr );
}
Wee.wr.TextLabel.prototype.loadData = function( data )
{
    var me = this;
    if( data && typeof( data ) == "object" ){
        this.setText( data.text );
    }else
        this.setText( data );
}
Wee.wr.TextLabel.prototype.changePosition = function( param, ms )
{
    var me = this;
    if( me.textAttr["text-anchor"] == "start" ){
        this.showText.animate({x: param.x + 10,
            y: Math.floor(param.y +this.height/2) }, ms );
    }else{
        /*this.showText.attr( { x: (param.x + this.width/2 ),
            y: Math.floor(param.y +this.height/2) } ); */
        this.showText.animate({ x: (param.x + this.width/2 ),
            y: Math.floor(param.y +this.height/2)}, ms );
    }
    me.changeWidgetPosition(param ,ms );
}
Wee.wr.TextLabel.prototype.animatePath = function( params, totalMS )
{
    var me = this;
    var _textPath = {};
    for( var ms in params ){
        var _path = params[ms];
        _textPath[ms] = {
            x: (_path.x + this.width/2 ),
            y: Math.floor(_path.y +this.height/2)
        }
    }
    this.showText.animate( _textPath, totalMS );
    me.widgetAnimatePath( params ,totalMS );
}
//--------------------------------------------------------------------------------------------------------------
Wee.wr.ComleteRate = function( paper,config )
{
    var dfltComleteRateConfig = {
        ydLabelAttr:{
            text:'預定量',
            textColor:"#FFFFFF"
        },
        tjLabelAttr:{
            text:'停機臺數',
            textColor:"#FFFFFF"
        },
        titleAttr:{
            text:'PRC加硫',
            textColor:"#FFFFFF"
        },
        title:'',
        panAttr:{
            backAttr:{
                //fill: "black",
                stroke: "#A9A9A9",
                "stroke-width":0,
                cursor: "pointer",
                "fill-opacity":1
            },
            label:'達成率',
            per: 0
        },
        tdAttr:{
            textColor:'yellow',
            text:'0',
            mindleLine: 0,
            textRatio: 0.6,
            backColor:"90-#B0B0B0-#1E1E1E:50-#B0B0B0"
        },
        tjAttr:{
            textColor:'red',
            text:'0',
            mindleLine: 0,
            textRatio: 0.6,
            backColor:"90-#B0B0B0-#1E1E1E:50-#B0B0B0"
        },
        wPosition:"l",
        wRatio:0.6,
        borderWidth:0
    };
    Wee.wr.ComleteRate.superclass.constructor.call( this, paper, Wee.apply({}, config, dfltComleteRateConfig ) );

    this.dc = new Wee.wr.ProgressPan( this.paper, Wee.apply({}, this.panAttr, dfltComleteRateConfig.panAttr  ) );
    this.ydL = new Wee.wr.TextLabel( this.paper, Wee.apply({}, this.ydLabelAttr, dfltComleteRateConfig.ydLabelAttr  ) );
    this.ydV = new Wee.wr.TextLabel( this.paper, Wee.apply({}, this.tdAttr, dfltComleteRateConfig.tdAttr  ) );
    this.tjL = new Wee.wr.TextLabel( this.paper, Wee.apply({}, this.tjLabelAttr, dfltComleteRateConfig.tjLabelAttr  ) );
    this.tjV = new Wee.wr.TextLabel( this.paper, Wee.apply({}, this.tjAttr, dfltComleteRateConfig.tjAttr  ) );
    this.tt = new Wee.wr.TextLabel( this.paper, Wee.apply({}, this.titleAttr, dfltComleteRateConfig.titleAttr  ) );

    this.setAttr( Wee.apply({}, this.backAttr, dfltComleteRateConfig.backAttr));
};
Wee.extend( Wee.wr.ComleteRate, Wee.wr.Widget );
Wee.wr.ComleteRate.prototype.drawAction = function() {
    var _x = this.x + this.borderWidth,
        _y = this.y + this.borderWidth,
        _width = this.width - 2*this.borderWidth,
        _height = this.height - 2*this.borderWidth;
    if( this.wPosition == "r" ){
        var _lWidth = parseInt( _width * (1 - this.wRatio) );
        var _rWidth = _width - _lWidth;
        this.tt.attr({ x: _x + _lWidth + 2, y: _y , width: _rWidth, height: _height * 0.3 });
        if( this.title != '' )this.tt.setText( this.title );
        this.dc.attr({ x: _x + _lWidth + 2, y: _y + _height * 0.3, width: _rWidth-2, height: _height * 0.7 });
        this.ydL.attr({ x: _x, y: _y , width: _lWidth, height: _height * 0.2 });
        this.ydV.attr({ x: _x, y: _y + _height * 0.2 , width: _lWidth, height: _height * 0.25 });
        this.tjL.attr({ x: _x, y: _y + _height * 0.5 , width: _lWidth, height: _height * 0.2 });
        this.tjV.attr({ x: _x, y: _y + _height * 0.7 , width: _lWidth, height: _height * 0.25 });
    }else{
        var _lWidth = parseInt( _width * this.wRatio );
        var _rWidth = _width - _lWidth;
        this.tt.attr({ x: _x, y: _y , width: _lWidth-2, height: _height * 0.3 });
        if( this.title != '' )this.tt.setText( this.title );
        this.dc.attr({ x: _x, y: _y + _height * 0.3, width: _lWidth-2, height: _height * 0.7 });
        this.ydL.attr({ x: _x + _lWidth, y: _y , width: _rWidth, height: _height * 0.2 });
        this.ydV.attr({ x: _x + _lWidth, y: _y + _height * 0.2 , width: _rWidth, height: _height * 0.25 });
        this.tjL.attr({ x: _x + _lWidth, y: _y + _height * 0.5 , width: _rWidth, height: _height * 0.2 });
        this.tjV.attr({ x: _x + _lWidth, y: _y + _height * 0.7 , width: _rWidth, height: _height * 0.25 });
    }
    this.tt.drawAction();
    this.dc.drawAction();
    this.ydL.drawAction();
    this.ydV.drawAction();
    this.tjL.drawAction();
    this.tjV.drawAction();

    this.drawWidget();
}
Wee.wr.ComleteRate.prototype.loadData = function( data )
{
    //preorder 預訂量，　
    if( data.preorder ) this.ydV.setText(data.preorder  );
    else this.ydV.setText( "0"  );
    if( data.stopEquipNum ) this.tjV.setText(data.stopEquipNum  );
    else this.tjV.setText( "0"  );
    if( data.cmplRate ) this.dc.setPer( data.cmplRate  );
    else this.dc.setPer( 0  );
}
//--------------------------------------------------------------------------------------------------------------
Wee.wr.SimpleEquip = function( paper,config )
{
    var me = this;
    var simpleEquipConfig = {
        runColor: "#33ff66",
        stopColor:"#ff3300",
        holdColor:"#ffff33",
        deltColor:"#CCCCCC",
        status:"",
        textAttr:{
            fill:"#1C1C1C",
            stroke:"#1C1C1C",
            "font-weight":"normal",
            "text-anchor":"middle"
        },
        backAttr:{
            fill: "#CCCCCC",
            stroke: "#CCCCCC",
            "stroke-width":1,
            cursor: "pointer",
            "fill-opacity":1
        },
        textRatio:0.4
    };
    Wee.wr.SimpleEquip.superclass.constructor.call( this, paper, Wee.apply({}, config, simpleEquipConfig ) );
    this.initAttr();
    this.showText = this.paper.text(0,0,this.text ).attr( Wee.apply({}, this.textAttr, simpleEquipConfig.textAttr) ).hide();
    this.setAttr( Wee.apply({}, this.backAttr, simpleEquipConfig.backAttr));
    this.border.mousedown( function( e ){
        WRListener.fireEvent("equipClick", me.id　 );
    });
};
Wee.extend( Wee.wr.SimpleEquip,Wee.wr.Widget );
Wee.wr.SimpleEquip.prototype.initAttr = function()
{
    if( this.status == "RUN" ){
        this.backAttr["fill"] = this.runColor;
    }else if( this.status == "STOP" ){
        this.backAttr["fill"] = this.stopColor;
    }else if( this.status == "HOLD" ){
        this.backAttr["fill"] = this.holdColor;
    }else {
        this.backAttr["fill"] = this.deltColor;
    }
}
Wee.wr.SimpleEquip.prototype.drawAction = function()
{
    var me = this;
    if( this.text && this.text != '' ){
        var titleWordsAttr ={
            x:(this.x + this.width/2 ),
            y:Math.floor(this.y +this.height/2),
            "font-size":this.height*this.textRatio
        };
        if( me.textAttr["text-anchor"] == "start" )
            titleWordsAttr = {
                x: this.x + 10,
                y: Math.floor(this.y +this.height/2),
                "font-size":this.height*this.textRatio
                //text: this.value
            };
        this.showText.attr( titleWordsAttr ).show();
        if( this.transform ){
            this.showText.transform( this.transform );
        }
    }
    this.drawWidget();
}
Wee.wr.SimpleEquip.prototype.loadData = function( status )
{
    if( status == "RUN" ){
        this.setAttr( { fill: this.runColor });
    }else if( status == "STOP" ){
        this.setAttr( { fill: this.stopColor });
    }else if( status == "HOLD" ){
        this.setAttr( { fill: this.holdColor });
    }else {
        this.setAttr( { fill: this.deltColor });
    }
}

Wee.wr.LabelField = function( paper, config, groupSet ){
    var dfltAttr = {
        labelColer:"#0893F5",
        labelSize: 14,
        labelAnchor:"start",
        valueColer:"#B3EE3A",
        valueSize: 13,
        valueAnchor:"start",
        labelRate:0.35,
        label:'',
        value:''
    };
    Wee.wr.EquipInfra.superclass.constructor.call( this, paper, Wee.apply({}, config, dfltAttr ), groupSet  );
    this.labelObj = this.paper.text( 0, 0, "").hide();
    this.valueObj = this.paper.text( 0, 0, "").hide();
    if( this.groupSet ){
        this.groupSet.push( this.labelObj, this.valueObj );
    }
}

Wee.extend( Wee.wr.LabelField,Wee.wr.Widget );

Wee.wr.LabelField.prototype.drawAction = function()
{
    var _labelX = this.x,  _y = this.y + this.height / 2 ,_valueX = this.x +　this.width * this.labelRate;
    this.labelObj.attr( {
        x : _labelX,
        y: _y,
        text: this.label,
        fill:this.labelColer,
        stroke:this.labelColer,
        "font-weight":"normal",
        "font-size" : this.labelSize,
        "text-anchor":this.labelAnchor }).show();

    this.valueObj.attr( {
        x : _valueX,
        y: _y,
        text: this.value,
        fill:this.valueColer,
        stroke:this.valueColer,
        "font-weight":"normal",
        "font-size" : this.valueSize,
        "text-anchor":this.valueAnchor }).show();
}

Wee.wr.LabelField.prototype.setValue = function( value )
{
    var me = this;
    this.value = value;
    this.valueObj.attr({ text: value });
}



Wee.wr.EquipInfra = function( paper,config, groupSet )
{
    var me = this;
    var equipInfraConfig = {
        titleAttr:{
            fill:"#0893F5",
            stroke:"#0893F5",
            "font-weight":"normal",
            "font-size" : 20,
            "text-anchor":"middle"
        },
        splitAttr: {
            stroke:"#3366ff",
            "stroke-width":1,
            "stroke-dasharray":"--"
        },
        sendTitleAttr:{
            fill:"#E94520",
            stroke:"#0893F5",
            "font-weight":"normal",
            "font-size" : 14,
            "text-anchor":"middle"
        },
        titleBackColor:"90-#B0B0B0-#1E1E1E:50-#B0B0B0",
        title:"裝置資訊",
        rowHeight:30
    };
    Wee.wr.EquipInfra.superclass.constructor.call( this, paper, Wee.apply({}, config, equipInfraConfig ), groupSet );
    //----------------------------------------------------------------------------------------------------------
    this.titleBack = this.paper.rect( 0, 0, 100,30, 15 ).hide();
    this.titleObj = this.paper.text( 10, 10, "").hide();
    //----------------------------------------------------------------------------------------------------------
    this.resrce = new Wee.wr.LabelField( paper, { label: "裝置編碼", value: "" }, groupSet );
    this.eqType = new Wee.wr.LabelField( paper, { label: "機臺型別", value: "" }, groupSet );
    this.eqDesc = new Wee.wr.LabelField( paper, { label: "裝置名稱", value: "" }, groupSet );
    this.workCenter = new Wee.wr.LabelField( paper, { label: "車間", value: "" }, groupSet );
    this.department = new Wee.wr.LabelField( paper, { label: "部門", value: "" }, groupSet );
    this.status = new Wee.wr.LabelField( paper, { label: "狀態", value: "" }, groupSet );
    this.eqInfraSplit = this.paper.path('M 0,0 H5 z').attr( this.splitAttr ).hide();
    //----------------------------------------------------------------------------------------------------------
    this.specification = new Wee.wr.LabelField( paper, { label: "生產規格", value: "" }, groupSet );
    this.productionSchedule = new Wee.wr.LabelField( paper, { label: "生產進度", value: "" }, groupSet );
    this.rejectRation = new Wee.wr.LabelField( paper, { label: "不良統計", value: "" }, groupSet );
    this.stopTime = new Wee.wr.LabelField( paper, { label: "停機時間", value: "" }, groupSet );
    this.stopReason = new Wee.wr.LabelField( paper, { label: "停機原因", value: "" }, groupSet );
    //----------------------------------------------------------------------------------------------------------
    this.itemSplit  = this.paper.path('M 0,0 H5 z').attr( this.splitAttr ).hide();
    this.toolSplit = this.paper.path('M 0,0 H5 z').attr( this.splitAttr ).hide();
    this.itemLabel = this.paper.text( 10, 10, "上料資訊" ).attr( this.sendTitleAttr ).hide();
    this.toolLabel = this.paper.text( 10, 10, "工具資訊" ).attr( this.sendTitleAttr ).hide();
    this.groupSet.push( this.titleBack, this.titleObj,this.eqInfraSplit, this.itemSplit, this.toolSplit, this.itemLabel, this.toolLabel );
    this.groupSet.mousedown( function( e ){
        WRListener.fireEvent("CloseEquipInfra" );
    });
    //----------------------------------------------------------------------------------------------------------
    this.itemSet = [];
    this.toolSet = [];
};
Wee.extend( Wee.wr.EquipInfra,Wee.wr.Widget );

Wee.wr.EquipInfra.prototype.drawAction = function()
{
    var _x = this.x + 10, _y = this.y, _height = this.height, _width = this.width - 20,_height = this.height;
    var _lableValueWidth =  parseInt( _width  / 2 );
    //----------------------------------------------------------------------------------------------------------
    this.titleBack.attr( {x: this.x, y: this.y, width: this.width, height: this.rowHeight, fill: this.titleBackColor }).show();
    this.titleObj.attr( { x: this.x + this.width/2, y: this.y +  this.rowHeight / 2, text: this.title });
    this.titleObj.attr( this.titleAttr ).show();
    //----------------------------------------------------------------------------------------------------------
    this.resrce.attr({
        x: _x,
        y: _y + this.rowHeight /2,
        width: _lableValueWidth,
        height: this.rowHeight * 2
    });
    this.resrce.drawAction();
    this.eqType.attr({
        x: _x + _lableValueWidth,
        y: _y + this.rowHeight /2,
        width: _lableValueWidth,
        height: this.rowHeight * 2
    });
    this.eqType.drawAction();
    //----------------------------------------------------------------------------------------------------------
    this.eqDesc.attr({
        x: _x,
        y: _y + this.rowHeight * 2,
        width: _lableValueWidth,
        height: this.rowHeight
    });
    this.eqDesc.drawAction();
    //----------------------------------------------------------------------------------------------------------
    this.workCenter.attr({
        x: _x,
        y: _y + this.rowHeight * 3,
        width: _lableValueWidth,
        height: this.rowHeight
    });
    this.workCenter.drawAction();
    this.department.attr({
        x: _x + _lableValueWidth,
        y: _y + this.rowHeight * 3,
        width: _lableValueWidth,
        height: this.rowHeight
    });
    this.department.drawAction();
    //----------------------------------------------------------------------------------------------------------
    this.status.attr({
        x: _x,
        y: _y + this.rowHeight * 4,
        width: _lableValueWidth,
        height: this.rowHeight
    });
    this.status.drawAction();
    //----------------------------------------------------------------------------------------------------------
    this.specification.attr({
        x: _x,
        y: _y + this.rowHeight * 5,
        width: _lableValueWidth,
        height: this.rowHeight
    });
    this.specification.drawAction();
    this.productionSchedule.attr({
        x: _x + _lableValueWidth,
        y: _y + this.rowHeight * 5,
        width: _lableValueWidth,
        height: this.rowHeight
    });
    this.productionSchedule.drawAction();
    //----------------------------------------------------------------------------------------------------------
    this.rejectRation.attr({
        x: _x,
        y: _y + this.rowHeight * 6,
        width: _lableValueWidth,
        height: this.rowHeight
    });
    this.rejectRation.drawAction();
    this.stopTime.attr({
        x: _x + _lableValueWidth,
        y: _y + this.rowHeight * 6,
        width: _lableValueWidth,
        height: this.rowHeight
    });
    this.stopTime.drawAction();
    //----------------------------------------------------------------------------------------------------------
    this.stopReason.attr({
        x: _x,
        y: _y + this.rowHeight * 7,
        width: _lableValueWidth,
        height: this.rowHeight
    });
    this.stopReason.drawAction();
    //----------------------------------------------------------------------------------------------------------
    var _eqInfraPath = "M"+ _x + ","+ (  _y + this.rowHeight * 5 )+" H"+ ( _x + _width  ) +" z";
    this.eqInfraSplit.attr({path:_eqInfraPath }).show();
    var itemSpkitPath = "M"+ _x + ","+ (  _y + this.rowHeight * 8 )+" H"+ ( _x + _width  ) +" z";
    this.itemSplit.attr({path:itemSpkitPath}).show();
    var toolSpkitPath = "M"+ ( _x + _lableValueWidth ) + ","+ (  _y + this.rowHeight * 8 )+" V"+ ( _y + this.rowHeight * 12  ) +" z";
    this.toolSplit.attr({path:toolSpkitPath }).show();

    this.itemLabel.attr({ x: _x + _lableValueWidth / 2 , y:  _y + this.rowHeight * 17 / 2 }).show();
    this.toolLabel.attr({ x: _x + _lableValueWidth * 3 / 2, y: _y + this.rowHeight * 17 / 2  }).show();

    this.drawWidget();
}

Wee.wr.EquipInfra.prototype.loadData = function( data )
{
    if( !data )return;
    var equipInfra = data["RESOURCE_INFO"];
    var equipItems = data["MATERIAL_LIST"];
    var equipTools = data["TOOL_LIST"];
    //----------------------------------------------------------------------------------------------------------
    if( equipInfra ){
        this.resrce.setValue( equipInfra.resrce );
        this.eqType.setValue( equipInfra.eqType );
        this.eqDesc.setValue( equipInfra.description );
        this.workCenter.setValue( equipInfra.workCenter );
        this.department.setValue( equipInfra.department );
        this.status.setValue( equipInfra.status );
        this.specification.setValue( equipInfra.specification );
        this.productionSchedule.setValue( equipInfra.productionSchedule );
        this.rejectRation.setValue( equipInfra.rejectRation );
        this.stopTime.setValue( equipInfra.stopTime );
        this.stopReason.setValue( equipInfra.stopReason );
    }
    //----------------------------------------------------------------------------------------------------------
    if( equipItems ){
        var itemQty = equipItems.length;
        var _itemX = this.x + 10, _itemY = this.y + 9 * this.rowHeight;
        var _itemWidth = (this.width - 20) / 4;
        for( var i = 0; i < equipItems.length; i++ ){
            var equipItem = equipItems[i];
            var k = i % 6, _col = parseInt( i / 6 );
            var itemObj = new Wee.wr.TextLabel( this.paper,{
                x: _itemX  + _col * _itemWidth,
                y: _itemY + k * this.rowHeight / 2,
                width: _itemWidth,
                height: this.rowHeight / 2 - 5,
                text: equipItem.item,
                //backColor: "red",
                textAnchor: 'start',
                textColor:"#B3EE3A",
                textRatio: 1
            } );
            itemObj.drawAction();
            this.itemSet.push( itemObj );
        }
    }
    //----------------------------------------------------------------------------------------------------------
    if( equipTools ) {
        var _toolX = this.x + this.width  / 2 , _toolY = this.y + 9 * this.rowHeight;
        var _toolWidth = this.width / 4 - 10;
        for (var i = 0; i < equipTools.length; i++) {
            var equipTool = equipTools[i];
            var toolObj = new Wee.wr.TextLabel(this.paper, {
                x: _toolX + 5,
                y: _toolY + i * this.rowHeight / 2,
                width: _toolWidth,
                height: this.rowHeight / 2 - 5,
                text: equipTool.toolNumber + ' ' + equipTool.usageCount+ '/' + equipTool.washUsageCount,
                textColor: "#B3EE3A",
                textAnchor: 'start',
                textRatio: 1
            });
            toolObj.drawAction();
            this.toolSet.push( toolObj );
        }
    }
}

Wee.wr.EquipInfra.prototype.reset = function(  )
{
    this.resrce.setValue( "" );
    this.eqType.setValue( "" );
    this.eqDesc.setValue( "" );
    this.workCenter.setValue( "" );
    this.department.setValue( "" );
    this.status.setValue( "" );
    this.specification.setValue( "" );
    this.productionSchedule.setValue( "" );
    this.rejectRation.setValue( "" );
    this.stopTime.setValue( "" );
    this.stopReason.setValue( "" );
    for( var i = 0; i < this.itemSet.length; i++ ){
        this.itemSet[i].remove();
    }
    for( var i = 0; i < this.toolSet.length; i++ ){
        this.toolSet[i].remove();
    }
}

/**
 * 百分比表示
 * @param paper
 * @param config
 * @constructor
 *  2019-12-1 joe
 */
Wee.wr.Percentage = function( paper,config )
{
    var defaultPercentage = {
        numLabelAttr:{
            textRatio:0.7,
            mindleLine:0,
            textColor:"#E94520",
            borderWidth:1,
            backColor:'90-#B0B0B0-#1E1E1E:50-#B0B0B0',
            borderColor:"#084B5B"
        },
        dclPointAttr:{
            fill: "#E94520",
            stroke: "#E94520",
            "stroke-width":1,
            "fill-opacity":1
        },
        prcntSignAttr:{
            fill:"#FFFFFF",
            stroke:"#FFFFFF",
            "font-size":12,
            "font-weight":"normal",
            "text-anchor":"start"
        },
        perFlag:0
    };

    Wee.wr.Percentage.superclass.constructor.call( this, paper, Wee.apply({}, config, defaultPercentage ) );
    this.startNum = new Wee.wr.TextLabel( this.paper, Wee.apply( {text:0}, this.numLabelAttr, defaultPercentage.numLabelAttr ));
    this.middleNum = new Wee.wr.TextLabel( this.paper, Wee.apply( {text:0}, this.numLabelAttr, defaultPercentage.numLabelAttr ));
    this.lastNum = new Wee.wr.TextLabel( this.paper, Wee.apply( {text:0}, this.numLabelAttr, defaultPercentage.numLabelAttr ));
    this.prcntSignAttr = this.paper.text( 1,1, "%" ).attr( defaultPercentage.prcntSignAttr ).hide();
};
Wee.extend( Wee.wr.Percentage, Wee.wr.Widget );

Wee.wr.Percentage.prototype.drawAction = function()
{
    var me = this,_width,_height;
    if( me.width > 2 * me.height ){
        _width = 2 * me.height;
        _height = me.height;
    }else{
        _height = me.width / 2;
        _width = me.width;
    }
    //----------------------------------------------------------------------------------------------------------
    var _startX = me.x + ( me.width - _width )/ 2,
        _startY = me.y + ( me.height - _height )/ 2,
        _spaceW = Math.trunc( _width / 4 ), _r = 0.2*_spaceW;
    if( _r < 2 )_r = 2;
    //----------------------------------------------------------------------------------------------------------
    this.startNum.attr({x:_startX, y: _startY, width: 2*_spaceW -5, height: _height  });
    this.middleNum.attr({x:(_startX + 2*_spaceW ), y: _startY, width: 2*_spaceW - 5, height: _height  });
    this.startNum.drawAction();
    this.middleNum.drawAction();
    //----------------------------------------------------------------------------------------------------------
    if( !this.decimalPoint )
        this.decimalPoint = this.paper.circle( (_startX + 4.2 * _spaceW - 2),( _startY + _height - 0.4*_spaceW ), _r).attr( this.dclPointAttr );
    //----------------------------------------------------------------------------------------------------------
    if( this.perFlag == 0 ){
        this.decimalPoint.show();
        this.lastNum.attr({x:( _startX + 4.4*_spaceW ), y: _startY, width: 2*_spaceW - 5, height: _height  });
        this.prcntSignAttr.attr({x:( _startX + 6.4*_spaceW ),y:( _startY + 7*_height/8 )});
        this.lastNum.drawAction();
        this.prcntSignAttr.show();
    }else{
        this.decimalPoint.hide();
        this.lastNum.attr({x:( _startX + 4*_spaceW ), y: _startY, width: 2*_spaceW - 5, height: _height  });
        this.prcntSignAttr.attr({x:( _startX + 6*_spaceW ),y:( _startY + 7*_height/8 )});
        this.lastNum.drawAction();
        this.prcntSignAttr.show();
    }

}

Wee.wr.Percentage.prototype.loadData = function( value )
{

    if( !value )value = 0;
    else if( value >= 10 ){

    }
    //----------------------------------------------------------------------------------------------------------
    if( value >= 1 && value < 10 ){
        if( this.perFlag == 0 ){
            this.perFlag = 1;
            this.drawAction();
        }
        var _value = value + "";
        var _index = _value.indexOf( "." );
        if( _index == -1 ){
            this.startNum.setText(  value );
            this.middleNum.setText( 0 );
            this.lastNum.setText( 0 );
        }else{
            this.startNum.setText( _value.substring(_index - 1, _index ) );
            var middleValue = _value.substring(_index + 1, _index + 2 );
            this.middleNum.setText( middleValue ? middleValue:0 );
            var lastValue = _value.substring(_index + 2, _index + 3 );
            this.lastNum.setText( lastValue ? lastValue:0 );
        }
    }else{
        if( this.perFlag == 1 ){
            this.perFlag = 0;
            this.drawAction();
        }
        if( value == 0 ){
            this.startNum.setText( 0 );
            this.middleNum.setText( 0 );
            this.lastNum.setText( 0 );
        }else{
            var _value = value + "";
            var startValue = _value.substring( 2,3 );
            var middleValue = _value.substring( 3,4 );
            var lastValue = _value.substring( 4,5 );
            this.startNum.setText( startValue );
            this.middleNum.setText( middleValue ? middleValue:0 );
            this.lastNum.setText( lastValue ? lastValue:0 );
        }
    }
}
