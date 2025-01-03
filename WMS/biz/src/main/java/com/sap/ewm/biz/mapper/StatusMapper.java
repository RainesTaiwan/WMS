package com.sap.ewm.biz.mapper;

import com.sap.ewm.biz.model.Status;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 狀態主數據 Mapper 介面
 * </p>
 *
 * @author Syngna
 * @since 2020-07-06
 */
@Repository
public interface StatusMapper extends BaseMapper<Status> {
    List<Status> listStatusByStatusGroup(@Param("locale") String locale, @Param("statusGroup") String statusGroup);
}