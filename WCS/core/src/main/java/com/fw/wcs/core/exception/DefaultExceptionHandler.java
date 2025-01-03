package com.fw.wcs.core.exception;

import com.fw.wcs.core.base.AjaxResult;
import com.fw.wcs.core.base.CustomLocalDateTimeEditor;
import com.fw.wcs.core.utils.I18nUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;

@ControllerAdvice
@ResponseBody
public class DefaultExceptionHandler {

    Logger logger  = LoggerFactory.getLogger( DefaultExceptionHandler.class );

    /**
     * 將前臺傳遞過來的日期格式的字串，自動轉化為Date型別
     */
    @InitBinder
    public void initBinder(WebDataBinder binder)
    {
        binder.registerCustomEditor(LocalDateTime.class, new CustomLocalDateTimeEditor("yyyy-MM-dd HH:mm:ss", true));
    }

    /**
     * 系統異常
     */
    @ExceptionHandler({ RuntimeException.class, Exception.class })
    public AjaxResult handleException(Exception e)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw, true);
        e.printStackTrace(pw);
        pw.flush();
        sw.flush();
        logger.error( sw.toString() );
        return AjaxResult.error(I18nUtil.getI18nText("system.error") + " :"+ e.getMessage());
    }
}
