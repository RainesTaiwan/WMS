package com.sap.ewm.core.I18n;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

/**
 * 資原始檔配置載入
 * 
 * @author ervin
 */
@Configuration
public class I18nConfiguration extends WebMvcConfigurerAdapter
{
    @Bean
    public LocaleResolver localeResolver()
    {
        /*SessionLocaleResolver slr = new SessionLocaleResolver();
        // 預設語言
        slr.setDefaultLocale(Locale.SIMPLIFIED_CHINESE);
        return slr;*/

        CookieLocaleResolver clr = new CookieLocaleResolver();
        clr.setCookieName( "lang" );
        clr.setCookieMaxAge( -1 );
        clr.setCookiePath("/");
        clr.setDefaultLocale( Locale.SIMPLIFIED_CHINESE );
        return clr;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor()
    {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        // 參數名
        lci.setParamName("lang");
        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry)
    {
        super.addInterceptors( registry );
        registry.addInterceptor(localeChangeInterceptor());
    }
}