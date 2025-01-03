package com.fw.wcs.sys.mapper;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.fw.wcs.sys.model.RFIDReader;
import com.fw.wcs.sys.model.RFIDReaderMask;
import com.fw.wcs.sys.model.RFIDReaderTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @author Glanz
 *
 */
public interface RFIDReaderTaskMapper extends BaseMapper<RFIDReaderTask> {
    List<RFIDReaderTask> findTaskByMessageID(@Param("messageID") String messageID);
    List<RFIDReaderTask> findTaskByRFID(@Param("readerID") String readerID);
}
