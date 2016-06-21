package com.teamagam.gimelgimel.app.network.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import org.jetbrains.annotations.NotNull;

/**
 * A receiver that is notified for any connectivity problem and changes.
 * The receiver uses {@link NetworkAvailableListener} to notify other classes about any changes in the network.
 */
public class ConnectivityStatusReceiver extends BroadcastReceiver {

    public static final String INTENT_NAME = "connectivityStatus";
    public static final String NETWORK_AVAILABLE_EXTRA = "networkAvailable";

    /**
     * Broadcasts no network message to {@link ConnectivityStatusReceiver}
     * @param context The context to use
     */
    public static void broadcastNoNetwork(Context context) {
        Intent intent = new Intent(ConnectivityStatusReceiver.INTENT_NAME);
        intent.putExtra(ConnectivityStatusReceiver.NETWORK_AVAILABLE_EXTRA, false);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    /**
     * Broadcasts that the network is available to {@link ConnectivityStatusReceiver}
     * @param context The context to use
     */
    public static void broadcastAvailableNetwork(Context context) {
        Intent intent = new Intent(ConnectivityStatusReceiver.INTENT_NAME);
        intent.putExtra(ConnectivityStatusReceiver.NETWORK_AVAILABLE_EXTRA, true);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private NetworkAvailableListener mNetworkAvailableListener;
    private boolean mIsNetworkAvailable;

    public ConnectivityStatusReceiver(@NotNull NetworkAvailableListener listener) {
        mNetworkAvailableListener = listener;
        mIsNetworkAvailable = true;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isNetworkAvailable = intent.getBooleanExtra(NETWORK_AVAILABLE_EXTRA, true);

        if(mNetworkAvailableListener != null && mIsNetworkAvailable != isNetworkAvailable) {
            mNetworkAvailableListener.onNetworkAvailableChange(isNetworkAvailable);
            mIsNetworkAvailable = isNetworkAvailable;
        }
    }

    /**
     * Listener for {@link NetworkChangeReceiver},
     * it gets updates when the network state changes
     */
    public interface NetworkAvailableListener {
        void onNetworkAvailableChange(boolean isNetworkAvailable);
    }
}
