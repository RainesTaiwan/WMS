package com.fw.wcs.sys.service;

import com.baomidou.mybatisplus.service.IService;
import com.fw.wcs.sys.model.ReceiveStation;

/**
 * <p>
 *  服務類
 * </p>
 *
 * @author Leon
 *
 */
public interface ReceiveStationService extends IService<ReceiveStation> {

    ReceiveStation getOutboundReceiveStation();
    ReceiveStation getReceiveStation(String receiveStation);

    //感測棧板在指定輸送帶的指定工位上，有回傳1，沒有回傳0
    //代號【CHK_SP_CNV】感測棧板
    boolean palletOnReceiveStation(String receiveStation, String station);

    //感測棧板的長寬高重在指定輸送帶上，若棧板資訊合法，則回傳1，反之則回傳0
    //代號【CHK_SP_INFO】確認棧板信息
    boolean palletInfoCheck(String palletId, String receiveStation);

    void updateReceiveStation(ReceiveStation receiveStation);

    // 由PLC發起的檢查，確認任務是否執行完畢
    void plcCheckReceiveStation(String receiveStation, String station);

    // 輸送帶狀態報告ASRS (先告知WMS  由WMS告知ASRS)
    void reportASRS(String receiveStation, String type);
}