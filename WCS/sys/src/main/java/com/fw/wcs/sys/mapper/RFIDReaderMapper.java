package com.fw.wcs.sys.mapper;

import com.fw.wcs.sys.model.RFIDReader;
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

@Repository
public interface RFIDReaderMapper extends BaseMapper<RFIDReader> {
    void updateStatusByReaderID(@Param("readerID") String readerID, @Param("et") RFIDReader rfidReader);
    RFIDReader findRFIDReaderID(@Param("receiveStation") String receiveStation, @Param("station") String station);
    RFIDReader findReceiveStation(@Param("readerID") String readerID);
}