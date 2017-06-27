package com.teamagam.gimelgimel.app.map.main;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.app.common.launcher.Navigator;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.map.BaseMapViewModel;
import com.teamagam.gimelgimel.app.map.GGMapView;
import com.teamagam.gimelgimel.app.map.MapEntityClickedListener;
import com.teamagam.gimelgimel.app.map.OnMapGestureListener;
import com.teamagam.gimelgimel.domain.layers.DisplayVectorLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.map.ActOnFirstLocationInteractorFactory;
import com.teamagam.gimelgimel.domain.map.DisplayMapEntitiesInteractorFactory;
import com.teamagam.gimelgimel.domain.map.SelectKmlEntityInteractorFactory;
import com.teamagam.gimelgimel.domain.map.SelectMessageByEntityInteractorFactory;
import com.teamagam.gimelgimel.domain.map.ViewerCameraController;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.KmlEntityInfo;
import com.teamagam.gimelgimel.domain.rasters.DisplayIntermediateRastersInteractorFactory;

@AutoFactory
public class ViewerViewModel extends BaseMapViewModel<ViewerFragment>
    implements ViewerCameraController {

  private static final AppLogger sLogger = AppLoggerFactory.create(ViewerViewModel.class);
  private final SelectKmlEntityInteractorFactory mSelectKmlEntityInfoInteractorFactory;
  private final SelectMessageByEntityInteractorFactory mSelectMessageByEntityInteractorFactory;
  private final ActOnFirstLocationInteractorFactory mActOnFirstLocationInteractorFactory;
  private final Navigator mNavigator;
  private final GGMapView mMapView;

  private boolean mLocateMeEnabled;

  public ViewerViewModel(
      @Provided DisplayMapEntitiesInteractorFactory displayMapEntitiesInteractorFactory,
      @Provided DisplayVectorLayersInteractorFactory displayVectorLayersInteractorFactory,
      @Provided
          DisplayIntermediateRastersInteractorFactory displayIntermediateRastersInteractorFactory,
      @Provided SelectKmlEntityInteractorFactory selectKmlEntityInfoInteractorFactory,
      @Provided SelectMessageByEntityInteractorFactory selectMessageByEntityInteractorFactory,
      @Provided ActOnFirstLocationInteractorFactory actOnFirstLocationInteractorFactory,
      @Provided Navigator navigator,
      GGMapView ggMapView) {
    super(displayMapEntitiesInteractorFactory, displayVectorLayersInteractorFactory,
        displayIntermediateRastersInteractorFactory, ggMapView);
    mSelectKmlEntityInfoInteractorFactory = selectKmlEntityInfoInteractorFactory;
    mSelectMessageByEntityInteractorFactory = selectMessageByEntityInteractorFactory;
    mActOnFirstLocationInteractorFactory = actOnFirstLocationInteractorFactory;
    mNavigator = navigator;
    mMapView = ggMapView;
  }

  @Override
  public void init() {
    super.init();
    mMapView.setOnEntityClickedListener(new MapEntityClickedSelectExecutor());
    mMapView.setOnMapGestureListener(new LongPressListener());
    mLocateMeEnabled = false;
    mActOnFirstLocationInteractorFactory.create(ls -> {
      mLocateMeEnabled = true;
    }).execute();
  }

  @Override
  public void setViewerCamera(Geometry geometry) {
    mMapView.lookAt(geometry);
  }

  public void onLocationFabClicked() {
    if (mLocateMeEnabled) {
      sLogger.userInteraction("Locate me button clicked, centering map");
      mMapView.centerOverCurrentLocationWithAzimuth();
    } else {
      sLogger.userInteraction("Locate me button clicked, unknown location");
      mView.informUnknownLocation();
    }
  }

  private void openSendGeoDialog(PointGeometry pointGeometry) {
    mNavigator.navigateToSendGeoMessage(pointGeometry);
  }

  private class MapEntityClickedSelectExecutor implements MapEntityClickedListener {
    @Override
    public void entityClicked(String entityId) {
      mSelectMessageByEntityInteractorFactory.create(entityId).execute();
    }

    @Override
    public void kmlEntityClicked(KmlEntityInfo kmlEntityInfo) {
      sLogger.d(String.format("KML entity was clicked: %s, layer: %s", kmlEntityInfo.getName(),
          kmlEntityInfo.getVectorLayerId()));
      mSelectKmlEntityInfoInteractorFactory.create(kmlEntityInfo).execute();
    }
  }

  private class LongPressListener implements OnMapGestureListener {
    @Override
    public void onLongPress(PointGeometry pointGeometry) {
      openSendGeoDialog(pointGeometry);
    }

    @Override
    public void onTap(PointGeometry pointGeometry) {

    }
  }
}
