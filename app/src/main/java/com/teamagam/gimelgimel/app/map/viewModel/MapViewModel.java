package com.teamagam.gimelgimel.app.map.viewModel;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
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
import com.teamagam.gimelgimel.app.message.view.SendMessageDialogFragment;
import com.teamagam.gimelgimel.app.model.ViewsModels.UsersLocationViewModel;
import com.teamagam.gimelgimel.app.utils.Constants;
import com.teamagam.gimelgimel.app.view.Navigator;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.base.subscribers.SimpleSubscriber;
import com.teamagam.gimelgimel.domain.location.GetLastLocationInteractorFactory;
import com.teamagam.gimelgimel.domain.map.DisplayMapEntitiesInteractor;
import com.teamagam.gimelgimel.domain.map.DisplayMapEntitiesInteractorFactory;
import com.teamagam.gimelgimel.domain.map.LoadViewerCameraInteractor;
import com.teamagam.gimelgimel.domain.map.LoadViewerCameraInteractorFactory;
import com.teamagam.gimelgimel.domain.map.SaveViewerCameraInteractorFactory;
import com.teamagam.gimelgimel.domain.map.SelectEntityInteractorFactory;
import com.teamagam.gimelgimel.domain.map.ViewerCameraController;
import com.teamagam.gimelgimel.domain.map.entities.ViewerCamera;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSampleEntity;
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
public class MapViewModel implements ViewerCameraController, MapEntityClickedListener,
        DisplayMapEntitiesInteractor.Displayer{

    private IMapView mMapView;

    @Inject
    DisplayMapEntitiesInteractorFactory mDisplayMapEntitiesInteractorFactory;

    @Inject
    SelectEntityInteractorFactory mSelectEntityInteractorFactory;

    //injects
    @Inject
    UsersLocationViewModel mUserLocationsVM;

    @Inject
    GeoEntityTransformer mGeoEntityTransformer;

    //factories
    @Inject
    LoadViewerCameraInteractorFactory mLoadFactory;

    @Inject
    SaveViewerCameraInteractorFactory mSaveFactory;

    @Inject
    Navigator mNavigator;

    @Inject
    GetLastLocationInteractorFactory getLastLocationInteractorFactory;

    //interactors
    private DisplayMapEntitiesInteractor mDisplayMapEntitiesInteractor;
    private LoadViewerCameraInteractor mLoadViewerCameraInteractor;

    //logger
    private Logger sLogger = LoggerFactory.create(getClass());

    private ViewerCamera mCurrentViewerCamera;
    private List<String> mVectorLayers;
    private final Activity mActivity;
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

    public void start() {
    }

    public void resume() {

    }

    public void pause() {

    }

    public void stop() {
        saveCurrentViewerCamera();
    }

    public void destroy() {
        if (mDisplayMapEntitiesInteractor != null) {
            mDisplayMapEntitiesInteractor.unsubscribe();
        }
        if (mLoadViewerCameraInteractor != null) {
            mLoadViewerCameraInteractor.unsubscribe();
        }
    }

    public void sendMessageClicked() {
        sLogger.userInteraction("Send message button clicked");
        new SendMessageDialogFragment()
                .show(mActivity.getFragmentManager(), "sendMessageDialog");
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
    public void setViewerCamera(ViewerCamera viewerCamera) {
        mMapView.setCameraPosition(viewerCamera);
    }

    @Override
    public void entityClicked(String layerId, String entityId) {
        mSelectEntityInteractorFactory.create(entityId).execute();
    }

    private void saveCurrentViewerCamera() {
        mSaveFactory.create(mCurrentViewerCamera).execute();
    }

    public OnMapGestureListener getGestureListener() {
        return new GGMapGestureListener(this, mMapView);
    }

    public ViewerCamera getViewerCamera() {
        return mCurrentViewerCamera;
    }

    public void openSendGeoDialog(PointGeometryApp pointGeometry) {
        mNavigator.navigateToSendGeoMessage(pointGeometry, mActivity);
    }

    private void createVectorLayerIfNeeded(String layerTag) {
        if (!mVectorLayers.contains(layerTag)) {
            mVectorLayers.add(layerTag);
            mMapView.addLayer(layerTag);
        }
    }

    private class ZoomToSubscriber extends SimpleSubscriber<LocationSampleEntity> {
        @Override
        public void onNext(LocationSampleEntity locationSampleEntity) {
            if (locationSampleEntity == null) {
                Toast.makeText(mContext, R.string.locate_me_fab_no_known_location,
                        Toast.LENGTH_SHORT).show();
            } else {
                PointGeometryApp location = new PointGeometryApp(
                        locationSampleEntity.getLocation().getLatitude(),
                        locationSampleEntity.getLocation().getLongitude(),
                        (double)Constants.LOCATE_ME_BUTTON_ALTITUDE_METERS);

                mMapView.lookAt(location);
            }
        }
    }
}
