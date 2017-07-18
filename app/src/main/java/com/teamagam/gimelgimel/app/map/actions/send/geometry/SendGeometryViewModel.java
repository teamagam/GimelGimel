package com.teamagam.gimelgimel.app.map.viewModel;

import android.content.Context;
import android.graphics.Color;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.BR;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.ViewModels.ViewDismisser;
import com.teamagam.gimelgimel.app.map.GGMapView;
import com.teamagam.gimelgimel.app.map.actions.MapDrawer;
import com.teamagam.gimelgimel.app.map.actions.MapEntityFactory;
import com.teamagam.gimelgimel.app.map.viewModel.BaseGeometryStyleViewModel;
import com.teamagam.gimelgimel.domain.dynamicLayers.DisplayDynamicLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.layers.DisplayVectorLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.map.DisplayMapEntitiesInteractorFactory;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Polygon;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Polyline;
import com.teamagam.gimelgimel.domain.messages.CreateAndQueueGeoMessageInteractorFactory;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PointSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PolygonSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PolylineSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol;
import com.teamagam.gimelgimel.domain.messages.SendGeoMessageInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.QueueGeoMessageForSendingInteractorFactory;
import com.teamagam.gimelgimel.domain.rasters.DisplayIntermediateRastersInteractorFactory;
import io.reactivex.functions.Consumer;
import java.util.ArrayList;
import java.util.List;

import static android.text.TextUtils.isEmpty;

@AutoFactory
public class SendGeometryViewModel extends BaseGeometryStyleViewModel {

  private final InvalidInputNotifier mInvalidInputNotifier;
  private final ViewDismisser mViewDismisser;
  private final MapDrawer mMapDrawer;
  private final MapEntityFactory mMapEntityFactory;
  private final QueueGeoMessageForSendingInteractorFactory mSendGeoMessageInteractorFactory;
  private final List<PointGeometry> mSelectedPoints;
  private String mDescription;
  private boolean mIsSwitchChecked;
  private String mDisabledColor;

  protected SendGeometryViewModel(@Provided Context context,
      @Provided DisplayMapEntitiesInteractorFactory displayMapEntitiesInteractorFactory,
      @Provided DisplayVectorLayersInteractorFactory displayVectorLayersInteractorFactory,
      @Provided DisplayDynamicLayersInteractorFactory displayDynamicLayersInteractorFactory,
      @Provided
          DisplayIntermediateRastersInteractorFactory displayIntermediateRastersInteractorFactory,
      @Provided QueueGeoMessageForSendingInteractorFactory sendGeoMessageInteractorFactory,
      GGMapView ggMapView,
      InvalidInputNotifier invalidInputNotifier,
      Consumer<Integer> pickColor,
      Consumer<String> pickBorderStyle,
      ViewDismisser viewDismisser) {
    super(displayMapEntitiesInteractorFactory, displayVectorLayersInteractorFactory,
        displayDynamicLayersInteractorFactory, displayIntermediateRastersInteractorFactory, null,
        context, ggMapView, pickColor, pickBorderStyle);
    mInvalidInputNotifier = invalidInputNotifier;
    mPickColor = pickColor;
    mPickBorderStyle = pickBorderStyle;
    mViewDismisser = viewDismisser;
    mMapDrawer = new MapDrawer(ggMapView);
    mMapEntityFactory = new MapEntityFactory();
    mSendGeoMessageInteractorFactory = sendGeoMessageInteractorFactory;
    mIsSwitchChecked = false;
    mSelectedPoints = new ArrayList<>();
    mDisabledColor = colorToString(context.getResources().getColor(R.color.gray_dark));
  }

  @Override
  public void onFillColorClick() {
    if (!isPolylineState()) {
      super.onFillColorClick();
    }
  }

  @Override
  public int getFillColor() {
    if (isPolylineState()) {
      return Color.parseColor(mDisabledColor);
    } else {
      return Color.parseColor(mFillColor);
    }
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
    notifyPropertyChanged(BR._all);
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

  private Symbol getCurrentSymbol() {
    switch (mSelectedPoints.size()) {
      case 0:
        return null;
      case 1:
        return getPointSymbol();
      case 2:
        return getPolylineSymbol();
      default:
        return getSymbolBySwitch();
    }
  }

  private PointSymbol getPointSymbol() {
    return new PointSymbol.PointSymbolBuilder().setTintColor(mBorderColor).build();
  }

  private PolylineSymbol getPolylineSymbol() {
    return new PolylineSymbol.PolylineSymbolBuilder().setBorderColor(mBorderColor)
        .setBorderStyle(mBorderStyle)
        .build();
  }

  private PolygonSymbol getPolygonSymbol() {
    return new PolygonSymbol.PolygonSymbolBuilder().setBorderColor(mBorderColor)
        .setBorderStyle(mBorderStyle)
        .setFillColor(mFillColor)
        .build();
  }

  private Symbol getSymbolBySwitch() {
    if (isPolylineState()) {
      return getPolylineSymbol();
    } else {
      return getPolygonSymbol();
    }
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
    mMapDrawer.draw(mMapEntityFactory.createPoint(point, getPointSymbol()));
  }

  private void displayPolyline(List<PointGeometry> points) {
    mMapDrawer.draw(mMapEntityFactory.createPolyline(points, getPolylineSymbol()));
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
    mMapDrawer.draw(mMapEntityFactory.createPolygon(points, getPolygonSymbol()));
  }

  public interface InvalidInputNotifier {
    void notifyInvalid();
  }
}