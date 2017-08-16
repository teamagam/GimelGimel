package com.teamagam.gimelgimel.app.message.viewModel;

import android.content.Context;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.ViewModels.ViewDismisser;
import com.teamagam.gimelgimel.app.common.launcher.Navigator;
import com.teamagam.gimelgimel.domain.icons.entities.Icon;
import com.teamagam.gimelgimel.domain.map.SpatialEngine;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PointSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol;
import com.teamagam.gimelgimel.domain.messages.QueueGeoMessageForSendingInteractor;
import com.teamagam.gimelgimel.domain.messages.QueueGeoMessageForSendingInteractorFactory;
import com.teamagam.gimelgimel.domain.utils.PreferencesUtils;
import javax.inject.Inject;

public class SendGeoMessageViewModel extends SendMessageViewModel {

  @Inject
  Context mContext;
  @Inject
  QueueGeoMessageForSendingInteractorFactory mQueueGeoMessageForSendingInteractorFactory;
  @Inject
  PreferencesUtils mPreferencesUtils;
  @Inject
  SpatialEngine mSpatialEngine;
  @Inject
  Navigator mNavigator;

  private PointGeometry mPoint;
  private Icon mSelectedIcon;
  private IconDisplayer mIconDisplayer;

  @Inject
  public SendGeoMessageViewModel() {
    super();
  }

  public void init(ViewDismisser view, PointGeometry point, IconDisplayer iconDisplayer) {
    mPoint = point;
    mView = view;
    mIconDisplayer = iconDisplayer;
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

  @Override
  protected void executeInteractor() {
    PointGeometry pointGeometry =
        new PointGeometry(mPoint.getLatitude(), mPoint.getLongitude(), mPoint.getAltitude());

    QueueGeoMessageForSendingInteractor interactor =
        mQueueGeoMessageForSendingInteractorFactory.create(mText, pointGeometry, getSymbol());
    interactor.execute();
  }

  public void onSymbolSelectionClicked() {
    mNavigator.openIconSelectionDialog(this::updateSelectedIcon);
  }

  private Symbol getSymbol() {
    String id = mSelectedIcon.getId();
    return new PointSymbol.PointSymbolBuilder().setIconId(id).build();
  }

  private void updateSelectedIcon(Icon icon) {
    mSelectedIcon = icon;
    mIconDisplayer.display(icon);
  }

  public interface IconDisplayer {
    void display(Icon icon);
  }
}
