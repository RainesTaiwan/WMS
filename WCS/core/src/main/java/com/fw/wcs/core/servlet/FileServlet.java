package com.fw.wcs.core.servlet;

import com.fw.wcs.core.base.AjaxResult;
import com.fw.wcs.core.utils.ObjectMapperUtil;
import com.fw.wcs.core.utils.SpringUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.util.ReflectionUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.List;

public class FileServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost( req, resp );
    }

    protected void doPost( HttpServletRequest req, HttpServletResponse resp ) throws ServletException, IOException {
        AjaxResult ajaxResult = null;
        try {
            FileItem fileItem = parseFile( req );
            String pathInfo = req.getPathInfo();
            String[] paths = pathInfo.split( "/" );
            if( fileItem != null && paths.length >=2 ){
                Object target = SpringUtil.getBean( paths[1] );
                Method method = target.getClass().getDeclaredMethod( paths[2], FileItem.class, HttpServletRequest.class );
                ReflectionUtils.makeAccessible( method );
                Object result = method.invoke( target, fileItem, req );
                ajaxResult = AjaxResult.success().put( "value", result );
            }else {
                ajaxResult = AjaxResult.success();
            }
        } catch ( Exception e ) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw, true);
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
            ajaxResult = AjaxResult.error( sw.toString() );
        }
        resp.setCharacterEncoding( "UTF-8" );
        resp.getWriter().println( ObjectMapperUtil.writeValueAsString( ajaxResult ) );
        resp.getWriter().flush();
        resp.getWriter().close();
    }

    public FileItem parseFile( HttpServletRequest req ){
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold( DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD );
        ServletFileUpload upload = new ServletFileUpload( factory );
        upload.setSizeMax( 1024 * 1024 * 20 );
        upload.setHeaderEncoding( "UTF-8" );
        List<FileItem> fileItems = null;
        try {
            fileItems = upload.parseRequest( req );
            for( FileItem fileItem : fileItems ){
                if( !fileItem.isFormField() ){
                    return fileItem;
                }
            }
        } catch (FileUploadException e) {
            e.printStackTrace();
        }
        return null;
    }
}
