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
import com.teamagam.gimelgimel.domain.messages.CreateAndQueueGeoMessageInteractor;
import com.teamagam.gimelgimel.domain.messages.CreateAndQueueGeoMessageInteractorFactory;
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
  CreateAndQueueGeoMessageInteractorFactory mInteractorFactory;
  @Inject
  PreferencesUtils mPreferencesUtils;
  @Inject
  SpatialEngine mSpatialEngine;

  private List<Icon> mIcons;
  private int mTypeIdx;
  private PointGeometry mPoint;

  @Inject
  public SendGeoMessageViewModel() {
    super();
  }

  public void init(ViewDismisser view, PointGeometry point) {
    mIcons = new ArrayList<>();
    mPoint = point;
    mView = view;
    mDisplayIconsInteractorFactory.create(icon -> {
      mIcons.add(icon);
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
    return generateIconNames();
  }

  private String[] generateIconNames() {
    String[] names = new String[mIcons.size()];
    for (int i = 0; i < mIcons.size(); i++) {
      names[i] = mIcons.get(i).getDisplayName();
    }

    return names;
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
        mSendGeoMessageInteractorFactory.create(mText, pointGeometry, getSymbol());

    CreateAndQueueGeoMessageInteractor interactor =
        mSendGeoMessageInteractorFactory.create(mText, pointGeometry, getSymbol());



    interactor.execute();
  }

  private Symbol getSymbol() {
    String id = mIcons.get(getTypeIdx()).getId();

    return new PointSymbol.PointSymbolBuilder().setIconId(id).build();
  }
}
