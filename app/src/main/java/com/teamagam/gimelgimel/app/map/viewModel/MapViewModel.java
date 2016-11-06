package com.teamagam.gimelgimel.app.map.viewModel;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.app.control.sensors.LocationFetcher;
import com.teamagam.gimelgimel.app.injectors.scopes.PerActivity;
import com.teamagam.gimelgimel.app.map.model.VectorLayer;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.map.view.GGMapView;
import com.teamagam.gimelgimel.app.map.view.ViewerFragment;
import com.teamagam.gimelgimel.app.map.viewModel.adapters.GeoEntityTransformer;
import com.teamagam.gimelgimel.app.map.viewModel.gestures.GGMapGestureListener;
import com.teamagam.gimelgimel.app.map.viewModel.gestures.OnMapGestureListener;
import com.teamagam.gimelgimel.app.message.model.contents.LocationSample;
import com.teamagam.gimelgimel.app.message.view.SendMessageDialogFragment;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageMapEntitiesViewModel;
import com.teamagam.gimelgimel.app.model.ViewsModels.UsersLocationViewModel;
import com.teamagam.gimelgimel.app.utils.Constants;
import com.teamagam.gimelgimel.app.view.Navigator;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.base.subscribers.SimpleSubscriber;
import com.teamagam.gimelgimel.domain.map.GetMapVectorLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.map.LoadViewerCameraInteractor;
import com.teamagam.gimelgimel.domain.map.LoadViewerCameraInteractorFactory;
import com.teamagam.gimelgimel.domain.map.SaveViewerCameraInteractorFactory;
import com.teamagam.gimelgimel.domain.map.SyncMapVectorLayersInteractor;
import com.teamagam.gimelgimel.domain.map.SyncMapVectorLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.map.ViewerCameraController;
import com.teamagam.gimelgimel.domain.map.entities.ViewerCamera;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;

import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

/**
 * View Model that handles map related callbacks from user in {@link ViewerFragment}.
 * Also handles it's view {@link GGMapView}.
 * <p>
 * Controls communication between views and models of the presentation
 * layer.
 */
@PerActivity
public class MapViewModel implements ViewerCameraController {
//implements SendGeographicMessageDialog.SendGeographicMessageDialogInterface

    private IMapView mMapView;

    private Map<String, VectorLayer> mVectorLayers;

    //interactors
    private SyncMapVectorLayersInteractor mSyncMapEntitiesInteractor;

    //injects
    @Inject
    MessageMapEntitiesViewModel mMessageLocationVM;

    @Inject
    UsersLocationViewModel mUserLocationsVM;

    @Inject
    GeoEntityTransformer mGeoEntityTransformer;

    //factories
    @Inject
    GetMapVectorLayersInteractorFactory getMapEntitiesInteractorFactory;

    @Inject
    SyncMapVectorLayersInteractorFactory syncMapEntitiesInteractorFactory;

    @Inject
    LoadViewerCameraInteractorFactory mLoadFactory;

    @Inject
    SaveViewerCameraInteractorFactory mSaveFactory;

    @Inject
    Navigator mNavigator;

    private final Activity mActivity;

    private Context mContext;

    //logger
    private Logger sLogger = LoggerFactory.create(getClass());
    private ViewerCamera mCurrentViewerCamera;
    private LoadViewerCameraInteractor mLoadViewerCameraInteractor;

    @Inject
    public MapViewModel(Context context, Activity activity) {
        mContext = context;
        mActivity = activity;

        mVectorLayers = new TreeMap<>();
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
        if (mSyncMapEntitiesInteractor != null) {
            mSyncMapEntitiesInteractor.unsubscribe();
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

        LocationSample lastKnownLocation = LocationFetcher.getInstance(
                mContext).getLastKnownLocation();

        if (lastKnownLocation == null) {
            Toast.makeText(mContext, R.string.locate_me_fab_no_known_location,
                    Toast.LENGTH_SHORT).show();
        } else {
            PointGeometryApp location = lastKnownLocation.getLocation();

            location.altitude = Constants.LOCATE_ME_BUTTON_ALTITUDE_METERS;
            mMapView.lookAt(location);
        }
    }

    public void mapReady() {
        mLoadViewerCameraInteractor = mLoadFactory.create(this);
        mLoadViewerCameraInteractor.execute();

        getMapEntitiesInteractorFactory.create(new GetMapVectorLayersSubscriber()).execute();

        mSyncMapEntitiesInteractor = syncMapEntitiesInteractorFactory.create(
                new SyncMapVectorLayersSubscriber());
        mSyncMapEntitiesInteractor.execute();

        mMapView.getViewerCameraObservable().subscribe(new SimpleSubscriber<ViewerCamera>() {
            @Override
            public void onNext(ViewerCamera viewerCamera) {
                mCurrentViewerCamera = viewerCamera;
            }
        });
    }

    private void drawAll(Collection<GeoEntity> geoEntities) {
        for (GeoEntity geoEntity : geoEntities) {
            drawEntity(geoEntity);
        }
    }

    private void drawEntity(GeoEntity geoEntity) {
        VectorLayer vectorLayer = getVectorLayer(geoEntity.getLayerTag());
        vectorLayer.addEntity(mGeoEntityTransformer.transform(geoEntity));
    }

    private void updateVectorLayers(GeoEntityNotification geoEntityNotification) {
        switch (geoEntityNotification.getAction()) {
            case GeoEntityNotification.ADD:
                drawEntity(geoEntityNotification.getGeoEntity());
                break;
            case GeoEntityNotification.REMOVE:
                hideEntity(geoEntityNotification.getGeoEntity());
                break;
            default:
        }
    }

    private void hideEntity(GeoEntity geoEntity) {
        VectorLayer vectorLayer = getVectorLayer(geoEntity.getLayerTag());
        vectorLayer.removeEntity(geoEntity.getId());
    }

    private VectorLayer getVectorLayer(String layerTag) {
        if (mVectorLayers.containsKey(layerTag)) {
            return mVectorLayers.get(layerTag);
        }

        VectorLayer vectorLayer = new VectorLayer(layerTag);
        mVectorLayers.put(layerTag, vectorLayer);
        mMapView.addLayer(vectorLayer);

        return vectorLayer;
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

    private class GetMapVectorLayersSubscriber extends SimpleSubscriber<Collection<GeoEntity>> {
        @Override
        public void onNext(Collection<GeoEntity> geoEntities) {
            drawAll(geoEntities);
        }
    }

    private class SyncMapVectorLayersSubscriber extends SimpleSubscriber<GeoEntityNotification> {

        @Override
        public void onNext(GeoEntityNotification geoEntityNotification) {
            updateVectorLayers(geoEntityNotification);
        }

        @Override
        public void onError(Throwable e) {
            sLogger.e("point next error: ", e);
        }
    }
}
