jQuery.sap.declare("util.Number");

util.NumberUtil = {
    toFixed: function (number, d) {
        var s = number + "";
        if( !d ) d = 0;
        if(s.indexOf(".")==-1) s+=".";
        s+=new Array(d+1).join("0");
        if(new RegExp("^(-|\\+)?(\\d+(\\.\\d{0,"+(d+1)+"})?)\\d*$").test(s)){
            var s="0"+RegExp.$2,pm=RegExp.$1,a=RegExp.$3.length,b=true;
            if(a==d+2){
                a=s.match(/\d/g);
                if(parseInt(a[a.length-1])>4){
                    for(var i=a.length-2;i>=0;i--){
                        a[i]=parseInt(a[i])+1;
                        if(a[i]==10){
                            a[i]=0;
                            b=i!=1;
                        }else break;
                    }
                }
                s=a.join("").replace(new RegExp("(\\d+)(\\d{"+d+"})\\d$"),"$1.$2");

            }if(b)s=s.substr(1);
            return (pm+s).replace(/\.$/,"");
        }return number+"";
    },
    /**
     * 加法函式
     * @param arg1
     * @param arg2
     * @returns {number}
     */
    add: function(arg1, arg2) {
        var r1, r2, m;
        try {
            r1 = arg1.toString().split(".")[1].length;
        } catch(e) {
            r1 = 0;
        }
        try {
            r2 = arg2.toString().split(".")[1].length;
        } catch(e) {
            r2 = 0;
        }
        m = Math.pow(10, Math.max(r1, r2));
        return (arg1 * m + arg2 * m) / m;
    },
    /**
     * 減法函式
     * @param arg1
     * @param arg2
     * @returns {number}
     */
    sub: function(arg1, arg2) {
        var r1, r2, m, n;
        try {
            r1 = arg1.toString().split(".")[1].length;
        } catch(e) {
            r1 = 0;
        }
        try {
            r2 = arg2.toString().split(".")[1].length;
        } catch(e) {
            r2 = 0;
        }
        m = Math.pow(10, Math.max(r1, r2));
        //last modify by deeka
        //動態控制精度長度
        n = (r1 >= r2) ? r1: r2;
        return ((arg1 * m - arg2 * m) / m).toFixed(n);
    },
    /**
     * 乘法函式
     * @param arg1
     * @param arg2
     * @returns {number}
     */
    mul: function(arg1, arg2) {
        var m = 0,
            s1 = arg1.toString(),
            s2 = arg2.toString();
        try {
            m += s1.split(".")[1].length;
        } catch(e) {}
        try {
            m += s2.split(".")[1].length;
        } catch(e) {}
        return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m);
    },
    /**
     * 除法函式
     * @param arg1
     * @param arg2
     * @returns {number}
     */
    div: function(arg1, arg2) {
        var t1 = 0,
            t2 = 0,
            r1, r2;
        try {
            t1 = arg1.toString().split(".")[1].length;
        } catch(e) {}
        try {
            t2 = arg2.toString().split(".")[1].length;
        } catch(e) {}
        with(Math) {
            r1 = Number(arg1.toString().replace(".", ""));
            r2 = Number(arg2.toString().replace(".", ""));
            return (r1 / r2) * pow(10, t2 - t1);
        }
    }
};