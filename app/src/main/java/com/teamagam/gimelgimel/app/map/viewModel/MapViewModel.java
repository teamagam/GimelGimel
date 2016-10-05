package com.teamagam.gimelgimel.app.map.viewModel;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.app.control.sensors.LocationFetcher;
import com.teamagam.gimelgimel.app.injectors.scopes.PerActivity;
import com.teamagam.gimelgimel.app.map.model.VectorLayer;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometry;
import com.teamagam.gimelgimel.app.map.view.GGMapView;
import com.teamagam.gimelgimel.app.map.view.ViewerFragment;
import com.teamagam.gimelgimel.app.map.viewModel.adapters.GeoEntityTransformer;
import com.teamagam.gimelgimel.app.message.view.SendMessageDialogFragment;
import com.teamagam.gimelgimel.app.model.ViewsModels.MessageMapEntitiesViewModel;
import com.teamagam.gimelgimel.app.model.ViewsModels.UsersLocationViewModel;
import com.teamagam.gimelgimel.app.model.entities.LocationSample;
import com.teamagam.gimelgimel.app.utils.Constants;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.base.subscribers.SimpleSubscriber;
import com.teamagam.gimelgimel.domain.map.GetMapVectorLayersInteractorFactory;
import com.teamagam.gimelgimel.domain.map.SyncMapVectorLayersInteractor;
import com.teamagam.gimelgimel.domain.map.SyncMapVectorLayersInteractorFactory;
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
public class MapViewModel {
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

    private final Activity mActivity;

    private Context mContext;

    //logger
    private Logger sLogger = LoggerFactory.create(getClass());

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
    }

    public void destroy() {
        if (mSyncMapEntitiesInteractor != null) {
            mSyncMapEntitiesInteractor.unsubscribe();
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
            PointGeometry location = lastKnownLocation.getLocation();

            location.altitude = Constants.LOCATE_ME_BUTTON_ALTITUDE_METERS;
            mMapView.goToLocation(location);
        }
    }


    public void mapReady() {
        getMapEntitiesInteractorFactory.create(new GetMapVectorLayersSubscriber()).execute();

        mSyncMapEntitiesInteractor = syncMapEntitiesInteractorFactory.create(
                new SyncMapVectorLayersSubscriber());
        mSyncMapEntitiesInteractor.execute();
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
    }
}
