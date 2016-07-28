package com.teamagam.gimelgimel.app.network.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.teamagam.gimelgimel.app.common.logging.Logger;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.app.utils.Constants;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * An implementation of {@link BroadcastReceiver}, that gets notification every time the network changes.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    private static final Logger sLogger = LoggerFactory.create(NetworkChangeReceiver.class);

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            boolean isNetworkAvailable = isNetworkAvailable(context);

            broadcastConnectivityStatus(context.getApplicationContext(), isNetworkAvailable);
        } catch (Exception ex) {
            sLogger.w("NetworkChangedReceiver onReceive failed", ex);
        }
    }

    /**
     * Sends the result of the network status to the ConnectivityStatusReceiver
     *
     * @param context            The current context
     * @param isNetworkAvailable The result to be sent to the ConnectivityStatusReceiver
     */
    private void broadcastConnectivityStatus(Context context, boolean isNetworkAvailable) {
        if (isNetworkAvailable) {
            ConnectivityStatusReceiver.broadcastAvailableNetwork(context);
        } else {
            ConnectivityStatusReceiver.broadcastNoNetwork(context);
        }
    }

    private boolean isNetworkAvailable(Context context) {
        final ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isNetworkAvailable = networkInfo != null && networkInfo.isConnected();

        if (isNetworkAvailable) {
            boolean isConnectionWorking = verifyConnection();

            return isConnectionWorking;
        }

        return isNetworkAvailable;
    }

    /**
     * Ensures that we can connect to the server,
     * covers some situations when we have WiFi connection,
     * but we can't connect to the internet with it.
     *
     * @return True if we can reach our API, else false
     */
    private boolean verifyConnection() {
        try {
            OkHttpClient httpClientBuilder = new OkHttpClient.Builder()
                    .connectTimeout(3000, TimeUnit.MILLISECONDS)
                    .build();

            Request request = new Request.Builder()
                    .get()
                    .url(Constants.MESSAGING_SERVER_URL)
                    .build();

            Call call = httpClientBuilder.newCall(request);

            call.execute();

            return true;
        } catch (SocketTimeoutException | UnknownHostException ex) {
            return false;
        } catch (IOException ex) {
            // Ignore other IO exceptions such as 500 or any other server side errors
            sLogger.v(ex.getMessage());

            return true;
        }
    }
}
