package com.sap.ewm.biz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sap.ewm.biz.model.AsrsRfid;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ASRS訂單物料主數據 Mapper 介面
 *
 * @author Glanz
 * @since 2020-04-20
 */
@Repository
public interface ASRSFRIDMapper extends BaseMapper<AsrsRfid> {
    void updateAsrsRfidByID(@Param("handle") String handle, @Param("et") AsrsRfid asrsRfid);
    AsrsRfid findAsrsRfidByID(@Param("handle") String handle);
    List<AsrsRfid> findRFIDByVoucherNo(@Param("voucherNo") String voucherNo);
    List<AsrsRfid> findRFIDByVoucherNoWithStatus(@Param("voucherNo") String voucherNo, @Param("status") String status);
    List<AsrsRfid> findRFIDByCarrierWithStatus(@Param("carrier") String carrier, @Param("status") String status);
    List<AsrsRfid> findRFIDByHandlingID(@Param("handlingId") String handlingId);
    List<AsrsRfid> findRFIDByWOSERIAL(@Param("woSerial") String woSerial);
}
