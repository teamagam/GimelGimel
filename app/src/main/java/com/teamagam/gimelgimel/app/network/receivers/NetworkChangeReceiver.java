package com.teamagam.gimelgimel.app.network.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.teamagam.gimelgimel.app.network.rest.RestAPI;

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

    @Override
    public void onReceive(Context context, Intent intent) {
        final ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isNetworkAvailable = networkInfo != null && networkInfo.isConnected();

        if(isNetworkAvailable) {
            isNetworkAvailable = verifyConnection();
        }

        Intent connectivityIntent = new Intent(ConnectivityStatusReceiver.INTENT_NAME);
        connectivityIntent.putExtra(ConnectivityStatusReceiver.NETWORK_AVAILABLE_EXTRA, isNetworkAvailable);

        LocalBroadcastManager.getInstance(context).sendBroadcast(connectivityIntent);
    }

    /**
     * Ensures that we can connect to the server,
     * covers some situations when we have WiFi connection,
     * but we can't connect to the internet with it.
     * @return True if we can reach our API, else false
     */
    private boolean verifyConnection() {
        try {
            OkHttpClient httpClientBuilder = new OkHttpClient.Builder()
                    .connectTimeout(3000, TimeUnit.MILLISECONDS)
                    .build();

            Request request = new Request.Builder()
                    .get()
                    .url(RestAPI.MESSAGING_API_BASE_URL)
                    .build();

            Call call = httpClientBuilder.newCall(request);

            call.execute();

            return true;
        } catch (SocketTimeoutException | UnknownHostException ex) {
            return false;
        } catch (IOException ex) {
            // Ignore other IO exceptions such as 500 or any other server side errors
            Log.v("NetworkChangeReceiver", ex.getMessage());

            return true;
        }
    }
}
