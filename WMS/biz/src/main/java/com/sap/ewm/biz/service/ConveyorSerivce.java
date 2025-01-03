package com.sap.ewm.biz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sap.ewm.biz.model.Conveyor;
import java.util.List;

/**
 * Conveyor主數據 服務類
 *
 * @author Glanz
 * @since 2021-08-10
 */
public interface ConveyorSerivce extends IService<Conveyor>{
    // 取出輸送帶工作量
    int[] findAllConveyorWorkload();

    // 取出指定輸送帶工作量
    int findConveyorWorkload(String conveyor);

    // 取出輸送帶優先權
    int[] findAllConveyorPriority();

    // 告知選擇輸送帶
    String selectConveyor(String woType);

    // 增加輸送帶工作量
    void changeConveyorWorkload(String conveyor, int num);

}
