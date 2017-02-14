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
import com.teamagam.gimelgimel.domain.map.MapEntitySelectedInteractorFactory;
import com.teamagam.gimelgimel.domain.map.SelectEntityInteractorFactory;
import com.teamagam.gimelgimel.domain.map.ViewerCameraController;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSample;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;

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
    private IMapView mMapView;
    private Context mContext;


    @Inject
    public MapViewModel(Context context, Activity activity) {
        mContext = context;
        mActivity = activity;
    }

    public void setMapView(IMapView mapView) {
        mMapView = mapView;
    }

    @Override
    public void destroy() {
        unsubscribe(mDisplayMapEntitiesInteractor,
                mDisplayVectorLayersInteractor);
    }

    public void zoomToLastKnownLocation() {
        sLogger.userInteraction("Locate me button clicked");
        getLastLocationInteractorFactory.create(new ZoomToSubscriber()).execute();
    }

    public void onMapReady() {
        mDisplayMapEntitiesInteractor = mDisplayMapEntitiesInteractorFactory.create(this);
        mDisplayMapEntitiesInteractor.execute();

        mDisplayVectorLayersInteractor = mDisplayVectorLayersInteractorFactory.create(
                new VectorLayersDisplayer());

        mDisplayVectorLayersInteractor.execute();
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


    public OnMapGestureListener getGestureListener() {
        return new GGMapGestureListener(this, mMapView);
    }

    public void openSendGeoDialog(PointGeometryApp pointGeometry) {
        Navigator.navigateToSendGeoMessage(pointGeometry, mActivity);
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
        public void displayShown(VectorLayerPresentation vectorLayer) {
            mMapView.showVectorLayer(vectorLayer);
        }

        @Override
        public void displayHidden(VectorLayerPresentation vectorLayer) {
            mMapView.hideVectorLayer(vectorLayer.getId());
        }
    }
}
