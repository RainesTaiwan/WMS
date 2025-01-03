Date.prototype.toDateTimeString = function () {
    var month = this.getMonth() + 1;
    var date = this.getDate();
    var hour = this.getHours();
    var minute = this.getMinutes();
    var second = this.getSeconds();
    var ms = this.getMilliseconds();

    var temp = this.getFullYear();
    temp += ((month < 10) ? '-0' : '-') + month;
    temp += ((date < 10) ? '-0' : '-') + date;
    temp += ((hour < 10) ? ' 0' : ' ') + hour;
    temp += ((minute < 10) ? ':0' : ':') + minute;
    temp += ((second < 10) ? ':0' : ':') + second;
    if(ms < 10) {
        temp += '.00' + ms;
    } else if(ms < 100) {
        temp += '.0' + ms;
    } else {
        temp += '.' + ms;
    }
    return temp;
};

Date.prototype.toYMD = function() {
    var month = this.getMonth() + 1;
    var date = this.getDate();

    var temp = this.getFullYear();
    temp += (month < 10) ? '0' + month : month;
    temp += (date < 10) ? '0' + date : date;
    return temp;
} 

Date.prototype.secondsOfDay = function() {
    var hour = this.getHours();
    var minute = this.getMinutes();
    var second = this.getSeconds();
    return 3600 * hour + 60 * minute + second;    
}

Date.prototype.add = function (offset) {
    if(!offset) {
        return this;
    }

    var value = offset.substr(0, offset.length - 1);
    if(value == 0) {
        return this;
    }

    var unit = offset.substr(offset.length - 1);
    if(unit === "d") {
        return new Date(this.valueOf() + value * 86400000);
    } else if(unit === "h") {
        return new Date(this.valueOf() + value * 3600000);
    } else if(unit === "m") {
        return new Date(this.valueOf() + value * 60000);
    } else if(unit === "s") {
        return new Date(this.valueOf() + value * 1000);
    }

    return this;
};
