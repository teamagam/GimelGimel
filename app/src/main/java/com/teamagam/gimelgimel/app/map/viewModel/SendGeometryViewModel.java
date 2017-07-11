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
import com.teamagam.gimelgimel.domain.map.entities.symbols.PolygonSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PolylineSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol;
import com.teamagam.gimelgimel.domain.messages.SendGeoMessageInteractorFactory;
import com.teamagam.gimelgimel.domain.rasters.DisplayIntermediateRastersInteractorFactory;
import io.reactivex.functions.Action;
import java.util.ArrayList;
import java.util.List;

import static android.text.TextUtils.isEmpty;

@AutoFactory
public class SendGeometryViewModel extends BaseMapViewModel {

  private static AppLogger sLogger = AppLoggerFactory.create();

  private final InvalidInputNotifier mInvalidInputNotifier;
  private final ViewDismisser mViewDismisser;
  private final MapDrawer mMapDrawer;
  private final MapEntityFactory mMapEntityFactory;
  private final SendGeoMessageInteractorFactory mSendGeoMessageInteractorFactory;
  private final List<PointGeometry> mSelectedPoints;
  private Action mPickColor;
  private String mDescription;
  private boolean mIsSwitchChecked;
  private boolean mIsBorderColorPicking;
  private String mBorderColor;
  private String mBorderStyle;
  private String mFillColor;

  protected SendGeometryViewModel(
      @Provided DisplayMapEntitiesInteractorFactory displayMapEntitiesInteractorFactory,
      @Provided DisplayVectorLayersInteractorFactory displayVectorLayersInteractorFactory,
      @Provided
          DisplayIntermediateRastersInteractorFactory displayIntermediateRastersInteractorFactory,
      @Provided SendGeoMessageInteractorFactory sendGeoMessageInteractorFactory,
      GGMapView ggMapView,
      InvalidInputNotifier invalidInputNotifier, Action pickColor,
      ViewDismisser viewDismisser) {
    super(displayMapEntitiesInteractorFactory, displayVectorLayersInteractorFactory,
        displayIntermediateRastersInteractorFactory, ggMapView);
    mInvalidInputNotifier = invalidInputNotifier;
    mPickColor = pickColor;
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
      mSendGeoMessageInteractorFactory.create(mDescription, getCurrentGeometry(),
          getCurrentSymbol()).execute();
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

  public void onBorderStyleSelect() {

  }

  public void onBorderColorSelect() {
    try {
      mPickColor.run();
      mIsBorderColorPicking = true;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void onFillColorSelect() {
    try {
      mPickColor.run();
      mIsBorderColorPicking = false;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void onBorderStyleSelected(String style) {
    sLogger.userInteraction("Send geometry border style changed to " + style);
    mBorderStyle = style;
    refreshDisplayedGeometry();
  }

  public void onColorSelected(boolean positiveResult, int color) {
    if (positiveResult) {
      if (mIsBorderColorPicking) {
        onBorderColorSelected(color);
      } else {
        onFillColorSelected(color);
      }
    }
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

  private Symbol getCurrentSymbol() {
    if (isPolylineState()) {
      return new PolylineSymbol.PolylineSymbolBuilder().setBorderColor(mBorderColor)
          .setBorderStyle(mBorderStyle)
          .build();
    }
    return new PolygonSymbol.PolygonSymbolBuilder().setBorderColor(mBorderColor)
        .setBorderStyle(mBorderStyle)
        .setFillColor(mFillColor)
        .build();
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

  private void onBorderColorSelected(int color) {
    sLogger.userInteraction("Send geometry border color changed to " + colorToString(color));
    mBorderColor = colorToString(color);
    refreshDisplayedGeometry();
  }

  private void onFillColorSelected(int color) {
    sLogger.userInteraction("Send geometry fill color changed to " + colorToString(color));
    mFillColor = colorToString(color);
    refreshDisplayedGeometry();
  }

  private String colorToString(int color) {
    return Integer.toHexString(color).toUpperCase();
  }

  private void displayPolygon(List<PointGeometry> points) {
    mMapDrawer.draw(mMapEntityFactory.createPolygon(points));
  }

  public interface InvalidInputNotifier {
    void notifyInvalid();
  }
}