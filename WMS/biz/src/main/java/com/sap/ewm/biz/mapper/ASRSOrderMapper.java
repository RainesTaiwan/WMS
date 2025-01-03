package com.sap.ewm.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sap.ewm.biz.model.AsrsOrder;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 訂單主數據 Mapper 介面
 *
 * @author Glanz
 * @since 2020-04-20
 */
@Repository
public interface ASRSOrderMapper extends BaseMapper<AsrsOrder> {
    List<AsrsOrder> findASRSOrderByWoSerial(@Param("woSerial") String woSerial);
    List<AsrsOrder> findASRSOrderByMessageId(@Param("messageId") String messageId);
    List<AsrsOrder> findASRSOrderByVoucherNo(@Param("voucherNo") String voucherNo);
    List<AsrsOrder> findASRSOrderByStatus(@Param("status") String status);
    List<AsrsOrder> findASRSOrderByResourceAndStatus(@Param("resource") String resource, @Param("status") String status);
    AsrsOrder findASRSOrderById(@Param("handle") String handle);
}