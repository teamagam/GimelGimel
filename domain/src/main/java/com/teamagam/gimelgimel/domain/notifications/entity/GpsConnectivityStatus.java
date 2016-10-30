package com.teamagam.gimelgimel.domain.notifications.entity;

/**
 *
 */
public class GpsConnectivityStatus {

    public static GpsConnectivityStatus createConnected() {
        return new GpsConnectivityStatus(true);
    }

    public static GpsConnectivityStatus createDisconnected() {
        return new GpsConnectivityStatus(false);
    }

    private boolean mIsConnected;

    private GpsConnectivityStatus(boolean isConnected) {
        mIsConnected = isConnected;
    }

    public boolean isConnected() {
        return mIsConnected;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        GpsConnectivityStatus that = (GpsConnectivityStatus) o;

        return mIsConnected == that.mIsConnected;
    }

    @Override
    public int hashCode() {
        return (mIsConnected ? 1 : 0);
    }
}
