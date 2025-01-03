package com.fw.wcs.core.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by Administrator on 2018/8/20.
 */
public class StringUtils {

    public static String trimSite(String handle) {
        if (handle == null || handle.trim().length() == 0) {
            return null;
        }
        String[] temp = handle.split(":");
        if( temp.length < 2 ) {
            return handle;
        }
        String perfix = temp[1];
        String[] temp1 = perfix.split(",");
        if( temp1.length < 2 ) {
            return handle;
        }
        return temp1[0];
    }

    public static String trimHandle(String handle) {
        if (handle == null || handle.trim().length() == 0) {
            return null;
        }

        String[] temp = handle.split(",");
        if( temp.length < 2 ) {
            return handle;
        }
        return temp[1];
    }

    public static String trimRevision(String handle) {
        if (handle == null || handle.trim().length() == 0) {
            return null;
        }

        int index = handle.lastIndexOf(",");
        if (index < 0) {
            return null;
        }
        else {
            return handle.substring(index + 1);
        }
    }

    public static String genHandle(String bo, String site, String... keys) {
        return String.format("%s:%s,%s", bo, site, String.join(",", keys));
    }

    public static String genPrefix(String bo, String site) {
        return bo == null ? null : String.format("%s:%s,", bo, site);
    }

    static private Random randomer = new Random();
    static private char[] QUID_AlphaNumericArray = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z'
    };
    static private char[] QUID_AlphaArray = {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z'
    };

    static public String createQUID()
    {
        return createAlphaNumericText( 32 );
    }

    static public String createAlphaNumericText(int length)
    {
        StringBuilder str_builer = new StringBuilder();
        char c = '\0';
        // ------------------------------------------------------------------------------------------------------------------
        c = QUID_AlphaArray[randomer.nextInt( QUID_AlphaArray.length )];
        str_builer.append( c );
        // ------------------------------------------------------------------------------------------------------------------
        for( int i = 0; i < length - 1; i++ ) {
            c = QUID_AlphaNumericArray[randomer.nextInt( QUID_AlphaNumericArray.length )];
            str_builer.append( c );
        } // for i
        // ------------------------------------------------------------------------------------------------------------------
        return str_builer.toString();
    }

    // -----------------------------------------------------------------------------------------------------------------------------------------
    static public String format(String message, String arg0)
    {
        if( message.length() <= 0 ) return message;
        message = replace( message, "%0", arg0 );
        return message;
    }

    static public String format(String message, String arg0, String arg1)
    {
        if( message.length() <= 0 ) return message;
        message = replace( message, "%0", arg0 );
        message = replace( message, "%1", arg1 );
        return message;
    }

    static public String format(String message, String arg0, String arg1, String arg2)
    {
        if( message.length() <= 0 ) return message;
        message = replace( message, "%0", arg0 );
        message = replace( message, "%1", arg1 );
        message = replace( message, "%2", arg2 );
        return message;
    }

    static public String format(String message, String arg0, String arg1, String arg2, String arg3)
    {
        if( message.length() <= 0 ) return message;
        message = replace( message, "%0", arg0 );
        message = replace( message, "%1", arg1 );
        message = replace( message, "%2", arg2 );
        message = replace( message, "%3", arg3 );
        return message;
    }

    static public String format(String message, String arg0, String arg1, String arg2, String arg3, String arg4)
    {
        if( message.length() <= 0 ) return message;
        message = replace( message, "%0", arg0 );
        message = replace( message, "%1", arg1 );
        message = replace( message, "%2", arg2 );
        message = replace( message, "%3", arg3 );
        message = replace( message, "%4", arg4 );
        return message;
    }

    static public String format(String message, String arg0, String arg1, String arg2, String arg3, String arg4, String arg5)
    {
        if( message.length() <= 0 ) return message;
        message = replace( message, "%0", arg0 );
        message = replace( message, "%1", arg1 );
        message = replace( message, "%2", arg2 );
        message = replace( message, "%3", arg3 );
        message = replace( message, "%4", arg4 );
        message = replace( message, "%5", arg5 );
        return message;
    }

    static public String format(String message, String arg0, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6)
    {
        if( message.length() <= 0 ) return message;
        message = replace( message, "%0", arg0 );
        message = replace( message, "%1", arg1 );
        message = replace( message, "%2", arg2 );
        message = replace( message, "%3", arg3 );
        message = replace( message, "%4", arg4 );
        message = replace( message, "%5", arg5 );
        message = replace( message, "%6", arg6 );
        return message;
    }

    static public String format(String message, List<String> args)
    {
        if( message.length() <= 0 ) return message;
        if( args == null ) return message;
        // -----------------------------------------------------------------------------------------------------------------------------
        for( int i = 0; i < args.size(); i++ ) {
            message = replace( message, "%" + i, args.get( i ) );
        }//for i
        // -----------------------------------------------------------------------------------------------------------------------------
        return message;
    }

    static public String format(String message, Object... args)
    {
        if( message.length() <= 0 ) return message;
        if( args == null ) return message;
        // -----------------------------------------------------------------------------------------------------------------------------
        for( int i = 0; i < args.length; i++ ) {
            message = replace( message, "%" + i, String.valueOf(args[i]) );
        }//for i
        // -----------------------------------------------------------------------------------------------------------------------------
        return message;
    }

    static public String protectSQL(String str)
    {
        str = replace( str, "'", "''" );
        return str;
    }

    static public boolean sensitive(String str)
    {
        for( int i = 0; i < str.length(); i++ ) {
            char c = str.charAt( i );
            int c_index = (int) c;
            // -----------------------------------------------------------------------------------------------------------------------------
            if( c_index > 127 ) return true;
            else if( StringUtils.sensitive( c ) ) return true;
        } // for i

        return false;
    }

    static public boolean sensitive(char c)
    {
        if( c == '\r' || c == '\n' || c == '\"' || c == '&' || c == '\'' || c == '<' || c == '>' || c == '{' || c == '}' ) return true;

        return false;
    }

    static public String protectXML(String str)
    {
        if( str.length() <= 0 ) return str;
        str = replace( str, "\r", "@#10" );
        str = replace( str, "\n", "@#13" );
        str = replace( str, "\"", "@#34" );
        str = replace( str, "&", "@#38" );
        str = replace( str, "'", "@#39" );
        str = replace( str, ";", "@#59" );
        str = replace( str, "<", "@#60" );
        str = replace( str, ">", "@#62" );
        str = replace( str, "{", "@#123" );
        str = replace( str, "}", "@#125" );
        return str;
    }

    static public String restoreXML(String str)
    {
        if( str.length() <= 0 ) return str;
        str = replace( str, "@#10", "\r" );
        str = replace( str, "@#13", "\n" );
        str = replace( str, "@#34", "\"" );
        str = replace( str, "@#38", "&" );
        str = replace( str, "@#39", "'" );
        str = replace( str, "@#59", ";" );
        str = replace( str, "@#60", "<" );
        str = replace( str, "@#62", ">" );
        str = replace( str, "@#123", "{" );
        str = replace( str, "@#125", "}" );
        return str;
    }

    static public String protectWebXml(String str)
    {
        if( isEmpty( str ) ) return str;
        str = replace( str, "\"", "&quot;" );
        str = replace( str, "&", "&amp;" );
        str = replace( str, "'", "&apos;" );
        str = replace( str, "<", "&lt;" );
        str = replace( str, ">", "&gt;" );
        return str;
    }

    static public String restoreWebXml(String str)
    {
        if( isEmpty( str ) ) return str;
        str = replace( str, "&quot;", "\"" );
        str = replace( str, "&amp;", "&" );
        str = replace( str, "&apos;", "'" );
        str = replace( str, "&lt;", "<" );
        str = replace( str, "&gt;", ">" );
        return str;
    }

    static public String space(int value, int slotcnt)
    {
        return space( value + "", '0', slotcnt );
    }

    static public String space(String str, char spacer, int slotcnt)
    {
        if( slotcnt <= 0 ) return "";
        if( str.length() >= slotcnt ) return str;
        // ------------------------------------------------------------------------------------------------------------------
        StringBuffer str_buf = new StringBuffer();
        // ------------------------------------------------------------------------------------------------------------------
        slotcnt = slotcnt - str.length();
        for( int i = 0; i < slotcnt; i++ ) {
            str_buf.append( spacer );
        } // for i
        str_buf.append( str );
        // ------------------------------------------------------------------------------------------------------------------
        return str_buf.toString();
    }

    static public String rspace(String str, char spacer, int slotcnt)
    {
        if( slotcnt <= 0 ) return "";
        if( str.length() >= slotcnt ) return str;
        // ------------------------------------------------------------------------------------------------------------------
        StringBuffer str_buf = new StringBuffer();
        // ------------------------------------------------------------------------------------------------------------------
        str_buf.append( str );
        slotcnt = slotcnt - str.length();
        for( int i = 0; i < slotcnt; i++ ) {
            str_buf.append( spacer );
        } // for i
        // ------------------------------------------------------------------------------------------------------------------
        return str_buf.toString();
    }

    static public boolean bool(Object value)
    {
        try {
            if( value == null ) return false;
            // ------------------------------------------------------------------------------------------------------------------
            return bool( value.toString() );

        } catch( Exception e ) {
            ; // Do nothing. That's all.
        } // try
        // ------------------------------------------------------------------------------------------------------------------
        return false;
    }

    static public boolean bool(String str)
    {
        try {
            if( str == null ) return false;
            if( str.equalsIgnoreCase( "TRUE" ) ) return true;
            if( str.equalsIgnoreCase( "Y" ) ) return true;
            if( str.equalsIgnoreCase( "YES" ) ) return true;
            if( Integer.parseInt( str ) > 0 ) return true;

        } catch( Exception e ) {
            ; // Do nothing. That's all.
        } // try
        // ------------------------------------------------------------------------------------------------------------------
        return false;
    }

    static public String strDouble(String format, Double value)
    {
        DecimalFormat df = new DecimalFormat( format );
        return df.format( value );
    }

    static public boolean isAlphaNumeric(String str)
    {
        if( str.length() <= 0 ) return false;
        // ------------------------------------------------------------------------------------------------------------------
        for( int i = 0; i < str.length(); i++ ) {
            char c = str.charAt( i );
            if( i == 0 ) {
                if( isAlpha( c ) || c == '_' ) continue;
            } else if( isAlphaNumeric( c ) || c == '_' ) {
                continue;
            } // if
            return false;
        } // for i

        return true;
    }

    static public boolean isInteger(String str)
    {
        for( int i = 0; i < str.length(); i++ ) {
            char c = str.charAt( i );
            if( isNumeric( c ) ) continue;
            return false;
        } // for i
        //--------------------------------------------------------------------------------------------------------
        return true;
    }

    static public boolean isNumeric(String str)
    {
        return isDouble( str );
    }

    static public boolean isDouble(String str)
    {
        boolean dot = false;

        for( int i = 0; i < str.length(); i++ ) {
            char c = str.charAt( i );
            if( isNumeric( c ) ) {
                continue;
            } else if( c == '.' && !dot ) {
                dot = true;
                continue;
            } // if
            return false;
        } // for i

        return true;
    }

    static public boolean isSciFormat(String str)
    {
        int exp = -1;

        for( int i = 0; i < str.length(); i++ ) {
            char c = str.charAt( i );
            if( isNumeric( c ) ) {
                continue;
            } else if( i > 0 && c == 'E' && exp < 0 ) {
                exp = i;
                continue;
            } // if
            return false;
        } // for i
        if( exp == str.length() ) return false;

        return true;
    }

    static public boolean isSeparator(char c)
    {
        return ( c == ' ' || c == '\t' || c == '\n' || c == '\r' )
                ? true : false;
    }

    static public boolean isSeparator(String str)
    {
        if( str.equals( " " ) ) return true;
        if( str.equals( "\t" ) ) return true;
        if( str.equals( "\n" ) ) return true;
        if( str.equals( "\r" ) ) return true;

        return false;
    }

    static public boolean isAlpha(char c)
    {
        return ( ( c >= 'A' && c <= 'Z' ) || ( c >= 'a' && c <= 'z' ) )
                ? true : false;
    }

    static public boolean isAlphaNumeric(char c)
    {
        return ( ( c >= 'A' && c <= 'Z' ) ||
                ( c >= 'a' && c <= 'z' ) ||
                ( c >= '0' && c <= '9' ) || c == '_' )
                ? true : false;
    }

    static public boolean isNumeric(char c)
    {
        return ( ( c >= '0' && c <= '9' ) ) ? true : false;
    }

    static public int integer(String str)
    {
        StringBuilder str_buf = new StringBuilder();
        //--------------------------------------------------------------------------------------------------------
        for( int i = 0; i < str.length(); i++ ) {
            char c = str.charAt( i );
            if( isNumeric( c ) ) str_buf.append( c );
        } // for i
        //--------------------------------------------------------------------------------------------------------
        if( str_buf.length() <= 0 ) return 0;
        //--------------------------------------------------------------------------------------------------------
        return Integer.parseInt( str_buf.toString() );
    }

    static public String replace(String str, String replaced, String replacement)
    {
        if( str == null ) return null;
        if( str.equals( "" ) ) return "";
        // -------------------------------------------------------------------------------------------------------------------------------------
        StringBuilder str_buf = new StringBuilder();
        int start = 0, i = 0;
        // -------------------------------------------------------------------------------------------------------------------------------------
        i = str.indexOf( replaced, start );
        while( i >= 0 ) {
            str_buf.append( str.substring( start, i ) );
            str_buf.append( replacement );
            start = i + replaced.length();
            i = str.indexOf( replaced, start );
        } // while
        if( start < str.length() ) str_buf.append( str.substring( start ) );

        return str_buf.toString();
    }

    static public String decode(String str, String charset) throws UnsupportedEncodingException
    {
        return URLDecoder.decode( str, charset );
    }

    static public String escape(String str)
    {
        return escape( str, "%" );
    }

    static public String escape(String str, String prefix)
    {
        StringBuffer str_buf = new StringBuffer();
        String prefix_ext = prefix + "u";
        // -------------------------------------------------------------------------------------------------------------------------------
        str_buf.ensureCapacity( str.length() * 6 );
        for( int i = 0; i < str.length(); i++ ) {
            char c = str.charAt( i );
            if( Character.isDigit( c ) || Character.isLowerCase( c ) || Character.isUpperCase( c ) ) {
                str_buf.append( c );
            } else if( c < 256 ) {
                str_buf.append( prefix );
                if( c < 16 ) str_buf.append( "0" );
                str_buf.append( Integer.toString( c, 16 ) );
            } else {
                str_buf.append( prefix_ext );
                str_buf.append( Integer.toString( c, 16 ) );
            }
        } // for i
        return str_buf.toString();
    }

    public static String unescape(String str)
    {
        return unescape( str, "%" );
    }

    public static String unescape(String str, String prefix)
    {
        if( str.length() <= 0 ) return str;
        StringBuffer str_buf = new StringBuffer();
        int pos = 0, lastPos = 0;
        char c = '\0';
        // -------------------------------------------------------------------------------------------------------------------------------
        str_buf.ensureCapacity( str.length() );
        while( lastPos < str.length() ) {
            pos = str.indexOf( prefix, lastPos );
            // -------------------------------------------------------------------------------------------------------------------------------
            if( pos == lastPos ) {
                if( str.charAt( pos + 1 ) == 'u' ) {
                    c = (char) Integer.parseInt( str.substring( pos + 2, pos + 6 ), 16 );
                    str_buf.append( c );
                    lastPos = pos + 6;
                } else {
                    c = (char) Integer.parseInt( str.substring( pos + 1, pos + 3 ), 16 );
                    str_buf.append( c );
                    lastPos = pos + 3;
                } // if src
            } else {
                if( pos == -1 ) {
                    str_buf.append( str.substring( lastPos ) );
                    lastPos = str.length();
                } else {
                    str_buf.append( str.substring( lastPos, pos ) );
                    lastPos = pos;
                } // if pos
            } // if pos
        } // while

        return str_buf.toString();
    }

    static private Throwable getRootCause(Throwable cause)
    {
        if( cause == null ) return null;
        // -----------------------------------------------------------------------------------------------------------------------------------------
        Throwable parent = cause.getCause();
        // -----------------------------------------------------------------------------------------------------------------------------------------
        if( parent == null ) return cause;
        // -----------------------------------------------------------------------------------------------------------------------------------------
        return getRootCause( parent );
    }

    static public String assembleStackTrace(Throwable exception, boolean indent)
    {
        StringBuilder str_buf = new StringBuilder();
        Throwable cause = getRootCause( exception );
        // -----------------------------------------------------------------------------------------------------------------------------------------
        if( cause != null ) {
            str_buf.append( "Caused by " + cause.getClass().getName() + ": " + cause.getMessage() );
            str_buf.append( "\n" );
            str_buf.append( assembleStackTrace( cause.getStackTrace(), indent ) );
        } else {
            str_buf.append( assembleStackTrace( exception.getStackTrace(), indent ) );
        } // if
        // -----------------------------------------------------------------------------------------------------------------------------------------
        return str_buf.toString();
    }

    static public String assembleStackTrace(StackTraceElement[] stackTraceElements, boolean indent)
    {
        StringBuilder str_buf = new StringBuilder();
        // -----------------------------------------------------------------------------------------------------------------------------------------
        for( int i = 0; i < stackTraceElements.length; i++ ) {
            StackTraceElement stackTraceElement = stackTraceElements[i];
            // -----------------------------------------------------------------------------------------------------------------------------------------
            if( indent ) str_buf.append( "\t" );
            str_buf.append( stackTraceElement.toString() );
            str_buf.append( "\n" );
        } // for i
        // -----------------------------------------------------------------------------------------------------------------------------------------
        return str_buf.toString();
    }

    static public String assembleExceptionText(Exception exception)
    {
        return assembleExceptionText( null, exception, null );
    }

    static public String assembleExceptionText(String prefix, Exception exception, String postfix)
    {
        StringBuilder str_buf = new StringBuilder();
        String message = exception.getMessage();
        // ----------------------------------------------------------------------------------------------------------------------------------------
        str_buf.append( exception.getClass().getName() );
        str_buf.append( ": " );
        // ----------------------------------------------------------------------------------------------------------------------------------------
        if( prefix != null ) {
            str_buf.append( prefix );
            str_buf.append( "; " );
        } // if
        // ----------------------------------------------------------------------------------------------------------------------------------------
        message = StringUtils.powertrim( message );
        str_buf.append( message );
        // ----------------------------------------------------------------------------------------------------------------------------------------
        if( postfix != null ) {
            str_buf.append( postfix );
            str_buf.append( "; " );
        } // if
        // ----------------------------------------------------------------------------------------------------------------------------------------
        str_buf.append( "\n" );
        str_buf.append( assembleStackTrace( exception, true ) );
        // ----------------------------------------------------------------------------------------------------------------------------------------
        return str_buf.toString();
    }

    static public String strip(String str)
    {
        boolean startWith = str.startsWith( "\"" );
        boolean endWith = str.endsWith( "\"" );
        // ----------------------------------------------------------------------------------------------------------------------------------------
        if( startWith && endWith ) {
            str = str.substring( 1, str.length() - 1 );
        } else if( startWith ) {
            str = str.substring( 1 );
        } else if( endWith ) {
            str = str.substring( 0, str.length() - 1 );
        } // if
        // ----------------------------------------------------------------------------------------------------------------------------------------
        return str;
    }

    static public String extract(String str, String begin, String end)
    {
        int i = str.indexOf( begin );
        if( i < 0 ) return null;
        int j = str.indexOf( end, i + 1 );
        if( j < 0 ) return null;
        // ----------------------------------------------------------------------------------------------------------------------------------------
        str = str.substring( i + begin.length(), j ).trim();
        // ----------------------------------------------------------------------------------------------------------------------------------------
        return str;
    }

    static public List<String> parseList(String str, String cut)
    {
        List<String> instance = new ArrayList<String>();
        // ---------------------------------------------------------------------------------------------------------------------
        parseList( instance, str, cut );
        // ---------------------------------------------------------------------------------------------------------------------
        return instance;
    }

    static public List<String> parseList(List<String> instance, String str, String cut)
    {
        String item = "";
        int start = 0;
        int i = -1;
        // ---------------------------------------------------------------------------------------------------------------------
        if( instance != null ) instance.clear();
        // ---------------------------------------------------------------------------------------------------------------------
        str = str.trim();
        if( str.length() <= 0 ) return instance;
        // ---------------------------------------------------------------------------------------------------------------------
        str += cut;
        i = str.indexOf( cut, start );
        while( i > -1 ) {
            item = str.substring( start, i ).trim();
            instance.add( item );
            start = i + cut.length();
            i = str.indexOf( cut, start );
        } // while i

        return instance;
    }

    static public Map<String, String> parseMap(String str, String gap, String cut)
    {
        Map<String, String> instance = new HashMap<String, String>();
        // ---------------------------------------------------------------------------------------------------------------------
        parseMap( instance, str, gap, cut );
        // ---------------------------------------------------------------------------------------------------------------------
        return instance;
    }

    static public String parseString(String content, String tag, String end)
    {
        String str = "";
        // ---------------------------------------------------------------------------------------------------------------------
        int i = content.indexOf( tag );
        if( i < 0 ) return str;
        int j = content.indexOf( end, i + 1 );
        if( j < 0 ) return str;
        str = content.substring( i + tag.length(), j );
        // ---------------------------------------------------------------------------------------------------------------------
        return str;
    }

    static public void parseMap(Map<String, String> instance, String str, String gap, String cut)
    {
        String item = "";
        int start = 0;
        int i = -1, j = -1;
        // ---------------------------------------------------------------------------------------------------------------------
        if( instance != null ) instance.clear();
        // ---------------------------------------------------------------------------------------------------------------------
        str = str.trim();
        if( str.length() <= 0 ) return;
        // ---------------------------------------------------------------------------------------------------------------------
        str += cut;
        i = str.indexOf( cut, start );
        while( i > -1 ) {
            item = str.substring( start, i ).trim();
            j = item.indexOf( gap );
            if( j > -1 ) {
                String key = item.substring( 0, j ).trim();
                String value = item.substring( j + gap.length() ).trim();
                // ---------------------------------------------------------------------------------------------------------------------
                instance.put( key, value );
            } // if
            // ---------------------------------------------------------------------------------------------------------------------
            start = i + cut.length();
            i = str.indexOf( cut, start );
        } // while i
    }

    static public String intColumn(int column)
    {
        int col = column + 1;
        int system = 26;
        char[] digArr = new char[100];
        int ind = 0;
        // ---------------------------------------------------------------------------------------------------------------------
        while( col > 0 ) {
            int mod = col % system;
            if( mod == 0 ) mod = system;
            digArr[ind++] = digChar( mod );
            col = ( col - 1 ) / 26;
        }
        // ---------------------------------------------------------------------------------------------------------------------
        StringBuffer bf = new StringBuffer( ind );
        // ---------------------------------------------------------------------------------------------------------------------
        for( int i = ind - 1; i >= 0; i-- ) {
            bf.append( digArr[i] );
        }

        return bf.toString();
    }

    static public char digChar(int dig)
    {
        int acs = dig - 1 + 'A';
        return (char) acs;
    }

    static public boolean isEmpty(String str)
    {
        boolean isEmpty = false;
        if( str == null ) isEmpty = true;
        else if( str.equals( "" ) ) isEmpty = true;
        return isEmpty;
    }

    static public boolean notEmpty(String str)
    {
        return !isEmpty( str );
    }

    static public boolean isBlank(String str)
    {
        boolean isBlank = false;
        if( str == null ) isBlank = true;
        else if( str.trim().equals( "" ) ) isBlank = true;
        return isBlank;
    }

    static public boolean notBlank(String str)
    {
        return !isBlank(str);
    }

    static public String toString(String str, String charset)
    {
        String rtn_str = "";
        try {
            rtn_str = new String( str.getBytes(), charset );
        } catch( UnsupportedEncodingException e ) {
            // e.printStackTrace();
        } // try
        //--------------------------------------------------------------------------------
        return rtn_str;
    }

    /**
     * 將ErrorStack轉化為String.
     */
    static public String getStackTraceAsString(Exception e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    // added by Arrow, 2012-10-11
    static public void trim(StringBuffer str_buf)
    {
        for( int i = str_buf.length() - 1; i >= 0; i-- ) {
            char c = str_buf.charAt( i );
            // -------------------------------------------------------------------------------------------------------------------------------------
            if( c == ' ' || c == '\r' || c == '\t' ) {
                str_buf.deleteCharAt( i );
            } else {
                break;
            } // if
        } // for i
    }

    // added by Arrow, 2012-10-12
    static public String powertrim(String str)
    {
        if( str == null ) return null; // added by Arrow, 2012-10-18
        // -------------------------------------------------------------------------------------------------------------------------------------
        int i = -1;
        // -------------------------------------------------------------------------------------------------------------------------------------
        for( i = str.length() - 1; i >= 0; i-- ) {
            char c = str.charAt( i );
            // -------------------------------------------------------------------------------------------------------------------------------------
            if( c == ' ' || c == '\r' || c == '\t' || c == '\n' ) {
                ;
            } else {
                break;
            } // if
        } // for i
        // -------------------------------------------------------------------------------------------------------------------------------------
        if( i >= 0 && i < ( str.length() - 1 ) ) {
            str = str.substring( 0, i + 1 );
        } // if
        // -------------------------------------------------------------------------------------------------------------------------------------
        return str;
    }

    public static String toUnicode(final String gbString)
    {
        char[] utfBytes = gbString.toCharArray();
        StringBuffer buffer = new StringBuffer();
        for( int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++ ) {
            String hexB = Integer.toHexString( utfBytes[byteIndex] );
            if( hexB.length() <= 2 ) {
                hexB = "00" + hexB;
            }
            buffer.append( "\\u" + hexB );
        }
        // -------------------------------------------------------------------------------------------------------------------------------------
        return buffer.substring( 0 );
    }

    public static String quote( String string ) {
        if( string == null || string.length() == 0 ) {
            return "";
        }

        char b;
        char c = 0;
        int i;
        int len = string.length();
        StringBuffer sb = new StringBuffer( len * 2 );
        String t;
        char[] chars = string.toCharArray();
        char[] buffer = new char[1030];
        int bufferIndex = 0;
        //sb.append( '"' );
        for( i = 0; i < len; i += 1 ) {
            if( bufferIndex > 1024 ) {
                sb.append( buffer, 0, bufferIndex );
                bufferIndex = 0;
            }
            b = c;
            c = chars[i];
            switch( c ) {
                case '\\':
                case '"':
                    buffer[bufferIndex++] = '\\';
                    buffer[bufferIndex++] = c;
                    break;
                case '/':
                    if( b == '<' ) {
                        buffer[bufferIndex++] = '\\';
                    }
                    buffer[bufferIndex++] = c;
                    break;
                default:
                    if( c < ' ' ) {
                        switch( c ) {
                            case '\b':
                                buffer[bufferIndex++] = '\\';
                                buffer[bufferIndex++] = 'b';
                                break;
                            case '\t':
                                buffer[bufferIndex++] = '\\';
                                buffer[bufferIndex++] = 't';
                                break;
                            case '\n':
                                buffer[bufferIndex++] = '\\';
                                buffer[bufferIndex++] = 'n';
                                break;
                            case '\f':
                                buffer[bufferIndex++] = '\\';
                                buffer[bufferIndex++] = 'f';
                                break;
                            case '\r':
                                buffer[bufferIndex++] = '\\';
                                buffer[bufferIndex++] = 'r';
                                break;
                            default:
                                t = "000" + Integer.toHexString( c );
                                int tLength = t.length();
                                buffer[bufferIndex++] = '\\';
                                buffer[bufferIndex++] = 'u';
                                buffer[bufferIndex++] = t.charAt( tLength - 4 );
                                buffer[bufferIndex++] = t.charAt( tLength - 3 );
                                buffer[bufferIndex++] = t.charAt( tLength - 2 );
                                buffer[bufferIndex++] = t.charAt( tLength - 1 );
                        }
                    } else {
                        buffer[bufferIndex++] = c;
                    }
            }
        }
        sb.append( buffer, 0, bufferIndex );
        //sb.append( '"' );
        return sb.toString();
    }

    /**
     * 將字串陣列轉化為in條件用的字串
     *
     * @param objs
     * @return
     */
    public static String getInClause(List<?> objs) {
        if(objs == null || objs.size() == 0)
            return null;
        Object[] array = new Object[objs.size()];
        objs.toArray(array);
        return getInClause(array);
    }

    /**
     * 將字串陣列轉化為in條件用的字串
     *
     * @param objs
     * @return
     */
    public static String getInClause(Object[] objs) {
        StringBuffer result = new StringBuffer();

        for (int i = 0; i < objs.length; i++) {
            if (i > 0) {
                result.append(",");
            }
            if (objs[i] instanceof String) {
                result.append("'" + objs[i] + "'");
            } else {
                result.append(objs[i]);
            }
        }
        return result.toString();
    }

    //以指定長度 分隔字串
    public static List< String > splitString(String str, int length) {
        List < String > ret = new ArrayList< String >();
        if (length >= str.length()) {
            ret.add(str);
        } else {
            while (length < str.length()) {
                ret.add(str.substring(0, length));
                str = str.substring(length, str.length());
            }
            ret.add(str);
        }
        return ret;
    }

    public static boolean startsWith(String str, String prefix) {
        return startsWith(str, prefix, false);
    }

    public static boolean startsWithIgnoreCase(String str, String prefix) {
        return startsWith(str, prefix, true);
    }

    private static boolean startsWith(String str, String prefix, boolean ignoreCase) {
        return str != null && prefix != null?(prefix.length() > str.length()?false:str.regionMatches(ignoreCase, 0, prefix, 0, prefix.length())):str == null && prefix == null;
    }
    public static Vector tokenize(String input, String delimiters)
    {
        Vector v = new Vector();
        StringTokenizer t = new StringTokenizer(input, delimiters);

        while (t.hasMoreTokens()) {
            v.addElement(t.nextToken());
        }
        return v;
    }

    public static String leftPad(String s, String padChar, int l)
    {
        while (s.length() < l) {
            s = padChar + s;
        }
        return s;
    }

    public static String dayOfWeek(int i){
        String dayOfWeek = "";
        switch (i){
            case 1: dayOfWeek = "一"; break;
            case 2: dayOfWeek = "二"; break;
            case 3: dayOfWeek = "三"; break;
            case 4: dayOfWeek = "四"; break;
            case 5: dayOfWeek = "五"; break;
            case 6: dayOfWeek = "六"; break;
            case 7: dayOfWeek = "日"; break;
        }
        return dayOfWeek;
    }

    public static boolean equals(String a, String b) {
        if( a == null ) {
            a = "";
        }
        if( b == null ) {
            b = "";
        }
        return a.equals(b);
    }

    /**
     *字串根據分割字元separator拆分后str1是否包含str2
     * @param str1
     * @param str2
     * @param separator
     * @return
     */
    public static boolean stringSplitContains(String str1, String str2, String separator) {
        if(isBlank(str1)) {
            return false;
        }
        if(isBlank(str2)) {
            return true;
        }
        String[] a = str1.split(separator);
        String[] b = str2.split(separator);

        for(int i = 0; i < a.length; i++) {
            a[i] = a[i].trim();
        }
        for(int i = 0; i < b.length; i++) {
            b[i] = b[i].trim();
        }

        Set<String> aSet = new HashSet(Arrays.asList(a));
        aSet.addAll(Arrays.asList(b));
        return aSet.size() == a.length;
    }

    /**
     * @param status
     * @return
     * 裝置狀態對應關係
     * 303 : hold[2], 2 : 欠料[3], 302 : reset[0], 其它狀態全部為pass[1]
     */
    public static String convertResourceStatus( String status ){
        String retStatus = "";
        switch ( status ){
            case "303": retStatus = "2"; break;
            case "2": retStatus = "3"; break;
            case "302": retStatus = "0"; break;
            default:retStatus = "1";break;
        }
        return retStatus;
    }
}