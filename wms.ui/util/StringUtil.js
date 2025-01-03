jQuery.sap.declare("util.StringUtil");

util.StringUtil = {

    /**
     * helper function to see if input string is blank
     *
     * @param str {String} to check
     * @return true if string is blank (undefined, null or only spaces), else true
     */
    isBlank: function (str) {
        if (str == undefined || str == null || str == "") {
            //jQuery.sap.log.debug("isBlank: input string is undefined, null or empty");
            return true;
        }

        if (this.trimString(str.toString()) == "") {
            //jQuery.sap.log.debug("isBlank: trimmed input string is empty");
            return true;
        }
        //jQuery.sap.log.debug("isBlank: str <" + str + "> not empty");
        return false;
    },

    /**
     * helper function to trim leading and trailing spaces from a string
     *
     * @param str {String} to trim spaces from
     * @return updated String
     */
    trimString: function (str) {
        var c;
        for (var i = 0; i < str.length; i++) {
            c = str.charCodeAt(i);
            if (c == 32 || c == 10 || c == 13 || c == 9 || c == 12) continue; else break;
        }
        for (var j = str.length - 1; j >= i; j--) {
            c = str.charCodeAt(j);
            if (c == 32 || c == 10 || c == 13 || c == 9 || c == 12) continue; else break;
        }

        //jQuery.sap.log.debug("trimString: i = " + i + ", j = " + j);

        return str.substring(i, j + 1);
    },

    /**
     * helper function to encode the input string (including single quotes)
     *
     * @param {String} str String to encode
     * @return encoded string
     */
    encodeString: function (str) {
        if (this.isBlank(str)) {
            return str;
        }
        return jQuery.sap.encodeURL(str);
    },

    /**
     * helper function to check if one string ends with another
     *
     * @param {String} str String to check
     * @param {String} stringCheck String to search
     * @return true if str ends with sSerach
     */
    endsWith: function (str, sSearch) {
        return (str.lastIndexOf(sSearch) === (str.length - sSearch.length)) > 0;
    },

    /**
     * This utility method substitutes in the String _orig all occurrences of string
     * _tosubstr with the string _withsubst
     *
     * @param _orig The string to search
     * @param _tosubst what to search for
     * @param _withsubst what to substitute with
     * @param _startIndex start replacing from this index
     *
     * @return String  new string with substitution executed
     */
    replaceStrings: function (_orig, _tosubst, _withsubst, _startIndex) {

        var postString = null;
        var preString = null;
        var newString = _orig;
        var withString = _withsubst;
        if (_withsubst == null || _withsubst == undefined) withString = "";

        var foundat = -1;
        var nextStart = -1;
        if ((foundat = _orig.indexOf(_tosubst, _startIndex)) != -1) {
            // check for existing preString
            if (foundat > 0) {
                preString = _orig.substring(0, foundat);
            } else {
                preString = "";
            }

            var indexafter = foundat + _tosubst.length;
            postString = _orig.substring(indexafter);

            newString = preString + withString + postString;

            nextStart = preString.length + withString.length;
        }

        if (newString !== _orig) {
            // Maybe there are more occurrences of _tosubst; so repeat.
            return this.replaceStrings(newString, _tosubst, withString, nextStart);
        }

        return newString;

    },

    wrapWithQuote: function (str) {
        if (this.isBlank(str)) {
            return str;
        }
        var res = undefined;
        if (str.charAt(0) != "'") {
            res = "'".concat(str);
        }
        if (str.charAt(str.length - 1) != "'") {
            res = res.concat("'");
        }
        return res;

    },

    printTimeStamp: function (sMessage) {
        var currentDate = new Date();
        var day = currentDate.getUTCDate();
        var month = currentDate.getUTCMonth() + 1;
        var year = currentDate.getUTCFullYear();
        var hours = currentDate.getUTCHours();
        var minutes = currentDate.getUTCMinutes();
        var seconds = currentDate.getUTCSeconds();
        var milliseconds = currentDate.getUTCMilliseconds();
        jQuery.sap.log.info(sMessage + ": " + year + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds + ":" + milliseconds);
    },

    /**
     * Sets focus when passed the view (this.getView();) and the control id.
     * * WARNING * - This method must be used within the onAfterShow() of the controller.
     **/
    setFocus: function (view, control) {
        var focusedControl = view.byId(control);
        focusedControl.focus();
    },
    stringFormat: function (args) {
        if (arguments.length == 0)
            return null;
        var str = arguments[0];
        for (var i = 1; i < arguments.length; i++) {
            var re = new RegExp('\\{' + (i - 1) + '\\}', 'gm');
            str = str.replace(re, arguments[i]);
        }
        return str;
    },
    base64Encode: function (input) {
        var _keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
        var output = "";
        var chr1, chr2, chr3, enc1, enc2, enc3, enc4;
        var i = 0;
        input = this._utf8_encode(input);
        while (i < input.length) {
            chr1 = input.charCodeAt(i++);
            chr2 = input.charCodeAt(i++);
            chr3 = input.charCodeAt(i++);
            enc1 = chr1 >> 2;
            enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
            enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
            enc4 = chr3 & 63;
            if (isNaN(chr2)) {
                enc3 = enc4 = 64;
            } else if (isNaN(chr3)) {
                enc4 = 64;
            }
            output = output +
                _keyStr.charAt(enc1) + _keyStr.charAt(enc2) +
                _keyStr.charAt(enc3) + _keyStr.charAt(enc4);
        }
        return output;
    },
    base64Decode: function (input) {
        var _keyStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";
        var output = "";
        var chr1, chr2, chr3;
        var enc1, enc2, enc3, enc4;
        var i = 0;
        input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");
        while (i < input.length) {
            enc1 = _keyStr.indexOf(input.charAt(i++));
            enc2 = _keyStr.indexOf(input.charAt(i++));
            enc3 = _keyStr.indexOf(input.charAt(i++));
            enc4 = _keyStr.indexOf(input.charAt(i++));
            chr1 = (enc1 << 2) | (enc2 >> 4);
            chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
            chr3 = ((enc3 & 3) << 6) | enc4;
            output = output + String.fromCharCode(chr1);
            if (enc3 != 64) {
                output = output + String.fromCharCode(chr2);
            }
            if (enc4 != 64) {
                output = output + String.fromCharCode(chr3);
            }
        }
        output = this._utf8_decode(output);
        return output;
    },
    _utf8_encode: function (string) {
        string = string.replace(/\r\n/g, "\n");
        var utftext = "";
        for (var n = 0; n < string.length; n++) {
            var c = string.charCodeAt(n);
            if (c < 128) {
                utftext += String.fromCharCode(c);
            } else if ((c > 127) && (c < 2048)) {
                utftext += String.fromCharCode((c >> 6) | 192);
                utftext += String.fromCharCode((c & 63) | 128);
            } else {
                utftext += String.fromCharCode((c >> 12) | 224);
                utftext += String.fromCharCode(((c >> 6) & 63) | 128);
                utftext += String.fromCharCode((c & 63) | 128);
            }

        }
        return utftext;
    },
    _utf8_decode: function (utftext) {
        var string = "";
        var i = 0;
        var c = c1 = c2 = 0;
        while (i < utftext.length) {
            c = utftext.charCodeAt(i);
            if (c < 128) {
                string += String.fromCharCode(c);
                i++;
            } else if ((c > 191) && (c < 224)) {
                c2 = utftext.charCodeAt(i + 1);
                string += String.fromCharCode(((c & 31) << 6) | (c2 & 63));
                i += 2;
            } else {
                c2 = utftext.charCodeAt(i + 1);
                c3 = utftext.charCodeAt(i + 2);
                string += String.fromCharCode(((c & 15) << 12) | ((c2 & 63) << 6) | (c3 & 63));
                i += 3;
            }
        }
        return string;
    },
    uuid: function() {
        var s = [];
        var hexDigits = "0123456789abcdef";
        for (var i = 0; i < 36; i++) {
            s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
        }
        s[14] = "4";  // bits 12-15 of the time_hi_and_version field to 0010
        s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);  // bits 6-7 of the clock_seq_hi_and_reserved to 01
        s[8] = s[13] = s[18] = s[23] = "-";

        var uuid = s.join("");
        return uuid;
    }
}