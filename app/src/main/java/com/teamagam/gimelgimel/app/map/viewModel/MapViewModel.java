package com.teamagam.gimelgimel.app.map.viewModel;

import android.app.Activity;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.app.common.launcher.Navigator;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.map.view.GGMapView;
import com.teamagam.gimelgimel.app.map.view.MapEntityClickedListener;
import com.teamagam.gimelgimel.app.map.view.ViewerFragment;
import com.teamagam.gimelgimel.domain.layers.DisplayVectorLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.map.DisplayMapEntitiesInteractorFactory;
import com.teamagam.gimelgimel.domain.map.SelectKmlEntityInteractorFactory;
import com.teamagam.gimelgimel.domain.map.SelectMessageByEntityInteractorFactory;
import com.teamagam.gimelgimel.domain.map.ViewerCameraController;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.KmlEntityInfo;
import com.teamagam.gimelgimel.domain.rasters.DisplayIntermediateRastersInteractorFactory;

@AutoFactory
public class MapViewModel extends BaseMapViewModel<ViewerFragment>
        implements ViewerCameraController {

    private static final AppLogger sLogger = AppLoggerFactory.create(
            MapViewModel.class);

    private final SelectKmlEntityInteractorFactory mSelectKmlEntityInfoInteractorFactory;
    private final SelectMessageByEntityInteractorFactory mSelectMessageByEntityInteractorFactory;

    private final Navigator mNavigator;
    private final Activity mActivity;
    private final GGMapView mMapView;

    public MapViewModel(
            @Provided DisplayMapEntitiesInteractorFactory displayMapEntitiesInteractorFactory,
            @Provided DisplayVectorLayersInteractorFactory displayVectorLayersInteractorFactory,
            @Provided DisplayIntermediateRastersInteractorFactory
                    displayIntermediateRastersInteractorFactory,
            @Provided SelectKmlEntityInteractorFactory selectKmlEntityInfoInteractorFactory,
            @Provided SelectMessageByEntityInteractorFactory selectMessageByEntityInteractorFactory,
            @Provided Navigator navigator,
            Activity activity,
            GGMapView ggMapView) {
        super(displayMapEntitiesInteractorFactory, displayVectorLayersInteractorFactory,
                displayIntermediateRastersInteractorFactory, ggMapView);
        mSelectKmlEntityInfoInteractorFactory = selectKmlEntityInfoInteractorFactory;
        mSelectMessageByEntityInteractorFactory = selectMessageByEntityInteractorFactory;
        mNavigator = navigator;
        mActivity = activity;
        mMapView = ggMapView;
    }

    @Override
    public void init() {
        super.init();
        mMapView.setOnEntityClickedListener(new MapEntityClickedSelectExecutor());
        mMapView.setOnMapGestureListener(this::openSendGeoDialog);
    }

    @Override
    public void setViewerCamera(Geometry geometry) {
        mMapView.lookAt(geometry);
    }

    public void onLocationFabClicked() {
        sLogger.userInteraction("Locate me button clicked");
        mMapView.centerOverCurrentLocationWithAzimuth();
    }

    private void openSendGeoDialog(PointGeometry pointGeometry) {
        mNavigator.navigateToSendGeoMessage(PointGeometryApp.create(pointGeometry));
    }

    private class MapEntityClickedSelectExecutor implements MapEntityClickedListener {
        @Override
        public void entityClicked(String entityId) {
            mSelectMessageByEntityInteractorFactory.create(entityId).execute();
        }

        @Override
        public void kmlEntityClicked(KmlEntityInfo kmlEntityInfo) {
            sLogger.d(String.format("KML entity was clicked: %s, layer: %s",
                    kmlEntityInfo.getName(), kmlEntityInfo.getVectorLayerId()));
            mSelectKmlEntityInfoInteractorFactory.create(kmlEntityInfo).execute();
        }
    }
}
