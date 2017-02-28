package com.teamagam.gimelgimel.app.map.viewModel;

import android.app.Activity;
import android.content.Context;

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
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerPresentation;
import com.teamagam.gimelgimel.domain.location.GetLastLocationInteractorFactory;
import com.teamagam.gimelgimel.domain.map.DisplayMapEntitiesInteractor;
import com.teamagam.gimelgimel.domain.map.DisplayMapEntitiesInteractorFactory;
import com.teamagam.gimelgimel.domain.map.DisplayVectorLayersInteractor;
import com.teamagam.gimelgimel.domain.map.DisplayVectorLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.map.MapEntitySelectedInteractorFactory;
import com.teamagam.gimelgimel.domain.map.SelectEntityInteractorFactory;
import com.teamagam.gimelgimel.domain.map.ViewerCameraController;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;

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
    MapEntitySelectedInteractorFactory mMapEntitySelectedInteractorFactory;

    @Inject
    GetLastLocationInteractorFactory getLastLocationInteractorFactory;

    @Inject
    DisplayVectorLayersInteractorFactory mDisplayVectorLayersInteractorFactory;

    @Inject
    GeoEntityTransformer mGeoEntityTransformer;

    private DisplayMapEntitiesInteractor mDisplayMapEntitiesInteractor;
    private DisplayVectorLayersInteractor mDisplayVectorLayersInteractor;
    private GGMapView mMapView;
    private Context mContext;


    @Inject
    public MapViewModel(Context context, Activity activity) {
        mContext = context;
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
                mDisplayVectorLayersInteractor);
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
        public void displayShown(VectorLayerPresentation vectorLayer) {
            mMapView.showVectorLayer(vectorLayer);
        }

        @Override
        public void displayHidden(VectorLayerPresentation vectorLayer) {
            mMapView.hideVectorLayer(vectorLayer.getId());
        }
    }

    private class MapEntityClickedSelectExecutor implements MapEntityClickedListener {
        @Override
        public void entityClicked(String entityId) {
            mSelectEntityInteractorFactory.create(entityId).execute();
        }
    }
}
