package com.teamagam.gimelgimel.app.map.viewModel;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.app.common.base.ViewModels.ViewDismisser;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.map.view.GGMapView;
import com.teamagam.gimelgimel.domain.layers.DisplayVectorLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.map.DisplayMapEntitiesInteractorFactory;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Polygon;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Polyline;
import com.teamagam.gimelgimel.domain.messages.SendGeoMessageInteractorFactory;
import com.teamagam.gimelgimel.domain.rasters.DisplayIntermediateRastersInteractorFactory;
import java.util.ArrayList;
import java.util.List;

import static android.text.TextUtils.isEmpty;

@AutoFactory
public class SendGeometryViewModel extends BaseMapViewModel {

  private static final String EMPTY_STRING = "";
  private static AppLogger sLogger = AppLoggerFactory.create();

  private final InvalidInputNotifier mInvalidInputNotifier;
  private final ViewDismisser mViewDismisser;
  private final MapDrawer mMapDrawer;
  private final MapEntityFactory mMapEntityFactory;
  private final SendGeoMessageInteractorFactory mSendGeoMessageInteractorFactory;
  private final List<PointGeometry> mSelectedPoints;

  private String mDescription;
  private boolean mIsSwitchChecked;

  protected SendGeometryViewModel(
      @Provided DisplayMapEntitiesInteractorFactory displayMapEntitiesInteractorFactory,
      @Provided DisplayVectorLayersInteractorFactory displayVectorLayersInteractorFactory,
      @Provided
          DisplayIntermediateRastersInteractorFactory displayIntermediateRastersInteractorFactory,
      @Provided SendGeoMessageInteractorFactory sendGeoMessageInteractorFactory,
      GGMapView ggMapView,
      InvalidInputNotifier invalidInputNotifier,
      ViewDismisser viewDismisser) {
    super(displayMapEntitiesInteractorFactory, displayVectorLayersInteractorFactory,
        displayIntermediateRastersInteractorFactory, ggMapView);
    mInvalidInputNotifier = invalidInputNotifier;
    mViewDismisser = viewDismisser;
    mMapDrawer = new MapDrawer(ggMapView);
    mMapEntityFactory = new MapEntityFactory();
    mSendGeoMessageInteractorFactory = sendGeoMessageInteractorFactory;
    mIsSwitchChecked = false;
    mSelectedPoints = new ArrayList<>();
  }

  public void onSendFabClicked() {
    sLogger.userInteraction("Send geometry fab clicked");
    if (isValidForm()) {
      mSendGeoMessageInteractorFactory.create(mDescription, getCurrentGeometry(), EMPTY_STRING)
          .execute();
      mViewDismisser.dismiss();
    } else {
      mInvalidInputNotifier.notifyInvalid();
    }
  }

  public void onUndoClicked() {
    sLogger.userInteraction("Send geometry undo clicked");
    if (!mSelectedPoints.isEmpty()) {
      removeLastSelectedPoint();
      refreshDisplayedGeometry();
    }
  }

  public void onSwitchChanged(boolean isChecked) {
    sLogger.userInteraction("Send geometry switch changed to " + isChecked);
    mIsSwitchChecked = isChecked;
    refreshDisplayedGeometry();
  }

  public String getDescription() {
    return mDescription;
  }

  public void setDescription(String description) {
    sLogger.userInteraction("Send geometry description changed to " + description);
    mDescription = description;
  }

  public void onLocationSelection(PointGeometry point) {
    sLogger.userInteraction(
        "Send geometry location selected " + point.getLatitude() + "/" + point.getLongitude());
    mSelectedPoints.add(point);
    refreshDisplayedGeometry();
  }

  private boolean isValidForm() {
    return !isEmpty(mDescription) && isValidGeometry();
  }

  private boolean isValidGeometry() {
    if (isPolylineState()) {
      return mSelectedPoints.size() > 1;
    } else {
      return mSelectedPoints.size() > 2;
    }
  }

  private Geometry getCurrentGeometry() {
    if (isPolylineState()) {
      return new Polyline(mSelectedPoints);
    }
    return new Polygon(mSelectedPoints);
  }

  private void removeLastSelectedPoint() {
    mSelectedPoints.remove(mSelectedPoints.size() - 1);
  }

  private void refreshDisplayedGeometry() {
    removeOldDisplayedGeometry();
    displayCurrentGeometry();
  }

  private void removeOldDisplayedGeometry() {
    mMapDrawer.clear();
  }

  private void displayCurrentGeometry() {
    switch (mSelectedPoints.size()) {
      case 0:
        break;
      case 1:
        displayPoint(mSelectedPoints.get(0));
        break;
      case 2:
        displayPolyline(mSelectedPoints);
        break;
      default:
        displayBySwitch();
    }
  }

  private void displayPoint(PointGeometry point) {
    mMapDrawer.draw(mMapEntityFactory.createPoint(point));
  }

  private void displayPolyline(List<PointGeometry> points) {
    mMapDrawer.draw(mMapEntityFactory.createPolyline(points));
  }

  private void displayBySwitch() {
    if (isPolylineState()) {
      displayPolyline(mSelectedPoints);
    } else {
      displayPolygon(mSelectedPoints);
    }
  }

  private boolean isPolylineState() {
    return !mIsSwitchChecked;
  }

  private void displayPolygon(List<PointGeometry> points) {
    mMapDrawer.draw(mMapEntityFactory.createPolygon(points));
  }

  public interface InvalidInputNotifier {
    void notifyInvalid();
  }
}