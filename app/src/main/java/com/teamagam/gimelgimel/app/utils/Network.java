package com.teamagam.gimelgimel.app.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * Created by Gil.Raytan on 21-Mar-16.
 */
public class Network {

    private static InetAddress ip;

    public static String updateMacAdress()
    {
        String mac = null;
        try {
            ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            mac = network.getHardwareAddress().toString();
        }
        catch (Exception e)
        {
            //todo::
        }
        return mac;
    }


}
