package com.teamagam.gimelgimel.data.message.repository;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.teamagam.gimelgimel.data.base.repository.SubjectRepository;
import com.teamagam.gimelgimel.domain.messages.NetworkStateReceiverListener;
import io.reactivex.Observable;
import javax.inject.Inject;

public class NetworkStateReceiverListenerImpl extends BroadcastReceiver
    implements NetworkStateReceiverListener {

  private SubjectRepository<Boolean> mNetworkStateSubject;

  @Inject
  public NetworkStateReceiverListenerImpl(Context context) {
    mNetworkStateSubject = SubjectRepository.createSimpleSubject();
    context.registerReceiver(this,
        new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
  }

  @Override
  public void onReceive(final Context context, final Intent intent) {
    int status = NetworkUtil.getConnectivityStatusString(context);
    networkStateChange(status == NetworkUtil.NETWORK_STATUS_NOT_CONNECTED);
  }

  @Override
  public void networkStateChange(Boolean isOffline) {
    if (isOffline) {
      mNetworkStateSubject.add(false);
    } else {
      mNetworkStateSubject.add(true);
    }
  }

  @Override
  public Observable<Boolean> getObservable() {
    return mNetworkStateSubject.getObservable();
  }

  static class NetworkUtil {
    static final int NETWORK_STATUS_NOT_CONNECTED = 0, NETWORK_STATUS_WIFI = 1,
        NETWORK_STATUS_MOBILE = 2;
    static int TYPE_WIFI = 1;
    static int TYPE_MOBILE = 2;
    static int TYPE_NOT_CONNECTED = 0;

    static int getConnectivityStatus(Context context) {
      ConnectivityManager cm =
          (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

      NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
      if (null != activeNetwork) {
        if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) return TYPE_WIFI;

        if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) return TYPE_MOBILE;
      }
      return TYPE_NOT_CONNECTED;
    }

    static int getConnectivityStatusString(Context context) {
      int conn = NetworkUtil.getConnectivityStatus(context);
      int status = 0;
      if (conn == NetworkUtil.TYPE_WIFI) {
        status = NETWORK_STATUS_WIFI;
      } else if (conn == NetworkUtil.TYPE_MOBILE) {
        status = NETWORK_STATUS_MOBILE;
      } else if (conn == NetworkUtil.TYPE_NOT_CONNECTED) {
        status = NETWORK_STATUS_NOT_CONNECTED;
      }
      return status;
    }
  }
}

