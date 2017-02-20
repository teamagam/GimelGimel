package com.teamagam.gimelgimel.app.map.viewModel;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;
import com.teamagam.gimelgimel.app.common.launcher.Navigator;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.common.utils.Constants;
import com.teamagam.gimelgimel.app.injectors.scopes.PerActivity;
import com.teamagam.gimelgimel.app.map.cesium.MapEntityClickedListener;
import com.teamagam.gimelgimel.app.map.model.EntityUpdateEventArgs;
import com.teamagam.gimelgimel.app.map.model.entities.Entity;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.map.view.GGMapView;
import com.teamagam.gimelgimel.app.map.view.ViewerFragment;
import com.teamagam.gimelgimel.app.map.viewModel.adapters.GeoEntityTransformer;
import com.teamagam.gimelgimel.app.map.viewModel.gestures.GGMapGestureListener;
import com.teamagam.gimelgimel.app.map.viewModel.gestures.OnMapGestureListener;
import com.teamagam.gimelgimel.domain.base.interactors.Interactor;
import com.teamagam.gimelgimel.domain.base.subscribers.SimpleSubscriber;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerPresentation;
import com.teamagam.gimelgimel.domain.location.GetLastLocationInteractorFactory;
import com.teamagam.gimelgimel.domain.map.DisplayMapEntitiesInteractor;
import com.teamagam.gimelgimel.domain.map.DisplayMapEntitiesInteractorFactory;
import com.teamagam.gimelgimel.domain.map.DisplayVectorLayersInteractor;
import com.teamagam.gimelgimel.domain.map.DisplayVectorLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.map.LoadViewerCameraInteractor;
import com.teamagam.gimelgimel.domain.map.LoadViewerCameraInteractorFactory;
import com.teamagam.gimelgimel.domain.map.MapEntitySelectedInteractorFactory;
import com.teamagam.gimelgimel.domain.map.SaveViewerCameraInteractorFactory;
import com.teamagam.gimelgimel.domain.map.SelectEntityInteractorFactory;
import com.teamagam.gimelgimel.domain.map.ViewerCameraController;
import com.teamagam.gimelgimel.domain.map.entities.ViewerCamera;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSample;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * View Model that handles map related callbacks from user in {@link ViewerFragment}.
 * Also handles it's view {@link GGMapView}.
 * <p>
 * Controls communication between views and models of the presentation
 * layer.
 */
