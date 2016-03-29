package com.teamagam.gimelgimel.app.utils;

import android.support.annotation.Nullable;
import android.util.Log;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;


/**
 * A static class for simple network configuration access
 */
public class NetworkUtil {


    private static final String LOG_TAG = NetworkUtil.class.getSimpleName();

    //todo: move to config
    private static String DEFAULT_MAC_NETWORK;

    private static InetAddress sIp = getIpAddress();
    private static String sMacAddress = getMacAddress(DEFAULT_MAC_NETWORK);

    private static InetAddress getIpAddress() {
        try {
            return InetAddress.getLocalHost();
        } catch (Exception e) {
            //todo: handle exception
        }
        return null;
    }

    /**
     * Returns MAC address of the given interface name.
     *
     * @param interfaceName eth0, wlan0 or NULL=use first interface
     * @return mac address or empty string
     */
    private static String getMacAddress(String interfaceName) {
        //todo: move to config
        if (!PlatformUtil.isEmulator()) {
            interfaceName = "wlan0";
        } else {
            interfaceName = null;
        }
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            NetworkInterface networkInterface = null;
            while (interfaces.hasMoreElements()) {
                NetworkInterface current = interfaces.nextElement();
                if (interfaceName == null ||
                        interfaceName.equalsIgnoreCase(current.getName())) {
                    networkInterface = current;
                    break;
                }
            }

            if (networkInterface == null) {
                Log.d(LOG_TAG, "No matching interface found for name: " + interfaceName);
                return null;
            }

            return getHardwareAddress(networkInterface);
        } catch (Exception ex) {
            Log.e(LOG_TAG, "Failed retrieving MAC address");
            //todo: handle exception
        }
        return null;
    }

    @Nullable
    private static String getHardwareAddress(
            NetworkInterface networkInterface) throws SocketException {
        byte[] mac = networkInterface.getHardwareAddress();
        if (mac == null) {
            return null;
        }

        StringBuilder buf = new StringBuilder();
        for (int idx = 0; idx < mac.length; idx++) {
            buf.append(String.format("%02X:", mac[idx]));
        }
        if (buf.length() > 0) {
            buf.deleteCharAt(buf.length() - 1);
        }
        return buf.toString();
    }

    public static InetAddress getIp() {
        return sIp;
    }

    public static String getMac() {
        return sMacAddress;
    }
}
