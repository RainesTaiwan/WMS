/**
 * Created by fw-cs on 2019/3/4.
 */
//---------------------------------------------------------------------------------------------------------------
/*
可以更改字型和底色的文字標識
主要配置項
 textColor:"#808000", //字型顏色
 backColor:"#00FA9A", //底色顏色
 text:"AFGGG",        //預設字
 textRatio:0.2       //字型大小
 borderWidth:1,      //邊框尺寸
 borderColor:"#9400D3"//邊框顏色
 */
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
//---------------------------------------------------------------------------------------------------------------
Wee.wr.MultDisplayBoard = function( paper,config )
{
    var multDisplayBoardConfig = {
        layout:"col",//row OR col  row是按順序從上到下，title, firstLabel，firstValue,secondLabel, secondValue
        borderWidth:0,      //邊框尺寸
        borderColor:"white",//邊框顏色
        showGroup:2,//顯示數值 的組數，每組包含label和Value，可以為1，2，3
        titleRatio:0.2,
        lableRatio:0.4,
        backR:0,
        titleAttr:{
            textRatio:0.6,
            textAttr:{
                fill:"#FFD700",
                stroke:"#FFD700",
                "font-weight":"noral",
                "text-anchor":"middle"
            },
            backAttr:{
                fill:"#000080",
                "stroke-width":1,
                stroke: "white",
                "fill-opacity":.9
            },
            text:"標題"
        },
        firstLabelAttr:{
            textRatio:0.6,
            textAttr:{
                fill:"#FFD700",
                stroke:"#FFD700"
            },
            backAttr:{
                fill:"#2E8B57",
                "stroke-width":1,
                stroke: "white"
            },
            text:"Label1"
        },
        firstValueAttr:{
            textRatio:0.6,
            textAttr:{
                fill:"white",
                stroke:"white"
            },
            backAttr:{
                fill:"black",
                "stroke-width":1,
                stroke: "white",
                "fill-opacity":.8
            },
            text:"0"
        },
        secondLabelAttr:{
            textRatio:0.6,
            textAttr:{
                fill:"#FFD700",
                stroke:"#FFD700"
            },
            backAttr:{
                fill:"#2E8B57",
                "stroke-width":1,
                stroke: "white"
            },
            text:"Label2"
        },
        secondValueAttr:{
            textRatio:0.6,
            textAttr:{
                fill:"white",
                stroke:"white"
            },
            backAttr:{
                fill:"black",
                "stroke-width":1,
                stroke: "white",
                "fill-opacity":.8
            },
            text:"0"
        },
        thirdLabelAttr:{
            textRatio:0.6,
            textAttr:{
                fill:"black",
                stroke:"black"
            },
            backAttr:{
                fill:"#FFD700",
                "stroke-width":1,
                stroke: "white"
            },
            text:"label3"
        },
        thirdValueAttr:{
            textRatio:0.,
            textAttr:{
                fill:"white",
                stroke:"white"
            },
            backAttr:{
                fill:"black",
                "stroke-width":1,
                stroke: "white",
                "fill-opacity":.8
            },
            text:"0"
        },
        backAttr:{
            fill: "#A9A9A9",
            stroke: "#A9A9A9",
            "stroke-width":1,
            cursor: "pointer",
            "fill-opacity":1
        }
    };
    Wee.wr.MultDisplayBoard.superclass.constructor.call( this, paper, Wee.apply({}, config, multDisplayBoardConfig ) );
    this.initAttr();
    this.showTitle = new Wee.wr.TextLabel( this.paper, Wee.apply({}, this.titleAttr, multDisplayBoardConfig.titleAttr  ) );

    this.firstLabel = new Wee.wr.TextLabel( this.paper, Wee.apply({}, this.firstLabelAttr, multDisplayBoardConfig.firstLabelAttr  ) );
    this.firstValue = new Wee.wr.TextLabel( this.paper,   Wee.apply({}, this.firstValueAttr, multDisplayBoardConfig.firstValueAttr  ) );

    this.secondLabel = new Wee.wr.TextLabel( this.paper,  Wee.apply({}, this.secondLabelAttr, multDisplayBoardConfig.secondLabelAttr ));
    this.secondValue = new Wee.wr.TextLabel( this.paper,  Wee.apply({}, this.secondValueAttr, multDisplayBoardConfig.secondValueAttr ) );

    this.thirdLabel = new Wee.wr.TextLabel(this.paper, Wee.apply({}, this.thirdLabelAttr, multDisplayBoardConfig.thirdLabelAttr));
    this.thirdValue = new Wee.wr.TextLabel(this.paper, Wee.apply({}, this.thirdValueAttr, multDisplayBoardConfig.thirdValueAttr));

    this.setAttr(  Wee.apply({},this.backAttr,multDisplayBoardConfig.backAttr) );
}
Wee.extend( Wee.wr.MultDisplayBoard, Wee.wr.Widget );

Wee.wr.MultDisplayBoard.prototype.initAttr = function()
{
    if( this.backColor ){
        this.backAttr["fill"] = this.backColor;
        this.backAttr["fill-opacity"] = 1;
    }
    if( this.backR ){
        this.backAttr["r"] = this.backR;
    }
    if( this.borderWidth ){
        this.backAttr["stroke-width"] = this.borderWidth;
        this.backAttr["stroke"] = this.borderColor || this.backAttr["fill"];
        if( ( this.width -  2*this.borderWidth )<= 0 || ( this.height - 2*this.borderWidth ) <= 0 ){
            console.log( "MultDisplayBoard，BorderWidth的寬度大於給出範圍的長度或寬度" );
            return;
        }
        this.backAttr["x"] = this.x + this.borderWidth;
        this.backAttr["y"] = this.y + this.borderWidth;
        this.backAttr["width"] =  this.width -  2*this.borderWidth ;
        this.backAttr["height"] = this.height - 2*this.borderWidth;
    }
}

Wee.wr.MultDisplayBoard.prototype.drawAction = function()
{
    var _x = this.x + this.borderWidth,
        _y = this.y + this.borderWidth,
        _width = this.width - 2*this.borderWidth,
        _height = this.height - 2*this.borderWidth;

    this.showTitle.attr({x:_x + 1, y:_y ,width:_width -2, height: _height * this.titleRatio });

    if( this.layout == "row" ){
        var _groupRate = ( 1- this.titleRatio ) / this.showGroup;
        if( this.showGroup > 0 ){
            this.firstLabel.attr({x:_x + 1, y:_y + _height*this.titleRatio ,width:_width -2, height:_height*_groupRate* this.lableRatio});
            this.firstValue.attr({x:_x + 1, y:_y+ _height*(this.titleRatio + _groupRate* this.lableRatio ),width:_width -2, height:_height*_groupRate* (1- this.lableRatio) });
            if( this.showGroup > 1 ){
                this.secondLabel.attr({ x:_x + 1, y:_y + _height*( this.titleRatio + _groupRate ),width:_width -2, height:_height*_groupRate* this.lableRatio  });
                this.secondValue.attr({x:_x + 1, y:_y+ _height*(this.titleRatio + _groupRate + _groupRate* this.lableRatio ),width:_width -2, height:_height*_groupRate* (1- this.lableRatio) });
                if( this.showGroup > 2 ){
                    this.thirdLabel.attr({ x:_x + 1, y:_y + _height*( this.titleRatio + 2*_groupRate ), width:_width -2, height:_height*_groupRate* this.lableRatio  });
                    this.thirdValue.attr({x:_x + 1, y:_y+ _height*(this.titleRatio +2*_groupRate + _groupRate* this.lableRatio ),width:_width -2, height:_height*_groupRate* (1- this.lableRatio) });
                }
            }
        }
    }else if( this.layout == "row-group" ){
        var _groupRate = ( 1- this.titleRatio ) / this.showGroup;
        if( this.showGroup > 0 ){
            this.firstLabel.attr({x:_x + 2, y:_y + _height*this.titleRatio ,width:_width* this.lableRatio -2, height:_height*_groupRate});
            this.firstValue.attr({x:_x + 2 + _width* this.lableRatio, y:_y + _height*this.titleRatio ,width:_width*(1-this.lableRatio) -2, height:_height*_groupRate });
            if( this.showGroup > 1 ){
                this.secondLabel.attr({x:_x + 2, y:_y + _height*(this.titleRatio + _groupRate ),width:_width* this.lableRatio -2, height:_height*_groupRate});
                this.secondValue.attr({x:_x + 2 + _width* this.lableRatio, y:_y + _height*( this.titleRatio + _groupRate),width:_width*(1-this.lableRatio) -2, height:_height*_groupRate });
                if( this.showGroup > 2 ){
                    this.thirdLabel.attr({x:_x + 2, y:_y + _height*(this.titleRatio + 2*_groupRate ) ,width:_width* this.lableRatio -2, height:_height*_groupRate});
                    this.thirdValue.attr({x:_x + 2 + _width* this.lableRatio, y:_y + _height*( this.titleRatio + 2*_groupRate) ,width:_width*(1-this.lableRatio) -2, height:_height*_groupRate });
                }
            }
        }
    }else if(this.layout == "col" ){
        var _groupRate = 1 / this.showGroup;
        if( this.showGroup > 0 ){
            this.firstLabel.attr({ x:_x + 2, y:_y + _height*this.titleRatio, width:_width*_groupRate*this.lableRatio -4, height:_height*(1 - this.titleRatio ) });
            this.firstValue.attr({ x:_x + 2+ _width*_groupRate*this.lableRatio , y:_y+ _height* this.titleRatio, width:_width*_groupRate*( 1 - this.lableRatio ) -4, height:_height*(1 - this.titleRatio ) });
            if( this.showGroup > 1 ){
                this.secondLabel.attr({ x:_x + 2 +_width*_groupRate, y:_y + _height*this.titleRatio ,width:_width*_groupRate*this.lableRatio -4, height:_height*(1 - this.titleRatio ) });
                this.secondValue.attr({ x:_x + 2 +_width*_groupRate + _width*_groupRate*this.lableRatio, y:_y+ _height* this.titleRatio , width:_width*_groupRate*( 1 - this.lableRatio ), height:_height*(1 - this.titleRatio )});
                if( this.showGroup > 2 ){
                    this.thirdLabel.attr({ x:_x +_width*2*_groupRate + 2, y:_y + _height*this.titleRatio ,width:_width*_groupRate*this.lableRatio -4, height:_height*(1 - this.titleRatio ) });
                    this.thirdValue.attr({ x:_x +_width*2*_groupRate + _width*_groupRate*this.lableRatio + 2, y:_y+ _height* this.titleRatio + _height*(1 - this.titleRatio )*this.lableRatio , width:_width*_groupRate*( 1 - this.lableRatio ), height:_height* (1- this.titleRatio) });
                }
            }
        }
    }else if(this.layout == "col-group" ) {
        var _groupRate = 1/ this.showGroup;
        if( this.showGroup > 0 ){
            this.firstLabel.attr({ x:_x + 2, y:_y + _height*this.titleRatio ,width:_width*_groupRate -4, height:_height*(1 - this.titleRatio )*this.lableRatio });
            this.firstValue.attr({ x:_x + 2, y:_y+ _height* this.titleRatio + _height*(1 - this.titleRatio )*this.lableRatio , width:_width*_groupRate -4, height:_height*(1 - this.titleRatio )*(1 - this.lableRatio) });
            if( this.showGroup > 1 ){
                this.secondLabel.attr({ x:_x + 2 +_width*_groupRate, y:_y + _height*this.titleRatio ,width:_width*_groupRate -4, height:_height*(1 - this.titleRatio )*this.lableRatio });
                this.secondValue.attr({ x:_x + 2 +_width*_groupRate, y:_y+ _height* this.titleRatio + _height*(1 - this.titleRatio )*this.lableRatio , width:_width*_groupRate -4, height:_height*(1 - this.titleRatio )*(1 - this.lableRatio) });
                if( this.showGroup > 2 ){
                    this.thirdLabel.attr({ x:_x +_width*2*_groupRate + 2, y:_y + _height*this.titleRatio ,width:_width*_groupRate -4, height:_height*(1 - this.titleRatio )*this.lableRatio });
                    this.thirdValue.attr({ x:_x +_width*2*_groupRate + 2, y:_y+ _height* this.titleRatio + _height*(1 - this.titleRatio )*this.lableRatio , width:_width -4, height:_height* (1- this.titleRatio)* (1 - this.lableRatio ) });
                }
            }
        }
    }
    else{
        console.log( "MultDisplayBoard中Layout為" + this.layout + "，沒有此型別" );
        return;
    }

    this.showTitle.drawAction();
    if( this.showGroup  > 0 ){
        this.firstLabel.drawAction();
        this.firstValue.drawAction();
        if( this.showGroup  > 1 ){
            this.secondLabel.drawAction();
            this.secondValue.drawAction();
            if( this.showGroup  > 2 ){
                this.thirdLabel.drawAction();
                this.thirdValue.drawAction();
            }
        }
    }

    this.drawWidget();
};

Wee.wr.MultDisplayBoard.prototype.setFirstValue = function ( num )
{
    this.firstValue.setText( num );
}

Wee.wr.MultDisplayBoard.prototype.setSecondValue = function ( num )
{
    this.secondValue.setText( num );
}

Wee.wr.MultDisplayBoard.prototype.setFirstLabel = function ( num )
{
    this.firstLabel.setText( num );
}

Wee.wr.MultDisplayBoard.prototype.setSecondLable = function ( num )
{
    this.secondLabel.setText( num );
}

Wee.wr.MultDisplayBoard.prototype.setThirdLabel = function ( num )
{
    this.thirdLabel.setText( num );
}

Wee.wr.MultDisplayBoard.prototype.setThirdValue = function ( num )
{
    this.thirdValue.setText( num );
}

Wee.wr.MultDisplayBoard.prototype.setValues = function ( first, second )
{
    this.setFirstValue( first );
    this.setSecondValue( second );
}

Wee.wr.MultDisplayBoard.prototype.loadData = function ( data )
{
    var me = this;
    //------------------------------------------------------------------------------------------------------------------
    if( data.rawNum ){
        me.setFirstValue( data.rawNum );
    }
    if( data.clinkerNum ){
        me.setSecondValue( data.clinkerNum );
    }
}



//---------------------------------------------------------------------------------------------------------------
/*
三色指示燈
 */
Wee.wr.SignaLamp = function( paper,config )
{
    var signaLampConfig = {
        redLightAttr:{
            fill: "	#FF0000",
            "fill-opacity":.5
        },
        greenLightAttr:{
            fill: "	#00FF00",
            "fill-opacity":.5
        },
        yellowLightAttr:{
            fill: "	#FFFF00",
            "fill-opacity":.5
        },
        backAttr:{
            fill: "	#000000",
            stroke: "#000000",
            "stroke-width":1,
            cursor: "pointer",
            "fill-opacity":1
        },
        layout:"row"
    };
    Wee.wr.SignaLamp.superclass.constructor.call( this, paper, Wee.apply({}, config, signaLampConfig ) );
};
Wee.extend( Wee.wr.SignaLamp, Wee.wr.Widget );
Wee.wr.SignaLamp.prototype.drawAction = function()
{
    var me = this;
    if( me.layout == "row" ){
        var _diameter = this.width >= this.height*3 ? Math.trunc( this.height * 0.8 ) : Math.trunc( 0.8*this.width/3 );
        var _widthSpace = Math.trunc( ( this.width - 3*_diameter )/4 );
        var _heightSpace = Math.trunc( ( this.height - _diameter )/2 );
        this.greenLight = this.paper.circle(this.x + _widthSpace + _diameter/2,this.y + _heightSpace + _diameter/2,_diameter/2).attr( this.greenLightAttr ).show();
        this.yellowLight = this.paper.circle(this.x + 2*_widthSpace +_diameter + _diameter/2,this.y + _heightSpace + _diameter/2,_diameter/2).attr( this.yellowLightAttr ).show();
        this.redLight = this.paper.circle(this.x + 3*_widthSpace +2*_diameter + _diameter/2,this.y + _heightSpace + _diameter/2,_diameter/2).attr( this.redLightAttr ).show();
    }else{
        var _diameter = this.heigh >= this.width*3 ? Math.trunc( this.width * 0.7 ) : Math.trunc( 0.7*this.height/3 );
        var _widthSpace = Math.trunc( ( this.width - _diameter )/2 );
        var _heightSpace = Math.trunc( ( this.height - 3*_diameter )/4 );
        var lX = this.x + _widthSpace + _diameter/2;
        this.greenLight = this.paper.circle( lX, this.y + _heightSpace + _diameter/2, _diameter/2 ).attr( this.greenLightAttr ).show();
        this.yellowLight = this.paper.circle( lX, this.y + 2*_heightSpace + _diameter*3/2, _diameter/2 ).attr( this.yellowLightAttr ).show();
        this.redLight = this.paper.circle( lX, this.y + 3*_heightSpace + _diameter*5/2, _diameter/2 ).attr( this.redLightAttr ).show();
    }

    this.setAttr( this.backAttr );
    this.drawWidget();
}

Wee.wr.SignaLamp.prototype.setValue = function( v )
{
    if( v )v = v.toLowerCase();
    if( v == "red" || v=="down" ){
        this.redLight.attr( { "fill-opacity":1 });
        this.yellowLight.attr( { "fill-opacity":.3 });
        this.greenLight.attr( { "fill-opacity":.3 });
    }else if( v == "yellow" || v=="idle" ){
        this.redLight.attr( { "fill-opacity":.3 });
        this.yellowLight.attr( { "fill-opacity":1 });
        this.greenLight.attr( { "fill-opacity":.3 });
    }else if( v == "green" || v=="run" ){
        this.redLight.attr( { "fill-opacity":.3 });
        this.yellowLight.attr( { "fill-opacity":.3 });
        this.greenLight.attr( { "fill-opacity":1 });
    }else{
        this.redLight.attr( { "fill-opacity":.3 });
        this.yellowLight.attr( { "fill-opacity":.3 });
        this.greenLight.attr( { "fill-opacity":.3 });
    }
}

Wee.wr.SignaLamp.prototype.loadData = function( obj )
{
    var me = this;
    if( obj && typeof( obj ) == "object" )
        me.setValue( obj.status  );
    else
        me.setValue( obj );
}

//---------------------------------------------------------------------------------------------------------------
/*
有指示燈和兩個數據的裝置
 layout:"col",// row Or col
 showQty:"double",//double or alone

 */
Wee.wr.SignaEquipment = function( paper,config )
{
    var singnaEquipmentConfig = {
        layout:"col",// row Or col
        showQty:2,//1 or 2
        titleRatio:0.25, //標題所點比例
        lightRatio:0.3,//指示燈所佔比例
        labelRatio:0.45, //label所點比例
        titleAttr:{
            textAttr:{
                fill:"#FFD700",
                stroke:"#FFD700",
                "font-weight":"normal",
                "text-anchor":"middle"
            },
            backAttr:{
                fill:"#000080",
                "stroke-width":1,
                stroke: "white",
                "fill-opacity":.8
            },
            text:"A車執行狀態"
        },
        firstLabelAttr:{
            textAttr:{
                fill:"#FFD700",
                stroke:"#FFD700",
                "font-weight":"normal",
                "text-anchor":"middle"
            },
            backAttr:{
                fill:"#2E8B57",
                "stroke-width":1,
                stroke: "white",
                "fill-opacity":.8
            },
            text:"標題"
        },
        firstValueAttr:{
            textAttr:{
                fill:"#FFD700",
                stroke:"white",
                "font-weight":"normal",
                "text-anchor":"middle"
            },
            backAttr:{
                fill:"black",
                "stroke-width":1,
                stroke: "white",
                "fill-opacity":.8
            },
            text:""
        },
        secondLableAttr:{
            textAttr:{
                fill:"#FFD700",
                stroke:"#FFD700",
                "font-weight":"normal",
                "text-anchor":"middle"
            },
            backAttr:{
                fill:"#FFA500",
                "stroke-width":1,
                stroke: "white",
                "fill-opacity":.8
            },
            text:"標題"
        },
        secondValueAttr:{
            textAttr:{
                fill:"#FFD700",
                stroke:"white",
                "font-weight":"normal",
                "text-anchor":"middle"
            },
            backAttr:{
                fill:"black",
                "stroke-width":1,
                stroke: "white",
                "fill-opacity":.8
            },
            text:""
        },
        lightAttr:{
            backAttr:{
                fill: "	#000000",
                stroke: "white",
                "stroke-width":1,
                cursor: "pointer",
                "fill-opacity":1
            }
        },
        backAttr:{
            fill: "	#000000",
            stroke: "#000000",
            "stroke-width":1,
            cursor: "pointer",
            "fill-opacity":.8
        }
    };
    Wee.wr.SignaEquipment.superclass.constructor.call( this, paper, Wee.apply({}, config, singnaEquipmentConfig ) );
    this.title = new Wee.wr.TextLabel( this.paper, Wee.apply( {}, this.titleAttr, singnaEquipmentConfig.titleAttr ));
    this.firstLabel  = new Wee.wr.TextLabel( this.paper, Wee.apply( {}, this.firstLabelAttr, singnaEquipmentConfig.firstLabelAttr ));
    this.firstValue = new Wee.wr.TextLabel( this.paper,  Wee.apply( {}, this.firstValueAttr, singnaEquipmentConfig.firstValueAttr ));
    this.secondLabel = new Wee.wr.TextLabel( this.paper,  Wee.apply( {}, this.secondLableAttr, singnaEquipmentConfig.secondLableAttr ));
    this.secondValue = new Wee.wr.TextLabel( this.paper,  Wee.apply( {}, this.secondValueAttr, singnaEquipmentConfig.secondValueAttr ));
    this.Light = new Wee.wr.SignaLamp( this.paper, Wee.apply( {}, this.lightAttr, singnaEquipmentConfig.lightAttr ));
    this.setAttr( Wee.apply( {}, this.backAttr , singnaEquipmentConfig.backAttr ) );
};
Wee.extend( Wee.wr.SignaEquipment, Wee.wr.Widget );

