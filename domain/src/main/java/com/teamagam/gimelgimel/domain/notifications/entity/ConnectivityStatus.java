package com.teamagam.gimelgimel.domain.notifications.entity;

/**
 *
 */
public class ConnectivityStatus {

    public static final ConnectivityStatus CONNECTED = new ConnectivityStatus(true);

    public static final ConnectivityStatus DISCONNECTED = new ConnectivityStatus(false);

    private boolean mIsConnected;

    private ConnectivityStatus(boolean isConnected) {
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

        ConnectivityStatus that = (ConnectivityStatus) o;

        return mIsConnected == that.mIsConnected;
    }

    @Override
    public int hashCode() {
        return (mIsConnected ? 1 : 0);
    }
}
