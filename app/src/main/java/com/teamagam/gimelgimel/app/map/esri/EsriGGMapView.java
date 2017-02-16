package com.teamagam.gimelgimel.app.map.esri;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.AttributeSet;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.MapView;
import com.esri.android.map.TiledLayer;
import com.esri.android.map.ags.ArcGISLocalTiledLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.event.OnLongPressListener;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.common.utils.Constants;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.map.view.GGMapView;
import com.teamagam.gimelgimel.app.map.view.MapEntityClickedListener;
import com.teamagam.gimelgimel.app.map.viewModel.gestures.OnMapGestureListener;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerPresentation;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;

import java.io.File;


public class EsriGGMapView extends MapView implements GGMapView {

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
    private OnMapGestureListener mOnMapGestureListener;

    public EsriGGMapView(Context context) {
        super(context);
        init();
    }

    public EsriGGMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    public void setGGMapGestureListener(OnMapGestureListener onMapGestureListener) {
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
    public void showVectorLayer(VectorLayerPresentation vectorLayerPresentation) {

    }

    @Override
    public void hideVectorLayer(String vectorLayerId) {

    }

    @Override
    public void setOnReadyListener(OnReadyListener onReadyListener) {
        mOnReadyListener = onReadyListener;
    }

    private void init() {
        setBasemap();
        setAllowRotationByPinch(true);
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
        setOnStatusChangedListener(null);
        configureBasemap();
        setupEntityClicksNotifications();
        setupLongPressNotification();
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
}