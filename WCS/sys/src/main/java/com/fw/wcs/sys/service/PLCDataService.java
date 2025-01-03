package com.fw.wcs.sys.service;

import com.fw.wcs.sys.model.PLCData;
import com.baomidou.mybatisplus.service.IService;

import java.util.Date;
import java.util.List;

/**
 *  服務類
 *
 * @author Glanz
 *
 */
public interface PLCDataService extends IService<PLCData>{
    //透過輸送帶找尋PLCData
    PLCData findPLCDataByConveyor(String conveyor);

    //將plcData存入
    void insertPLCData(PLCData plcData);

    // plcData進行讀取判斷處理
    void readPLCData(String lenNum, List<String> dataList);

    // plc 資料分析
    void analyzePLCData(String conveyor, List<String> dataList);

    // plc 無響應處理
    void plcNoResponse(String lenNum);

    // plcData進行書寫判斷處理
    void writePLCData(String lenNum, int[] data);

    // 將16 位元的int轉成 2個 Byte
    byte[] hexIntToByteArray(int value);

    // 將  32 位元 共 4個Byte/16 位元 共 2個Byte 轉成 int，num表示有幾個Byte
    int byteArrayToInt(byte[] hexByte, int num);

    // 模擬發送PLCData
    void SimulationPLCData(String conveyor, String Type, int[] data);


}
