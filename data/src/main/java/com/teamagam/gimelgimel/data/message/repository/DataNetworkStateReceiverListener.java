package com.teamagam.gimelgimel.data.message.repository;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.teamagam.gimelgimel.data.base.repository.SubjectRepository;
import com.teamagam.gimelgimel.domain.messages.NetworkStateReceiverListener;
import com.teamagam.gimelgimel.domain.notifications.entity.ConnectivityStatus;
import io.reactivex.Observable;
import javax.inject.Inject;

public class DataNetworkStateReceiverListener extends BroadcastReceiver
    implements NetworkStateReceiverListener {

  private SubjectRepository<ConnectivityStatus> mNetworkStateSubject;
  private Context mContext;

  @Inject
  public DataNetworkStateReceiverListener(Context context) {
    mNetworkStateSubject = SubjectRepository.createSimpleSubject();
    mContext = context;
  }

  @Override
  public void start() {
    mContext.registerReceiver(this, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
  }

  @Override
  public void onReceive(final Context context, final Intent intent) {
    ConnectivityStatus status = NetworkUtil.getConnectivityStatusString(context);
    networkStateChange(status);
  }

  @Override
  public void networkStateChange(ConnectivityStatus connectivityStatus) {
    mNetworkStateSubject.add(connectivityStatus);
  }

  @Override
  public Observable<ConnectivityStatus> getObservable() {
    return mNetworkStateSubject.getObservable();
  }

  static class NetworkUtil {
    static int TYPE_WIFI = 1;
    static int TYPE_MOBILE = 2;
    static int TYPE_NOT_CONNECTED = 0;

    static int getConnectivityStatus(Context context) {
      ConnectivityManager cm =
          (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

      NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
      if (activeNetwork != null) {
        if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
          return TYPE_WIFI;
        }
        if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
          return TYPE_MOBILE;
        }
      }
      return TYPE_NOT_CONNECTED;
    }

    static ConnectivityStatus getConnectivityStatusString(Context context) {
      int conn = NetworkUtil.getConnectivityStatus(context);
      if (conn == NetworkUtil.TYPE_WIFI) {
        return ConnectivityStatus.CONNECTED;
      } else if (conn == NetworkUtil.TYPE_MOBILE) {
        return ConnectivityStatus.CONNECTED;
      }
      return ConnectivityStatus.DISCONNECTED;
    }
  }
}

