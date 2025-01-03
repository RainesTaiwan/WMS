package com.sap.ewm.core.base;

import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class BaseController {

    public void fileDownload(File file, Boolean delete, HttpServletResponse response, HttpServletRequest request )
    {
        try
        {
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition", "attachment;fileName=" + setFileDownloadHeader(request, file.getName()));
            FileInputStream fis = new FileInputStream( file );
            OutputStream out = response.getOutputStream();
            IOUtils.copy( fis, response.getOutputStream() );
            fis.close();
            out.flush();
            out.close();
            if (delete)
            {
                file.delete();
            }
        }
        catch (Exception e)
        {

        }
    }

    public String setFileDownloadHeader(HttpServletRequest request, String fileName) throws UnsupportedEncodingException
    {
        final String agent = request.getHeader("USER-AGENT");
        String filename = fileName;
        if (agent.contains("MSIE"))
        {
            // IE瀏覽器
            filename = URLEncoder.encode(filename, "utf-8");
            filename = filename.replace("+", " ");
        }
        else if (agent.contains("Firefox"))
        {
            // 火狐瀏覽器
            filename = new String(fileName.getBytes(), "ISO8859-1");
        }
        else if (agent.contains("Chrome"))
        {
            // google瀏覽器
            filename = URLEncoder.encode(filename, "utf-8");
        }
        else
        {
            // 其它瀏覽器
            filename = URLEncoder.encode(filename, "utf-8");
        }
        return filename;
    }
}
