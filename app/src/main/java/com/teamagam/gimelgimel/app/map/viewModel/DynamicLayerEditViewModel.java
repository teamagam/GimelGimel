package com.teamagam.gimelgimel.app.map.viewModel;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.Observable;
import android.view.View;
import com.android.databinding.library.baseAdapters.BR;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.map.view.GGMapView;
import com.teamagam.gimelgimel.app.map.viewModel.gestures.OnMapGestureListener;
import com.teamagam.gimelgimel.domain.dynamicLayers.DisplayDynamicLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.dynamicLayers.remote.SendRemoteAddDynamicEntityRequestInteractorFactory;
import com.teamagam.gimelgimel.domain.icons.DisplayIconsInteractorFactory;
import com.teamagam.gimelgimel.domain.layers.DisplayVectorLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.map.DisplayMapEntitiesInteractorFactory;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PointEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PolygonEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.PolylineEntity;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PointSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PolygonSymbol;
import com.teamagam.gimelgimel.domain.map.entities.symbols.PolylineSymbol;
import com.teamagam.gimelgimel.domain.rasters.DisplayIntermediateRastersInteractorFactory;
import io.reactivex.functions.Consumer;
import java.util.ArrayList;
import java.util.List;

@AutoFactory
public class DynamicLayerEditViewModel extends BaseGeometryStyleViewModel {

  private final SendRemoteAddDynamicEntityRequestInteractorFactory
      mAddDynamicEntityRequestInteractorFactory;
  private final List<PointGeometry> mPoints;
  private final List<GeoEntity> mNewEntities;
  private GGMapView mGGMapView;
  private MapDrawer mMapDrawer;
  private MapEntityFactory mEntityFactory;
  private OnDrawGestureListener mGestureListener;
  private Consumer<PointGeometry> mOnMapClick;
  private GeoEntity mCurrentEntity;
  private boolean mIsOnEditMode;
  private int mBorderStyleVisibility;
  private int mBorderColorVisibility;
  private int mFillColorVisibility;
  private int mIconPickerVisibility;

  protected DynamicLayerEditViewModel(@Provided Context context,
      @Provided DisplayMapEntitiesInteractorFactory displayMapEntitiesInteractorFactory,
      @Provided DisplayVectorLayersInteractorFactory displayVectorLayersInteractorFactory,
      @Provided DisplayDynamicLayersInteractorFactory displayDynamicLayersInteractorFactory,
      @Provided
          DisplayIntermediateRastersInteractorFactory displayIntermediateRastersInteractorFactory,
      @Provided DisplayIconsInteractorFactory displayIconsInteractorFactory,
      @Provided
          SendRemoteAddDynamicEntityRequestInteractorFactory addDynamicEntityRequestInteractorFactory,
      GGMapView ggMapView,
      Consumer<Integer> pickColor,
      Consumer<String> pickBorderStyle) {
    super(displayMapEntitiesInteractorFactory, displayVectorLayersInteractorFactory,
        displayDynamicLayersInteractorFactory, displayIntermediateRastersInteractorFactory,
        displayIconsInteractorFactory, context, ggMapView, pickColor, pickBorderStyle);

    mAddDynamicEntityRequestInteractorFactory = addDynamicEntityRequestInteractorFactory;
    mGGMapView = ggMapView;

    mIsOnEditMode = false;
    mPoints = new ArrayList<>();
    mNewEntities = new ArrayList<>();
    mMapDrawer = new MapDrawer(mGGMapView);
    mEntityFactory = new MapEntityFactory();
    mGestureListener = new OnDrawGestureListener();

    mGGMapView.setOnMapGestureListener(null);
    addOnPropertyChangedCallback(new OnPropertyChanged());
  }

  @Override
  public void init() {
    super.init();
    super.loadIcons();
  }

  @Bindable
  public boolean isOnEditMode() {
    return mIsOnEditMode;
  }

  public int getBorderStyleVisibility() {
    return mBorderStyleVisibility;
  }

  public int getBorderColorVisibility() {
    return mBorderColorVisibility;
  }

  public int getFillColorVisibility() {
    return mFillColorVisibility;
  }

  public int getIconPickerVisibility() {
    return mIconPickerVisibility;
  }

  public void onNewTabSelection(int newTabResource) {
    if (!mIsOnEditMode) {
      setupDrawingMode(newTabResource);
    }
  }

  @Override
  public void onBorderStyleSelected(String borderStyle) {
    super.onBorderStyleSelected(borderStyle);
    refreshCurrentEntity();
  }

  public void sendCurrentGeometry() {
    setIsOnEditMode(false);

    if (mCurrentEntity != null) {
      mAddDynamicEntityRequestInteractorFactory.create(mCurrentEntity).execute();
      mNewEntities.add(mCurrentEntity);
      mMapDrawer.erase(mCurrentEntity);
      clearCurrentEntity();
    }
  }

