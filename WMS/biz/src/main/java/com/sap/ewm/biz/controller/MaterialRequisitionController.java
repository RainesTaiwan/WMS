package com.sap.ewm.biz.controller;

import com.alibaba.fastjson.JSONObject;
import com.sap.ewm.biz.constants.CommonConstants;
import com.sap.ewm.biz.dto.MaterialRequisitionDTO;
import com.sap.ewm.biz.dto.MaterialRequisitionStorageBinDTO;
import com.sap.ewm.biz.model.ShelfOffLog;
import com.sap.ewm.biz.service.MaterialRequisitionService;
import com.sap.ewm.biz.service.ShelfOffLogService;
import com.sap.ewm.core.base.AjaxResult;
import com.sap.ewm.core.security.common.CommonMethods;
import com.sap.ewm.core.utils.CacheUtil;
import com.sap.ewm.core.utils.DateUtil;
import com.sap.ewm.core.utils.StringUtils;
import com.sap.ewm.sys.service.MessageSendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author Syngna
 * @since 2020-07-06
 */
@RestController
@RequestMapping("/material-requisition")
public class MaterialRequisitionController {

    @Autowired
    private MaterialRequisitionService materialRequisitionService;

    @Autowired
    private MessageSendService messageSendService;

    @Autowired
    private ShelfOffLogService shelfOffLogService;

    /**
     * @param materialRequisitionDTO
     * @return
     */
    @ResponseBody
    @GetMapping("/requisition-confirm")
    public AjaxResult doMaterialRequisition(MaterialRequisitionDTO materialRequisitionDTO) {
        List<MaterialRequisitionStorageBinDTO> materialRequisitionStorageBinDTOList = materialRequisitionService.doMaterialRequisition(CommonMethods.getUser(), materialRequisitionDTO);
        if (materialRequisitionStorageBinDTOList == null || materialRequisitionStorageBinDTOList.size() == 0) {
            return AjaxResult.error("未找物料[" + materialRequisitionDTO.getItem() + "]對應的可用庫存");
        }
        String queueName = "carrier.outbound.notice.process";
        for (MaterialRequisitionStorageBinDTO materialRequisitionStorageBinDTO : materialRequisitionStorageBinDTOList) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("MESSAGE_TYPE" , queueName);
            jsonObject.put("MESSAGE_ID" , StringUtils.createQUID());
            jsonObject.put("CORRELATION_ID" , StringUtils.createQUID());
            jsonObject.put("SEND_TIME" , DateUtil.format(new Date()));
            jsonObject.put("CARRIER" , StringUtils.trimHandle(materialRequisitionStorageBinDTO.getCarrierBo()));
            jsonObject.put("STORAGE_LOCATION" , StringUtils.trimHandle(materialRequisitionStorageBinDTO.getStorageLocationBo()));
            jsonObject.put("STORAGE_BIN" , materialRequisitionStorageBinDTO.getStorageBin());
            jsonObject.put("QTY" , materialRequisitionStorageBinDTO.getQty());
            jsonObject.put("SPLIT" , materialRequisitionStorageBinDTO.isSplit() ? CommonConstants.Y : CommonConstants.N);

            ShelfOffLog shelfOffLog = new ShelfOffLog();
            shelfOffLog.setmessageId(jsonObject.getString("MESSAGE_ID"));
            shelfOffLog.setCorrelationId(jsonObject.getString("CORRELATION_ID"));
            shelfOffLog.setActionCode("MR"); // marerial requisition
            shelfOffLog.setCarrier(jsonObject.getString("CARRIER"));
            shelfOffLog.setStorageLocation(jsonObject.getString("STORAGE_LOCATION"));
            shelfOffLog.setStorageBin(jsonObject.getString("STORAGE_BIN"));
            shelfOffLog.setInventoryBo(materialRequisitionStorageBinDTO.getInventoryBo());
            shelfOffLog.setQty(materialRequisitionStorageBinDTO.getQty());
            shelfOffLog.setSplit(jsonObject.getString("SPLIT"));
            shelfOffLog.setCreateTime(LocalDateTime.now());
            shelfOffLogService.save(shelfOffLog);
            messageSendService.send(queueName, jsonObject);
        }
        return AjaxResult.success();
    }
}