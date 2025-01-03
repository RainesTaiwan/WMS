package com.sap.ewm.core.utils;

public class TimeUtils {

    public static String minsToString(long value) {
        String result = value < 0 ? "-" : "";
        long pv = Math.abs(value);
        long min = pv % 60;
        long hr = (pv - min) % 24;
        long day = (pv - min - 60 * hr) / 1440;
        if (day > 0) {
            return result + String.format("%02d:%02d:%02d", day, hr, min);
        }
        else {
            return result + String.format("%02d:%02d", hr, min);
        }
    }

    /**
     * 輸出格式：08:00:00
     * @param value
     * @return
     */
    public static String stringToTime(String value){
        String timeStr = "";
        int valueInt = Integer.parseInt( value);
        int hour = valueInt/3600;
        timeStr = hour < 10 ? ("0"+hour) : (hour+"");
        int minute = valueInt%3600/60;
        timeStr += ":" + (minute < 10 ? ("0"+minute) : (minute+"")) + ":";
        int second = valueInt%3600%60;
        timeStr += second < 10 ? ("0"+second) : (second+"");

        return timeStr;
    }
}
