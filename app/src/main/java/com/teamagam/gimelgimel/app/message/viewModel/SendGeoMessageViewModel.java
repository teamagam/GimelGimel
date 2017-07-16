package com.teamagam.gimelgimel.app.message.viewModel;

import android.content.Context;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.ViewModels.ViewDismisser;
import com.teamagam.gimelgimel.domain.map.SpatialEngine;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PointSymbol;
import com.teamagam.gimelgimel.domain.messages.CreateAndQueueGeoMessageInteractor;
import com.teamagam.gimelgimel.domain.messages.CreateAndQueueGeoMessageInteractorFactory;
import com.teamagam.gimelgimel.domain.utils.PreferencesUtils;
import javax.inject.Inject;

public class SendGeoMessageViewModel extends SendMessageViewModel {

  @Inject
  Context mContext;
  @Inject
  CreateAndQueueGeoMessageInteractorFactory mInteractorFactory;
  @Inject
  PreferencesUtils mPreferencesUtils;
  @Inject
  SpatialEngine mSpatialEngine;

  private String[] mTypes;
  private int mTypeIdx;
  private PointGeometry mPoint;

  @Inject
  public SendGeoMessageViewModel() {
    super();
  }

  public void init(ViewDismisser view, PointGeometry point) {
    mTypes = mContext.getResources().getStringArray(R.array.geo_location_types);
    mPoint = point;
    mView = view;
  }

  public String getFormattedPoint() {
    if (mPreferencesUtils.shouldUseUtm()) {
      PointGeometry point = new PointGeometry(mPoint.getLatitude(), mPoint.getLongitude());
      PointGeometry utmPoint = mSpatialEngine.projectToUTM(point);
      return mContext.getString(R.string.utm_36N_format, utmPoint.getLongitude(),
          utmPoint.getLatitude());
    } else {
      return mContext.getString(R.string.geo_dd_format, mPoint.getLatitude(),
          mPoint.getLongitude());
    }
  }

  public String[] getTypes() {
    return mTypes;
  }

  public int getTypeIdx() {
    return mTypeIdx;
  }

  public void setTypeIdx(int typeId) {
    mTypeIdx = typeId;
  }

  @Override
  protected void executeInteractor() {
    com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry pointGeometry =
        new com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry(
            mPoint.getLatitude(), mPoint.getLongitude(), mPoint.getAltitude());

    CreateAndQueueGeoMessageInteractor interactor =
        mInteractorFactory.create(mText, pointGeometry, getMessageType());

    interactor.execute();
  }

  private String getMessageType() {
    switch (mTypeIdx) {
      case 0:
        return PointSymbol.POINT_TYPE_BUILDING;
      case 1:
        return PointSymbol.POINT_TYPE_ENEMY;
      default:
        return PointSymbol.POINT_TYPE_GENERAL;
    }
  }
}
