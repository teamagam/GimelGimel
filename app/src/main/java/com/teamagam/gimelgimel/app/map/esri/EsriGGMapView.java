package com.teamagam.gimelgimel.app.map.esri;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationListener;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.MapView;
import com.esri.android.map.TiledLayer;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.event.OnLongPressListener;
import com.esri.android.map.event.OnPinchListener;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.ogc.kml.KmlLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.common.utils.Constants;
import com.teamagam.gimelgimel.app.map.esri.graphics.GraphicsLayerGGAdapter;
import com.teamagam.gimelgimel.app.map.esri.plugins.Compass;
import com.teamagam.gimelgimel.app.map.esri.plugins.ScaleBar;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.map.view.GGMapView;
import com.teamagam.gimelgimel.app.map.view.MapEntityClickedListener;
import com.teamagam.gimelgimel.app.map.viewModel.gestures.OnMapGestureListener;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerPresentation;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;

import java.io.File;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;


public class EsriGGMapView extends MapView implements GGMapView {

    public static final int PLUGINS_PADDING = 25;
    private static final AppLogger sLogger = AppLoggerFactory.create();
    private static final String ESRI_STATE_PREF_KEY = "esri_state";
    private static final SpatialReference WGS_84_GEO = SpatialReference.create(
            SpatialReference.WKID_WGS84
    );

    private OnReadyListener mOnReadyListener;
    private TiledLayer mBaseLayer;
    private GraphicsLayerGGAdapter mGraphicsLayerGGAdapter;
    private GraphicsLayer mGraphicsLayer;
    private MapEntityClickedListener mMapEntityClickedListener;
    private Map<String, KmlLayer> mVectorLayerIdToKmlLayerMap;
    private OnMapGestureListener mOnMapGestureListener;
    private Collection<OnPinchListener> mOnPinchListeners;
    private RelativeLayout mPluginsContainerLayout;
    private LocationDisplayer mLocationDisplayer;
    private Compass mCompass;

    public EsriGGMapView(Context context) {
        super(context);
        init(context);
    }

    public EsriGGMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    @Override
    public void unpause() {
        super.unpause();
        if (mCompass != null) {
            mCompass.start();
        }
    }

    @Override
    public void pause() {
        super.pause();
        if (mCompass != null) {
            mCompass.stop();
        }
    }

    @Override
    public void setOnMapGestureListener(OnMapGestureListener onMapGestureListener) {
        mOnMapGestureListener = onMapGestureListener;
    }

    @Override
    public void saveState() {
        SharedPreferences prefs = getDefaultSharedPreferences();
        String retainState = retainState();
        prefs.edit().putString(ESRI_STATE_PREF_KEY, retainState).apply();
    }

    @Override
    public void restoreState() {
        if (hasLastExtent()) {
            restoreLastExtent();
        }
    }

    @Override
    public void centerOverCurrentLocationWithAzimuth() {
        setScale(Constants.LOCATE_ME_BUTTON_VIEWER_SCALE);
        mLocationDisplayer.centerAndShowAzimuth();
    }

    @Override
    public void lookAt(PointGeometryApp point) {
        Point esriPoint = transformToEsri(point);
        centerAt(esriPoint, true);
    }

    @Override
    public void lookAt(PointGeometryApp point, float cameraHeight) {
        Point esriPoint = transformToEsri(point);
        zoomToResolution(esriPoint, cameraHeight);
    }

    @Override
    public void updateMapEntity(GeoEntityNotification geoEntityNotification) {
        GeoEntity entity = geoEntityNotification.getGeoEntity();
        int action = geoEntityNotification.getAction();
        switch (action) {
            case GeoEntityNotification.ADD:
                mGraphicsLayerGGAdapter.draw(entity);
                break;
            case GeoEntityNotification.REMOVE:
                mGraphicsLayerGGAdapter.remove(entity.getId());
                break;
            case GeoEntityNotification.UPDATE:
                mGraphicsLayerGGAdapter.remove(entity.getId());
                mGraphicsLayerGGAdapter.draw(entity);
                break;
            default:
                sLogger.w("Update map entity could not be done because of unknown action code: "
                        + action);
        }
    }

