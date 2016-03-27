package com.teamagam.gimelgimel.app.utils;

import android.content.Context;

import com.teamagam.gimelgimel.R;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

/**
 * Created by Gil.Raytan on 21-Mar-16.
 */
public class NetworkUtil {


    private static String source = "wlan0";
    private static InetAddress mIp = getIpAddress();
    private static String mMac = getMacAddress(source);

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
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                if (interfaceName != null) {
                    if (!intf.getName().equalsIgnoreCase(interfaceName)) {
                        continue;
                    }
                }
                byte[] mac = intf.getHardwareAddress();
                if (mac == null)  {
                    return null;
                }

                StringBuilder buf = new StringBuilder();
                for (int idx = 0; idx < mac.length; idx++)
                    buf.append(String.format("%02X:", mac[idx]));
                if (buf.length() > 0) {
                    buf.deleteCharAt(buf.length() - 1);
                }
                return buf.toString();
            }
        } catch (Exception ex) {
            //todo: handle exception
        }
        return null;
    }

    public static InetAddress getIp() {
        return mIp;
    }

    public static void setIp(InetAddress mIp) {
        NetworkUtil.mIp = mIp;
    }

    public static String getMac() {
        return mMac;
    }

    public static void setMac(String mMacAddress) {
        NetworkUtil.mMac = mMacAddress;
    }

}
