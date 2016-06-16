package com.teamagam.gimelgimel.app.network.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * A receiver that is notified for any connectivity problem and changes.
 * The receiver uses {@link NetworkAvailableListener} to notify other classes about any changes in the network.
 */
public class ConnectivityStatusReceiver extends BroadcastReceiver {

    public static final String INTENT_NAME = "connectivityStatus";
    public static final String NETWORK_AVAILABLE_EXTRA = "networkAvailable";

    private NetworkAvailableListener mNetworkAvailableListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isNetworkAvailable = intent.getBooleanExtra(NETWORK_AVAILABLE_EXTRA, true);

        if(mNetworkAvailableListener != null) {
            mNetworkAvailableListener.onNetworkAvailableChange(isNetworkAvailable);
        }
    }

    public void setListener(NetworkAvailableListener listener) {
        mNetworkAvailableListener = listener;
    }

    /**
     * Listener for {@link NetworkChangeReceiver},
     * it gets updates when the network state changes
     */
    public interface NetworkAvailableListener {
        void onNetworkAvailableChange(boolean isNetworkAvailable);
    }
}
