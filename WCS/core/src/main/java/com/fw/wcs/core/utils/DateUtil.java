package com.fw.wcs.core.utils;

import com.fw.wcs.core.exception.BusinessException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {

    public static final String DATE_FORMAT1 = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String DATE_FORMAT2 = "yyyy/MM/dd HH:mm:ss.SSS";
    public static final String DATE_FORMAT3 = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT4 = "yyyy/MM/dd HH:mm:ss";
    public static final String DATE_FORMAT5 = "yyyy-MM-dd";
    public static final String DATE_FORMAT6 = "yyyy/MM/dd";

    public static final String DATE_FORMAT7 = "yyyyMMddHHmmssSSS";

    public static String getDateTimeWithRandomNum() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT7);
        int n1 = (int)(Math.random()*9); //產生0~9的數值
        int n2 = (int)(Math.random()*9); //產生0~9的數值
        int n3 = (int)(Math.random()*9); //產生0~9的數值
        int n4 = (int)(Math.random()*9); //產生0~9的數值
        return dateFormat.format(new Date()) + Integer.toString(n1) + Integer.toString(n2) + Integer.toString(n3) + Integer.toString(n4);
    }

    public static String getDateGMT8Time() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT1);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        return "GMT+8 "+dateFormat.format(new Date());
    }

    public static String getDateTimemessageId() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT7);
        return dateFormat.format(new Date());
    }

    public static String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT3);
        return dateFormat.format(new Date());
    }

    public static String format( Date date, String pattern ){
        SimpleDateFormat dateFormat = new SimpleDateFormat( pattern );
        return dateFormat.format( date );
    }

    public static String format( Date date ){
        return format( date, DATE_FORMAT1 );
    }

    public static Date parse( String date, String pattern ) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat( pattern );
            return dateFormat.parse( date );
        } catch (ParseException e) {
            throw BusinessException.build( "common.dateFormatNotSupport", new String[]{ date } );
        }
    }

    public static Date parse( String date ){
        if( date.length() == 23 ){
            if( date.indexOf( "/" ) > -1 ){
                return parse( date, DATE_FORMAT2 );
            }else if( date.indexOf( "-" ) > -1 ){
                return parse( date, DATE_FORMAT1 );
            }
        }
        if( date.length() == 19 ){
            if( date.indexOf( "/" ) > -1 ){
                return parse( date, DATE_FORMAT4 );
            }else if( date.indexOf( "-" ) > -1 ){
                return parse( date, DATE_FORMAT3 );
            }
        }
        if( date.length() == 10 ){
            if( date.indexOf( "/" ) > -1 ){
                return parse( date, DATE_FORMAT6 );
            }else if( date.indexOf( "-" ) > -1 ){
                return parse( date, DATE_FORMAT5 );
            }
        }
        throw BusinessException.build( "common.dateFormatNotSupport", new String[]{ date } );
    }

    public static Date addDays( Date date, int days ){
        Date date1 = (Date) date.clone();
        date1.setTime( date.getTime() + days * 24 * 60 * 60 * 1000 );
        return date1;
    }

    public static Date addHours( Date date, int hours ){
        Date date1 = (Date) date.clone();
        date1.setTime( date.getTime() + hours * 60 * 60 * 1000 );
        return date1;
    }

    public static Date addMinutes( Date date, int minutes ){
        Date date1 = (Date) date.clone();
        date1.setTime( date.getTime() + minutes * 60 * 1000 );
        return date1;
    }

    public static String formatDate(Date date, String fmt) {
        SimpleDateFormat format = new SimpleDateFormat(fmt);
        return format.format(date);
    }

    /**
     * 使用簡單化的方式來解析日期時間格式.
     *
     * @param dateStr
     * @return
     */
    public final static Date parseSimpleDateTime(String dateStr) {
        return parseSimpleDT("yyyy-MM-dd HH:mm:ss", dateStr);
    }

    /**
     * 使用指定的模式來解析字串日期時間.
     *
     * @param pattern
     * @param dateStr
     * @return
     */
    public final static Date parseSimpleDT(String pattern, String dateStr) {
        try {
            return new SimpleDateFormat(pattern, Locale.US).parse(dateStr);
        } catch (ParseException ex) {
            return null;
        }
    }

    /***
     * 把日期轉換成字串
     *
     * @param time
     * @return
     */
    public static String convertToStr(Date time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(time);
    }

    /**
     * 在某一指定的日期基礎上進行日期的偏差設定，參數意義同getNowDate
     *
     * @param date
     * @param deviation
     * @return
     */
    public final static Date setDate(Date date, int... deviation) {
        Calendar cal = Calendar.getInstance(Locale.US);
        cal.setTime(date);
        if (deviation.length < 1) {
            return cal.getTime();
        }
        final int[] filed = {Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH, Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND};
        for (int i = 0; i < deviation.length; i++) {
            cal.add(filed[i], deviation[i]);
        }
        return cal.getTime();
    }


    /***
     * 判斷兩日期是否同一天
     * @param d1
     * @param d2
     * @return
     */
    public static boolean sameDate(Date d1, Date d2) {
        LocalDate localDate1 = ZonedDateTime.ofInstant(d1.toInstant(), ZoneId.systemDefault()).toLocalDate();
        LocalDate localDate2 = ZonedDateTime.ofInstant(d2.toInstant(), ZoneId.systemDefault()).toLocalDate();
        return localDate1.isEqual(localDate2);
    }

    /**
     * 判斷兩日期是否同一周
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean sameWeek(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
        //subYear==0,說明是同一年
        if (subYear == 0) {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR)) {
                return true;
            }
        } else if (subYear == 1 && cal2.get(Calendar.MONTH) == 11) {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true;
        } else if (subYear == -1 && cal1.get(Calendar.MONTH) == 11) {
            if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
                return true;
        }
        return false;
    }

    /**
     * 判斷兩日期是否同一月
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean sameMonth(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        int month1 = cal1.get(Calendar.MONTH);
        int month2 = cal2.get(Calendar.MONTH);

        String m1 = "" + year1 + month1;
        String m2 = "" + year2 + month2;

        return m1.equals(m2);
    }

    /**
     * 判斷兩日期是否同一年
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean sameYear(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR);
    }
}
