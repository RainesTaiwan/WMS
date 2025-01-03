package com.sap.ewm.biz.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sap.ewm.biz.model.ReportAsrs;

/**
 * 回報ASRS訂單結果要求主數據 服務類
 *
 * @author Glanz
 * @since 2020-08-12
 */
public interface ReportASRSService extends IService<ReportAsrs>{
    // SAVE回報清單資訊
    void saveReportASRS(String carrier, String woSerial, String handlingId);

    // 回報ASRS指定工單內容WO.Result
    void reportASRS(String woSerial);

    // IO 類型決定
    String woTypeToIOType(String woType);

    // IN、OUT 類型決定
    String woTypeToInOutType(String woType);
}
