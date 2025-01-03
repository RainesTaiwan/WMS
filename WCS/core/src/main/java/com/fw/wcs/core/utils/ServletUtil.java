package com.fw.wcs.core.utils;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 客戶端工具類
 *
 */
public class ServletUtil
{
    /**
     * 獲取String參數
     */
    public static String getParameter(String name)
    {
        return getRequest() == null ? null : getRequest().getParameter(name);
    }



    /**
     * 獲取request
     */
    public static HttpServletRequest getRequest()
    {
        return getRequestAttributes() == null ? null : getRequestAttributes().getRequest();
    }

    /**
     * 獲取response
     */
    public static HttpServletResponse getResponse()
    {
        return getRequestAttributes() == null ? null : getRequestAttributes().getResponse();
    }

    /**
     * 獲取session
     */
    public static HttpSession getSession()
    {
        return getRequest() == null ? null : getRequest().getSession();
    }

    public static ServletRequestAttributes getRequestAttributes()
    {
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        return (ServletRequestAttributes) attributes;
    }

    /**
     * 將字串渲染到客戶端
     * 
     * @param response 渲染對像
     * @param string 待渲染的字串
     * @return null
     */
    public static String renderString(HttpServletResponse response, String string)
    {
        try
        {
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