Wee.wr.SignaEquipment.prototype.drawAction = function()
{
    this.title.attr( { x:this.x, y:this.y, width:this.width,height:this.height * this.titleRatio } );

    if( this.layout == "col" ){
       var  _groupRatio  = 1/ this.showQty;
        var _dataShowHeight = this.height * ( 1 - this.titleRatio - this.lightRatio ) ;
        if( this.showQty > 0 ){
            this.firstLabel.attr( { x:this.x, y: ( this.y + this.height* this.titleRatio ), width:( this.width * _groupRatio ), height:_dataShowHeight * this.labelRatio  } );
            this.firstValue.attr({ x:this.x, y: ( this.y + this.height* this.titleRatio + _dataShowHeight * this.labelRatio  ), width:( this.width * _groupRatio ), height:_dataShowHeight *( 1 - this.labelRatio ) } );
            if( this.showQty > 1 ){
                this.secondLabel.attr( { x:this.x + this.width * _groupRatio , y: ( this.y + this.height* this.titleRatio ), width:( this.width * _groupRatio ), height:_dataShowHeight * this.labelRatio  } );
                this.secondValue.attr({ x:this.x + this.width * _groupRatio, y: ( this.y + this.height* this.titleRatio + _dataShowHeight * this.labelRatio  ), width:( this.width * _groupRatio ), height:_dataShowHeight *( 1 - this.labelRatio ) } );
            }
        }
    }else if( this.layout == "row" ){
        var _groupRatio = 0;
        if( this.showQty > 0 ){
            _groupRatio  = ( 1 - this.titleRatio - this.lightRatio )/ this.showQty;
            this.firstLabel.attr( { x:this.x, y:this.y + this.height* this.titleRatio, width:this.width * this.labelRatio, height:this.height * _groupRatio } );
            this.firstValue.attr({ x:this.x + this.width * this.labelRatio, y:this.y + this.height*this.titleRatio, width:this.width * (1 - this.labelRatio), height:this.height * _groupRatio } );
            if( this.showQty > 1 ){
                this.secondLabel.attr( { x:this.x, y:( this.y + this.height* this.titleRatio + this.height * _groupRatio), width:this.width * this.labelRatio, height:this.height * _groupRatio } );
                this.secondValue.attr({ x:this.x + this.width * this.labelRatio, y:( this.y + this.height*this.titleRatio + this.height * _groupRatio ), width:this.width * (1 - this.labelRatio), height:this.height * _groupRatio } );
            }
        }
    }else{
        console.log( "SignaEquipment中ShowQty為" + this.layout + "，沒有此型別" );
        return;
    }

    this.Light.attr( { x:this.x, y:this.y + this.height*0.7, width:this.width, height:this.height * this.lightRatio } );

    this.title.drawAction();
    if( this.showQty > 0 ){
        this.firstLabel.drawAction();
        this.firstValue.drawAction();
        if( this.showQty > 1 ){
            this.secondLabel.drawAction();
            this.secondValue.drawAction();
        }
    }
    this.Light.drawAction();

    this.drawWidget();
}

Wee.wr.SignaEquipment.prototype.loadData = function( data )
{
    var me = this;

    if( data.status ){
        var _status = data.status.toLowerCase();
        if( _status  == "run" )
        me.Light.setValue( "green" );
        else if( _status  == "idle" )
            me.Light.setValue( "yellow" );
        else if( _status  == "stop" )
            me.Light.setValue( "red" );
        else{
            me.Light.setValue( "break" );
            console.log( "SignaEquipment中LoadData" + data + "，狀態資訊不正確！" );
        }
    }else{
        me.Light.setValue( "break" );
    }

    if( data.statusOnly )return;
    //------------------------------------------------------------------------------------------------------------------
    if( data.source || data.okQty  || data.okQty == 0 ){
        this.firstValue.setText( data.source  || data.okQty  );
    }else{
        this.firstValue.setText( "" );
    }

    if( data.target  || data.ncQty  || data.ncQty == 0 ){
        this.secondValue.setText( data.target  || data.ncQty  );
    }else{
        this.secondValue.setText( "" );
    }
}
//---------------------------------------------------------------------------------------------------------------
/*
g一個標題，一具數據顯示區
 titleHeigh:0.4,//標題所佔比例
 layout:"up",//down OR up
 borderWidth:3, //邊框
 borderColor:"#00FFFF", //邊框顏色
 backColor:"black", //y底色
 */
Wee.wr.HeadingArea = function( paper,config )
{
    var headingAreaConfig = {
        layout:"up",//down OR up
        titleHeigh:0.4,
        title:"",
        borderWidth:0,
        borderColor:"",
        backColor:"",
        backR:0,
        titleAttr:{
            textAttr:{
                fill:"#FFD700",
                stroke:"#FFD700",
                "font-weight":"normal",
                "text-anchor":"middle"
            },
            backAttr:{
                fill:"#000080",
                "stroke-width":0,
                stroke: "white",
                "fill-opacity":1
            },
            text:"顯示標題"
        },
        dataShowAttr:{
            textRatio:0.3,
            textAttr:{
                fill:"#FFD700",
                stroke:"#FFD700",
                "font-weight":"normal",
                "text-anchor":"middle"
            },
            backAttr:{
                fill:"black",
                "stroke-width":0,
                stroke: "#7FFFAA",
                "fill-opacity":.8
            },
            text:"數據呈現"
        },
        backAttr:{
            fill: "#C0C0C0",
            stroke: "",
            "stroke-width":0,
            cursor: "pointer",
            "fill-opacity":1
        }
    };
    Wee.wr.HeadingArea.superclass.constructor.call( this, paper, Wee.apply({}, config, headingAreaConfig ) );
    this.initAttr();
    this.title = new Wee.wr.TextLabel( this.paper, Wee.apply( {}, this.titleAttr, headingAreaConfig.titleAttr ));
    this.dataShow = new Wee.wr.TextLabel( this.paper, Wee.apply( {}, this.dataShowAttr, headingAreaConfig.dataShowAttr ));
    this.setAttr( Wee.apply( {}, this.backAttr, headingAreaConfig.backAttr ) );

}
Wee.extend( Wee.wr.HeadingArea, Wee.wr.Widget );

Wee.wr.HeadingArea.prototype.initAttr = function()
{
    if( this.backColor ){
        this.backAttr["fill"] = this.backColor;
        this.backAttr["fill-opacity"] = 1;
    }
    if( this.borderWidth && this.borderWidth >0 ){
        this.backAttr["stroke-width"] = this.borderWidth;
        this.backAttr["stroke"] = this.borderColor?this.borderColor:this.backAttr["fill"] ;
    }
    if( this.backR && this.backR != 0 ){
        this.backAttr["r"] = this.backR;
    }
    if( this.title && this.title != "" ){
        this.titleAttr["text"] = this.title;
    }
    if( this.text && this.text != "" ){
        this.dataShowAttr["text"] = this.text;
    }
}

Wee.wr.HeadingArea.prototype.drawAction = function()
{
    var _x = this.x + this.borderWidth,
        _y = this.y + this.borderWidth,
        _width = this.width - 2*this.borderWidth,
        _height = this.height - 2*this.borderWidth;

    if( this.layout == "up" ){
        this.title.attr( { x:_x + 2, y:_y + 2, width:_width - 4, height:_height * this.titleHeigh - 2 } );
        this.dataShow.attr( {  x:_x + 2, y:_y+_height * this.titleHeigh + 2, width:_width - 4 , height:_height * (1 -this.titleHeigh ) - 2 } );
    }else if( this.layout == "down"  ){
        this.title.attr( { x:_x + 2, y:_y + _height * ( 1 - this.titleHeigh ) + 2, width:_width - 4,height:_height * this.titleHeigh - 2 } );
        this.dataShow.attr( {  x:_x +2, y:_y + 2, width:_width - 4, height:_height * (1 -this.titleHeigh ) - 2 } );
    }else{
        console.log( "HeadingArea中Layout為" + this.layout + "，沒有此型別" );
        return;
    }

    this.title.drawAction();
    this.dataShow.drawAction();

    this.drawWidget();
}

Wee.wr.HeadingArea.prototype.changeDate = function( date )
{
    this.dataShow.setText( date );
}

Wee.wr.HeadingArea.prototype.loadData = function( data )
{
    this.dataShow.setText( data.text );
}
//---------------------------------------------------------------------------------------------------------------
/*
CNC接駁站
 */
Wee.wr.CNCTranship = function( paper,config )
{
    var cncTranshipConfig = {
        layout:'top',//top or bottom
        titleAttr:{
            textRatio:0.6,
            textAttr:{
                fill:"black",
                stroke:"black"
            },
            backAttr:{
                fill:"#A9A9A9",
                "stroke-width":0,
                stroke: "#7FFFAA",
                "fill-opacity":1
            },
            text:"01#接駁站"
        },
        firstAgvAttr:{
            textRatio:0.4,
            textAttr:{
                fill:"black",
                stroke:"black"
            },
            backAttr:{
                fill:"#2E8B57",
                "stroke-width":2,
                stroke: "white"
            },
            text:"A01"
        },
        secondAgvAttr:{
            textRatio:0.4,
            textAttr:{
                fill:"black",
                stroke:"black"
            },
            backAttr:{
                fill:"#2E8B57",
                "stroke-width":2,
                stroke: "white"
            },
            text:"A02"
        },
        rowTitleAttr:{
            textRatio:0.6,
            textAttr:{
                fill:"white",
                stroke:"white"
            },
            backAttr:{
                fill:"#2E8B57",
                "stroke-width":0,
                stroke: "#7FFFAA",
                "fill-opacity":.8
            },
            text:"生料"
        },
        rowNumAttr:{
            textRatio:0.6,
            textAttr:{
                fill:"white",
                stroke:"white"
            },
            backAttr:{
                fill:"black",
                "stroke-width":0,
                stroke: "black",
                "fill-opacity":.8
            },
            text:"0"
        },
        clinkerTitleAttr:{
            textRatio:0.6,
            textAttr:{
                fill:"white",
                stroke:"white"
            },
            backAttr:{
                fill:"#FFA500",
                "stroke-width":0,
                stroke: "#7FFFAA",
                "fill-opacity":.8
            },
            text:"熟料"
        },
        clinkerNumAttr:{
            textRatio:0.6,
            textAttr:{
                fill:"white",
                stroke:"white"
            },
            backAttr:{
                fill:"black",
                "stroke-width":0,
                stroke: "black",
                "fill-opacity":.8
            },
            text:"0"
        },
        capAttr:{
            textRatio:0.5,
            textAttr:{
                fill:"white",
                stroke:"white"
            },
            backAttr:{
                fill:"black",
                "stroke-width":0,
                stroke: "black",
                "fill-opacity":.8
            },
            text:"0"
        },
        backAttr:{
            fill: "#A9A9A9",
            stroke: "#A9A9A9",
            "stroke-width":1,
            cursor: "pointer",
            "fill-opacity":1
        }
    };
    Wee.wr.CNCTranship.superclass.constructor.call( this, paper, Wee.apply({}, config, cncTranshipConfig ) );

    this.showTitle = new Wee.wr.TextLabel( this.paper, Wee.apply( {}, this.titleAttr, cncTranshipConfig.titleAttr ));

    this.firstAgv = new Wee.wr.TextLabel( this.paper, Wee.apply( {}, this.firstAgvAttr, cncTranshipConfig.firstAgvAttr ));
    this.secondAgv = new Wee.wr.TextLabel( this.paper,  Wee.apply( {}, this.secondAgvAttr, cncTranshipConfig.secondAgvAttr ));
    this.firstCapNum = new Wee.wr.TextLabel( this.paper,  Wee.apply( {}, this.capAttr, cncTranshipConfig.capAttr ));
    this.secondCapNum = new Wee.wr.TextLabel( this.paper,  Wee.apply( {}, this.capAttr, cncTranshipConfig.capAttr ));

    this.rowTitle = new Wee.wr.TextLabel( this.paper, Wee.apply( {}, this.rowTitleAttr, cncTranshipConfig.rowTitleAttr ));
    this.rowNum = new Wee.wr.TextLabel( this.paper,  Wee.apply( {}, this.rowNumAttr, cncTranshipConfig.rowNumAttr ));
    this.clinkerTitle = new Wee.wr.TextLabel( this.paper,  Wee.apply( {}, this.clinkerTitleAttr, cncTranshipConfig.clinkerTitleAttr ));
    this.clinkerNum = new Wee.wr.TextLabel( this.paper,  Wee.apply( {}, this.clinkerNumAttr, cncTranshipConfig.clinkerNumAttr ));
    this.setAttr( Wee.apply( {}, this.backAttr, cncTranshipConfig.backAttr )  );
}
Wee.extend( Wee.wr.CNCTranship, Wee.wr.Widget );

Wee.wr.CNCTranship.prototype.drawAction = function()
{
    if( this.layout == "bottom" ){
        this.showTitle.attr({ x: ( this.x + 2 ), y:this.y , width:( this.width - 4 ), height:( this.height*0.15 - 2 ) });

        this.rowTitle.attr({ x: ( this.x + 2 ), y:( this.y + this.height*0.15 ) , width:( this.width*0.5 - 3) , height:( this.height*0.15-2 ) });
        this.clinkerTitle.attr({ x: ( this.x + this.width*0.5 + 2 ), y:this.y + this.height*0.15 , width:( this.width*0.5 - 3 ) , height:(this.height*0.15 - 2 ) });

        this.rowNum.attr({ x: ( this.x + 2 ), y:( this.y + this.height*0.3 ), width:( this.width*0.5 - 3 ) , height:( this.height*0.2 - 2 ) });
        this.clinkerNum.attr({ x:( this.x + this.width*0.5 + 2 ), y:( this.y + this.height*0.3 ) , width:( this.width*0.5 - 3 ) , height:( this.height*0.2 - 2 ) });

        this.firstAgv.attr({ x:(this.x + 2 ), y:(this.y + this.height*0.5) , width:( this.width*0.5 - 3 ) , height:( this.height*0.3 - 2 ) });
        this.secondAgv.attr({ x:( this.x + this.width*0.5 + 2 ), y:(this.y + this.height*0.5 ) , width:( this.width*0.5 - 3 ) , height:(this.height*0.3 - 2) });

        this.firstCapNum.attr({ x:(this.x + 2 ), y:(this.y + this.height*0.8) , width:( this.width*0.5 - 3 ) , height:( this.height*0.2 - 2 ) });
        this.secondCapNum.attr({ x:( this.x + this.width*0.5 + 2 ), y:(this.y + this.height*0.8 ) , width:( this.width*0.5 - 3 ) , height:(this.height*0.2 - 2) });
    }else if( this.layout == "top" ){
        this.rowTitle.attr({ x:( this.x + 2 ), y:this.y , width:( this.width*0.5 - 3 ), height:( this.height*0.2-2 ) });
        this.clinkerTitle.attr({ x:( this.x + this.width*0.5 + 2 ), y:this.y, width:( this.width*0.5 - 3 ) , height:( this.height*0.2 - 2 ) });

        this.rowNum.attr({ x:( this.x + 2 ), y:( this.y + this.height*0.2 ) , width:( this.width*0.5 - 3 ) , height:( this.height*0.3 - 2 ) });
        this.clinkerNum.attr({ x:( this.x + this.width*0.5 + 2 ), y:( this.y + this.height*0.2 ) , width:( this.width*0.5 - 3 ) , height:( this.height*0.3 - 2 ) });

        this.firstAgv.attr({ x:(this.x + 2 ), y:(this.y + this.height*0.5) , width:( this.width*0.5 - 3 ) , height:( this.height*0.3 - 2 ) });
        this.secondAgv.attr({ x:( this.x + this.width*0.5 + 2 ), y:(this.y + this.height*0.5 ) , width:( this.width*0.5 - 3 ) , height:(this.height*0.3 - 2) });

        this.showTitle.attr({ x:( this.x + 2 ), y:( this.y + this.height*0.8 ), width:( this.width - 4 ), height:( this.height*0.2 - 2 ) });
    }else{
        console.log( "CNCTranship中Layout為" + this.layout + "，沒有此型別" );
        return;
    }

    this.showTitle.drawAction();
    this.firstAgv.drawAction();
    this.firstCapNum.drawAction();
    this.secondCapNum.drawAction();
    this.secondAgv.drawAction();
    this.rowTitle.drawAction();
    this.rowNum.drawAction();
    this.clinkerTitle.drawAction();
    this.clinkerNum.drawAction();

    this.drawWidget();
};

Wee.wr.CNCTranship.prototype.setAgvStatus = function()
{

}
Wee.wr.CNCTranship.prototype.changeNum = function( row, clinke )
{

}

Wee.wr.CNCTranship.prototype.changeRowNum = function( num )
{

}

Wee.wr.CNCTranship.prototype.changeClinkeNum = function( num )
{

}

Wee.wr.CNCTranship.prototype.loadData = function( data )
{
    var me = this;
    //------------------------------------------------------------------------------------------------------------------
    if( data.type == "port" ){
        if( data.rawNum ) this.rowNum.setText( data.rawNum );
        if( data.clinkerNum ) this.clinkerNum.setText( data.clinkerNum );
        if( data.status ) this.changeShipStatus( data.status );
    }
    //------------------------------------------------------------------------------------------------------------------
    if( data.type == "cnc" ){
        var _cncObj, _cncQty;
        if( data.layout == "1" ){
            _cncObj = this.firstAgv;
            _cncQty = this.firstCapNum;
        }else if( data.layout == "2"){
            _cncObj = this.secondAgv;
            _cncQty = this.secondCapNum;
        }
        if( data.status ) me.changeStatus( _cncObj, data.status );
        if( data.statusOnly )return;
        _cncQty.setText( data.qty );
    }
}

Wee.wr.CNCTranship.prototype.changeStatus = function( obj, status )
{
    var me = this,_color;
    if( status ) status = status.toLowerCase();
    if(status == "run" ){
        _color = "green";
    }else if(status == "idle"){
        _color = "yellow";
    }else if( status == "stop"){
        _color = "red";
    }else{
        _color = "#A9A9A9";
    }
    obj.setBackAttr({ fill: _color,"fill-opacity":1});
}



Wee.wr.CNCTranship.prototype.changeShipStatus = function( status )
{
    var me = this,_color;
    if( status ) status = status.toLowerCase();
    if(status == "run" ){
        _color = "green";
    }else if(status == "idle"){
        _color = "yellow";
    }else if( status == "stop"){
        _color = "red";
    }else{
        _color = "#A9A9A9";
    }
    this.showTitle.setBackAttr({
        fill: _color,
        "fill-opacity":1
    });
}


/*
圖片顯示
 */
