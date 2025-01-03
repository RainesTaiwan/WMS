package com.sap.ewm.biz.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sap.ewm.biz.model.ReportAsrs;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 回報ASRS訂單結果要求主數據 Mapper 介面
 *
 * @author Glanz
 * @since 2020-08-12
 */
@Repository
public interface ReportASRSMapper extends BaseMapper<ReportAsrs>{
    List<ReportAsrs> findASRSOrderByWoSerial(@Param("woSerial") String woSerial);
}