@PerActivity
public class MapViewModel extends BaseViewModel<ViewerFragment>
        implements ViewerCameraController, MapEntityClickedListener,
        DisplayMapEntitiesInteractor.Displayer {

    private static final AppLogger sLogger = AppLoggerFactory.create(
            MapViewModel.class);

    private final Activity mActivity;

    @Inject
    DisplayMapEntitiesInteractorFactory mDisplayMapEntitiesInteractorFactory;

    @Inject
    SelectEntityInteractorFactory mSelectEntityInteractorFactory;

    @Inject
    LoadViewerCameraInteractorFactory mLoadFactory;

    @Inject
    SaveViewerCameraInteractorFactory mSaveFactory;

    @Inject
    MapEntitySelectedInteractorFactory mMapEntitySelectedInteractorFactory;

    @Inject
    GetLastLocationInteractorFactory getLastLocationInteractorFactory;

    @Inject
    DisplayVectorLayersInteractorFactory mDisplayVectorLayersInteractorFactory;

    @Inject
    GeoEntityTransformer mGeoEntityTransformer;

    private DisplayMapEntitiesInteractor mDisplayMapEntitiesInteractor;
    private LoadViewerCameraInteractor mLoadViewerCameraInteractor;
    private DisplayVectorLayersInteractor mDisplayVectorLayersInteractor;
    private IMapView mMapView;
    private ViewerCamera mCurrentViewerCamera;
    private List<String> mVectorLayers;
    private Context mContext;


    @Inject
    public MapViewModel(Context context, Activity activity) {
        mContext = context;
        mActivity = activity;
        mVectorLayers = new ArrayList<>();
    }

    public void setMapView(IMapView mapView) {
        mMapView = mapView;
    }

    @Override
    public void stop() {
        saveCurrentViewerCamera();
    }

    @Override
    public void destroy() {
        unsubscribe(mDisplayMapEntitiesInteractor,
                mLoadViewerCameraInteractor,
                mDisplayVectorLayersInteractor);
    }

    public void zoomToLastKnownLocation() {
        sLogger.userInteraction("Locate me button clicked");
        getLastLocationInteractorFactory.create(new ZoomToSubscriber()).execute();
    }

    public void mapReady() {
        mLoadViewerCameraInteractor = mLoadFactory.create(this);
        mLoadViewerCameraInteractor.execute();

        mDisplayMapEntitiesInteractor = mDisplayMapEntitiesInteractorFactory.create(this);
        mDisplayMapEntitiesInteractor.execute();

        mDisplayVectorLayersInteractor = mDisplayVectorLayersInteractorFactory.create(
                new VectorLayersDisplayer());

        mDisplayVectorLayersInteractor.execute();

        mMapView.getViewerCameraObservable().subscribe(new SimpleSubscriber<ViewerCamera>() {
            @Override
            public void onNext(ViewerCamera viewerCamera) {
                mCurrentViewerCamera = viewerCamera;
            }
        });
    }

    @Override
    public void displayEntityNotification(GeoEntityNotification geoEntityNotification) {
        String layerTag = geoEntityNotification.getGeoEntity().getLayerTag();
        createVectorLayerIfNeeded(layerTag);

        Entity entity = mGeoEntityTransformer.transform(geoEntityNotification.getGeoEntity());
        int eventType;
        EntityUpdateEventArgs entityUpdateEventArgs = null;
        switch (geoEntityNotification.getAction()) {
            case GeoEntityNotification.ADD:
                eventType = EntityUpdateEventArgs.LAYER_CHANGED_EVENT_TYPE_ADD;
                entityUpdateEventArgs = new EntityUpdateEventArgs(layerTag, entity, eventType);
                break;
            case GeoEntityNotification.REMOVE:
                eventType = EntityUpdateEventArgs.LAYER_CHANGED_EVENT_TYPE_REMOVE;
                entityUpdateEventArgs = new EntityUpdateEventArgs(layerTag, entity, eventType);
                break;
            case GeoEntityNotification.UPDATE:
                eventType = EntityUpdateEventArgs.LAYER_CHANGED_EVENT_TYPE_UPDATE;
                entityUpdateEventArgs = new EntityUpdateEventArgs(layerTag, entity, eventType);
                break;
            default:
        }
        mMapView.updateMapEntity(entityUpdateEventArgs);
    }

    @Override
    public void setViewerCamera(Geometry geometry) {
        PointGeometry pg = (PointGeometry) geometry;
        PointGeometryApp pointGeometry = PointGeometryApp.create(pg);

        mMapView.lookAt(pointGeometry);
    }

    @Override
    public void entityClicked(String layerId, String entityId) {
        mMapEntitySelectedInteractorFactory.create(entityId).execute();
    }

    public OnMapGestureListener getGestureListener() {
        return new GGMapGestureListener(this, mMapView);
    }

    public ViewerCamera getViewerCamera() {
        return mCurrentViewerCamera;
    }

    @Override
    public void setViewerCamera(ViewerCamera viewerCamera) {
        mMapView.setCameraPosition(viewerCamera);
    }

    public void openSendGeoDialog(PointGeometryApp pointGeometry) {
        Navigator.navigateToSendGeoMessage(pointGeometry, mActivity);
    }

    private void saveCurrentViewerCamera() {
        mSaveFactory.create(mCurrentViewerCamera).execute();
    }

    private void createVectorLayerIfNeeded(String layerTag) {
        if (!mVectorLayers.contains(layerTag)) {
            mVectorLayers.add(layerTag);
            mMapView.addLayer(layerTag);
        }
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

    private class ZoomToSubscriber extends SimpleSubscriber<LocationSample> {
        @Override
        public void onNext(LocationSample locationSample) {
            if (locationSample == null) {
                Toast.makeText(mContext, R.string.locate_me_fab_no_known_location,
                        Toast.LENGTH_SHORT).show();
            } else {
                PointGeometryApp location = new PointGeometryApp(
                        locationSample.getLocation().getLatitude(),
                        locationSample.getLocation().getLongitude(),
                        (double) Constants.LOCATE_ME_BUTTON_ALTITUDE_METERS);

                mMapView.lookAt(location);
            }
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
}
