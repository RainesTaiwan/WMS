package com.fw.wcs.core.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class IPUtil {

    public static String getIp(){
        String serverIp = "";
        try {
            Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();
            InetAddress ip = null;
            while (netInterfaces.hasMoreElements() && serverIp == "") {
                NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
                Enumeration inetAddrs = ni.getInetAddresses();
                while ( inetAddrs.hasMoreElements() ){
                    ip = (InetAddress) inetAddrs.nextElement();
                    if (!ip.isLoopbackAddress() && ip.isSiteLocalAddress() ) {
                        serverIp = ip.getHostAddress();
                        break;
                    } else {
                        ip = null;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return serverIp;
    }
}
