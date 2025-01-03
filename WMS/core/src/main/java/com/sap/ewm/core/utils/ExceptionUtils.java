package com.sap.ewm.core.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author Ervin Chen
 * @date 2020/2/11 15:57
 */
public class ExceptionUtils {

    public static String toString(Exception e){
        StringWriter stringWriter= new StringWriter();
        PrintWriter writer= new PrintWriter(stringWriter);
        e.printStackTrace(writer);
        StringBuffer buffer= stringWriter.getBuffer();
        return buffer.toString();
    }
}
