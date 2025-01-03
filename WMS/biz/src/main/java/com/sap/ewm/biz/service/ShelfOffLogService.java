package com.sap.ewm.biz.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sap.ewm.biz.model.ShelfOffLog;
import com.sap.ewm.core.base.FrontPage;

import java.util.List;

/**
 * <p>
 * shelf off log 服務類
 * </p>
 *
 * @author syngna
 * @since 2020-08-04
 */
public interface ShelfOffLogService extends IService<ShelfOffLog> {

    /**
     *  分頁查詢
     * @param frontPage
     * @return
     */
    IPage<ShelfOffLog> selectPage(FrontPage<ShelfOffLog> frontPage, ShelfOffLog shelfOffLog);

    List<ShelfOffLog> selectList(ShelfOffLog shelfOffLog);

    // 透過MessageId找ShelfOffLog
    ShelfOffLog findShelfOffLogByMessageId(String messageId);
}