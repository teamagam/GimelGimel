package com.teamagam.gimelgimel.app.map.viewModel;

import android.app.Activity;

import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;
import com.teamagam.gimelgimel.app.common.launcher.Navigator;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.injectors.scopes.PerActivity;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.map.view.GGMapView;
import com.teamagam.gimelgimel.app.map.view.MapEntityClickedListener;
import com.teamagam.gimelgimel.app.map.view.ViewerFragment;
import com.teamagam.gimelgimel.app.map.viewModel.adapters.GeoEntityTransformer;
import com.teamagam.gimelgimel.domain.base.interactors.Interactor;
import com.teamagam.gimelgimel.domain.layers.DisplayVectorLayersInteractor;
import com.teamagam.gimelgimel.domain.layers.DisplayVectorLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerPresentation;
import com.teamagam.gimelgimel.domain.location.GetLastLocationInteractorFactory;
import com.teamagam.gimelgimel.domain.map.DisplayMapEntitiesInteractor;
import com.teamagam.gimelgimel.domain.map.DisplayMapEntitiesInteractorFactory;
import com.teamagam.gimelgimel.domain.map.SelectEntityInteractorFactory;
import com.teamagam.gimelgimel.domain.map.SelectKmlEntityInteractorFactory;
import com.teamagam.gimelgimel.domain.map.ViewerCameraController;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.KmlEntityInfo;
import com.teamagam.gimelgimel.domain.messages.SelectMessageInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.repository.EntityMessageMapper;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;
import com.teamagam.gimelgimel.domain.rasters.DisplayIntermediateRastersInteractor;
import com.teamagam.gimelgimel.domain.rasters.DisplayIntermediateRastersInteractorFactory;

import javax.inject.Inject;

@PerActivity
public class MapViewModel extends BaseViewModel<ViewerFragment>
        implements ViewerCameraController,
        DisplayMapEntitiesInteractor.Displayer {

    private static final AppLogger sLogger = AppLoggerFactory.create(
            MapViewModel.class);

    private final Activity mActivity;

    @Inject
    DisplayMapEntitiesInteractorFactory mDisplayMapEntitiesInteractorFactory;

    @Inject
    SelectEntityInteractorFactory mSelectEntityInteractorFactory;

    @Inject
    SelectMessageInteractorFactory mSelectMessageInteractorFactory;

    @Inject
    SelectKmlEntityInteractorFactory mSelectKmlEntityInfoInteractorFactory;

    @Inject
    GetLastLocationInteractorFactory getLastLocationInteractorFactory;

    @Inject
    DisplayVectorLayersInteractorFactory mDisplayVectorLayersInteractorFactory;

    @Inject
    DisplayIntermediateRastersInteractorFactory mDisplayRastersInteractorFactory;

    @Inject
    GeoEntityTransformer mGeoEntityTransformer;

    @Inject
    EntityMessageMapper mEntityMessageMapper;

    private DisplayMapEntitiesInteractor mDisplayMapEntitiesInteractor;
    private DisplayVectorLayersInteractor mDisplayVectorLayersInteractor;
    private DisplayIntermediateRastersInteractor mDisplayRastersInteractor;
    private GGMapView mMapView;


    @Inject
    public MapViewModel(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void start() {
        super.start();
        mMapView.setOnEntityClickedListener(new MapEntityClickedSelectExecutor());
        mMapView.setOnMapGestureListener(this::openSendGeoDialog);
        mMapView.setOnReadyListener(this::onMapReady);
    }

    @Override
    public void destroy() {
        unsubscribe(mDisplayMapEntitiesInteractor,
                mDisplayVectorLayersInteractor,
                mDisplayRastersInteractor);
    }

    @Override
    public void displayEntityNotification(GeoEntityNotification geoEntityNotification) {
        mMapView.updateMapEntity(geoEntityNotification);
    }

    @Override
    public void setViewerCamera(Geometry geometry) {
        PointGeometry pg = (PointGeometry) geometry;
        PointGeometryApp pointGeometry = PointGeometryApp.create(pg);

        mMapView.lookAt(pointGeometry);
    }

    public void setMapView(GGMapView mapView) {
        mMapView = mapView;
    }

    public void onLocationFabClicked() {
        sLogger.userInteraction("Locate me button clicked");
        mMapView.centerOverCurrentLocationWithAzimuth();
    }

    public void onMapReady() {
        mDisplayMapEntitiesInteractor = mDisplayMapEntitiesInteractorFactory.create(this);
        mDisplayMapEntitiesInteractor.execute();

        mDisplayVectorLayersInteractor = mDisplayVectorLayersInteractorFactory.create(
                new VectorLayersDisplayer());

        mDisplayVectorLayersInteractor.execute();

        mDisplayRastersInteractor = mDisplayRastersInteractorFactory.create(
                new IntermediateRasterDisplayer());
        mDisplayRastersInteractor.execute();
    }

    private void openSendGeoDialog(PointGeometry pointGeometry) {
        Navigator.navigateToSendGeoMessage(PointGeometryApp.create(pointGeometry), mActivity);
    }

    private void unsubscribe(Interactor... interactors) {
        for (Interactor interactor : interactors) {
            unsubscribe(interactor);
        }
    }

    private void unsubscribe(Interactor interactor) {
        if (interactor != null) {
            interactor.unsubscribe();
        }
    }

    private class VectorLayersDisplayer implements DisplayVectorLayersInteractor.Displayer {
        @Override
        public void display(VectorLayerPresentation vectorLayerPresentation) {
            if (vectorLayerPresentation.isShown()) {
                mMapView.showVectorLayer(vectorLayerPresentation);
            } else {
                mMapView.hideVectorLayer(vectorLayerPresentation.getId());
            }
        }
    }

    private class MapEntityClickedSelectExecutor implements MapEntityClickedListener {
        @Override
        public void entityClicked(String entityId) {
            mSelectEntityInteractorFactory.create(entityId).execute();
            String messageId = mEntityMessageMapper.getMessageId(entityId);
            mSelectMessageInteractorFactory.create(messageId).execute();
        }

        @Override
        public void kmlEntityClicked(KmlEntityInfo kmlEntityInfo) {
            sLogger.d(String.format("KML entity was clicked: %s, layer: %s",
                    kmlEntityInfo.getName(), kmlEntityInfo.getVectorLayerId()));
            mSelectKmlEntityInfoInteractorFactory.create(kmlEntityInfo).execute();
        }
    }

    private class IntermediateRasterDisplayer
            implements DisplayIntermediateRastersInteractor.Displayer {
        @Override
        public void display(DisplayIntermediateRastersInteractor.IntermediateRasterPresentation
                                    intermediateRasterPresentation) {
            if (intermediateRasterPresentation.isShown()) {
                mMapView.setIntermediateRaster(intermediateRasterPresentation);
            } else {
                mMapView.removeIntermediateRaster();
            }
        }
    }
}