    @Override
    public void setOnEntityClickedListener(MapEntityClickedListener mapEntityClickedListener) {
        mMapEntityClickedListener = mapEntityClickedListener;
    }

    @Override
    public void showVectorLayer(VectorLayerPresentation vlp) {
        hideIfDisplayed(vlp.getId());
        addLayer(vlp);
    }

    @Override
    public void hideVectorLayer(String vectorLayerId) {
        removeLayer(mVectorLayerIdToKmlLayerMap.get(vectorLayerId));
        mVectorLayerIdToKmlLayerMap.remove(vectorLayerId);
    }

    @Override
    public void setOnReadyListener(OnReadyListener onReadyListener) {
        mOnReadyListener = onReadyListener;
    }

    private void init(Context context) {
        setBasemap();
        setAllowRotationByPinch(true);
        mVectorLayerIdToKmlLayerMap = new TreeMap<>();
        mOnPinchListeners = new LinkedList<>();
        setOnPinchListener(new PinchGestureDelegator());
        mLocationDisplayer = getLocationDisplayer(context);
    }

    private void setBasemap() {
        loadBasemap();
        setupNotificationOnBasemapLoaded();
    }

    private void loadBasemap() {
        mBaseLayer = getTiledLayer();
        addLayer(mBaseLayer);
    }

    private TiledLayer getTiledLayer() {
        String localTpkFile = getLocalTpkFilepath();
        if (isFileExists(localTpkFile)) {
            sLogger.d("Creating base map from local tpk");
            return new ArcGISLocalTiledLayer(localTpkFile);
        } else {
            sLogger.d("Creating base map from remote service");
            return new ArcGISTiledMapServiceLayer(Constants.ARC_GIS_TILED_MAP_SERVICE_URL);
        }
    }

    private boolean isFileExists(String filepath) {
        return new File(filepath).exists();
    }

    private String getLocalTpkFilepath() {
        return getContext().getExternalFilesDir(null)
                + File.separator
                + Constants.OFFLINE_TPK_DIR_NAME
                + File.separator
                + Constants.OFFLINE_TPK_FILENAME;
    }

    private void setupNotificationOnBasemapLoaded() {
        setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                if (isBaseLayerLoaded(o, status)) {
                    onBasemapLoaded();
                }
            }

