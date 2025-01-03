package com.fw.wcs.core.base;

import com.baomidou.mybatisplus.plugins.Page;

import java.util.List;

/**
 * 用來接收頁面傳過來的查詢欄位對像
 * Created by Administrator on 2018/10/10.
 */
public class FrontPage<T> {
    //是否是查詢
    private String globalQuery;

    //時間戳（毫秒）
    private String nd;

    //每頁顯示條數
    private int rows;

    //目前頁數
    private int page;

    //升序的欄位
    private List<String> ascx;

    //降序的欄位
    private List<String> descx;

    public String getGlobalQuery() {
        return globalQuery;
    }

    public void setGlobalQuery(String globalQuery) {
        this.globalQuery = globalQuery;
    }

    public String getNd() {
        return nd;
    }

    public void setNd(String nd) {
        this.nd = nd;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<String> getAscx() {
        return ascx;
    }

    public void setAscx(List<String> ascx) {
        this.ascx = ascx;
    }

    public List<String> getDescx() {
        return descx;
    }

    public void setDescx(List<String> descx) {
        this.descx = descx;
    }

    //獲取mybatisPlus封裝的Page對像
    public Page<T> getPagePlus() {
        Page<T> pagePlus = new Page<T>();
        pagePlus.setCurrent(this.page);
        pagePlus.setSize(this.rows);
        pagePlus.setAscs(this.ascx);
        pagePlus.setDescs( this.descx );
        return pagePlus;
    }
}