Wee.wr.ImageIcon = function( paper,config )
{
    var imageIcon = {
        startX:0,
        startY:0,
        x:0,
        y:0,
        width:100,
        height:100,
        srcPath:"../images/machine/",
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

/**
 * 主線AGV軌道
 * @param paper
 * @param config
 * @constructor
 */
Wee.wr.MainAGVPath = function( paper,config )
{
    var agvPathConfig = {
        borderWidth:0,
        agvWidth:30,
        agvHeight:25,
        portWidth:15,
        portHeight:20,
        parkWidth:60,
        minHeigh:160,
        colWidth:50,
        pathHeight:60,
        rRRate:0.5,
        rateMS:10000,
        turnMS:1500,
        points:{
            "51":"RC1",
            "76":"RC1",
            "52":"RC2",
            "77":"RC2",

            "1":"RR1",
            "3":"RR1",
            "2":"RR2",
            "4":"RR2",

            "5":"A",
            "10":"A",
            "53":"A",

            "6":"B",
            "11":"B",
            "54":"B",

            "7":"C",
            "12":"C",
            "55":"C",

            "8":"D",
            "13":"D",
            "56":"D",

            "9":"E",
            "14":"E",
            "57":"E",

            "64":"F",
            "58":"F",
            "70":"F",

            "65":"G",
            "59":"G",
            "71":"G",

            "66":"H",
            "60":"H",
            "72":"H",

            "67":"I",
            "61":"I",
            "73":"I",

            "68":"J",
            "62":"J",
            "74":"J",

            "69":"J",
            "63":"J",
            "75":"J",
            //
            "21":"P1",
            "22":"P2",
            "23":"P3",
            "24":"P4",
            "25":"P5",
            "26":"P6"
        },
        stations:{
            //A自動接駁站，M手工接駁站，P停車點，RR滾筒生料接駁站，RC滾筒熟料接駁站
            //rate的比率是按照14等分切分時的
            "RC1":{
                "text":"熟",
                "type":"RC",
                "rate":1.25,
                "rTRate":4
            },
            "RC2":{
                "text":"熟",
                "type":"RC",
                "rate":1.75,
                "rTRate":4
            },
            "RR1":{
                "text":"生",
                "type":"RR",
                "rate":2.25,
                "rTRate":4
            },
            "RR2":{
                "text":"生",
                "type":"RR",
                "rate":2.75,
                "rTRate":4
            },
            "A":{
                "text":"A",
                "type":"A",
                "rate":3.5,
                "rTRate":4
            },
            "B":{
                "text":"B",
                "type":"M",
                "rate":4.5,
                "rTRate":5
            },
            "C":{
                "text":"C",
                "type":"M",
                "rate":5.5,
                "rTRate":6
            },
            "D":{
                "text":"D",
                "type":"M",
                "rate":6.5,
                "rTRate":7
            },
            "E":{
                "text":"E",
                "type":"M",
                "rate":7.5,
                "rTRate":8
            },
            "F":{
                "text":"F",
                "type":"M",
                "rate":8.5,
                "rTRate":9
            },
            "G":{
                "text":"G",
                "type":"M",
                "rate":9.5,
                "rTRate":10
            },
            "H":{
                "text":"H",
                "type":"M",
                "rate":10.5,
                "rTRate":11
            },
            "I":{
                "text":"I",
                "type":"M",
                "rate":11.5,
                "rTRate":12
            },
            "J":{
                "text":"J",
                "type":"M",
                "rate":12.5,
                "rTRate":13
            },
            "K":{
                "text":"K",
                "type":"M",
                "rate":13.5,
                "rTRate":14//轉彎點
            },
            //充電站
            "P1":{
                "text":"P1",
                "type":"P",
                "rate":0,
                "rateH":1
            },
            "P2":{
                "text":"P2",
                "type":"P",
                "rate":0,
                "rateH":2
            },
            "P3":{
                "text":"P3",
                "type":"P",
                "rate":0,
                "rateH":3
            },
            "P4":{
                "text":"P4",
                "type":"P",
                "rate":0,
                "rateH":4
            },
            "P5":{
                "text":"P5",
                "type":"P",
                "rate":0,
                "rateH":5
            },
            "P6":{
                "text":"P6",
                "type":"P",
                "rate":0,
                "rateH":6
            }
        },
        agvs:{
            "KAGV01":{
                "text":"1號",
                "dfltPos":"22"
            },
            "KAGV02":{
                "text":"2號",
                "dfltPos":"21"
            },
            "KAGV03":{
                "text":"3號",
                "dfltPos":"26"
            },
            "KAGV04":{
                "text":"4號",
                "dfltPos":"23"
            },
            "KAGV05":{
                "text":"5號",
                "dfltPos":"24"
            },
            "KAGV06":{
                "text":"6號",
                "dfltPos":"25"
            }
        },
        backPathAttr:{
            "stroke":"#FFFFFF",
            "stroke-width":10,
            "stroke-opacity":.5
        },
        pathAttr:{
            "stroke":"#CCCCCC",
            "stroke-dasharray":"-",
            "stroke-width":10
        },
        portClinkerAttr:{
            "stroke":"#FFA500",
            "fill":"#FFA500",
            "stroke-width":1,
            "stroke-opacity":.5
        },
        portRowAttr:{
            "stroke":"#40E0D0",
            "fill":"#2E8B57",
            "stroke-width":1,
            "stroke-opacity":.5
        },
        portTextAttr:{
            "font-size":15,
            "text-anchor":"middle",
            "stroke":"black",
            "stroke-opacity":.8
        },
        parkAttr:{
            "stroke":"#E0E0E0",
            "fill":"#E0E0E0",
            "stroke-width":3,
            "stroke-opacity":.8
        },
        parkTextAttr:{
            "font-size":12,
            "text-anchor":"end",
            "fill":"#33CC00",
            "stroke":"#33CC00",
            "stroke-opacity":.8
        },
        agvAttr:{
            "stroke":"#FFD700",
            "fill":"#FFD700",
            "stroke-width":2,
            "stroke-opacity":.8
        },
        agvTextAttr:{
            "font-size":8,
            "text-anchor":"middle",
            "stroke":"#0000E3",
            "stroke-opacity":.4
        }
    };

    Wee.wr.MainAGVPath.superclass.constructor.call( this, paper, Wee.apply({}, config, agvPathConfig ) );
};

Wee.extend( Wee.wr.MainAGVPath, Wee.wr.Widget );

Wee.wr.MainAGVPath.prototype.drawAction = function()
{
    var me = this, _x = this.x + this.borderWidth,
        _y = this.y + this.borderWidth,
        _width = this.width - 2*this.borderWidth,
        _height = this.height - 2*this.borderWidth;
    me.colWidth = parseInt(_width/14 );
    if( _height > 160 ) me.minHeigh = _height;

    me.drawMainPaths( _x, _y, _width, _height );
    me.drawMainPorts( _x, _y, _width,  _height );
    me.drawAGVs();
}

Wee.wr.MainAGVPath.prototype.drawMainPaths = function( x,y,width, height)
{
    var me = this;
    _y =  y + ( height - me.pathHeight)/2;
    //上規道
    var _tPath = "M" + ( x + me.colWidth ) + " "+ _y +"L" + parseInt( x + width ) + " "+ _y;
    me.paper.path( _tPath ).attr( me.backPathAttr ).show();
    me.paper.path( _tPath ).attr( me.pathAttr ).show();
    //下規道
    var _bY = parseInt( _y + me.pathHeight );
    var _bPath = "M" + ( x + me.colWidth ) +  " "+ _bY +"L" + parseInt( x + width ) + " "+ _bY;
    me.paper.path( _bPath ).attr( me.backPathAttr ).show();
    me.paper.path( _bPath ).attr( me.pathAttr ).show();
    //左邊彎道
    var _lPath = "M" + ( x + me.colWidth ) +  " "+ _y +"A50 50,0,0,0,"+ ( x + me.colWidth ) +  " " + _bY;
    me.paper.path( _lPath ).attr( me.backPathAttr).toBack().show();
    //右邊彎道
    var maxX = x + width;
    for( var i = 0; i < 11; i++ ){
        var _colX = maxX - i * me.colWidth;
        var _rPath = "M" + _colX + " "+ _y +"A50 50,0,0,1," + _colX + " " + _bY;
        me.paper.path( _rPath ).attr( me.backPathAttr ).toBack().show();
    }
}

Wee.wr.MainAGVPath.prototype.drawMainPorts = function( x, y, width, height )
{
    var me = this;
    var portY = y + ( height - me.pathHeight)/2 - me.portHeight - me.agvHeight - 5;
    // 停車區域
    me.paper.path( "M" + x  + "," + ( this.y + height ) + "V" + y + "H" + (this.x + me.parkWidth ) ).attr( {
        "stroke":"white",
        "stroke-width":3
    }).show();
    var _parkHeight = parseInt( height/6 ) ;
    //
    for( var key in me.stations ){
        var _obj = me.stations[ key ];
        if( _obj.type != "P" ){
            var _x = x + _obj.rate * me.colWidth;

            if( _obj.type == "A" || _obj.type == "M") {
                me.paper.rect( _x , portY, me.portWidth, me.portHeight, 3 ).attr( me.portClinkerAttr ).show();
                me.paper.rect( _x - me.portWidth , portY, me.portWidth, me.portHeight, 3 ).attr( me.portRowAttr ).show();
                me.paper.text( x + _obj.rate * me.colWidth, portY + me.portHeight/2, _obj.text ).attr( me.portTextAttr).show();
            }else {
                var _attr = {};
                if( _obj.type == "RR" )
                    _attr = me.portRowAttr;
                else if( _obj.type == "RC" )
                    _attr = me.portClinkerAttr;
                me.paper.rect( _x - me.portWidth /2, portY, me.portWidth, me.portHeight).attr( _attr ).show();
                me.paper.text( x + _obj.rate * me.colWidth, portY + me.portHeight/2, _obj.text ).attr( me.portTextAttr).show();
            }

            _obj.x = x + _obj.rate * me.colWidth - me.agvWidth/2;
            _obj.y = y + ( height - me.pathHeight ) / 2 - me.agvHeight - 3;
        }else{
            var _y = y + _obj.rateH*_parkHeight;
            //me.paper.rect( x , _y, me.parkWidth, me.parkHeight ).attr( me.parkAttr ).show();
            me.paper.path( "M" + x  + "," + _y  + "H" + (this.x + me.parkWidth ) ).attr( {
                "stroke":"white",
                "stroke-width":2}).show();
            me.paper.text( x + me.parkWidth, _y - _parkHeight/2,  _obj.text  ).attr( me.parkTextAttr ).show();
            _obj.y = _y -  me.agvHeight - 2;
            _obj.x = x + 2;
        }

    }//end for
}

Wee.wr.MainAGVPath.prototype.drawAGVs = function()
{
    var me = this;
    for( var key in me.agvs ){
        var agvAttr = me.agvs[ key ];
        var _station = me.stations[ me.points[agvAttr.dfltPos] ] ;
        var agv = new Wee.wr.AGV( me.paper, {
            x: _station.x,
            y: _station.y,
            width: me.agvWidth,
            height:me.agvHeight,
            text:agvAttr.text
        });
        agv.drawAction();
        //
        me.agvs[ key ].ins = agv;
    }
}

Wee.wr.MainAGVPath.prototype.getStation = function( point )
{
    var me = this;
    if( me.points[point] ){
        if( me.stations[me.points[point]] )
            return me.stations[me.points[point]]
        else{
            console.log( "在MainAGVPath中找不到對應的接駁站："+ point );
            return null;
        }
    }
    console.log( "在MainAGVPath中找不到對應的Point："+ point );
    return null;
}

Wee.wr.MainAGVPath.prototype.getPath = function( startP, endP )
{

}

Wee.wr.MainAGVPath.prototype.setAGVPosition = function( port, agvId )
{
    var me = this;
    var agv = me.agvs[ agvId].ins;
    if( !agv ){
        return;
    }
    var _station = me.getStation( port );
    if( _station )
    agv.setPosition( _station.x, _station.y );
}

Wee.wr.MainAGVPath.prototype.agvMove = function( agvKey, startP, endP )
{
    var me = this,strPath;
    var _start = me.getStation( startP );
    var _end = me.getStation( endP );
    if( !_start )_start = me.getStation( "21" );
    if( !_end )_end = me.getStation( "21" );
    var _pathRate = _end.rate - _start.rate;
    var _agv = me.agvs[ agvKey ].ins;
    _agv.setPosition( _start.x, _start.y );
    var movePath = {};
    var _totalMs;
    if( _pathRate >= 0 ){
        if( _start.rate > 0 ){
            //_agv.changePosition( {x:_end.x,y:_end.y }, _pathRate * me.rateMS );
            _totalMs = _pathRate * me.rateMS;
            movePath["100%"] = {x:_end.x,y:_end.y };
        }else{
            var _parkPath = {
                x : this.x + me.colWidth,
                y : _end.y
            }
            var _pathMs = me.turnMS;
            var _1Ms = me.rateMS * ( _end.rate - 1 );
            var _1Path = {
                x:_end.x,y:_end.y
            };
            _totalMs =_pathMs + _1Ms;
            movePath[ parseInt(_pathMs * 100 / _totalMs )  + "%" ] = _parkPath;
            movePath[ parseInt( (_1Ms + _pathMs ) * 100 / _totalMs )  + "%" ] = _1Path;
        }

    }else{
        var _1Path = {
            x : _start.x + ( _start.rTRate - _start.rate ) * me.colWidth + me.agvWidth/2,
            y : _start.y
        };
        var _1Ms = ( _start.rTRate - _start.rate ) * me.rateMS;
        var _2Path = {
            x : _start.x + ( _start.rTRate - _start.rate ) * me.colWidth + me.agvWidth/2,
            y : _start.y + me.pathHeight
        };
        var _2Ms = me.turnMS;
        var _3Path = {
            x : this.x +  me.colWidth - me.agvWidth/2,
            y : _start.y + me.pathHeight
        };
        var _3Ms = ( _start.rTRate - 1 ) * me.rateMS;
        if( _end.rate > 0 ){
            var _4Path = {
                x : this.x +  me.colWidth - me.agvWidth/2,
                y : _start.y
            };
            var _4Ms = me.turnMS;
            var _5Path = {
                x : _end.x,
                y : _end.y
            };
            var _5Ms = ( _end.rate - 1 ) * me.rateMS;
            //--------------------------------------------------------------------------------------------------------------
            _totalMs = _1Ms + _2Ms + _3Ms + _4Ms + _5Ms;
            movePath[ parseInt(_1Ms * 100 / _totalMs )  + "%" ] = _1Path;
            movePath[ parseInt( (_2Ms + _1Ms ) * 100 / _totalMs )  + "%" ] = _2Path;
            movePath[ parseInt( (_3Ms + _2Ms + _1Ms ) * 100 / _totalMs )  + "%" ] = _3Path;
            movePath[ parseInt( (_4Ms + _3Ms + _2Ms + _1Ms ) * 100 / _totalMs )  + "%" ] = _4Path;
            movePath[ parseInt( (_5Ms + _4Ms + _3Ms + _2Ms + _1Ms ) * 100 / _totalMs )  + "%" ] = _5Path;
        }else{
            var _parkPath = {
                x : _end.x,
                y : _end.y
            }
            var _pathMs = me.turnMS;
            _totalMs = _1Ms + _2Ms + _3Ms + _pathMs;
            movePath[ parseInt(_1Ms * 100 / _totalMs )  + "%" ] = _1Path;
            movePath[ parseInt( (_2Ms + _1Ms ) * 100 / _totalMs )  + "%" ] = _2Path;
            movePath[ parseInt( (_3Ms + _2Ms + _1Ms ) * 100 / _totalMs )  + "%" ] = _3Path;
            movePath[ parseInt( (_pathMs + _3Ms + _2Ms + _1Ms ) * 100 / _totalMs )  + "%" ] = _parkPath;
        }
    }
    //--------------------------------------------------------------------------------------------------------------
    _agv.animatePath( movePath, _totalMs );

}

Wee.wr.MainAGVPath.prototype.loadData = function( data )
{
    var me = this;
    if( !data || data.length == 0 ){
        console.log( "MainAGVPath -- load data is null:" +  data );
        return;
    }
    for( var i = 0; i < data.length; i++ ){
        var _obj = data[i];
        if( !_obj.id ){
            console.log( "MainAGVPath -- Data does not contain id:" +  data );
            continue;
        }
        var _agv = me.agvs[ _obj["id"] ];
        if( !_agv ){
            console.log( "MainAGVPath -- AGV is not exist!" +  _obj );
            continue;
        }

        if( _obj.source != "" && _obj.target != ""  ){
            me.agvMove( _obj["id"], _obj["source"], _obj["target"]);
        }else{
            me.setAGVPosition( _obj["source"], _obj["id"] );
        }
        //_agv.setStatus( _obj.status );
    }

}

/**
 * AGV小車
 * @param paper
 * @param config
 * @constructor
 */
Wee.wr.AGV = function( paper,config )
{
    var defaultAGVConfig = {
        textColor:"",
        textAnchor:"",
        borderColor:"white",
        borderWidth:1,
        backColor:"yellow",
        textAttr:{
            backColor:"yellow",
            textColor:"black",
            borderColor:"black",
            backR:5,
            textRatio:.5
        },
        wheelR:3,
        wheelAttr:{
            "stroke":"black",
            "fill":"white",
            "stroke-width":2,
            "fill-opacity":.6
        }
    };

    Wee.wr.AGV.superclass.constructor.call( this, paper, Wee.apply({}, config, defaultAGVConfig ) );
    this.initAttr();
    //this.showText = this.paper.text(0,0,this.text ).attr( Wee.apply({}, this.textAttr, defaultAGVConfig.textAttr) ).hide();
    this.showText = new Wee.wr.TextLabel( this.paper, Wee.apply({}, this.textAttr, defaultAGVConfig.textAttr ) );
    this.leftWheel = this.paper.circle(0, 0, this.wheelR ).attr( Wee.apply({}, this.wheelAttr, defaultAGVConfig.wheelAttr )).hide();
    this.rightWheel = this.paper.circle(0, 0, this.wheelR ).attr( Wee.apply({}, this.wheelAttr, defaultAGVConfig.wheelAttr )).hide();
    this.motionPath = this.paper.path( "M0 0L01").hide();

    this.setAttr( Wee.apply({}, this.borderDef, defaultAGVConfig.borderDef));
};
Wee.extend( Wee.wr.AGV, Wee.wr.Widget );

Wee.wr.AGV.prototype.initAttr = function(){
    var me = this;
    if( me.textColor ){
        me.textAttr["textColor"] = me.textColor;
    }
    if( me.backColor ){
        me.textAttr["backColor"] = me.backColor;
    }
    if( me.text ){
        me.textAttr["text"] = me.text;
    }
}

Wee.wr.AGV.prototype.drawAction = function()
{
    var me = this;
    /*var titleWordsAttr =
    {
        x:(this.x + this.width/2 ),
        y:Math.floor(this.y +this.height/2)
    };
    this.showText.attr( titleWordsAttr ).show();
    */
    //this.showText.attr({ x: this.x, y: Math.floor( me.y + me.height/2 ), width: me.width, height: ( me.height - 2* me.wheelR ) });
    this.showText.attr({ x:this.x, y:this.y, width:this.width, height:( this.height - 2 * this.wheelR )} );
    me.showText.drawAction();

    this.leftWheel.attr({cx: ( this.x + 2*this.wheelR ), cy: ( this.y + this.height - this.wheelR )}).show();
    this.rightWheel.attr({cx:( this.x + this.width -  2*this.wheelR ), cy: ( this.y + this.height - this.wheelR )}).show();
    this.drawWidget();

};

Wee.wr.AGV.prototype.setBackAttr = function( attr )
{
    this.setAttr( attr );
}

Wee.wr.AGV.prototype.setMotionPath = function( path )
{
    this.motionPath.attr({ path:path });
}


Wee.wr.AGV.prototype.setPosition = function( x, y  )
{
    var me = this;
    me.x = x;
    me.y = y;
    me.drawAction();
}

Wee.wr.AGV.prototype.setStatus = function( status )
{
    var me = this;

}

Wee.wr.AGV.prototype.loadData= function( data )
{
    var me = this;

}

Wee.wr.AGV.prototype.changePosition = function( param, ms  )
{
    var me = this;
    me.showText.changePosition( param, ms );
    me.leftWheel.animate({cx: ( param.x + 2*this.wheelR ), cy: ( param.y + this.height - this.wheelR )}, ms );
    me.rightWheel.animate({cx:( param.x + this.width -  2*this.wheelR ), cy: ( param.y + this.height - this.wheelR )}, ms );
    me.changeWidgetPosition( param, ms );
}

Wee.wr.AGV.prototype.animatePath = function( movePaths, totalMS )
{
    var me = this;
    me.showText.animatePath( movePaths, totalMS );
    //------------------------------------------------------------------------------------------------------------------
    var _leftWheelPath = {},_rightWheelPath = {};
    for( var ms in movePaths ){
        var _path = movePaths[ms];
        _leftWheelPath[ms] = {
            cx: ( _path.x + 2*this.wheelR ), cy: ( _path.y + this.height - this.wheelR )
        };
        _rightWheelPath[ms] = {
            cx:( _path.x + this.width -  2*this.wheelR ), cy: ( _path.y + this.height - this.wheelR )
        };
    }
    me.leftWheel.animate(_leftWheelPath, totalMS );
    me.rightWheel.animate(_rightWheelPath, totalMS );
    //------------------------------------------------------------------------------------------------------------------
    me.widgetAnimatePath( movePaths, totalMS );
}



/**
 * 主動經AGV路徑
 * @param paper
 * @param config
 * @constructor
 */

Wee.wr.SubAGVPath = function( paper,config )
{
    var subAgvPathConfig = {
        borderWidth:0,
        agvWidth:30,
        agvHeight:25,
        portWidth:15,
        portHeight:20,
        parkWidth:40,
        parkHeight:20,
        minHeigh:160,
        colWidth:50,
        pathHeight:60,
        rateMS:10000,//
        turnMS:3000,//右加轉彎
        arcMS:3000,//左加轉彎
        points:{
            "17":"KTSMA01",
            "18":"KTSMA01",
            "19":"KTSMA01",
            "20":"KTSMA02",
            "21":"KTSMA02",
            "22":"KTSMA02",
            "23":"KTSMA03",
            "24":"KTSMA03",
            "25":"KTSMA03",
            "26":"KTSMA04",
            "27":"KTSMA04",
            "28":"KTSMA04",
            "29":"KTSMA05",
            "30":"KTSMA05",
            "31":"KTSMA05",
            "32":"KTSMA06",
            "33":"KTSMA06",
            "34":"KTSMA06",
            "35":"KTSMA07",
            "36":"KTSMA07",
            "37":"KTSMA07",
            "38":"KTSMA08",
            "39":"KTSMA08",
            "40":"KTSMA08",
            "41":"KTSMA09",
            "42":"KTSMA09",
            "43":"KTSMA09",
            "44":"KTSMA10",
            "45":"KTSMA10",
            "46":"KTSMA10",
            "47":"KTSMA11",
            "48":"KTSMA11",
            "49":"KTSMA11",
            "50":"KTSMA12",
            "51":"KTSMA12",
            "52":"KTSMA12",
            "53":"KTSMA13",
            "54":"KTSMA13",
            "55":"KTSMA13",
            "56":"KTSMA14",
            "57":"KTSMA14",
            "58":"KTSMA14",
            "59":"KTSMA15",
            "60":"KTSMA15",
            "61":"KTSMA15",
            "62":"KTSMA16",
            "63":"KTSMA16",
            "64":"KTSMA16",
            "16":"KTSM01"
        },
        stations :{
            //CNC是生產接駁站，M是主線接駁站
            //rate的比率是按照14等分切分時的
            "KTSMA01":{
                "text":"01",
                "type":"CNC-B",
                "rate":1.5,
                "rTRate":2
            },
            "KTSMA02":{
                "text":"02",
                "type":"CNC-B",
                "rate":2.5,
                "rTRate":3
            },
            "KTSMA03":{
                "text":"03",
                "type":"CNC-B",
                "rate":3.5,
                "rTRate":4
            },
            "KTSMA04":{
                "text":"04",
                "type":"CNC-B",
                "rate":4.5,
                "rTRate":5
            },
            "KTSMA05":{
                "text":"05",
                "type":"CNC-B",
                "rate":5.5,
                "rTRate":6
            },
            "KTSMA06":{
                "text":"06",
                "type":"CNC-B",
                "rate":6.5,
                "rTRate":7
            },
            "KTSMA07":{
                "text":"07",
                "type":"CNC-B",
                "rate":7.5,
                "rTRate":8
            },
            "KTSMA08":{
                "text":"08",
                "type":"CNC-B",
                "rate":8.5,
                "rTRate":9
            },
            "KTSMA09":{
                "text":"09",
                "type":"CNC-T",
                "rate":8.5,
                "rTRate":9
            },
            "KTSMA10":{
                "text":"10",
                "type":"CNC-T",
                "rate":7.5,
                "rTRate":8
            },
            "KTSMA11":{
                "text":"11",
                "type":"CNC-T",
                "rate":6.5,
                "rTRate":7
            },
            "KTSMA12":{
                "text":"12",
                "type":"CNC-T",
                "rate":5.5,
                "rTRate":6
            },
            "KTSMA13":{
                "text":"13",
                "type":"CNC-T",
                "rate":4.5,
                "rTRate":5
            },
            "KTSMA14":{
                "text":"14",
                "type":"CNC-T",
                "rate":3.5,
                "rTRate":4
            },
            "KTSMA15":{
                "text":"15",
                "type":"CNC-T",
                "rate":2.5,
                "rTRate":3
            },
            "KTSMA16":{
                "text":"16",
                "type":"CNC-T",
                "rate":1.5,
                "rTRate":2
            },
            "KTSM01":{
                "text":"主線",
                "type":"M",
                "rate":0.35
            }
        },
        agvs:{
            "KAGV07":{
                "text":"1號",
                "dfltPos":"16"
            },
            "KAGV08":{
                "text":"2號",
                "dfltPos":"16"
            },
            "KAGV09":{
                "text":"3號",
                "dfltPos":"16"
            }
        }
    };

    Wee.wr.SubAGVPath.superclass.constructor.call( this, paper, Wee.apply({}, config, subAgvPathConfig ) );
};

Wee.extend( Wee.wr.SubAGVPath, Wee.wr.MainAGVPath );

Wee.wr.SubAGVPath.prototype.drawAction = function()
{
    var me = this, _x = this.x + this.borderWidth,
        _y = this.y + this.borderWidth,
        _width = this.width - 2*this.borderWidth,
        _height = this.height - 2*this.borderWidth;
    me.colWidth = parseInt(_width/9 );
    if( _height > 160 ) me.minHeigh = _height;

    me.drawSubPaths( _x, _y, _width, _height );
    me.drawSubPorts( _x, _y, _width,  _height );
    me.drawAGVs();

    this.drawWidget();
}

Wee.wr.SubAGVPath.prototype.drawSubPaths = function( x,y,width, height)
{
    var me = this;
    _y =  y + ( height - me.pathHeight)/2;
    //上規道
    var _tPath = "M" + ( x + me.colWidth ) + " "+ _y +"L" + parseInt( x + width ) + " "+ _y;
    me.paper.path( _tPath ).attr( me.backPathAttr ).show();
    me.paper.path( _tPath ).attr( me.pathAttr ).show();
    //下規道
    var _bY = parseInt( _y + me.pathHeight );
    var _bPath = "M" + ( x + me.colWidth ) +  " "+ _bY +"L" + parseInt( x + width ) + " "+ _bY;
    me.paper.path( _bPath ).attr( me.backPathAttr ).show();
    me.paper.path( _bPath ).attr( me.pathAttr ).show();
    //左規道
    var _lPath= "M" + ( x + me.colWidth ) + " "+ _y + "A50 50,0,0,0," + ( x + me.colWidth/2 )+ " "+  parseInt( _y - me.pathHeight/2 )
        + "L" + ( x + me.colWidth/2 )+ " "+  parseInt( _y + 1.5*me.pathHeight )
        + "A50 50,0,0,0," + ( x + me.colWidth ) +  " "+ _bY ;
    me.paper.path( _lPath ).attr( me.backPathAttr ).show();
    me.paper.path( _lPath ).attr( me.pathAttr ).show();
    //右規道
    var maxX = x + width;
    for( var i = 0; i < 8; i++ ){
        var _colX = maxX - i * me.colWidth;
        var _rPath = "M" + _colX + " "+ _y +"A50 60,0,0,1," + _colX + " " + _bY;
        me.paper.path( _rPath ).attr( me.backPathAttr ).toBack().show();
    }

}

Wee.wr.SubAGVPath.prototype.drawSubPorts = function( x, y, width, height )
{
    var me = this;
    for( var key in me.stations )
    {
        var station = me.stations [ key ];
        var _x = x, _y = y, agvY = y;
        if( station.type == "CNC-B" ){
            _x = x + station.rate * me.colWidth;
            _y = y + ( height - me.pathHeight)/2  + me.pathHeight + me.agvHeight/2 + 5;
            agvY = y +  ( height - me.pathHeight)/2 + me.pathHeight - me.agvHeight/2;
        }else if( station.type == "CNC-T" ){
            _x = x + station.rate * me.colWidth;
            _y = y + ( height - me.pathHeight)/2  - me.portHeight - me.agvHeight/2 - 3;
            agvY = y +  ( height - me.pathHeight)/2  - me.agvHeight/2;
        }else if( station.type == "M" ){
            _y = y + height * 0.5;
            me.paper.rect(x, _y -  me.portWidth*2, me.portHeight, me.portWidth*2, 3 ).attr(me.portClinkerAttr).show();
            me.paper.rect(x, _y  ,  me.portHeight, me.portWidth * 2, 3 ).attr(me.portRowAttr).show();
            //station.x = x + me.colWidth/2 - me.agvHeight;
            station.x = this.x  + me.colWidth/2 - me.agvWidth/2;
            station.y = _y - me.agvHeight/2;
        }

        if( station.type != "M" ){
            me.paper.rect( _x - me.portWidth , _y, me.portWidth, me.portHeight, 3 ).attr( me.portClinkerAttr ).show();
            me.paper.rect( _x, _y, me.portWidth, me.portHeight, 3 ).attr( me.portRowAttr ).show();
            me.paper.text( _x, _y + me.portHeight/2, station.text ).attr( me.portTextAttr).show();
            station.x = _x - me.agvWidth/2;
            station.y = agvY;
        }


        /*
         //------------------------------------------------------------------------------------------------------
         if( point.type == "CM" ){
         me.paper.rect( _x - me.portWidth , _y, me.portWidth, me.portHeight, 3 ).attr( me.portClinkerAttr ).show();
         me.paper.rect( _x, _y, me.portWidth, me.portHeight, 3 ).attr( me.portRowAttr ).show();
         me.paper.text( _x, _y + me.portHeight/2, point.text ).attr( me.portTextAttr).show();
         point.x = _x;
         point.y = agvY;
         }else if( point.type == "CR" ){
         point.x = _x - me.portWidth;
         point.y = agvY;
         }else if( point.type == "CL" ){
         point.x = _x + me.portWidth;
         point.y = agvY;
         } */

    }//
}

Wee.wr.SubAGVPath.prototype.agvMove = function( agvKey, startP, endP )
{
    var me = this, strPath;
    var _start = me.getStation( startP );
    var _end = me.getStation( endP );
    if( !_start )_start = me.getStation( "16" );
    if( !_end )_end = me.getStation( "16" );
    //--------------------------------------------------------------------------------------------------------------
    var _pathRate = _end.rate - _start.rate;
    var _agv = me.agvs[agvKey].ins;
    //--------------------------------------------------------------------------------------------------------------
    _agv.setPosition(_start.x, _start.y);
    var movePath = {};
    var _totalMs;
    //--------------------------------------------------------------------------------------------------------------
    if( _start.type == "M"  ){
        var _1Path = {
            x : _start.x,
            y : _start.y + 0.75*me.pathHeight + me.agvHeight/2
        };
        var _1Ms = me.arcMS;
        var _2Path = {
            x : this.x + me.colWidth,
            y : this.y +  ( this.height - me.pathHeight)/2 + me.pathHeight - me.agvHeight/2
        };
        var _2Ms = me.arcMS;

        if( _end.type == "CNC-B" ){
            var _3Path = {
                x: _end.x,
                y: _end.y
            };
            var  _3Ms = me.rateMS * ( _end.rate - 1 );
            _totalMs = _1Ms + _2Ms + _3Ms;
            movePath[ parseInt(_1Ms * 100 / _totalMs )  + "%" ] = _1Path;
            movePath[ parseInt( ( _1Ms + _2Ms ) * 100 / _totalMs )  + "%" ] = _2Path;
            movePath[ parseInt( ( _1Ms + _2Ms +  _3Ms) * 100 / _totalMs )  + "%" ] = _3Path;
        }else if( _end.type == "CNC-T" ){
            var _3Path = {
                x: this.x + _end.rTRate * me.colWidth,
                y: this.y +  ( this.height - me.pathHeight)/2 + me.pathHeight - me.agvHeight/2
            };
            var  _3Ms = me.rateMS * ( _end.rTRate - 1 );

            var _4Path = {
                x: this.x + _end.rTRate * me.colWidth,
                y: _end.y
            };
            var  _4Ms = me.turnMS;

            var _5Path = {
                x: _end.x,
                y: _end.y
            };
            var  _5Ms = me.rateMS * ( _end.rTRate - _end.rate );

            _totalMs = _1Ms + _2Ms + _3Ms + _4Ms + _5Ms;
            movePath[ parseInt(_1Ms * 100 / _totalMs )  + "%" ] = _1Path;
            movePath[ parseInt( ( _1Ms + _2Ms ) * 100 / _totalMs )  + "%" ] = _2Path;
            movePath[ parseInt( ( _1Ms + _2Ms +  _3Ms) * 100 / _totalMs )  + "%" ] = _3Path;
            movePath[ parseInt( ( _1Ms + _2Ms +  _3Ms + _4Ms) * 100 / _totalMs )  + "%" ] = _4Path;
            movePath[ parseInt( ( _1Ms + _2Ms +  _3Ms + _4Ms + _5Ms) * 100 / _totalMs )  + "%" ] = _5Path;
        }
    }
    //--------------------------------------------------------------------------------------------------------------
    else if( _end.type == "M" ){
        var _1Path = {
            x : _end.x,
            y : _end.y
        };
        var _1Ms = me.arcMS;

        var _2Path = {
            x : _end.x,
            y : _end.y - 0.75*me.pathHeight - me.agvHeight/2
        };
        var _2Ms = me.arcMS;

        if(   _start.type == "CNC-B"  ){
            var _3Path = {
                x : this.x + me.colWidth,
                y : this.y +  ( this.height - me.pathHeight)/2 - me.agvHeight/2
            };
            var _3Ms = me.rateMS * ( _start.rTRate - 1 );

            var _4Path = {
                x: this.x + _start.rTRate * me.colWidth,
                y: this.y +  ( this.height - me.pathHeight)/2 - me.agvHeight/2
            };
            var  _4Ms = me.turnMS;

            var _5Path = {
                x: this.x + _start.rTRate * me.colWidth,
                y: _start.y
            };
            var  _5Ms = me.rateMS * ( _start.rTRate - _start.rate );

            _totalMs = _1Ms + _2Ms + _3Ms + _4Ms + _5Ms ;
            movePath[ parseInt(_5Ms * 100 / _totalMs )  + "%" ] = _5Path;
            movePath[ parseInt( ( _5Ms + _4Ms ) * 100 / _totalMs )  + "%" ] = _4Path;
            movePath[ parseInt( ( _5Ms + _4Ms +  _3Ms) * 100 / _totalMs )  + "%" ] = _3Path;
            movePath[ parseInt( ( _5Ms + _4Ms +  _3Ms+  _2Ms) * 100 / _totalMs )  + "%" ] = _2Path;
            movePath[ parseInt( ( _5Ms + _4Ms +  _3Ms+  _2Ms+  _1Ms) * 100 / _totalMs )  + "%" ] = _1Path;

        }else if( _start.type == "CNC-T" ){
            var _3Path = {
                x : this.x + me.colWidth,
                y : _start.y
            };
            var _3Ms = me.rateMS * ( _start.rate - 1 );
            _totalMs = _1Ms + _2Ms + _3Ms;
            movePath[ parseInt(_3Ms * 100 / _totalMs )  + "%" ] = _3Path;
            movePath[ parseInt( ( _3Ms + _2Ms ) * 100 / _totalMs )  + "%" ] = _2Path;
            movePath[ parseInt( ( _1Ms + _2Ms +  _3Ms) * 100 / _totalMs )  + "%" ] = _1Path;
        }
    }
    //--------------------------------------------------------------------------------------------------------------
    else if( _start.type == "CNC-B"  ){
        if( _end.type == "CNC-B"  ){
            if( _end.rate - _start.rate  < 0 ){
                console.log( "MainAGVPath -- agvMove end point is before start point!" +  _end );
                return;
            }
            _totalMs = me.rateMS * ( _end.rate - _start.rate )
            movePath[ "100%" ] = {
                x:_end.x,
                y:_end.y
            };
        }else if(  _end.type == "CNC-T"  ){
            var _1Path, _2Path, _3Path,_1Ms,_2Ms,_3Ms;

            if(_end.rate > _start.rate ){
                _1Path = {
                    x : this.x + _end.rTRate * me.colWidth,
                    y : _start.y
                };
                _1Ms = ( _end.rTRate - _start.rate ) * me.rateMS;

                _2Path = {
                    x : this.x + _end.rTRate * me.colWidth,
                    y : _end.y
                };
                _2Ms = me.turnMS;

                _3Path = {
                    x : _end.x,
                    y : _end.y
                };
                _3Ms = ( _end.rTRate - _end.rate ) * me.rateMS;

            }else{

                _1Path = {
                    x : this.x + _start.rTRate * me.colWidth,
                    y : _start.y
                };
                _1Ms = ( _start.rTRate - _start.rate ) * me.rateMS;

                _2Path = {
                    x : this.x + _start.rTRate * me.colWidth,
                    y : _end.y
                };
                _2Ms = me.turnMS;

                _3Path = {
                    x : _end.x,
                    y : _end.y
                };
                _3Ms = ( _start.rate - _end.rate ) * me.rateMS;
            }
            _totalMs = _1Ms + _2Ms + _3Ms;
            movePath[ parseInt(_1Ms * 100 / _totalMs )  + "%" ] = _1Path;
            movePath[ parseInt( ( _1Ms + _2Ms ) * 100 / _totalMs )  + "%" ] = _2Path;
            movePath[ parseInt( ( _1Ms + _2Ms +  _3Ms) * 100 / _totalMs )  + "%" ] = _3Path;
        }
    }
    //--------------------------------------------------------------------------------------------------------------
    else if( _start.type == "CNC-T"  ){
        if( _end.type != "CNC-T" ){
            console.log( "MainAGVPath -- agvMove end point is not right!" +  _end );
            return;
        }
        if( _end.rate > _start.rate ){
            console.log( "MainAGVPath -- agvMove end point is not right!" +  _end );
            return;
        }

        _totalMs = me.rateMS * ( _start.rate - _end.rate )
        movePath[ "100%" ] = {
            x:_end.x,
            y:_end.y
        };
    }
    _agv.animatePath( movePath, _totalMs );
}
/**
 * 流水線
 * @param paper
 * @param config
 * @constructor
 */
Wee.wr.FlowLinePath = function( paper,config )
{

    var flowLineConfig = {
        borderWidth:0,
        cols: 12,
        pathHeight:15,
        backPathColor:"#000000",
        fontPathColor:"#FFFFFF",
        backPathAttr:{
            "stroke":"black",
            "stroke-dasharray":"",
            "stroke-width":20,
            "stroke-opacity":.8
        },
        pathAttr:{
            "stroke":"#FFFFFF",
            "stroke-dasharray":".",
            "stroke-width":20,
            "stroke-opacity":.9
        }
    };

    Wee.wr.FlowLinePath.superclass.constructor.call( this, paper, Wee.apply({}, config, flowLineConfig ) );
};

Wee.extend( Wee.wr.FlowLinePath, Wee.wr.Widget );

Wee.wr.FlowLinePath.prototype.drawAction = function()
{
    var me = this, _x = this.x + this.borderWidth,
        _y = this.y + this.borderWidth,
        _width = this.width - 2*this.borderWidth,
        _height = this.height - 2*this.borderWidth;
    var colWidth = parseInt( _width/ me.cols );

    me.drawPath( _x , _y, colWidth * ( me.cols - 1 ), _height );

    this.drawWidget();
}

Wee.wr.FlowLinePath.prototype.drawPath = function( x, y, width, height )
{
    var me = this;
    //上規道
    var tY = parseInt( y + me.pathHeight );
    var bY = parseInt( y + height - me.pathHeight );
    var _path = "M" + x + " "+ tY +"L" + parseInt( x + width ) + " "+ tY
        + "A30 50,0,0,1,"+ parseInt( x + width ) +  " " + bY
        + "L"+ x + " "+ bY;
    this.path01 = me.paper.path( _path ).attr( me.backPathAttr).toBack().show();
    this.path02 = me.paper.path( _path ).attr( me.pathAttr ).show()
    /*
    function changAttr(){
        me.path02.attr( {"stroke-dasharray":strDasharray});

    }
    setInterval(changAttr, 1000);*/

    /*var tY = parseInt( y - 20 );
    var _tPath = "M" + x + " "+ tY +"L" + parseInt( x + width ) + " "+ tY ;
    me.paper.path( _tPath ).attr( me.backPathAttr ).show();
    me.paper.path( _tPath ).attr( me.pathAttr ).show();
    //下規道
    var bY = parseInt( y + height + 20 );
    var _bPath = "M" + x +  " "+ bY +"L" + parseInt( x + width ) + " "+ bY;
    me.paper.path( _bPath ).attr( me.backPathAttr ).show();
    me.paper.path( _bPath ).attr( me.pathAttr ).show();
    //右彎道
    var _rPath = "M" + parseInt( x + width ) +  " "+ tY +"A30 50,0,0,1,"+ parseInt( x + width ) +  " " + bY;
    me.paper.path( _rPath ).attr( me.backPathAttr).toBack().show();
    me.paper.path( _rPath ).attr( me.pathAttr ).show();*/
}

Wee.wr.FlowLinePath.prototype.changeColor = function( x, y, width, height )
{
    var me = this;
    var _color = me.backPathColor;
    me.backPathColor = me.fontPathColor;
    me.fontPathColor = _color;
    me.path01.attr({ "stroke": me.backPathColor });
    me.path02.attr({ "stroke": me.fontPathColor });
}

/**
 * OEE報表清單
 * @param paper
 * @param config
 * @constructor
 */
Wee.wr.OeeDisplay = function( paper,config )
{
    var oeeDisplayConfig = {
        rows:4,
        borderWidth:0,
        titleAttr:{
            textRatio:0.4,
            textAttr:{
                fill:"#000000",
                stroke:"#000000	",
                "font-weight":"noral",
                "text-anchor":"middle"
            },
            backAttr:{
                fill:"#DDDDDD",
                "stroke-width":1,
                stroke: "black",
                "fill-opacity":.8
            },
            text:"A06"
        },
        planAttr:{
            textRatio:0.4,
            textAttr:{
                fill:"#00FF00",
                stroke:"#00FF00"
            },
            backAttr:{
                fill:"#2E8B57",
                "stroke-width":1,
                stroke: "black",
                "fill-opacity":.01
            },
            text:""
        },
        prodAttr:{
            textRatio:0.4,
            textAttr:{
                fill:"#FF9933",
                stroke:"#FF9933"
            },
            backAttr:{
                fill:"black",
                "stroke-width":1,
                stroke: "black",
                "fill-opacity":.01
            },
            text:""
        },
        rateAttr:{
            textRatio:0.4,
            textAttr:{
                fill:"#FFFF00",
                stroke:"#FFFF00"
            },
            backAttr:{
                fill:"#FFA500",
                "stroke-width":1,
                stroke: "black",
                "fill-opacity":.01
            },
            text:""
        }
    };
    Wee.wr.OeeDisplay.superclass.constructor.call( this, paper, Wee.apply({}, config, oeeDisplayConfig ) );
    //----------------------------------------------------------------------------------------------------------
    this.titleField = new Wee.wr.TextLabel( this.paper, Wee.apply({}, this.titleAttr, oeeDisplayConfig.titleAttr  ) );
    this.planField = new Wee.wr.TextLabel( this.paper, Wee.apply({}, this.planAttr, oeeDisplayConfig.planAttr  ) );
    this.prodField = new Wee.wr.TextLabel( this.paper, Wee.apply({}, this.prodAttr, oeeDisplayConfig.prodAttr  ) );
    this.rateField = new Wee.wr.TextLabel( this.paper, Wee.apply({}, this.rateAttr, oeeDisplayConfig.rateAttr  ) );

}
Wee.extend( Wee.wr.OeeDisplay, Wee.wr.Widget );

Wee.wr.OeeDisplay.prototype.drawAction = function()
{
    var me  = this;
    var _x = this.x + this.borderWidth,
        _y = this.y + this.borderWidth,
        _width = this.width - 2*this.borderWidth,
        _height = this.height - 2*this.borderWidth;

    var rowHeight = parseInt( _height / me.rows );
    //title
    this.titleField.attr({x:_x, y: _y, width: _width, height: rowHeight });
    this.titleField.drawAction();
    //plan
    this.planField.attr({x:_x, y: _y + rowHeight, width: _width, height: rowHeight });
    this.planField.drawAction();
    //product
    this.prodField.attr({x:_x, y: _y + 2*rowHeight, width: _width, height: rowHeight });
    this.prodField.drawAction();
    //rate
    this.rateField.attr({x:_x, y: _y + 3*rowHeight, width: _width, height: rowHeight });
    this.rateField.drawAction();

    this.drawWidget();
}

Wee.wr.OeeDisplay.prototype.changeStatus = function( status )
{
    var me = this;
    if( status )status = status.toLowerCase( );
    if( status == "stop" ){
        this.titleField.setBackAttr({ fill:"red" });
    }else if( status == "idle" ){
        this.titleField.setBackAttr({ fill:"yellow" });
    }else if( status == "run" ){
        this.titleField.setBackAttr({ fill:"green" });
    }else{
        this.titleField.setBackAttr({ fill:"#DDDDDD" });
    }
}

Wee.wr.OeeDisplay.prototype.loadData = function( data )
{
    var me = this;
    if( data.plan != null ){
        this.planField.setText( data.plan );
    }
    if( data.product != null ){
        this.prodField.setText( data.product );
    }
    if( data.rate != null ){
        this.rateField.setText( data.rate );
    }
    me.changeStatus( data.status );
}

/**
 * 圖例
 * @param paper
 * @param config
 * @constructor
 */
Wee.wr.Legend = function( paper,config )
{
    var legendConfig = {
        legendW:20,
        legendH:10,
        legendR:2,
        borderWidth:2,
        layout:"row",
        space:10, //兩邊的距離
        legendS:20,//圖例佔用長度
        textSpace:10,
        legendAnchor:"start",
        legendT:"圖  例",
        showTitle:true,
        legendNum:4,
        titleAttr:{
            "fill":"white",
            "stroke":"white",
            "font-size":10,
            "text-anchor":"middle",
            "stroke-width":1
        },
        legendAttr:{
            "fill":"white",
            "stroke":"white",
            "font-size":10,
            "text-anchor":"start",
            "stroke-width":1
        },
        backAttr:{
            fill: "#A9A9A9",
            stroke: "#A9A9A9",
            "stroke-width":1,
            cursor: "pointer",
            "fill-opacity":1
        },
        legends:{
            "run":{
                "text":"執行",
                "color":"#66FF00"
            },
            "idle":{
                "text":"待料",
                "color":"yellow"
            },
            "down":{
                "text":"故障",
                "color":"red"
            },
            "stop":{
                "text":"停機",
                "color":"#F0F0F0"
            }
        }
    };

    Wee.wr.Legend.superclass.constructor.call( this, paper, Wee.apply({}, config, legendConfig ) );
    this.setAttr( Wee.apply( {}, this.backAttr, legendConfig.backAttr ) );
}

Wee.extend( Wee.wr.Legend, Wee.wr.Widget );

Wee.wr.Legend.prototype.drawAction = function()
{
    var me = this;
    var _x = this.x + this.borderWidth,
        _y = this.y + this.borderWidth,
        _width = this.width - 2*this.borderWidth,
        _height = this.height - 2*this.borderWidth;

    if( me.layout == "row" ){
        me.drawRowAction( _x, _y, _width, _height );
    }else if( me.layout == "col" ){
        me.drawColAction(  _x, _y, _width, _height);
    }

    this.drawWidget();
}

Wee.wr.Legend.prototype.drawRowAction = function( x, y, width, height )
{
    var me = this;
    var colNum = me.legendNum;
    if( me.showTitle )colNum = colNum + 1;
    var  colWidth =  parseInt(  ( width - 2*me.space )/ colNum );
    //標題
    var _bx = x + me.space ;
    var _bY = y + height/2 ;
    if( me.showTitle ){
        me.paper.text( _bx + colWidth/2, _bY, me.legendT ).attr( me.titleAttr).show();
        _bx = _bx + colWidth;
    }
    //圖例
    for( var i  in me.legends ){
        var lAttr = me.legends[ i ];
        this.paper.rect( _bx  , _bY - me.legendH/2, me.legendW, me.legendH, me.legendR ).attr({
            "fill": lAttr["color"],"stroke":lAttr["color"] }).show();
        this.paper.text( _bx + me.legendW + me.textSpace, _bY,  lAttr.text).attr( me.legendAttr ).show();
        _bx = _bx + colWidth;
    }
}

Wee.wr.Legend.prototype.drawColAction = function( x, y, width, height  )
{
    var me = this;
    var rowNum = me.legendNum;
    if( me.showTitle )rowNum = rowNum + 1;
    var  rowHeight =  parseInt(  ( height - 2*me.space )/ rowNum );
    //標題
    var _bY = y + me.space ;
    var _bX = x + width/2  ;
    if( me.showTitle ){
        me.paper.text( _bX , _bY + rowHeight/2 , me.legendT ).attr( me.titleAttr).show();
        _bY = _bY + rowHeight;
    }
    //圖例

    for( var i  in me.legends  ){
        var lAttr = me.legends[ i ];
        this.paper.rect( _bX - me.legendW - 5 , _bY + rowHeight/2 - me.legendH/2, me.legendW, me.legendH , me.legendR ).attr({
            "fill": lAttr["color"],"stroke":lAttr["color"] }).show();
        this.paper.text( _bX + 5, _bY + rowHeight/2 ,  lAttr.text).attr( me.legendAttr ).show();
        _bY = _bY + rowHeight;
    }
}

/**
 * OEE Panel
 * @param paper
 * @param config
 * @constructor
 */
Wee.wr.OeePanel = function( paper,config )
{
    var oeePanelConfig = {
        space:10, //兩邊的距離
        borderWidth:0,
        rateAttr:{
            "fill":"yellow",
            "stroke":"yellow",
            "text-anchor":"end",
            "font-size":24,
            "stroke-width":1
        },
        valueAttr:{
            "fill":"white",
            "stroke":"white",
            "text-anchor":"end",
            "font-size":24,
            "stroke-width":1
        },
        labelAttr:{
            "fill":"white",
            "stroke":"white",
            "text-anchor":"start",
            "font-size":24,
            "stroke-width":1
        }
    };
    Wee.wr.OeePanel.superclass.constructor.call( this, paper, Wee.apply({}, config, oeePanelConfig ) );

    this.setAttr( Wee.apply( {}, this.backAttr, oeePanelConfig.backAttr ) );
}

Wee.extend( Wee.wr.OeePanel, Wee.wr.Widget );

Wee.wr.OeePanel.prototype.drawAction = function()
{
    var me = this;
    var _x = this.x + this.borderWidth,
        _y = this.y + this.borderWidth,
        _width = this.width - 2*this.borderWidth,
        _height = this.height - 2*this.borderWidth;

    var rowHeight = parseInt( (_height - 2 * me.space)/ 3 );
    //Plan
    me.paper.text( _x + _width/4,  _y +  rowHeight * 0.5, "Plan" ).attr( me.labelAttr ).show();
    me.planField = me.paper.text( _x + _width* 3/4,  _y +  rowHeight * 0.5, "0").attr( me.valueAttr).show();
    //Product
    me.paper.text( _x + _width/4,  _y +  rowHeight * 1.5, "Product" ).attr( me.labelAttr ).show();
    me.productField = me.paper.text( _x + _width* 3/4,  _y +  rowHeight * 1.5, "0").attr( me.valueAttr).show();
    //Rate
    me.paper.text( _x + _width/4,  _y +  rowHeight * 2.5, "Rate" ).attr( me.labelAttr ).show();
    me.rateField = me.paper.text( _x + _width* 3/4,  _y +  rowHeight * 2.5, "0%").attr( me.rateAttr).show();

    this.drawWidget();
}

Wee.wr.OeePanel.prototype.loadData = function( data )
{
    var me = this;
    if( !data )return;
    me.planField.attr( { text : data["plan"] });
    me.productField.attr( { text : data["product"] });
    me.rateField.attr( { text : data["rate"] });
}



/**
 *
 * @param paper
 * @param config
 * @constructor
 */
Wee.wr.ColumnField = function( paper,config )
{
    var columnConfig = {
        borderWidth:0,
        fieldBorder:2,
        layout:'col',//col表示列，row表示行Label
        cols:3,
        titleRow:1,//title占的高度
        labelAttr:{
            textAttr:{
                fill:"white",
                stroke:"white",
                "font-weight":"normal",
                "text-anchor":"start"
            },
            backAttr:{
                fill:"#808080",
                "stroke-width":0,
                stroke: "white",
                "fill-opacity":1
            }
        },
        valueAttr:{
            textRatio:0.6,
            textAttr:{
                fill:"white",
                stroke:"white",
                "font-weight":"normal",
                "text-anchor":"middle"
            },
            backAttr:{
                fill:"black",
                "stroke-width":1,
                stroke: "white",
                "fill-opacity":1
            }
        },
        titleAttr:{
            textRatio:0.3,
            textAttr:{
                fill:"yellow",
                stroke:"yellow",
                "font-weight":"normal",
                "text-anchor":"middle"
            },
            backAttr:{
                fill:"#000080",
                "stroke-width":1,
                stroke: "white",
                "fill-opacity":1
            }
        }
    };

    Wee.wr.ColumnField.superclass.constructor.call( this, paper, Wee.apply({}, config, columnConfig ) );
    this.initAttr();
    this.setAttr( Wee.apply( {}, this.backAttr, columnConfig.backAttr ) );
}

Wee.extend( Wee.wr.ColumnField, Wee.wr.Widget );

Wee.wr.ColumnField.prototype.initAttr = function()
{
    var me  = this;
    if( me.titleRate ){
        me.titleAttr.textRatio = me.titleRate;
    }
    if( me.labelRate ){
        me.labelAttr.textRatio = me.labelRate;
    }
    if( me.valueRate ){
        me.valueAttr.textRatio = me.valueRate;
    }

    if( me.titleColor ){
        me.titleAttr.textAttr.stroke = me.titleColor;
        me.titleAttr.textAttr.fill = me.titleColor;
    }
    if( me.labelColor ){
        me.labelAttr.textAttr.stroke = me.labelColor;
        me.labelAttr.textAttr.fill = me.labelColor;
    }
    if( me.valueColor ){
        me.valueAttr.textAttr.stroke = me.valueColor;
        me.valueAttr.textAttr.fill = me.valueColor;
    }

}

Wee.wr.ColumnField.prototype.drawAction = function()
{
    var me = this;
    var _x = this.x + this.borderWidth,
        _y = this.y + this.borderWidth,
        _width = this.width - 2*this.borderWidth,
        _height = this.height - 2*this.borderWidth;
    //--------------------------------------------------------------------------------------------------------------
    if( me.layout == "col" )
        me.drawFields( _x, _y, _width, _height );
    else if( me.layout == "row" )
        me.drawLabelFields( _x, _y, _width, _height );
    //--------------------------------------------------------------------------------------------------------------
    this.drawWidget();
}

Wee.wr.ColumnField.prototype.drawFields = function(  _x, _y, _width, _height )
{
    var me = this, rowNum = me.fields.length ;
    var rowHeight = parseInt( _height / rowNum  );
    //--------------------------------------------------------------------------------------------------------------
    if( me.title && me.title !="" ){
        rowNum = rowNum + me.titleRow;
        rowHeight = parseInt( _height / rowNum  );
        var _text  = new Wee.wr.TextLabel( this.paper, me.titleAttr );
        _text.attr( {
            x: _x,
            //y: ( _y + me.fieldBorder ),
            y:_y,
            width: _width,
            //height: ( me.titleRow * rowHeight - 2 * me.fieldBorder )
            height: ( me.titleRow * rowHeight )
        });
        _text.setText( me.title );
        _text.drawAction();
        _y = _y + me.titleRow * rowHeight;
    }
    //--------------------------------------------------------------------------------------------------------------
    this.fieldList = {};
    for( var i = 0; i < me.fields.length; i++ ){
        var field = me.fields[i];
        var _text  = new Wee.wr.TextLabel( this.paper, field.valueAttr || me.valueAttr );
        _text.attr( {
            x: ( _x + me.fieldBorder ) ,
            y: ( _y + i * rowHeight + me.fieldBorder ),
            width: (_width - 2*+ me.fieldBorder ),
            height: ( rowHeight - 2 * me.fieldBorder )
        });
        _text.setText( field.text );
        _text.drawAction();
        if( field.name )
        this.fieldList[ field.name ] = _text;
    }
}

Wee.wr.ColumnField.prototype.loadData = function( data )
{
    var me = this;
    //--------------------------------------------------------------------------------------------------------------
    for( var key in data ){
        var field = this.fieldList[ key ];
        if( this.fieldList[ key ] ){
            field.setText( data[ key ] );
        }
    }
}

Wee.wr.ColumnField.prototype.drawLabelFields = function(  _x, _y, _width, _height )
{
    var me = this; var
    rows = Math.ceil( me.fields.length / me.cols);
    //--------------------------------------------------------------------------------------------------------------
    if( me.title && me.title != "" ){
        var _label = new Wee.wr.TextLabel( this.paper, me.titleAttr );
        _label.attr( {
            x: _x,
            y: _y ,
            width: 50,
            height: _height
        });
        _label.setText( me.title );
        _label.drawAction();
        _x = _x + 50;
        _width = _width - 50;
    }
    //--------------------------------------------------------------------------------------------------------------
    var rowHeight = parseInt( _height / rows );
    var colWidth = parseInt( _width / ( 2 * me.cols ) );
    //--------------------------------------------------------------------------------------------------------------
    this.fieldList = {};
    for( var i = 0; i < me.fields.length; i++ )
    {
        var row = Math.floor( i / me.cols );
        var col = i % me.cols;

        var field = me.fields[i];
        //Label
        var _label = new Wee.wr.TextLabel( this.paper, field.labelAttr || me.labelAttr );
        _label.attr( {
            x: _x + 2*col * colWidth,
            y: ( _y + row * rowHeight ),
            width: colWidth,
            height: rowHeight
        });
        _label.setText( field["label"] );
        _label.drawAction();
        //值
        var _text  = new Wee.wr.TextLabel( this.paper, field.valueAttr || me.valueAttr );
        _text.attr( {
            x: _x + ( 2*col + 1 ) * colWidth,
            y: ( _y + row * rowHeight ),
            width: colWidth,
            height: rowHeight
        });
        _text.setText( field["text"] );
        _text.drawAction();
        this.fieldList[ field.name ] = _text;
    }
}

/**
 * 餅狀圖
 * @param paper
 * @param config
 * @constructor
 */
Wee.wr.PieChart = function( paper,config )
{
    var pieChartConfig = {
        title: "稼動率分佈圖",
        titleH  : 30,
        legendH : 30,
        charts:{},
        titleAttr:{
            textRatio:0.5,
            textAttr:{
                fill:"yellow",
                stroke:"yellow"
            },
            backAttr:{
                fill:"#2E8B57",
                "stroke-width":0,
                stroke: "black",
                "fill-opacity":.01
            }
        },
        legendAttr:{
            backAttr:{
                fill:"black",
                "stroke-width":0,
                cursor: "pointer",
                "fill-opacity":.001
            }
        },
        textAttr:{
            "font-weight":"normal",
            "text-anchor":"middle",
            stroke: "black"
        },
        legends : {
            "run":{
                "text":"執行",
                "color":"#66FF00"
            },
            "idle":{
                "text":"待料",
                "color":"yellow"
            },
            "down":{
                "text":"故障",
                "color":"red"
            },
            "stop":{
                "text":"停機",
                "color":"#F0F0F0"
            }
        },
        data : [
            {
                "id":"run",
                "rate":0.9,
                "qty":500
            },{
                "id":"down",
                "rate":0.05,
                "qty":25
            },{
                "id":"stop",
                "rate":0.015,
                "qty":2
            },{
                "id":"idle",
                "rate":0.035,
                "qty":6
            }
        ]
    };

    Wee.wr.PieChart.superclass.constructor.call( this, paper, Wee.apply({}, config, pieChartConfig ) );

};
Wee.extend( Wee.wr.PieChart, Wee.wr.Widget );

Wee.wr.PieChart.prototype.initAttr = function()
{
    var me = this,chartDiam;
    chartDiam = me.height - me.titleH - me.legendH;
    if( this.title == '' )
        chartDiam = me.height - me.legendH;
    //
    if( chartDiam > me.width )chartDiam = me.width;
    //
    me.cy = me.titleH + (me.height - me.titleH - me.legendH)/2 + this.y;
    if( this.title == '' )
        me.cy = (me.height - me.legendH)/2 + this.y + 2;
    me.oDiam = parseInt( chartDiam / 2 );
    me.cx = me.width / 2 + this.x;
    me.iDiam = parseInt( chartDiam / 3 );
}

Wee.wr.PieChart.prototype.drawAction = function()
{
    var me = this;
    this.initAttr();
    //標題
    if( this.title !=  '' ){
        var _titleAttr = me.titleAttr;
        _titleAttr.x = me.x;
        _titleAttr.y = me.y;
        _titleAttr.width = me.width;
        _titleAttr.height = me.titleH;
        _titleAttr.text = me.title;
        this.titleLabel = new Wee.wr.TextLabel( this.paper, _titleAttr );
        this.titleLabel.drawAction();
    }
    //畫圖
    me.drawChart();
    //圖例
    var _legendAttr = me.legendAttr;
    _legendAttr.x = me.x;
    _legendAttr.y = me.y + me.height -me.legendH;
    _legendAttr.width = me.width;
    _legendAttr.height = me.legendH;
    _legendAttr.legendW = 15;
    _legendAttr.textSpace = 5;
    _legendAttr.showTitle = false;
    this.legendLabel = new Wee.wr.Legend( this.paper, _legendAttr);
    this.legendLabel.drawAction();
    //
    this.drawWidget();
}

Wee.wr.PieChart.prototype.drawChart = function(  )
{
    var me = this;
    var startAngle = 0;
    var angular = 0;
    var endAngle;
    var rad = Math.PI / 180;
    if( me.data && me.data.length > 0 ){
        for( var i = 0; i < me.data.length; i++ ){
            var _rateAttr = me.data[i];
            angular = 360 * _rateAttr["rate"];
            endAngle =  startAngle + angular;
            //--------------------------------------------------------------------------------------------------
            var ix1 = me.cx + me.iDiam * Math.cos( -startAngle * rad );
            var iy1 = me.cy + me.iDiam * Math.sin( -startAngle * rad );

            var x1 = me.cx + me.oDiam * Math.cos( -startAngle * rad );
            var y1 = me.cy + me.oDiam * Math.sin( -startAngle * rad );

            var ix2 = me.cx + me.iDiam * Math.cos( -endAngle * rad );
            var iy2 = me.cy + me.iDiam * Math.sin( -endAngle * rad );

            var x2 = me.cx + me.oDiam * Math.cos( -endAngle * rad );
            var y2 = me.cy + me.oDiam * Math.sin( -endAngle * rad );

            //var tx = me.cx + me.iDiam * Math.cos( -( startAngle + angular/2 )  * rad ) / 2 + me.oDiam * Math.cos( -( startAngle + angular/2 )  * rad )/2;
            //var ty = me.cy + me.iDiam * Math.sin( -( startAngle + angular/2 )  * rad ) / 2 + me.oDiam * Math.sin( -( startAngle + angular/2 )  * rad )/2;

            var bigArc;
            if( endAngle - startAngle > 180 ){
                bigArc = 1
            }else{
                bigArc = 0;
            }
            var pathStr = "M" + ix1 + "," + iy1 + "L" + x1 + "," + y1
                + " A" + me.oDiam +"," + me.oDiam + " 0 "+ bigArc + " 0 " + x2  + "," + y2
                +"L" + ix2 + "," + iy2
                +" A"+ me.iDiam +"," + me.iDiam + " 0 "+ bigArc + " 1 " + ix1  + "," + iy1+ "Z";
            //--------------------------------------------------------------------------------------------------
            if(  me.charts[ _rateAttr["id"] ] ){
                var _char = me.charts[ _rateAttr["id"] ];
                _char.attr( { "path":pathStr }).show();
                // var _text = me.charts[ _rateAttr["id"] + "_text" ];
                // _text.attr({"text":_rateAttr["qty"] }).show()
            }else{
                var _char =  this.paper.path(  pathStr ).attr({
                    "fill":me.legends[_rateAttr["id"].toLowerCase()]["color"],
                    "stroke":"white",
                    "font-size":10,
                    "text-anchor":"middle",
                    "stroke-width":1
                }).show();
                me.charts[ _rateAttr["id"] ] = _char;
                // var _text = this.paper.text( tx, ty, _rateAttr["qty"]).attr( me.textAttr).show();
                // me.charts[ _rateAttr["id"] + "_text"] = _text;
            }
            //--------------------------------------------------------------------------------------------------
            startAngle = endAngle;
        }
    }
}

Wee.wr.PieChart.prototype.loadData = function( data )
{
    var me = this;
    me.data = data;
    me.drawChart();
}

/**
 *  柱狀圖
 * @param paper
 * @param config
 * @constructor
 */
Wee.wr.BarChart = function( paper,config )
{
    var barChartConfig = {
        legendH : 20,
        colBorder:5,
        charts:[],
        axisAttr:{
            fill:"#9900FF",
            "stroke-width":2,
            "stroke":"#9900FF"
        },
        legendAttr:{
            backAttr:{
                fill:"black",
                "stroke-width":0,
                cursor: "pointer",
                "fill-opacity":.001
            }
        },
        textAttr:{
            "font-weight":"normal",
            "text-anchor":"middle",
            "stroke": "black"
        },
        axisYs:[ "standard","actual" ],
        legends : {
            "standard":{
                "text":"標準",
                "color":"#0066FF"
            },
            "actual":{
                "text":"實際",
                "color":"#66CC00"
            }
        },
        data:[]
    };

    Wee.wr.BarChart.superclass.constructor.call( this, paper, Wee.apply({}, config, barChartConfig ) );

};
Wee.extend( Wee.wr.BarChart, Wee.wr.Widget );


Wee.wr.BarChart.prototype.drawAction = function()
{
    var me = this;
    var _legendAttr = me.legendAttr;
    _legendAttr.x = me.x;
    _legendAttr.y = me.y;
    _legendAttr.width = me.width;
    _legendAttr.height = me.legendH;
    _legendAttr.legendW = 15;
    _legendAttr.textSpace = 5;
    _legendAttr.space = ( this.width - 100 )/2;
    _legendAttr.showTitle = false;
    _legendAttr.legends = me.legends;
    _legendAttr.legendNum = 2;
    this.legendLabel = new Wee.wr.Legend( this.paper, _legendAttr);
    this.legendLabel.drawAction();
    //x軸
    var  xAxisPath = "M"+ ( me.x + 10 ) + " " + ( me.y + me.height - 20 ) + "H" + ( me.x + me.width - 10 );
    this.paper.path( xAxisPath).attr( me.axisAttr ).show();
    //y軸
    /* var  yAxisPath = "M"+ ( me.x + 20 ) + " " + ( me.y + me.height - 10 ) + "V" + ( me.y + me.legendH + 10 ) ;
     this.paper.path( yAxisPath ).attr( me.axisAttr ).show();*/
    //圖例
    me.drawChart();
    //
    this.drawWidget();
}

Wee.wr.BarChart.prototype.drawChart = function(  )
{
    var me = this;
    var _count = me.data.length;
    var chartHeight = parseInt( me.height - me.legendH - 40 );
    var chartWidth = parseInt( me.width  - 40 );
    var _chartCount = me.axisYs.length;
    var colWidth = parseInt( chartWidth / _count ) ;
    var _chartWidth = parseInt( colWidth / _chartCount );
    //----------------------------------------------------------------------------------------------------------
    for( var i = 0; i < me.charts.length; i++ ){
        var _wd = me.charts[i];
        _wd.destory();
    }
    //----------------------------------------------------------------------------------------------------------
    if( _count == 0 )return;
    //----------------------------------------------------------------------------------------------------------
    var maxY = 0;
    for( var i = 0; i < _count; i++ ){
        var _data = me.data[ i ];
        for( var j = 0; j < _chartCount; j++ ){
            if( _data[me.axisYs[j]] &&  _data[me.axisYs[j]] > maxY )maxY =  _data[me.axisYs[j]];
        }
    }
    //----------------------------------------------------------------------------------------------------------
    //var _chartStart = this.x + 20;
    for( var k = 0; k < _count; k++ ){
        var _data = me.data[ k ];
        _chartStart = this.x + 20 + k * colWidth + _chartCount* me.colBorder/2;
        for( var m = 0; m < _chartCount; m++ ){
            var _chartAttr = me.axisYs[m];
            var _x = _chartStart + m * ( _chartWidth - me.colBorder ) ;
            var _height = parseInt( chartHeight * _data[ _chartAttr ] / maxY );
            var _y = this.y + this.height - 20 - _height;
            var _color = me.legends[_chartAttr]["color"];
            var _text = _data[_chartAttr];

            var _barChart = this.paper.rect( _x, _y, (_chartWidth - me.colBorder), (_height - 1 ) ).attr({
                "stroke": "white",
                "fill":_color
            }).show();
            me.charts.push( _barChart );
            var _barText = this.paper.text( _x + (_chartWidth - me.colBorder)/2, _y+5, _text).attr({
                "stroke": "yellow",
                "fill":"yellow"
            }).show();
            me.charts.push( _barText );
        }
        var _barLabel = this.paper.text( _chartStart + colWidth /2, this.y + this.height-10, _data["id"]).attr({
            "stroke": "yellow",
            "font-weight":"normal",
            "text-anchor":"middle",
            "fill":"yellow"
        }).show();
        me.charts.push( _barLabel );
    }
}

Wee.wr.BarChart.prototype.loadData = function( data )
{
    var me = this;
    me.data = data;
    me.drawChart();
}

/**
 *
 * @param paper
 * @param config
 * @constructor
 */
Wee.wr.BootDynamic = function( paper,config )
{
    var bootDynamicConfig = {
        borderWidth:0,
        fieldBorder:2,
        layout:'col',//col表示列，row表示行Label
        cols:3,
        labelAttr:{
            textAttr:{
                fill:"yellow",
                stroke:"yellow",
                "font-weight":"normal",
                "text-anchor":"middle"
            },
            backAttr:{
                fill:"#000080",
                "stroke-width":2,
                stroke: "white",
                "fill-opacity":1
            }
        },
        valueAttr:{
            textAttr:{
                fill:"white",
                stroke:"white",
                "font-weight":"normal",
                "text-anchor":"middle"
            },
            backAttr:{
                fill:"black",
                "stroke-width":2,
                stroke: "white",
                "fill-opacity":1
            }
        },
        titleAttr:{
            textRatio:0.2,
            textAttr:{
                fill:"yellow",
                stroke:"yellow",
                "font-weight":"normal",
                "text-anchor":"middle"
            },
            backAttr:{
                fill:"black",
                "stroke-width":2,
                stroke: "white",
                "fill-opacity":1
            }
        }
    };

    Wee.wr.BootDynamic.superclass.constructor.call( this, paper, Wee.apply({}, config, bootDynamicConfig ) );

    this.setAttr( Wee.apply( {}, this.backAttr, bootDynamicConfig.backAttr ) );
}

Wee.extend( Wee.wr.BootDynamic, Wee.wr.Widget );


Wee.wr.BootDynamic.prototype.drawAction = function()
{
    var me = this;
    var _x = this.x + this.borderWidth,
        _y = this.y + this.borderWidth,
        _width = this.width - 2*this.borderWidth,
        _height = this.height - 2*this.borderWidth;

    var colWidth = parseInt( ( _width - 240 ) / 7 );
    //----------------------------------------------------------------------------------------------------------
    var _title = new Wee.wr.TextLabel( this.paper, me.titleAttr );
    _title.attr( {
        x: _x,
        y: _y ,
        width: 50,
        height: _height
    });
    _title.setText( me.title );
    _title.drawAction();
    //----------------------------------------------------------------------------------------------------------
    var _label = new Wee.wr.TextLabel( this.paper, me.labelAttr );
    _label.attr( {
        x: _x + 50,
        y: _y ,
        width: colWidth,
        height: parseInt( _height / 2 )
    });
    _label.setText( "產能" );
    _label.drawAction();
    //----------------------------------------------------------------------------------------------------------
    this.capacity  = new Wee.wr.TextLabel( this.paper, me.valueAttr );
    this.capacity.attr( {
        x: _x + 50,
        y: _y + parseInt( _height / 2 ),
        width: colWidth,
        height: parseInt( _height / 2 )
    });
    this.capacity.drawAction();
    //----------------------------------------------------------------------------------------------------------
    this.progressPan = new Wee.wr.ProgressPan( this.paper,{
        x: _x + 50 + colWidth,
        y: _y,
        width:180,
        height:_height,
        imgBorder:2,
        per:0,
        backAttr:{
            fill: "black",
            stroke: "white",
            "stroke-width":2,
            cursor: "pointer",
            "fill-opacity":1
        }
    });
    this.progressPan.drawAction();
    //----------------------------------------------------------------------------------------------------------
    me.drawFields( _x + 230 + colWidth, _y, _width - 230 - colWidth  , _height);
    //----------------------------------------------------------------------------------------------------------
    this.drawWidget();
}

Wee.wr.BootDynamic.prototype.drawFields = function( _x, _y, _width, _height  )
{
    var me = this;
    var columnFieldsAttr = [
        {
            "name":"standardCT",
            "text":"",
            "label":"標準C/T(秒)",
            "valueAttr":{
                backAttr:{
                    fill:"black",
                    "stroke-width":2,
                    stroke: "white",
                    "fill-opacity":1
                }
            }
        },{
            "name":"activCT",
            "text":"",
            "label":"實際C/T(秒)",
            "valueAttr":{
                backAttr:{
                    fill:"black",
                    "stroke-width":2,
                    stroke: "white",
                    "fill-opacity":1
                }
            }
        },{
            "name":"diffCT",
            "text":"",
            "label":"差異CT",
            "valueAttr":{
                textAttr:{
                    fill:"#3300FF",
                    stroke:"#3300FF",
                    "font-weight":"normal",
                    "text-anchor":"middle"
                },
                backAttr:{
                    fill:"black",
                    "stroke-width":2,
                    stroke: "white",
                    "fill-opacity":1
                }
            }
        },{
            "name":"abolish",
            "text":"",
            "label":"不可重工數",
            "valueAttr":{
                textAttr:{
                    fill:"#FF0000",
                    stroke:"#FF0000",
                    "font-weight":"normal",
                    "text-anchor":"middle"
                },
                backAttr:{
                    fill:"black",
                    "stroke-width":2,
                    stroke: "white",
                    "fill-opacity":1
                }
            }
        },{
            "name":"oneYield",
            "text":"",
            "label":"一次良率",
            "valueAttr":{
                textAttr:{
                    fill:"#00FF00",
                    stroke:"#00FF00",
                    "font-weight":"normal",
                    "text-anchor":"middle"
                },
                backAttr:{
                    fill:"black",
                    "stroke-width":2,
                    stroke: "white",
                    "fill-opacity":1
                }
            }
        },{
            "name":"twoYield",
            "text":"",
            "label":"二次良率",
            "valueAttr":{
                textAttr:{
                    fill:"#FFFF00",
                    stroke:"#FFFF00",
                    "font-weight":"normal",
                    "text-anchor":"middle"
                },
                backAttr:{
                    fill:"black",
                    "stroke-width":2,
                    stroke: "white",
                    "fill-opacity":1
                }
            }
        }
    ];

    this.columnFields = new Wee.wr.ColumnField( this.paper, {
        x: _x,
        y: _y,
        width:_width,
        height:_height,
        layout:"row",
        fields:columnFieldsAttr,
        labelAttr:{
            textRatio:0.3,
            textAttr:{
                fill:"yellow",
                stroke:"yellow",
                "font-weight":"normal",
                "text-anchor":"start"
            },
            backAttr:{
                fill:"#000080",
                "stroke-width":2,
                stroke: "white",
                "fill-opacity":1
            }
        },
        valueAttr:{
            textAttr:{
                fill:"white",
                stroke:"white",
                "font-weight":"normal",
                "text-anchor":"middle"
            },
            backAttr:{
                fill:"black",
                "stroke-width":2,
                stroke: "#808080",
                "fill-opacity":1
            }
        },
    });

    this.columnFields.drawAction();
}


Wee.wr.BootDynamic.prototype.loadData = function( data )
{
    var me = this;
    //------------------------------------------------------------------------------------------------------------------
    this.capacity.setText( data.capacity );
    //------------------------------------------------------------------------------------------------------------------
    this.progressPan.setPer( data.per );
    //------------------------------------------------------------------------------------------------------------------
    this.columnFields.loadData( data );
    //------------------------------------------------------------------------------------------------------------------
}

/**
 *
 * @param paper
 * @param config
 * @constructor
 */
Wee.wr.ThreeDContainer = function( paper,config )
{
    var threeDContainer = {
        rows: 21,
        cols: 22,
        borderWidth: 1,
        //dataArea: ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K"],
        dataArea: [
            {
                "area":"A",
                "title":"A區-1夾",
                "qty":32,
                "prd":"1"
            },{
                "area":"B",
                "title":"B區-1夾",
                "qty":36,
                "prd":"1"
            },{
                "area":"C",
                "title":"C區-1夾",
                "qty":36,
                "prd":"1"
            },{
                "area":"D",
                "title":"D區-1夾",
                "qty":36,
                "prd":"1"
            },{
                "area":"E",
                "title":"E區-1夾",
                "qty":36,
                "prd":"1"
            },{
                "area":"F",
                "title":"F區-2夾",
                "qty":36,
                "prd":"2"
            },{
                "area":"G",
                "title":"G區-2夾",
                "qty":36,
                "prd":"2"
            },{
                "area":"H",
                "title":"H區-2夾",
                "qty":36,
                "prd":"2"
            },{
                "area":"I",
                "title":"I區-2夾",
                "qty":36,
                "prd":"2"
            },{
                "area":"J",
                "title":"J區-2夾",
                "qty":36,
                "prd":"2"
            },{
                "area":"K",
                "title":"K區-2夾",
                "qty":36,
                "prd":"2"
            }],
        equipAttr: {
            textRatio: 0.5,
            text: "0/0",
            textColor: "black",
            textAnchor: "",
            backColor: "#AAAAAA",
            borderWidth: 1,
            borderColor: "white"
        },
        /*titleAttr:{
            textRatio: 0.8,
            text: "0",
            textColor: "yellow",
            textAnchor: "",
            backColor: "#000080",
            borderWidth: 1,
            borderColor: "white"
        },*/
        titleAttr:{
            textRatio: 0.8,
            text: "0",
            textColor: "yellow",
            textAnchor: "",
            backColor: "#000080",
            borderWidth: 1,
            borderColor: "white"
        },
        title2Attr:{
            textRatio: 0.8,
            text: "0",
            textColor: "yellow",
            textAnchor: "",
            backColor: "#008080",
            borderWidth: 1,
            borderColor: "white"
        },
        eareAttr:{
            textRatio: 0.4,
            text: "0/0\n100%",
            textColor: "white",
            textAnchor: "",
            backColor: "black",
            borderWidth: 1,
            borderColor: "white"
        }
    };
    Wee.wr.ThreeDContainer.superclass.constructor.call( this, paper, Wee.apply({}, config, threeDContainer ) );

};

Wee.extend( Wee.wr.ThreeDContainer, Wee.wr.TableContainer );

Wee.wr.ThreeDContainer.prototype.initWidgets = function()
{
    var me = this;
    this.colWidth = parseInt(  this.width/this.cols );
    this.rowHeight = parseInt( this.height/this.rows );

    for( var i = 0; i < me.dataArea.length; i ++ ){
        var areaObj = me.dataArea[i];
        //--------------------------------------------------------------------------------------------------
        var _titleAttr = me.titleAttr;
        if(areaObj["prd"] == "2" )_titleAttr = me.title2Attr;
        _titleAttr.id = "LABLE_" + i;
        _titleAttr.col = 2*i + 1;
        _titleAttr.row = 1;
        _titleAttr.sizeX = 2;
        _titleAttr.sizeY = 1;
        _titleAttr.text = areaObj.title;
        var _title = new Wee.wr.TextLabel( this.paper, _titleAttr );
        this.addWidget( _title );
        //--------------------------------------------------------------------------------------------------
        var _areaAttr = me.eareAttr;
        _areaAttr.id = areaObj.area +"_Area";
        _areaAttr.col = 2*i + 1;
        _areaAttr.row = 2;
        _areaAttr.sizeX = 2;
        _areaAttr.sizeY = 2;
        var _area = new Wee.wr.TextLabel( this.paper, _areaAttr );
        //console.log( "Area ID:"+_area.id );
        this.addWidget( _area );
        //--------------------------------------------------------------------------------------------------
        /*for( var j = 1; j < 33; j++ ){
            var _col = 2 * i + 1;
            if( j > 16 )_col = _col + 1;
            //--------------------------------------------------------------------------------------------------
            var _row = j + 3;
            if( j > 16 )_row = 36 - j;
            //--------------------------------------------------------------------------------------------------
            var _attr = me.equipAttr;
            var strNum = j;
            if( j < 10 )strNum = "0" + j;
            _attr.id = "K" + areaObj.area + strNum;
            _attr.col = _col;
            _attr.row = _row;
            _attr.sizeX = 1;
            _attr.sizeY = 1;
            //--------------------------------------------------------------------------------------------------
            var _widget = new Wee.wr.TextLabel( this.paper, _attr );
            this.addWidget( _widget );
        } */
        for( var j = 0; j < areaObj.qty; j++ ){
            var _col = 2 * i + 2;
            if( j >=  ( areaObj.qty / 2 ) )_col = _col - 1;
            var _row;
            if( areaObj.qty == 32 ){
                _row = me.rows - 2 - j;
                if( j >=  ( areaObj.qty / 2 ) )_row = me.rows - areaObj.qty + j - 1;
            }else{
                _row = me.rows - j;
                if( j >=  ( areaObj.qty / 2 ) )_row = me.rows - areaObj.qty + j + 1;
            }

            var _attr = me.equipAttr;
            var strNum = j + 1;
            if( strNum < 10 )strNum = "0" + strNum;

            _attr.id = "K" + areaObj.area + strNum;
            _attr.col = _col;
            _attr.row = _row;
            _attr.sizeX = 1;
            _attr.sizeY = 1;
            //--------------------------------------------------------------------------------------------------
            var _widget = new Wee.wr.TextLabel( this.paper, _attr );
            this.addWidget( _widget );
        }
    }
};

Wee.wr.ThreeDContainer.prototype.equipReset = function(  )
{
    var me = this;
    for( var id in this.widgets ){
        //console.log( id + " split " + id.indexOf( "LABLE_" ) );
        if( id.indexOf( "LABLE_" )  > -1 )continue;

        var _widget = this.widgets[id];
        if( _widget ){
            if( id.indexOf( "_Area") > -1 ){
                _widget.setBackAttr({ "fill":"black"});
                _widget.setTextAttr({ "text":"0/0",fill:"white","stroke":"white"});
            }else{
                _widget.setBackAttr({ "fill":"#AAAAAA"});
                _widget.setTextAttr({ "text":"0/0",fill:"black","stroke":"black"});
            }
        }
    }
}

Wee.wr.ThreeDContainer.prototype.loadData = function( data )
{
    var me = this;
    //----------------------------------------------------------------------------------------------------------
    me.equipReset();
    //----------------------------------------------------------------------------------------------------------
    if( !data || data.length == 0 ){
        console.log( "ThreeDContainer -- load data:" +  data );
        return;
    }
    for( var i = 0; i < data.length; i++ ){
        var _obj = data[i];
        if( !_obj.id ){
            console.log( "ThreeDContainer -- Data does not contain id:" +  data );
            continue;
        }
        var _widget = me.getWidget( _obj["id"]);
        if( !_widget ){
            //console.log( "Container -- Widget is not exist!" +  _obj );
            continue;
        }

        if( _obj.checkQty && _obj.checkQty > 0 ){
            if( _obj.ncQty && _obj.ncQty != 0 ){
                _widget.setBackAttr({ "fill":"red"});
                _widget.setTextAttr({ "text": _obj.ncQty + "/" + _obj.checkQty,fill:"yellow","stroke":"yellow"});
            }else{
                _widget.setBackAttr({ "fill":"green"});
                _widget.setTextAttr({ "text":  "0/" + _obj.checkQty,fill:"yellow","stroke":"yellow"});
            }
        }

        if( _obj["id"].indexOf( "_Area" )  > -1 ){
            if( _obj.checkQty && _obj.checkQty > 0 ){

                if( _obj.ncQty && _obj.ncQty != 0 ){
                    var pctText = ( _obj.checkQty - _obj.ncQty ) * 100/_obj.checkQty;
                    _widget.setTextAttr({ "text": _obj.ncQty + "/" + _obj.checkQty + "\n"+  Math.round( pctText )+"%"});
                }else{
                    _widget.setTextAttr({ "text":  "0/" + _obj.checkQty+"\n100%"});
                }
            }else{
                _widget.setTextAttr({ "text":  "0/0\n100%"});
            }
        }
    }
}

/**
 *
 * @param paper
 * @param config
 * @constructor
 */
Wee.wr.EquipContainer = function( paper,config )
{
    var equipContainer = {
        rows: 20,
        cols: 22,
        borderWidth: 1,
        defaultValue:"",
        dataArea: [
            {
                "area":"A",
                "title":"A區-1夾",
                "qty":32,
                "prd":"1"
            },{
                "area":"B",
                "title":"B區-1夾",
                "qty":36,
                "prd":"1"
            },{
                "area":"C",
                "title":"C區-1夾",
                "qty":36,
                "prd":"1"
            },{
                "area":"D",
                "title":"D區-1夾",
                "qty":36,
                "prd":"1"
            },{
                "area":"E",
                "title":"E區-1夾",
                "qty":36,
                "prd":"1"
            },{
                "area":"F",
                "title":"F區-2夾",
                "qty":36,
                "prd":"2"
            },{
                "area":"G",
                "title":"G區-2夾",
                "qty":36,
                "prd":"2"
            },{
                "area":"H",
                "title":"H區-2夾",
                "qty":36,
                "prd":"2"
            },{
                "area":"I",
                "title":"I區-2夾",
                "qty":36,
                "prd":"2"
            },{
                "area":"J",
                "title":"J區-2夾",
                "qty":36,
                "prd":"2"
            },{
                "area":"K",
                "title":"K區-2夾",
                "qty":36,
                "prd":"2"
            }],
        //dataArea:["K","J","I","H","G","F","E","D","C","B", "A"],
        equipAttr: {
            textRatio: 0.5,
            text: "0",
            textColor: "black",
            textAnchor: "",
            backColor: "#F0F0F0",
            borderWidth: 1,
            borderColor: "white"
        },
        titleAttr:{
            textRatio: 0.4,
            text: "0",
            textColor: "yellow",
            textAnchor: "",
            backColor: "#000080",
            borderWidth: 1,
            borderColor: "white"
        },
        title2Attr:{
            textRatio: 0.4,
            text: "0",
            textColor: "yellow",
            textAnchor: "",
            backColor: "#008080",
            borderWidth: 1,
            borderColor: "white"
        },
        status:{
            "RUN":{
                "text":"執行",
                "color":"#66FF00"
            },
            "IDLE":{
                "text":"待料",
                "color":"yellow"
            },
            "DOWN":{
                "text":"故障",
                "color":"red"
            },
            "STOP":{
                "text":"停機",
                "color":"#F0F0F0"
            }
        }
    };
    Wee.wr.EquipContainer.superclass.constructor.call( this, paper, Wee.apply({}, config, equipContainer ) );
    //this.initWidgets();
};

Wee.extend( Wee.wr.EquipContainer, Wee.wr.TableContainer );

Wee.wr.EquipContainer.prototype.initWidgets = function()
{
    var me = this;
    this.colWidth = parseInt(  this.width/this.cols );
    this.rowHeight = parseInt( this.height/this.rows );

    for( var i = 0; i < me.dataArea.length; i ++ ){
        var areaObj = me.dataArea[i];
        var area = areaObj["area"];
        //--------------------------------------------------------------------------------------------------
        var _titleAttr = me.titleAttr;
        if(areaObj["prd"] == "2" )_titleAttr = me.title2Attr;
        _titleAttr.id = "LABLE_" + i;
        _titleAttr.col = 2*i + 1;
        _titleAttr.row = 19;
        _titleAttr.sizeX = 2;
        _titleAttr.sizeY = 2;
        _titleAttr.text = areaObj["title"];
        var _title = new Wee.wr.TextLabel( this.paper, _titleAttr );
        this.addWidget( _title );
        //--------------------------------------------------------------------------------------------------
        for( var j = 0; j < areaObj.qty; j++ ){
            var _col = 2 * i + 1;
            if( j >=  ( areaObj.qty / 2 ) )_col = _col + 1;
            var _row = j + 1;
            if( j >=  ( areaObj.qty / 2 ) )_row = areaObj.qty - j;
            var _attr = me.equipAttr;
            var strNum = areaObj.qty / 2 + j + 1;
            if( j >=  ( areaObj.qty / 2 ) )
                strNum = j - ( areaObj.qty / 2 ) + 1;
            if( strNum < 10 )strNum = "0" + strNum;
            _attr.id = "K" + area + strNum;
            _attr.col = _col;
            _attr.row = _row;
            _attr.sizeX = 1;
            _attr.sizeY = 1;
            if( me.defaultValue )
                _attr.text = me.defaultValue;
            else
                _attr.text = _attr.id;
            //--------------------------------------------------------------------------------------------------
            var _widget = new Wee.wr.TextLabel( this.paper, _attr );
            this.addWidget( _widget );
        }

        /*
        for( var j = 1; j < 37; j++ ){
            if( i == 0 && j > 32 )continue;
            var _col = 2 * i + 1;

            if( i == 0 ){
                if( j > 16 )_col = _col + 1;
            }else{
                if( j > 18 )_col = _col + 1;
            }
            //--------------------------------------------------------------------------------------------------
            //var _row = j + 2;
            var _row = j;
            if( i == 0 ){
                if( j > 16 )_row = 33 - j;
            }else{
                if( j > 18 )_row = 37 - j;
            }

            //--------------------------------------------------------------------------------------------------
            var _attr = me.equipAttr;
            var strNum = j;
            if( j < 10 )strNum = "0" + j;
            _attr.id = "K" + area + strNum;
            _attr.col = _col;
            _attr.row = _row;
            _attr.sizeX = 1;
            _attr.sizeY = 1;
            if( me.defaultValue )
                _attr.text = me.defaultValue;
            else
                _attr.text = _attr.id;
            //--------------------------------------------------------------------------------------------------
            var _widget = new Wee.wr.TextLabel( this.paper, _attr );
            this.addWidget( _widget );
        } */
    }
};

Wee.wr.EquipContainer.prototype.equipReset = function(  )
{
    var me = this;
    for( var id in this.widgets ){
        //console.log( id + " split " + id.indexOf( "LABLE_" ) );
        if( id.indexOf( "LABLE_" )  > -1 )continue;
        var _widget = this.widgets[id];
        if( _widget ){
            _widget.setBackAttr({ "fill": me.equipAttr.backColor });
            //_widget.setTextAttr({ "text":"0",fill:"black","stroke":"black"});
        }
    }
}

Wee.wr.EquipContainer.prototype.loadData = function( data, backColor )
{
    var me = this;
    //----------------------------------------------------------------------------------------------------------
    //me.equipReset();
    //----------------------------------------------------------------------------------------------------------
    if( !data || data.length == 0 ){
        console.log( "EquipContainer -- load data:" +  data );
        return;
    }
    for( var i = 0; i < data.length; i++ ){
        var _obj = data[i];
        if( !_obj.id ){
            console.log( "EquipContainer -- Data does not contain id:" +  data );
            continue;
        }
        var _widget = me.widgets[ _obj["id"] ];
        if( !_widget ){
            //console.log( "Container -- Widget is not exist!" +  _obj );
            continue;
        }
        if( _obj.status  ){
                me.changeWidgetStatus( _obj.status, _widget );
        }

        if( _obj.qty && _obj.qty > 0 ){
            if( backColor )
                _widget.setBackAttr({ "fill":backColor});
            else
                _widget.setBackAttr({ "fill":"red"});

            _widget.setTextAttr({ "text":_obj.qty,fill:"yellow","stroke":"yellow"});
        }
    }
}

Wee.wr.EquipContainer.prototype.changeWidgetStatus = function( sta, widget )
{
    var me = this;
    if( me.status[ sta ] ){
        var _sts = me.status[ sta ];
        widget.setBackAttr({ "fill" : _sts.color })
    }

}


/**
 *
 * @param paper
 * @param config
 * @constructor
 */
Wee.wr.EquipLabel = function( paper,config )
{
    var equipLabelConfig = {
        margin:10,
        logoColor:"",
        label:"NC308",
        labelRatio:.5,
        qtyRatio:.4,
        qty:45,
        logoHeight:20,
        highlight:{
            back:"#FFFF00",
            label:"black",
            qty:"red"
        },
        noHighlight:{
            back:"black",
            label:"white",
            qty:"red"
        },
        logoAttr:{
            fill:"green",
            stroke:"white"
        },
        labelAttr:{
            fill:"white",
            stroke:"white",
            "font-weight":"normal",
            "text-anchor":"start",
            "font-size":15
        },
        qtyAttr:{
            fill:"red",
            stroke:"red",
            "font-weight":"normal",
            "text-anchor":"end",
            "font-size":15
        },
        backAttr:{
            fill: "	red",
            stroke: "#C0C0C0",
            "stroke-width":0,
            cursor: "pointer",
            "fill-opacity":.001
        }
    };
    Wee.wr.EquipLabel.superclass.constructor.call( this, paper, Wee.apply({}, config, equipLabelConfig ) );

    this.initAttr();
    this.setAttr( Wee.apply({}, this.backAttr, equipLabelConfig.backAttr));
};
Wee.extend( Wee.wr.EquipLabel, Wee.wr.TextLabel );

Wee.wr.EquipLabel.prototype.initAttr = function()
{
    var me = this;
    if( me.logoColor ){
        me.logoAttr.fill = me.logoColor;
    }
}

Wee.wr.EquipLabel.prototype.drawAction = function()
{
    var me = this;
    var _width = this.width - 2 * me.margin;
    if( me.logoHeight > me.height )me.logoHeight = me.height;
    if( me.logoHeight > _width * 0.2 )me.logoHeight = _width * 0.2;

    if( me.labelRatio && me.labelRatio != 0 ){
        me.labelAttr["font-size"] = me.height*this.labelRatio
    }
    if( me.qtyRatio && me.qtyRatio != 0 ){
        me.qtyAttr["font-size"] = me.height*this.qtyRatio
    }
    //----------------------------------------------------------------------------------------------------------
    me.logoObj = this.paper.rect( me.x + me.margin, me.y + ( me.height - me.logoHeight )/2, me.logoHeight,me.logoHeight ).attr( me.logoAttr  );
    me.labelObj = this.paper.text( me.x + me.margin  + _width * 0.2 + 2, me.y + me.height / 2, me.label).attr( me.labelAttr );
    me.qtyObj = this.paper.text( me.x + me.width - me.margin, me.y + me.height / 2, me.qty).attr( me.qtyAttr );
    //----------------------------------------------------------------------------------------------------------
    me.logoObj.show();
    me.labelObj.show();
    me.qtyObj.show();
    //----------------------------------------------------------------------------------------------------------
    me.drawWidget();
}

Wee.wr.EquipLabel.prototype.remove = function()
{
    var me = this;
    me.logoObj.remove();
    me.labelObj.remove();
    me.qtyObj.remove();

    me.removeWidget();
}

Wee.wr.EquipLabel.prototype.highlightAction = function( blag )
{
    var me = this,_attr;
    if( blag )_attr = me.highlight;
    else _attr = me.noHighlight;
    me.labelObj.attr({ "stroke" : _attr.label,"fill" : _attr.label });
    me.qtyObj.attr({ stroke: _attr.qty, fill:_attr.qty });
    me.setAttr({fill: _attr.back,"fill-opacity":1});
}

/**
 *
 * @param paper
 * @param config
 * @constructor
 */
Wee.wr.GroupLablesContainer = function( paper,config )
{
    var groupLablesConfig = {
        rows: 5,
        cols: 1,
        nullText:"沒有相關項次",
        backAttr:{
            fill: "	black",
            stroke: "white",
            "stroke-width":2,
            cursor: "pointer",
            "fill-opacity":.8
        },
        logoColors:[
            "#FF0000","#FF44AA","#FF8800","#EE82EE","#FFA488"
        ],
        group:[
        ]
    };
    Wee.wr.GroupLablesContainer.superclass.constructor.call( this, paper, Wee.apply({}, config, groupLablesConfig ) );
    this.setAttr( Wee.apply({}, this.backAttr, groupLablesConfig.backAttr));
};

Wee.extend( Wee.wr.GroupLablesContainer, Wee.wr.TableContainer );

Wee.wr.GroupLablesContainer.prototype.initWidgets = function()
{
    var me = this;
    if( me.group.length == 0  ){
        var _widget = new Wee.wr.TextLabel( me.paper,{
            col:1,
            row:3,
            sizeX:1,
            sizeY:1,
            text:me.nullText
        });
        this.addWidget( _widget );
        return;
    }
    for( var i = 0; i < me.group.length; i++ ){
        var _data = me.group[i];
        var _attr = {};
        _attr.id = _data.id
        _attr.col = 1;
        _attr.row = i + 1 ;
        _attr.sizeX = 1;
        _attr.sizeY = 1;
        _attr.logoColor = me.logoColors[i];
        _attr.label = _data.ncCode||_data.id;
        _attr.qty = _data.qty;
        //--------------------------------------------------------------------------------------------------
        var _widget = new Wee.wr.EquipLabel( this.paper, _attr );
        this.addWidget( _widget );
    }
}

Wee.wr.GroupLablesContainer.prototype.getWidgetColor = function( id )
{
    var me = this;
    var _widget = me.widgets[ id ];
    if( !_widget)return "";
    return _widget["logoColor"];
}

Wee.wr.GroupLablesContainer.prototype.highlightWidget = function( id )
{
    var me = this;
    for( var key in me.widgets ){
        var _widget = me.widgets[ key ];
        if( _widget ){
            if( key == id ){
                _widget.highlightAction( true );
            }else{
                _widget.highlightAction( false );
            }
        }
    }

}

Wee.wr.GroupLablesContainer.prototype.loadData = function( data )
{
    var me = this;
    //----------------------------------------------------------------------------------------------------------
    me.removeAllWidget();
    //----------------------------------------------------------------------------------------------------------
    me.group = data;
    me.initWidgets();
    me.drawAction();
}

Wee.wr.GroupLablesContainer.prototype.resetLables = function( data )
{
    var me = this;
    me.removeAllWidget();
}


/**
 *
 * @param paper
 * @param config
 * @constructor
 */
Wee.wr.Top5NcContainer = function( paper,config )
{
    var top5NcConfig = {
        rows: 15,
        cols: 25,
        highlight:"#FFFF00",
        borderWidth: 1,
        NC_DATA:[],
        ncTimer:null,
        ncTigger:0,
        titleAttr:{
            xtype:"headingArea",
            col:1,
            row:1,
            sizeX:3,
            sizeY:5,
            titleHeigh:0.4,
            border:1,
            titleAttr:{
                textColor:"yellow", //字型顏色
                backColor:"#000080", //底色顏色
                text:"不良Top5",        //預設字
                textRatio:0.4,       //字型大小
                borderWidth:1,      //邊框尺寸
                borderColor:"white"//邊框顏色
            },
            dataShowAttr:{
                text:"",        //預設字
                textRatio:0.25
            }
        },
        allCheckAttr:{
            col: 1,
            row: 6,
            sizeX: 1,
            sizeY: 3,
            text:"全\n檢",
            id:"K-INSPECT",
            textRatio:.3,
            borderColor:"white",
            borderWidth:1,
            "textColor":"white",
            "backColor":"black"
        },
        blurAttr:{
            col: 2,
            row: 6,
            sizeX: 1,
            sizeY: 3,
            text:"去\n毛\n刺",
            id:"K-DEBUR",
            textRatio:.25,
            borderColor:"white",
            borderWidth:1,
            "textColor":"white",
            "backColor":"black"
        },
        fqcAttr:{
            col: 3,
            row: 6,
            sizeX: 1,
            sizeY: 3,
            text:"FQC",
            id:"K-FQC",
            textRatio:.25,
            borderColor:"white",
            borderWidth:1,
            transform:"r90",
            "textColor":"white",
            "backColor":"black"
        },
        ncLabelAttr:{
            col: 1,
            row: 9,
            sizeX: 3,
            sizeY: 1,
            text:"不良類別",
            textRatio:.6,
            textAnchor:"start",
            "textColor":"white"
        },
        ncListAttr:{
            col: 1,
            row: 10,
            sizeX: 3,
            sizeY: 6
        },
        cncListAttr:{
            col: 4,
            row: 1,
            sizeX:22,
            sizeY: 15,
            defaultValue:"0",
            equipAttr: {
                textRatio: 0.5,
                text: "0",
                textColor: "black",
                textAnchor: "",
                backColor: "green",
                borderWidth: 1,
                borderColor: "white"
            }
        }
    };
    Wee.wr.Top5NcContainer.superclass.constructor.call( this, paper, Wee.apply({}, config, top5NcConfig) );

    this.setAttr( Wee.apply({}, this.backAttr, top5NcConfig.backAttr));
};
Wee.extend( Wee.wr.Top5NcContainer, Wee.wr.TableContainer );

Wee.wr.Top5NcContainer.prototype.initWidgets = function()
{
    var me = this;
    me.title = new Wee.wr.HeadingArea( me.paper,me.titleAttr)
    this.addWidget( me.title );

    me.allCheck = new Wee.wr.TextLabel( me.paper,me.allCheckAttr)
    this.addWidget( me.allCheck );

    me.blur = new Wee.wr.TextLabel( me.paper,me.blurAttr )
    this.addWidget( me.blur );

    me.fqc = new Wee.wr.TextLabel( me.paper,me.fqcAttr )
    this.addWidget( me.fqc );

    me.ncLable = new Wee.wr.TextLabel( me.paper,me.ncLabelAttr )
    this.addWidget( me.ncLable );

    me.ncList = new Wee.wr.GroupLablesContainer( me.paper,me.ncListAttr )
    this.addWidget( me.ncList );

    me.cncList = new Wee.wr.EquipContainer( me.paper,me.cncListAttr )
    this.addWidget( me.cncList );
}


Wee.wr.Top5NcContainer.prototype.selectOperation = function( id )
{
    var me = this;
    if( id == "K-INSPECT" ){
        me.allCheck.setBackAttr({"fill":me.highlight,"fill-opacity":1 });
        me.allCheck.setTextAttr({"fill":"black","stroke":"black"});
        me.blur.setBackAttr({"fill":"black","fill-opacity":1 });
        me.blur.setTextAttr({"fill":"white","stroke":"white"});
        me.fqc.setBackAttr({"fill":"black","fill-opacity":1 });
        me.fqc.setTextAttr({"fill":"white","stroke":"white"});
    }else if( id == "K-DEBUR" ){
        me.blur.setBackAttr({"fill":me.highlight,"fill-opacity":1 });
        me.blur.setTextAttr({"fill":"black","stroke":"black"});
        me.allCheck.setBackAttr({"fill":"black","fill-opacity":1  });
        me.allCheck.setTextAttr({"fill":"white","stroke":"white"});
        me.fqc.setBackAttr({"fill":"black","fill-opacity":1 });
        me.fqc.setTextAttr({"fill":"white","stroke":"white"});
    }else if( id == "K-FQC" ){
        me.fqc.setBackAttr({"fill":me.highlight,"fill-opacity":1 });
        me.fqc.setTextAttr({"fill":"black","stroke":"black"});
        me.allCheck.setBackAttr({"fill":"black","fill-opacity":1  });
        me.allCheck.setTextAttr({"fill":"white","stroke":"white"});
        me.blur.setBackAttr({"fill":"black","fill-opacity":1 });
        me.blur.setTextAttr({"fill":"white","stroke":"white"});
    }else{
        me.allCheck.setBackAttr({"fill":"black","fill-opacity":1  });
        me.allCheck.setTextAttr({"fill":"white","stroke":"white"});
        me.blur.setBackAttr({"fill":"black","fill-opacity":1 });
        me.blur.setTextAttr({"fill":"white","stroke":"white"});
        me.fqc.setBackAttr({"fill":"black","fill-opacity":1 });
        me.fqc.setTextAttr({"fill":"white","stroke":"white"});
        return;
    }
}

Wee.wr.Top5NcContainer.prototype.drawNCTypes = function( ncCodes )
{
    var me = this;
    me.ncList.resetLables();
    me.ncList.loadData( ncCodes );
}

Wee.wr.Top5NcContainer.prototype.setNCData = function( operation, ncCode, ncDatas )
{
    var me = this;
    me.selectOperation( operation );
    me.ncList.highlightWidget( ncCode );
    me.cncList.equipReset();
    me.cncList.loadData( ncDatas );
}



Wee.wr.Top5NcContainer.prototype.loadNCTypes = function( id )
{
    var me = this;



    //----------------------------------------------------------------------------------------------------------
   /* $.get(wrUtil.serviceUrl + "/41-ncData.json",
        function( jsondata ){
            if( jsondata["header"]["code"] == 0 ){
                var _data = jsondata["value"];
                var _date = _data["date"] + "\n" + _data["time"];
                me.title.changeDate( _date );

                var _ncData = _data["top5NC"];
                me.ncList.loadData( _ncData );
                me.NC_DATA = _ncData;
                me.ncTigger = 0;
                me.ncLoop();
            }
        }, "json" );
        */

}



Wee.wr.Top5NcContainer.prototype.ncLoop = function(  )
{
    var me = this;
    var _length = me.NC_DATA.length;
    if( _length == 0 )return;
    var selectNc = me.NC_DATA[ me.ncTigger % _length  ];
    me.ncList.highlightWidget( selectNc["id"] );
    me.loadNCDetail( selectNc );
    me.ncTigger++;
}


Wee.wr.Top5NcContainer.prototype.loadNCDetail = function( selectNc )
{
    var me = this;
    var color = me.ncList.getWidgetColor( selectNc.id );
   /* $.get(wrUtil.serviceUrl + "/41-ncDetailData.json",
        function( jsondata ){
            if( jsondata["header"]["code"] == 0 ){
                var _data = jsondata["value"];

                me.cncList.loadData( _data, color  );
            }

        }, "json" ); */
}

/**
 * AGV小車異常
 * @param paper
 * @param config
 * @constructor
 *  2019-11-29 joe
 */
Wee.wr.AgvEquip = function( paper,config )
{
    var agvEquipConfig = {
        showQty:2,//1 or 2
        titleRatio:0.2, //標題所點比例
        lightRatio:0.15,//指示燈所佔比例
        iconRatio:0.5,
        titleAttr:{
            textAttr:{
                fill:"#FFD700",
                stroke:"#FFD700",
                "font-weight":"normal",
                "text-anchor":"middle"
            },
            backAttr:{
                fill:"#000080",
                "stroke-width":1,
                stroke: "white",
                "fill-opacity":.8
            },
            text:"1號車執行狀態"
        },
        labelAttr:{
            fill:"#FFD700",
            stroke:"#FFD700",
            "font-weight":"normal",
            "text-anchor":"middle"
        },
        valueAttr:{
            textAttr:{
                fill:"#FFD700",
                stroke:"white",
                "font-size":30,
                "font-weight":"normal",
                "text-anchor":"middle"
            },
            backAttr:{
                fill:"black",
                "stroke-width":1,
                stroke: "white",
                "fill-opacity":.8
            },
            text:""
        },
        lightAttr:{
            layout:"col",
            backAttr:{
                fill: "	#505050",
                stroke: "white",
                "stroke-width":1,
                cursor: "pointer",
                "fill-opacity":1
            }
        },
        iconAttr:{
            src:"../images/agv.png"
        },
        backAttr:{
            fill: "#808080",
            stroke: "#FFFFFF",
            "stroke-width":3,
            cursor: "pointer",
            "fill-opacity":.8
        },
        tagTextAttr:{
            fill:"#FFD700",
            stroke:"#FFD700",
            "font-weight":"normal",
            "font-size":30,
            "text-anchor":"middle"
        },
        tagMarkAttr:{
            //fill:"#FFFFFF",
            stroke:"#FFD700",
            "stroke-width":4
        },
        tagText:"1",
        maskAttr:{
            fill:"#FF0000",
            "fill-opacity":0
        },
        firstLable:"目標位置",
        secondLable:"現在位置",
        thirdLable:"停留時間",
        block:0,
        maskFlag:1,
        errorAudio:null

    };
    Wee.wr.AgvEquip.superclass.constructor.call( this, paper, Wee.apply({}, config, agvEquipConfig ) );
    this.title = new Wee.wr.TextLabel( this.paper, Wee.apply( {}, this.titleAttr, agvEquipConfig.titleAttr ));
    this.firstValue = new Wee.wr.TextLabel( this.paper,  Wee.apply( {}, this.valueAttr, agvEquipConfig.valueAttr ));
    this.secondValue = new Wee.wr.TextLabel( this.paper,  Wee.apply( {}, this.valueAttr, agvEquipConfig.valueAttr ));
    this.thirdValue = new Wee.wr.TextLabel( this.paper,  Wee.apply( {}, this.valueAttr, agvEquipConfig.valueAttr ));
    this.Light = new Wee.wr.SignaLamp( this.paper, Wee.apply( {}, this.lightAttr, agvEquipConfig.lightAttr ));
    this.icon = new Wee.wr.ImageIcon( this.paper, Wee.apply( {}, this.iconAttr, agvEquipConfig.iconAttr ) );
    this.maskObj = this.paper.rect( 0,0,0,0 ).attr( this.maskAttr ).hide();
    this.setAttr( Wee.apply( {}, this.backAttr , agvEquipConfig.backAttr ) );
};
Wee.extend( Wee.wr.AgvEquip, Wee.wr.Widget );

Wee.wr.AgvEquip.prototype.drawAction = function()
{
    this.title.attr( { x:this.x, y:this.y, width:this.width,height:this.height * this.titleRatio } );
    //----------------------------------------------------------------------------------------------------------
    var lWidth = this.width * this.lightRatio;
    var lHeight = this.height * ( 1 - this.titleRatio );
    this.Light.attr( { x:this.x, y:this.y + this.height * this.titleRatio , width: lWidth , height:lHeight } );
    //----------------------------------------------------------------------------------------------------------
    var iconX = this.x + this.width*this.lightRatio;
    var iconY = this.y + this.height * this.titleRatio;
    var iconW = this.width * this.iconRatio;
    var iconH = this.height * ( 1 - this.titleRatio );
    this.icon.attr( { x: iconX + iconW * 0.05, y: iconY + iconH*0.05 , width: iconW * 0.9 , height: iconH * 0.9} );
    this.paper.text( iconX + iconW * 0.6,iconY + iconH * 0.6,this.tagText ).attr( this.tagTextAttr ).show();
    this.paper.circle( iconX + iconW * 0.6, iconY + iconH * 0.6 ,iconH * 0.15 ).attr( this.tagMarkAttr ).show();
    //----------------------------------------------------------------------------------------------------------
    var tHeight = this.height * ( 1 - this.titleRatio );
    var tY = this.y + this.height * this.titleRatio;
    var tX = this.x + this.width*( this.lightRatio + this.iconRatio + 0.05 );
    var tWidth = this.width * ( 1 - this.lightRatio - this.iconRatio - 0.1 );
    var _tSpace = Math.trunc( tHeight / 15 );
    //----------------------------------------------------------------------------------------------------------
    this.paper.text( tX + tWidth/2 ,tY + _tSpace,this.firstLable ).attr( this.labelAttr ).show();
    this.firstValue.attr( { x : tX, y : tY + 2*_tSpace, width : tWidth, height : 3*_tSpace  } );
    this.paper.text( tX + tWidth/2, tY + 6 * _tSpace, this.secondLable ).attr( this.labelAttr ).show();
    this.secondValue.attr( { x:tX, y: tY + 7*_tSpace, width:tWidth, height: 3*_tSpace  } );
    this.paper.text( tX + tWidth/2,tY + 11 * _tSpace,this.thirdLable ).attr( this.labelAttr ).show();
    this.thirdValue.attr( { x:tX, y: tY + 12*_tSpace, width: tWidth, height: 3*_tSpace  } );
    //----------------------------------------------------------------------------------------------------------
    this.title.drawAction();
    this.Light.drawAction();
    this.icon.drawAction();
    this.firstValue.drawAction();
    this.secondValue.drawAction();
    this.thirdValue.drawAction();

    this.maskObj.toFront();
    this.maskObj.attr( { x:this.x, y:this.y, width:this.width, height: this.height }).show();
    this.drawWidget();
}

Wee.wr.AgvEquip.prototype.flicker = function(  )
{
    var me = this;
    if( me.block == "1" ){
        if( me.maskFlag == 1 ){
            this.maskObj.attr({"fill-opacity":0});
            me.maskFlag = "0";
        }else{
            this.maskObj.attr({"fill-opacity":0.7});
            me.maskFlag = "1";
        }
    }else{
        this.maskObj.attr({"fill-opacity":0});
    }
}

Wee.wr.AgvEquip.prototype.loadData = function( data )
{
    var me = this;

    if( data.status ){
        var _status = data.status.toLowerCase();
        if( _status  == "run" )
            me.Light.setValue( "green" );
        else if( _status  == "idle" )
            me.Light.setValue( "yellow" );
        else if( _status  == "stop" )
            me.Light.setValue( "red" );
        else{
            me.Light.setValue( "break" );
            console.log( "SignaEquipment中LoadData" + data + "，狀態資訊不正確！" );
        }
    }else{
        me.Light.setValue( "break" );
    }

    if( data.block ){
        me.block = data.block;
        if( me.block == "1" ){
            me.taskId = setInterval( function(){
                if( me.maskFlag == 1 ){
                    me.maskObj.attr({"fill-opacity":0});
                    me.maskFlag = "0";
                }else{
                    me.maskObj.attr({"fill-opacity":0.7});
                    me.maskFlag = "1";
                }
                me.errorAudio.play();
            },1000 );
            this.thirdValue.setTextAttr( { fill:"#FF0000",stroke:"#FF0000"});
        }else{
            if( me.taskId ){
                this.maskObj.attr({ "fill-opacity":0 });
                clearInterval( me.taskId  );
            }
            this.thirdValue.setTextAttr( { fill:"#33FF00",stroke:"#33FF00"});
        }
    }

    this.firstValue.setText( data.target );
    this.secondValue.setText( data.nowPosition );
    this.thirdValue.setText( data.stopTime );
};


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
            textColor:"#FFFF00",
            borderWidth:2,
            //backColor:'90-#B0B0B0-#1E1E1E:50-#B0B0B0',
            backColor:'90-#0A9FAB-#17689F:50-#0A9FAB'
        },
        dclPointAttr:{
            fill: "#FFFF00",
            stroke: "#FFFF00",
            "stroke-width":1,
            "fill-opacity":1
        },
        prcntSignAttr:{
            fill:"#FFFFFF",
            stroke:"#FFFFFF",
            "font-size":30,
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
        _spaceW = Math.trunc( _width / 7 ), _r = 0.2*_spaceW;
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

//--------------------------------------------------------------------------------------------------------------
/**
 * y計劃達成率
 * @param paper
 * @param config
 * @constructor
 * 2019-12-1 joe
 */
Wee.wr.PlanToAchieve = function( paper,config )
{
    var defaultPlanToAchieve = {
        title:"今日計劃達成率",
        titleAttr:{
            fill:"white",
            stroke:"white",
            "font-weight":"normal",
            "font-size":25,
            "text-anchor":"middle"
        },
        percentAttr:{},
        labelAttr:{
            textRatio:0.4,
            textColor:"#FFFFFF"
        },
        planValueAttr:{
            textRatio:0.8,
            textColor:"#00FFFF",
            textAnchor:"start"
        },
        compValueAttr:{
            textRatio:0.8,
            textColor:"#77FF00",
            textAnchor:"start"
        },
        backAttr:{
            fill: "#2828FF",
            stroke: "#2828FF",
            "stroke-width":0,
            cursor: "pointer",
            "fill-opacity":.001
        }
    };

    Wee.wr.PlanToAchieve.superclass.constructor.call( this, paper, Wee.apply({}, config, defaultPlanToAchieve ) );
    this.initAttr();
    this.titleObj = this.paper.text( 0,0,this.title ).attr( Wee.apply({}, this.titleAttr, defaultPlanToAchieve.titleAttr)).hide();
    this.percentObj = new Wee.wr.Percentage( this.paper,  Wee.apply({}, this.percentAttr, defaultPlanToAchieve.percentAttr  ));
    this.planLable = new Wee.wr.TextLabel( this.paper, Wee.apply({text: "預計產出" }, this.labelAttr, defaultPlanToAchieve.labelAttr  ) );
    this.planValue = new Wee.wr.TextLabel( this.paper, Wee.apply({text: "0" }, this.planValueAttr, defaultPlanToAchieve.planValueAttr  ) );
    this.compLable = new Wee.wr.TextLabel( this.paper, Wee.apply({text: "實際產出"}, this.labelAttr, defaultPlanToAchieve.labelAttr  ) );
    this.compValue = new Wee.wr.TextLabel( this.paper, Wee.apply({text: "0" }, this.compValueAttr, defaultPlanToAchieve.compValueAttr  ) );
    this.setAttr( Wee.apply({}, this.backAttr, defaultPlanToAchieve.backAttr));
};
Wee.extend( Wee.wr.PlanToAchieve, Wee.wr.Widget );

Wee.wr.PlanToAchieve.prototype.initAttr = function()
{
    if( this.borderColor ){
        this.backAttr["borderColor"] = this.borderColor;
    }
    if( this.borderStroke ){
        this.backAttr["borderStroke"] = this.borderStroke;
    }
    if( this.angle ){
        this.backAttr["angle"] = this.angle;
    }
    if( this.angleLength ){
        this.backAttr["angleLength"] = this.angleLength;
    }

}

Wee.wr.PlanToAchieve.prototype.drawAction = function()
{
    var me = this, _X = this.x, _Y = this.y, _W = this.width;
    var _tSpace = Math.trunc( me.height / 8 );
    //----------------------------------------------------------------------------------------------------------
    me.titleObj.attr({x: _X + _W / 2, y: _Y + 0.75 * _tSpace, text: me.title }).show();
    //----------------------------------------------------------------------------------------------------------
    this.percentObj.attr({x: _X + 5, y: _Y + 1.6 * _tSpace, width: _W - 5, height: 3.5* _tSpace });
    this.percentObj.drawAction();
    //----------------------------------------------------------------------------------------------------------
    this.planLable.attr({x: _X, y: _Y + 5.6 * _tSpace, width: _W/ 2, height:1.2 * _tSpace});
    this.planValue.attr({x: _X + _W/ 2, y: _Y + 5.6 * _tSpace, width: _W/ 2, height:1.2 * _tSpace });
    this.compLable.attr({x: _X, y: _Y + 6.8 * _tSpace, width: _W/ 2, height:1.2 * _tSpace });
    this.compValue.attr({x: _X + _W/ 2, y: _Y + 6.8 * _tSpace, width: _W/ 2, height:1.2 * _tSpace });
    this.planLable.drawAction();
    this.planValue.drawAction();
    this.compLable.drawAction();
    this.compValue.drawAction();

    this.drawWidget();
}

Wee.wr.PlanToAchieve.prototype.loadData = function( data )
{
    var me = this;
    //----------------------------------------------------------------------------------------------------------
    this.planValue.setText( data.planNum ? data.planNum : 0 );
    this.compValue.setText( data.compNum ? data.compNum : 0 );
    //----------------------------------------------------------------------------------------------------------
    if( !data.planNum || data.planNum == 0 )
        this.percentObj.loadData( 0 );
    else
        this.percentObj.loadData( data.compNum/ data.planNum );
}


/**
 *
 *
 * */
Wee.wr.GridList = function( paper,config )
{
    var defGridList = {
        borderColor:"#2828FF",
        borderStroke:1,
        angle:1,
        headers:[
            {
                title:"工單",
                id:"order",
                col:0.4
            },{
                title:"計劃",
                id:"plan",
                col:0.2
            },{
                title:"完成",
                id:"compl",
                col:0.2
            },{
                title:"完成率",
                id:"rate",
                col:0.2
            }
        ],
        headerHeight:24,
        rowHeight:16,
        headerAttr:{
            fill:"white",
            stroke:"white",
            "font-weight":"normal",
            "font-size":14,
            "text-anchor":"middle"
        },
        rowAttr:{
            fill:"white",
            stroke:"white",
            "font-weight":"normal",
            "font-size":12,
            "text-anchor":"middle"
        },
        doneAttr:{
            fill:"#00DD00",
            stroke:"#00DD00",
            "font-weight":"normal",
            "font-size":12,
            "text-anchor":"middle"
        },
        todoAttr:{
            fill:"#0066FF",
            stroke:"#0066FF",
            "font-weight":"normal",
            "font-size":12,
            "text-anchor":"middle"
        },
        doingAttr:{
            fill:"#FFFF33",
            stroke:"#FFFF33",
            "font-weight":"normal",
            "font-size":13,
            "text-anchor":"middle"
        },
        dataFilds:[],
        objHeader:{},
        data:[]
    };

    Wee.wr.GridList.superclass.constructor.call( this, paper, Wee.apply({}, config, defGridList ) );

    this.setAttr( Wee.apply({}, this.backAttr, defGridList.backAttr));

}
Wee.extend( Wee.wr.GridList, Wee.wr.Widget );

Wee.wr.GridList.prototype.drawAction = function()
{
    var me = this;
    //  Draw Header -------------------------------------------------------------------------------------------
    var _x = this.x, _y = this.y + this.headerHeight / 2;
    me.objHeader = {};
    for( var i = 0; i < me.headers.length; i++ )
    {
        var colHead = me.headers[i];
        var _width = me.width * colHead.col;
        this.paper.text( _x + _width / 2, _y, colHead.title).attr( me.headerAttr).show();
        me.objHeader[colHead.id] = _x + _width / 2;
        _x = _x + _width;
    }
    //----------------------------------------------------------------------------------------------------------
    me.drawData();
    //----------------------------------------------------------------------------------------------------------
    this.drawWidget();
}

Wee.wr.GridList.prototype.drawData = function()
{
    var me = this;
    var _y = this.y + this.headerHeight;
    var _rowAttr = me.rowAttr;
    for( var i = 0; i < me.data.length; i++ )
    {
        _y = _y + me.rowHeight / 2;
        var _row = me.data[i];
        if( _row["rate"] >= 1 ){
            _rowAttr = me.doneAttr;
        }else if( 0 == _row["rate"]  ){
            _rowAttr = me.todoAttr;
        }else{
            _rowAttr = me.doingAttr;
        }
        for( var _rec in _row){
            var _val = _row[_rec];
            if( _rec == "rate" ){
                _val = _val * 100 + "%";
            }
            var _record = this.paper.text( me.objHeader[ _rec ] , _y, _val ).attr( _rowAttr );
            me.dataFilds.push( _record );
            _record .show();
        }
        //------------------------------------------------------------------------------------------------------
        _y = _y + me.rowHeight;
        if( _y > (this.y + this.height ) ){
            break;
        }
    }
}

Wee.wr.GridList.prototype.loadData = function( data )
{
    var me = this;
    //----------------------------------------------------------------------------------------------------------
    for( var i = 0; i < me.dataFilds; i++ ){
        var _field = me.dataFilds[i];
        _field.remove();
    }
    //----------------------------------------------------------------------------------------------------------
    me.data = data;
    //----------------------------------------------------------------------------------------------------------
    me.drawData();
}


Wee.wr.LightEquip = function( paper,config )
{
    var defLightEquip = {
        layout:"row",
        lightRate:0.3,
        maskFlag:0,
        defLightAttr:{
            fill:"#BEBEBE",
            "fill-opacity":.5
        },
        redLightAttr:{
            fill:"#FF0000",
            "fill-opacity": 1
        },
        greenLightAttr:{
            fill: "	#00FF00",
            "fill-opacity": 1
        },
        yellowLightAttr:{
            fill: "#FFFF00",
            "fill-opacity": 1
        },
        lightBackAttr:{
            fill: "#000079",
            stroke: "#000079",
            "stroke-width":1
        },
        iconAttr:{
            src:"../images/agv.png"
        },
        backAttr:{
            fill: "	#000000",
            stroke: "#2828FF",
            "stroke-width":1,
            cursor: "pointer",
            "fill-opacity":.8
        },
        maskAttr:{
            fill: "	#ADADAD",
            "fill-opacity":.7
        }
    };
    Wee.wr.LightEquip.superclass.constructor.call( this, paper, Wee.apply({}, config, defLightEquip ) );

    this.iconObj = new Wee.wr.ImageIcon( this.paper, Wee.apply( {}, this.iconAttr, defLightEquip.iconAttr ) );

    this.setAttr( Wee.apply({}, this.backAttr, defLightEquip.backAttr));
};
Wee.extend( Wee.wr.LightEquip, Wee.wr.Widget );

Wee.wr.LightEquip.prototype.drawAction = function()
{
    var me = this;
    //----------------------------------------------------------------------------------------------------------
    if( me.layout == "row" ){
        var _lightWidth = this.width * this.lightRate,_lightR;
        if( _lightWidth > this.height ){
            _lightR = ( this.height - 4 ) / 2;
            _lightWidth = this.height;
        }else{
            _lightR = ( _lightWidth - 4 ) / 2;
        }
        //------------------------------------------------------------------------------------------------------
        this.lightBack = this.paper.rect( this.x + 1, this.y + 1, _lightWidth - 2, this.height - 2 ).attr( this.lightBackAttr );
        this.lightBack.show();

        this.light = this.paper.circle( this.x + _lightWidth/2,this.y + this.height/2, _lightR ).attr( this.maskFlag == 1 ? this.defLightAttr : this.greenLightAttr );
        this.light.show()
        //------------------------------------------------------------------------------------------------------
        this.iconObj.attr( { x: this.x + _lightWidth, y: this.y, width:this.width * ( 1 - this.lightRate ),height: this.height });
        this.iconObj.drawAction();
    }else{
        var _lightHeight = this.height * this.lightRate,_ligthR;
        if( _lightHeight > this.width ){
            _ligthR = ( this.width - 4 )/2;
            _lightHeight = this.width;
        }else{
            _ligthR = ( _lightHeight - 4 )/2;
        }
        //------------------------------------------------------------------------------------------------------
        this.lightBack = this.paper.rect( this.x+1, this.y+1, this.width-2, _lightHeight-2 ).attr( this.lightBackAttr );
        this.lightBack.show();

        this.light = this.paper.circle( this.x+this.width/2, this.y+_lightHeight/2, _ligthR ).attr( this.maskFlag == 1 ? this.defLightAttr : this.greenLightAttr  );
        this.light.show();
        //------------------------------------------------------------------------------------------------------
        this.iconObj.attr( { x: this.x, y: this.y + _lightHeight, width:this.width, height: this.height * ( 1 - this.lightRate ) });
        this.iconObj.drawAction();
    }
    //----------------------------------------------------------------------------------------------------------
    this.drawWidget();
    //----------------------------------------------------------------------------------------------------------
    if( this.maskFlag == 1 ){
        this.setAttr( this.defLightAttr );
        this.toFront();
    }
}

Wee.wr.LightEquip.prototype.loadData = function( status )
{
    var me = this;
    if( status == "run" ){
        me.light.attr( me.greenLightAttr );
    }else if( status == "idle" ){
        me.light.attr( me.yellowLightAttr );
    }else if( status == "down" ){
        me.light.attr( me.redLightAttr );
    }else if( status == "stop" ){
        me.light.attr( me.defLightAttr );
    }else{
        me.light.attr( me.defLightAttr );
    }
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
    if(status)status = status.toLowerCase();
    if( status == "run" ){
        this.setAttr( { fill: this.runColor });
    }else if( status == "down" ){
        this.setAttr( { fill: this.stopColor });
    }else if( status == "idle" ){
        this.setAttr( { fill: this.holdColor });
    }else {
        this.setAttr( { fill: this.deltColor });
    }
}


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
            fill: "black",
            stroke: "#A9A9A9",
            "stroke-width":1,
            cursor: "pointer",
            "fill-opacity":1
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

/**
 * 數字顯示器
 * @param paper
 * @param config
 * @constructor
 */
Wee.wr.ShowNumber = function( paper,config )
{
    var showNumberConfig = {
        numSpace:10,
        numBorderColor:"blue",
        numBackColor:"",
        numColor: "",
        digit:6,
        left: 0,
        right: 0,
        top: 0,
        bottom:0,
        unit:"",
        items:[],
        unitColor:"#1A94E6",
        unitSize: 20
    };
    Wee.wr.ShowNumber.superclass.constructor.call( this, paper, Wee.apply({}, config, showNumberConfig ) );
}

Wee.extend( Wee.wr.ShowNumber, Wee.wr.Widget );

Wee.wr.ShowNumber.prototype.drawAction = function()
{
    if( this.digit == 0 || this.digit < 0  )return;
    var numWidth, numHeight;
    if( this.unit != '' )
        numWidth = ( this.width - this.left - this.right ) / this.digit;
    else
        numWidth = ( this.width - this.left - this.right ) / ( this.digit + 1 );
    numHeight = this.height - this.top - this.bottom;
    //----------------------------------------------------------------------------------------------------------
    this.items = [];
    for( var i = 0; i < this.digit; i++ ) {
        var _text = new Wee.wr.TextLabel( this.paper, {
            x: this.x + this.left + i * numWidth,
            y: this.y + this.top,
            width: numWidth - this.numSpace/2,
            height: numHeight,
            borderWidth: 2,
            text: "",
            backColor: this.numBackColor,
            textColor: this.numColor,
            borderColor: this.numBorderColor
        })
        this.items.push( _text );
        _text.drawAction();
    }
    //----------------------------------------------------------------------------------------------------------
    if( this.unit != '' ){
        this.paper.text( this.x + this.left + this.digit * numWidth, this.y + this.top + numHeight / 2, this.unit  ).attr( {
            fill:"#1A94E6",
            stroke:this.unitColor,
            "font-weight":"normal",
            "font-size" : this.unitSize,
            "text-anchor":"start"
        } ).show();
    }
    //----------------------------------------------------------------------------------------------------------
    this.drawWidget();
}

Wee.wr.ShowNumber.prototype.loadData = function( value )
{
    var me = this, itemLength = this.items.length;;
    for( var i = 0; i < itemLength; i++ ){
        var _item = this.items[i];
        _item.setText( "" );
    }
    value = value + "";
    var strLength = value.length;
    if( strLength > this.digit ){
        console.log( "數據過大,無法呈現!" )
        return;
    }
    for( var i = 0; i < strLength; i++ ){
        var _num = value.substr( strLength - 1 - i , 1 );
        var _item = this.items[ itemLength - 1 - i ];
        _item.setText( _num );
    }
}
