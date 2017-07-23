package com.teamagam.gimelgimel.app.map.viewModel;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.map.view.GGMapView;
import com.teamagam.gimelgimel.app.map.viewModel.gestures.OnMapGestureListener;
import com.teamagam.gimelgimel.domain.layers.DisplayVectorLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.map.DisplayMapEntitiesInteractorFactory;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.rasters.DisplayIntermediateRastersInteractorFactory;
import io.reactivex.functions.Consumer;
import java.util.ArrayList;
import java.util.List;

@AutoFactory
public class DynamicLayerEditViewModel extends BaseMapViewModel {

  private final List<PointGeometry> mPoints;
  private final List<GeoEntity> mNewEntities;
  private GGMapView mGGMapView;
  private MapDrawer mMapDrawer;
  private MapEntityFactory mEntityFactory;
  private OnDrawGestureListener mGestureLisetner;
  private Consumer<PointGeometry> mOnMapClick;
  private GeoEntity mCurrentEntity;
  private boolean mIsOnEditMode;

  protected DynamicLayerEditViewModel(
      @Provided DisplayMapEntitiesInteractorFactory displayMapEntitiesInteractorFactory,
      @Provided DisplayVectorLayersInteractorFactory displayVectorLayersInteractorFactory,
      @Provided
          DisplayIntermediateRastersInteractorFactory displayIntermediateRastersInteractorFactory,
      GGMapView ggMapView) {
    super(displayMapEntitiesInteractorFactory, displayVectorLayersInteractorFactory,
        displayIntermediateRastersInteractorFactory, ggMapView);

    mGGMapView = ggMapView;

    mIsOnEditMode = false;
    mPoints = new ArrayList<>();
    mNewEntities = new ArrayList<>();
    mMapDrawer = new MapDrawer(mGGMapView);
    mEntityFactory = new MapEntityFactory();
    mGestureLisetner = new OnDrawGestureListener();

    mGGMapView.setOnMapGestureListener(null);
  }

  public boolean isOnEditMode() {
    return mIsOnEditMode;
  }

  public void onNewTabSelection(int newTabResource) {
    if (!mIsOnEditMode) {
      setListenerDrawingMode(newTabResource);
    } else {

    }
  }

  public void saveCurrentGeometry() {
    mIsOnEditMode = false;

    if (mCurrentEntity != null) {
      mNewEntities.add(mCurrentEntity);
      clearCurrentEntity();
    }
  }

  public void dismissCurrentGeometry() {
    mIsOnEditMode = false;
    mMapDrawer.erase(mCurrentEntity);
    clearCurrentEntity();
  }

  private void clearCurrentEntity() {
    mPoints.clear();
    mCurrentEntity = null;
  }

  private void setListenerDrawingMode(int newTabResource) {
    mGGMapView.setOnMapGestureListener(mGestureLisetner);

    if (newTabResource == R.id.tab_point) {
      mOnMapClick = this::drawPoint;
    } else if (newTabResource == R.id.tab_polyline) {
      mOnMapClick = this::drawPolyline;
    } else if (newTabResource == R.id.tab_polygon) {
      mOnMapClick = this::drawPolygon;
    } else {
      sLogger.w("Unknown tab selected");
      mGGMapView.setOnMapGestureListener(null);
    }
  }

  private void drawPoint(PointGeometry geometry) {
    mMapDrawer.erase(mCurrentEntity);
    mCurrentEntity = mEntityFactory.createPoint(geometry);
    mMapDrawer.draw(mCurrentEntity);
  }

  private void drawPolyline(PointGeometry pointGeometry) {
  }

  private void drawPolygon(PointGeometry pointGeometry) {

  }

  private class OnDrawGestureListener implements OnMapGestureListener {

    @Override
    public void onLongPress(PointGeometry pointGeometry) {

    }

    @Override
    public void onTap(PointGeometry pointGeometry) {
      try {
        mIsOnEditMode = true;
        mOnMapClick.accept(pointGeometry);
      } catch (Exception ignored) {
      }
    }
  }
}