            private boolean isBaseLayerLoaded(Object o, STATUS status) {
                return status == STATUS.LAYER_LOADED
                        && o instanceof Layer
                        && o == mBaseLayer;
            }
        });
    }

    private void onBasemapLoaded() {
        setInitialExtent();
        addDynamicGraphicLayer();
        notifyMapReady();
        setPlugins();
        setOnStatusChangedListener(null);
        configureBasemap();
        setupEntityClicksNotifications();
        setupLongPressNotification();
        setupLocationDisplayer();
    }

    private void setInitialExtent() {
        if (hasLastExtent()) {
            restoreLastExtent();
        } else {
            setExtentOverIsrael();
        }
    }

    private boolean hasLastExtent() {
        return getDefaultSharedPreferences().contains(ESRI_STATE_PREF_KEY);
    }

    private void restoreLastExtent() {
        SharedPreferences prefs = getDefaultSharedPreferences();
        String esriState = prefs.getString(ESRI_STATE_PREF_KEY, "");
        restoreState(esriState);
    }

    private SharedPreferences getDefaultSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(getContext());
    }

    private void setExtentOverIsrael() {
        Envelope israelEnvelope = new Envelope(
                Constants.ISRAEL_WEST_LONG_ENVELOPE,
                Constants.ISRAEL_SOUTH_LAT_ENVELOPE,
                Constants.ISRAEL_EAST_LONG_ENVELOPE,
                Constants.ISRAEL_NORTH_LAT_ENVELOPE);
        Envelope projectedIsraelEnvelope = (Envelope) projectFromWGS84(israelEnvelope);
        setExtent(projectedIsraelEnvelope);
    }

    private void addDynamicGraphicLayer() {
        mGraphicsLayer = new GraphicsLayer();
        addLayer(mGraphicsLayer);
        mGraphicsLayerGGAdapter = new GraphicsLayerGGAdapter(
                getContext(),
                mGraphicsLayer,
                WGS_84_GEO, getSpatialReference());
    }

    private void notifyMapReady() {
        if (mOnReadyListener != null) {
            mOnReadyListener.onReady();
        }
    }

    private void setPlugins() {
        setPluginsContainerLayout();
        setCompass();
        setScaleBar();
    }

    private void setPluginsContainerLayout() {
        mPluginsContainerLayout = createPluginsContainerLayout();
        addView(mPluginsContainerLayout);
    }

    private RelativeLayout createPluginsContainerLayout() {
        RelativeLayout layout = new RelativeLayout(getContext());
        layout.setLayoutParams(getPluginsContainerLayoutParams());
        layout.setPadding(
                PLUGINS_PADDING, PLUGINS_PADDING, PLUGINS_PADDING, PLUGINS_PADDING);
        return layout;
    }

    private LayoutParams getPluginsContainerLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    private void setCompass() {
        mCompass = new Compass(getContext(), null, this);
        mPluginsContainerLayout.addView(
                mCompass, getRelativeLayoutParams(RelativeLayout.ALIGN_PARENT_TOP));
        mCompass.start();
    }

    private void setScaleBar() {
        ScaleBar scaleBar = new ScaleBar(getContext(), null, this);
        mPluginsContainerLayout.addView(
                scaleBar, getRelativeLayoutParams(RelativeLayout.ALIGN_PARENT_BOTTOM));

        setOnZoomListener(scaleBar);
        mOnPinchListeners.add(scaleBar);
    }

    private RelativeLayout.LayoutParams getRelativeLayoutParams(int alignment) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(alignment);
        return params;
    }

    private void configureBasemap() {
        optimizePanning();
        setMaxScale(getMapMaxScale());
    }

    private void optimizePanning() {
        mBaseLayer.setBufferEnabled(true);
        mBaseLayer.setBufferExpansionFactor(2f);
    }

    private double getMapMaxScale() {
        return Math.max(
                mBaseLayer.getMaxScale(),
                Constants.VIEWER_MAX_SCALE_RATIO);
    }

    private void setupEntityClicksNotifications() {
        setOnSingleTapListener(new EntityClickedNotifier());
    }

    private void setupLongPressNotification() {
        setOnLongPressListener(new LongPressGestureNotifier());
    }

    private void notifyEntityClicked(String entityId) {
        if (mMapEntityClickedListener != null) {
            mMapEntityClickedListener.entityClicked(entityId);
        }
    }

    private LocationDisplayer getLocationDisplayer(Context context) {
        LocationListener locationListener = ((GGApplication) context.getApplicationContext())
                .getApplicationComponent()
                .locationListener();

        return new LocationDisplayer(getLocationDisplayManager(),
                locationListener);
    }

    private void setupLocationDisplayer() {
        mLocationDisplayer.displaySelfLocation();
        mLocationDisplayer.start();
    }

    private Point transformToEsri(PointGeometryApp point) {
        return transformToEsri(point.getPointDomain());
    }

    private Point transformToEsri(PointGeometry point) {
        return EsriUtils.transformAndProject(point, WGS_84_GEO, getSpatialReference());
    }

    private Geometry projectFromWGS84(Geometry p) {
        return GeometryEngine.project(p, WGS_84_GEO, getSpatialReference());
    }

    private Geometry projectToWgs84(Point point) {
        return GeometryEngine.project(point, getSpatialReference(),
                WGS_84_GEO);
    }

    private void hideIfDisplayed(String id) {
        if (isDisplayed(id)) {
            hideVectorLayer(id);
        }
    }

    private boolean isDisplayed(String vectorLayerId) {
        return mVectorLayerIdToKmlLayerMap.containsKey(vectorLayerId);
    }

    private void addLayer(VectorLayerPresentation vlp) {
        mVectorLayerIdToKmlLayerMap.put(vlp.getId(), new KmlLayer(vlp.getLocalURI().getPath()));
        addLayer(mVectorLayerIdToKmlLayerMap.get(vlp.getId()));
    }

    private class EntityClickedNotifier implements OnSingleTapListener {
        @Override
        public void onSingleTap(float screenX, float screenY) {
            int graphicId = getClickedGraphicId(screenX, screenY);
            if (isEntityClicked(graphicId)) {
                notifyEntityClicked(mGraphicsLayerGGAdapter.getEntityId(graphicId));
            }
        }

        private int getClickedGraphicId(float screenX, float screenY) {
            int[] graphicIDs = mGraphicsLayer.getGraphicIDs(screenX, screenY,
                    Constants.VIEWER_ENTITY_CLICKING_TOLERANCE_DP, 1);
            if (graphicIDs.length == 1) {
                return graphicIDs[0];
            }
            return -1;
        }

        private boolean isEntityClicked(int graphicId) {
            return graphicId != -1;
        }
    }

    private class LongPressGestureNotifier implements OnLongPressListener {
        @Override
        public boolean onLongPress(float screenX, float screenY) {
            PointGeometry pointGeometry = getClickedPointGeometry(screenX, screenY);
            notifyOnLocationChosen(pointGeometry);
            return false;
        }

        private PointGeometry getClickedPointGeometry(float screenX, float screenY) {
            Point mapPoint = EsriGGMapView.this.toMapPoint(screenX, screenY);
            Point wgs84Point = (Point) projectToWgs84(mapPoint);
            return new PointGeometry(wgs84Point.getY(), wgs84Point.getX(), wgs84Point.getZ());
        }

        private void notifyOnLocationChosen(PointGeometry pointGeometry) {
            if (mOnMapGestureListener != null) {
                mOnMapGestureListener.onLocationChosen(pointGeometry);
            }
        }
    }

    private class PinchGestureDelegator implements OnPinchListener {

        @Override
        public void prePointersMove(float v, float v1, float v2, float v3, double v4) {
            for (OnPinchListener l : mOnPinchListeners) {
                l.prePointersMove(v, v1, v2, v3, v4);
            }
        }

        @Override
        public void postPointersMove(float v, float v1, float v2, float v3, double v4) {
            for (OnPinchListener l : mOnPinchListeners) {
                l.postPointersMove(v, v1, v2, v3, v4);
            }
        }

        @Override
        public void prePointersDown(float v, float v1, float v2, float v3, double v4) {
            for (OnPinchListener l : mOnPinchListeners) {
                l.prePointersDown(v, v1, v2, v3, v4);
            }
        }

        @Override
        public void postPointersDown(float v, float v1, float v2, float v3, double v4) {
            for (OnPinchListener l : mOnPinchListeners) {
                l.postPointersDown(v, v1, v2, v3, v4);
            }
        }

        @Override
        public void prePointersUp(float v, float v1, float v2, float v3, double v4) {
            for (OnPinchListener l : mOnPinchListeners) {
                l.prePointersUp(v, v1, v2, v3, v4);
            }
        }

        @Override
        public void postPointersUp(float v, float v1, float v2, float v3, double v4) {
            for (OnPinchListener l : mOnPinchListeners) {
                l.postPointersUp(v, v1, v2, v3, v4);
            }
        }
    }
}