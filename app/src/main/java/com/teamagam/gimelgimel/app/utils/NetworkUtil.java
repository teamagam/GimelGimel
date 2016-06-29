package com.teamagam.gimelgimel.app.utils;

import android.support.annotation.Nullable;

import com.teamagam.gimelgimel.app.common.logging.Logger;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;


/**
 * A static class for simple network configuration access
 */
public class NetworkUtil {
    private static final Logger sLogger = LoggerFactory.create(NetworkUtil.class);

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
        String macString = null;
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
                sLogger.d("No matching interface found for name: " + interfaceName);
                sLogger.d("Using UUID mac-address fallback");
                macString = java.util.UUID.randomUUID().toString();
            } else {
                macString = getHardwareAddress(networkInterface);
            }
        } catch (Exception ex) {
            sLogger.e("Failed retrieving MAC address");
            //todo: handle exception
        }

        return macString;
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
