package com.teamagam.gimelgimel.app.message.viewModel;

import android.content.Context;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.ViewModels.ViewDismisser;
import com.teamagam.gimelgimel.domain.icons.DisplayIconsInteractorFactory;
import com.teamagam.gimelgimel.domain.icons.entities.Icon;
import com.teamagam.gimelgimel.domain.map.SpatialEngine;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PointSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol;
import com.teamagam.gimelgimel.domain.messages.SendGeoMessageInteractor;
import com.teamagam.gimelgimel.domain.messages.SendGeoMessageInteractorFactory;
import com.teamagam.gimelgimel.domain.utils.PreferencesUtils;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class SendGeoMessageViewModel extends SendMessageViewModel {

  @Inject
  Context mContext;
  @Inject
  DisplayIconsInteractorFactory mDisplayIconsInteractorFactory;
  @Inject
  SendGeoMessageInteractorFactory mSendGeoMessageInteractorFactory;
  @Inject
  PreferencesUtils mPreferencesUtils;
  @Inject
  SpatialEngine mSpatialEngine;

  private List<Icon> mTypes;
  private int mTypeIdx;
  private PointGeometry mPoint;

  @Inject
  public SendGeoMessageViewModel() {
    super();
  }

  public void init(ViewDismisser view, PointGeometry point) {
    mTypes = new ArrayList<>();
    mPoint = point;
    mView = view;
    mDisplayIconsInteractorFactory.create(icon -> {
      mTypes.add(icon);
      notifyChange();
    }).execute();
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
    String[] types = new String[mTypes.size()];

    return mTypes.toArray(types);
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

    SendGeoMessageInteractor interactor =
        mInteractorFactory.create(mText, pointGeometry, getSymbol());

    interactor.execute();
  }

  private Symbol getSymbol() {
    return new PointSymbol.PointSymbolBuilder().setIconId("")
        .setTintColor("")
        .build(); // implement symbol picking
  }
}
