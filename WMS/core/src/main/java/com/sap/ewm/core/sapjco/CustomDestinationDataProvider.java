package com.sap.ewm.core.sapjco;

import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.DestinationDataProvider;

import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class CustomDestinationDataProvider implements DestinationDataProvider {

    ConcurrentHashMap<String, Properties> destinationDB = new ConcurrentHashMap<String, Properties>();

    @Override
    public Properties getDestinationProperties(String destinationName) {
        return destinationDB.get( destinationName );
    }

    @Override
    public boolean supportsEvents() {
        return false;
    }

    @Override
    public void setDestinationDataEventListener(DestinationDataEventListener destinationDataEventListener) {

    }

    public void addDestinationProperties(String destinationName,Properties properties){
        destinationDB.put( destinationName, properties );
    }

}
