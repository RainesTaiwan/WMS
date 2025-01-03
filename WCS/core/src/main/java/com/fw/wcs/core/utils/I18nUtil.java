package com.fw.wcs.core.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * 國際化語言工具類
 *
 * @author ervin
 * @date 2019-03-13
 */
public class I18nUtil {

    /**
     * 獲取請求語言
     *
     * @return
     */
    public static Locale getLocale(){
        Locale.setDefault( Locale.CHINA );
        return LocaleContextHolder.getLocale();
    }

    public static MessageSource getMessageSource(){
        MessageSource messageSource = SpringUtil.getBean(MessageSource.class);
        return messageSource;
    }

    /**
     * 獲取國際化文字
     *
     * @param key 國際化鍵值
     * @param params 參數
     * @return
     */
    public static String getI18nText( String key, Object[] params ){
        return getMessageSource().getMessage( key, params, getLocale() );
    }

    /**
     * 獲取國際化文字
     *
     * @param key 國際化鍵值
     * @return
     */
    public static String getI18nText( String key ){
        return getMessageSource().getMessage( key, null, getLocale() );
    }
}
