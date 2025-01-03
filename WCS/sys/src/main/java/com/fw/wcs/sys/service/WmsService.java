package com.fw.wcs.sys.service;

public interface WmsService {

    void carrierOutboundTask(String carrier, String storageBin, String correlationId);
    void carrierInStorage(String carrie, String storageBin);
    void carrierOutStorage(String carrie, String storageBin);
}