  private void refreshCurrentEntity() {
    if (mCurrentEntity instanceof PolylineEntity) {
      refreshPolyline();
    } else if (mCurrentEntity instanceof PolygonEntity) {
      refreshPolygon();
    } else if (mCurrentEntity instanceof PointEntity) {
      drawPoint((PointGeometry) mCurrentEntity.getGeometry());
    }
  }

  private void clearCurrentEntity() {
    mPoints.clear();
    mCurrentEntity = null;
  }

  private void setIsOnEditMode(boolean isOnEditMode) {
    mIsOnEditMode = isOnEditMode;
    notifyPropertyChanged(BR.onEditMode);
  }

  private void setupDrawingMode(int newTabResource) {
    mGGMapView.setOnMapGestureListener(mGestureListener);

    if (newTabResource == R.id.tab_point) {
      mOnMapClick = this::drawPoint;
      setupPointStyleVisibility();
    } else if (newTabResource == R.id.tab_polyline) {
      mOnMapClick = this::drawPolyline;
      setupPolylineStyleVisibility();
    } else if (newTabResource == R.id.tab_polygon) {
      mOnMapClick = this::drawPolygon;
      setupPolygonStyleVisibility();
    } else {
      sLogger.w("Unknown tab selected");
      mGGMapView.setOnMapGestureListener(null);
    }

    notifyPropertyChanged(BR._all);
  }

  private void setupPointStyleVisibility() {
    mBorderColorVisibility = View.GONE;
    mBorderStyleVisibility = View.GONE;
    mFillColorVisibility = View.GONE;
    mIconPickerVisibility = View.VISIBLE;
  }

  private void setupPolygonStyleVisibility() {
    mBorderColorVisibility = View.VISIBLE;
    mBorderStyleVisibility = View.VISIBLE;
    mFillColorVisibility = View.VISIBLE;
    mIconPickerVisibility = View.GONE;
  }

  private void setupPolylineStyleVisibility() {
    mBorderColorVisibility = View.VISIBLE;
    mBorderStyleVisibility = View.VISIBLE;
    mFillColorVisibility = View.GONE;
    mIconPickerVisibility = View.GONE;
  }

  private void drawPoint(PointGeometry geometry) {
    PointSymbol symbol =
        new PointSymbol.PointSymbolBuilder().setIconId(mIcons.get(mIconIdx).getId()).build();

    mMapDrawer.erase(mCurrentEntity);
    mCurrentEntity = mEntityFactory.createPoint(geometry, symbol);
    mMapDrawer.draw(mCurrentEntity);
  }

  private void drawPolyline(PointGeometry pointGeometry) {
    mPoints.add(pointGeometry);
    refreshPolyline();
  }

  private void refreshPolyline() {
    PolylineSymbol symbol = new PolylineSymbol.PolylineSymbolBuilder().setBorderColor(mBorderColor)
        .setBorderStyle(mBorderStyle)
        .build();

    mMapDrawer.erase(mCurrentEntity);
    mCurrentEntity = mEntityFactory.createPolyline(mPoints, symbol);
    mMapDrawer.draw(mCurrentEntity);
  }

  private void drawPolygon(PointGeometry pointGeometry) {
    mPoints.add(pointGeometry);
    refreshPolygon();
  }

  private void refreshPolygon() {
    PolygonSymbol symbol = new PolygonSymbol.PolygonSymbolBuilder().setBorderColor(mBorderColor)
        .setBorderStyle(mBorderStyle)
        .setFillColor(mFillColor)
        .build();

    mMapDrawer.erase(mCurrentEntity);
    mCurrentEntity = mEntityFactory.createPolygon(mPoints, symbol);
    mMapDrawer.draw(mCurrentEntity);
  }

  private class OnDrawGestureListener implements OnMapGestureListener {

    @Override
    public void onLongPress(PointGeometry pointGeometry) {
    }

    @Override
    public void onTap(PointGeometry pointGeometry) {
      try {
        setIsOnEditMode(true);
        mOnMapClick.accept(pointGeometry);
      } catch (Exception ignored) {
        sLogger.w("Could not process map click");
      }
    }
  }

  private class OnPropertyChanged extends OnPropertyChangedCallback {
    @Override
    public void onPropertyChanged(Observable sender, int propertyId) {
      if (stylingPropertyChanged(propertyId)) {
        refreshCurrentEntity();
      }
    }

    private boolean stylingPropertyChanged(int propertyId) {
      return propertyId == BR.borderColor || propertyId == BR.fillColor || propertyId == BR.iconIdx;
    }
  }
}
