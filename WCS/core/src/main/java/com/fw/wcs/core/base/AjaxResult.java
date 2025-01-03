package com.fw.wcs.core.base;

import java.util.HashMap;

/**
 * 操作訊息提醒
 * 
 * @author ruoyi
 */
public class AjaxResult extends HashMap<String, Object>
{
    private static final long serialVersionUID = 1L;

    /**
     * 初始化一個新建立的 Message 對像
     */
    private AjaxResult()
    {
    }

    private static AjaxResult header( int code, String msg ){
        AjaxResult header = new AjaxResult();
        header.put( "code", code );
        header.put( "message", msg );
        AjaxResult wrapper = new AjaxResult();
        wrapper.put( "header", header );
        return wrapper;
    }

    /**
     * 返回錯誤訊息
     * 
     * @return 錯誤訊息
     */
    public static AjaxResult error()
    {
        return error("操作失敗");
    }

    /**
     * 返回錯誤訊息
     * 
     * @param msg 內容
     * @return 錯誤訊息
     */
    public static AjaxResult error(String msg)
    {
        return error(500, msg);
    }

    /**
     * 返回錯誤訊息
     * 
     * @param code 錯誤碼
     * @param msg 內容
     * @return 錯誤訊息
     */
    public static AjaxResult error(int code, String msg)
    {
        return header( code, msg );
    }

    /**
     * 返回成功訊息
     * 
     * @param msg 內容
     * @return 成功訊息
     */
    public static AjaxResult success(String msg)
    {
        return header( 0, msg );
    }
    
    /**
     * 返回成功訊息
     * 
     * @return 成功訊息
     */
    public static AjaxResult success()
    {
        return success("操作成功");
    }

    public static <T> AjaxResult success( T value )
    {
        return success().value( value );
    }

    /**
     * 返回成功訊息
     * 
     * @param key 鍵值
     * @param value 內容
     * @return 成功訊息
     */
    @Override
    public AjaxResult put(String key, Object value)
    {
        super.put(key, value);
        return this;
    }

    /**
     * 返回資訊增加數據
     *
     * @param value
     * @return
     */
    public <T> AjaxResult value( T value ){
        this.put( "value", value );
        return this;
    }
}
