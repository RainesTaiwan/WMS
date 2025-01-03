package com.sap.ewm.biz.mapper;

import com.sap.ewm.biz.model.ItemType;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 物料型別主數據 Mapper 介面
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
@Repository
public interface ItemTypeMapper extends BaseMapper<ItemType> {
    ItemType findItemTypeByHandle(@Param("handle") String handle);
}