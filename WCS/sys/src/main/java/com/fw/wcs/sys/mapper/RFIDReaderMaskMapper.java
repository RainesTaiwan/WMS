package com.fw.wcs.sys.mapper;

import com.fw.wcs.sys.model.CarrierTask;
import com.fw.wcs.sys.model.RFIDReader;
import com.fw.wcs.sys.model.RFIDReaderMask;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Glanz
 *
 */
public interface RFIDReaderMaskMapper extends BaseMapper<RFIDReaderMask> {
    void deleteRFID(@Param("voucherNo") String voucherNo);
    List<RFIDReaderMask> findRFIDList(@Param("voucherNo") String voucherNo);
}
