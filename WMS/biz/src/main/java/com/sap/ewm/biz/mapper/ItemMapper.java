package com.sap.ewm.biz.mapper;


import com.sap.ewm.biz.model.Item;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 物料主數據 Mapper 介面
 * </p>
 *
 * @author Glanz
 * @since 2020-04-21
 */
@Repository
public interface ItemMapper extends BaseMapper<Item> {
    Item findItemByName(@Param("item") String item);
}