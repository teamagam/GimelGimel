package com.teamagam.gimelgimel.app.map.actions.send.dynamicLayers;

import com.teamagam.gimelgimel.app.map.GGMapView;
import com.teamagam.gimelgimel.app.map.OnMapGestureListener;
import com.teamagam.gimelgimel.app.map.actions.MapDrawer;
import com.teamagam.gimelgimel.app.map.actions.MapEntityFactory;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.symbols.Symbol;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

abstract class AbsMapDrawer extends AbsMapAction {
  private final List<PointGeometry> mPoints;
  private final MapEntityFactory mMapEntityFactory;
  private final MapDrawer mMapDrawer;
  private OnEditingStartListener mListener;
  private GeoEntity mEntity;
  private GGMapView mGgMapView;
  private Symbol mSymbol;

  protected AbsMapDrawer(MapEntityFactory mapEntityFactory,
      MapDrawer mapDrawer,
      OnEditingStartListener listener) {
    mMapEntityFactory = mapEntityFactory;
    mMapDrawer = mapDrawer;
    mListener = listener;
    mPoints = new ArrayList<>();
  }

  @Override
  public void setupAction(GGMapView ggMapView, Symbol symbol) {
    mGgMapView = ggMapView;
    mSymbol = symbol;
    mGgMapView.setOnMapGestureListener(new OnMapGestureListener() {
      @Override
      public void onLongPress(PointGeometry pointGeometry) {

      }

      @Override
      public void onTap(PointGeometry pointGeometry) {
        if (mPoints.isEmpty()) {
          mListener.onEditingStarted();
        }
        mPoints.add(pointGeometry);
        draw();
      }
    });
  }

  @Override
  public void start() {
    mPoints.clear();
  }

  @Override
  public void stop() {
    if (mGgMapView != null) {
      mGgMapView.setOnMapGestureListener(null);
    }
    eraseOld();
  }

  @Override
  public void updateSymbol(Symbol symbol) {
    mSymbol = symbol;
    draw();
  }

  @Override
  public Collection<GeoEntity> getEntities() {
    return Collections.singletonList(mEntity);
  }

  protected abstract GeoEntity buildEntity(MapEntityFactory mapEntityFactory,
      List<PointGeometry> pointsHistory,
      Symbol symbol);

  private void draw() {
    eraseOld();
    if (!mPoints.isEmpty()) {
      mEntity = buildEntity(mMapEntityFactory, mPoints, mSymbol);
      mMapDrawer.draw(mEntity);
    }
  }

  private void eraseOld() {
    if (mEntity != null) {
      mMapDrawer.erase(mEntity);
    }
  }
}
