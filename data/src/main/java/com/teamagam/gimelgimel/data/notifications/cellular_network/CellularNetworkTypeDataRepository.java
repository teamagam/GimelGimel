package com.teamagam.gimelgimel.data.notifications.cellular_network;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import com.teamagam.gimelgimel.data.base.repository.SubjectRepository;
import com.teamagam.gimelgimel.domain.notifications.cellular_network.CellularNetworkTypeRepository;
import javax.inject.Inject;
import rx.Observable;

public class CellularNetworkTypeDataRepository implements CellularNetworkTypeRepository {

  private final CellularNetworkChangeDelegator mCellularNetworkChangeDelegator;
  private final TelephonyManager mTelephonyManager;

  private final SubjectRepository<Integer> mInnerRepo;
  private boolean mIsStarted;

  @Inject
  public CellularNetworkTypeDataRepository(Context context) {
    mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    mCellularNetworkChangeDelegator = new CellularNetworkChangeDelegator();
    mInnerRepo = SubjectRepository.createReplayCount(1);
    mIsStarted = false;
  }

  @Override
  public Observable<Integer> getChangesObservable() {
    if (!mIsStarted) {
      start();
    }
    return mInnerRepo.getObservable();
  }

  private void start() {
    mTelephonyManager.listen(mCellularNetworkChangeDelegator,
        PhoneStateListener.LISTEN_DATA_CONNECTION_STATE);
    mIsStarted = true;
  }

  private class CellularNetworkChangeDelegator extends PhoneStateListener {
    @Override
    public void onDataConnectionStateChanged(int state, int networkType) {
      switch (networkType) {
        case TelephonyManager.NETWORK_TYPE_GPRS:
        case TelephonyManager.NETWORK_TYPE_EDGE:
        case TelephonyManager.NETWORK_TYPE_CDMA:
        case TelephonyManager.NETWORK_TYPE_1xRTT:
        case TelephonyManager.NETWORK_TYPE_IDEN:
          mInnerRepo.add(CellularNetworkTypeRepository.NETWORK_CODE_2G);
          break;
        case TelephonyManager.NETWORK_TYPE_UMTS:
        case TelephonyManager.NETWORK_TYPE_EVDO_0:
        case TelephonyManager.NETWORK_TYPE_EVDO_A:
        case TelephonyManager.NETWORK_TYPE_HSDPA:
        case TelephonyManager.NETWORK_TYPE_HSUPA:
        case TelephonyManager.NETWORK_TYPE_HSPA:
        case TelephonyManager.NETWORK_TYPE_EVDO_B:
        case TelephonyManager.NETWORK_TYPE_EHRPD:
        case TelephonyManager.NETWORK_TYPE_HSPAP:
        case TelephonyManager.NETWORK_TYPE_LTE:
          mInnerRepo.add(CellularNetworkTypeRepository.NETWORK_CODE_3G);
          break;
      }
    }
  }
}
