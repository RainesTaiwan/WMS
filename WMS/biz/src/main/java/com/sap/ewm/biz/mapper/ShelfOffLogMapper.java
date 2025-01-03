package com.sap.ewm.biz.mapper;

import com.sap.ewm.biz.model.ShelfOffLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * shelf off log Mapper 介面
 * </p>
 *
 * @author syngna
 * @since 2020-08-04
 */
@Repository
public interface ShelfOffLogMapper extends BaseMapper<ShelfOffLog> {
    ShelfOffLog findShelfOffLogByMessageId(@Param("messageId") String messageId);
}