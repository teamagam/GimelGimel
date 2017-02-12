package com.teamagam.gimelgimel.app.map.esri;

import android.content.Context;
import android.util.AttributeSet;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.MapOptions;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
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
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;


public class EsriGGMapView extends MapView implements GGMapView {

    private static final AppLogger sLogger = AppLoggerFactory.create();
    private static final SpatialReference WGS_84_GEO = SpatialReference.create(
            SpatialReference.WKID_WGS84
    );

    private OnReadyListener mOnReadyListener;
    private ArcGISTiledMapServiceLayer mBaseLayer;
    private GraphicsLayerGGAdapter mGraphicsLayerGGAdapter;

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

        logTappedCoordinates();
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
        GraphicsLayer graphicsLayer = new GraphicsLayer();
        addLayer(graphicsLayer);
        mGraphicsLayerGGAdapter = new GraphicsLayerGGAdapter(graphicsLayer, WGS_84_GEO,
                getSpatialReference());
    }

    private void logTappedCoordinates() {
        setOnSingleTapListener(new OnSingleTapListener() {
            @Override
            public void onSingleTap(float screenx, float screeny) {
                Point point = EsriGGMapView.this.toMapPoint(screenx, screeny);
                sLogger.d("tapped : " + point);
//                int[] graphicIDs = mGraphicLayer.getGraphicIDs(screenx, screeny, 5, 1);
//                Graphic graphic = mGraphicLayer.getGraphic(graphicIDs[0]);
//                mMapEntityClickedListener.entityClicked("", );
            }
        });
    }

    private void setBasemap() {
        loadBasemap();
        setupNotificationOnBasemapLoaded();
    }

    private void loadBasemap() {
        mBaseLayer = new ArcGISTiledMapServiceLayer(Constants.ARC_GIS_TILED_MAP_SERVICE_URL);
        addLayer(mBaseLayer);
        setMapOptions(new MapOptions(MapOptions.MapType.SATELLITE));
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
        setExtentOverIsrael();
        addDynamicGraphicLayer();
        notifyMapReady();
        setOnStatusChangedListener(null);
    }

    private void notifyMapReady() {
        if (mOnReadyListener != null) {
            mOnReadyListener.onReady();
        }
    }

    private Point transformToEsri(PointGeometryApp point) {
        Point p;
        if (point.hasAltitude) {
            p = new Point(point.longitude, point.latitude, point.altitude);
        } else {
            p = new Point(point.longitude, point.latitude);
        }

        return (Point) projectFromWGS84(p);
    }

    private Geometry projectFromWGS84(Geometry p) {
        return GeometryEngine.project(p, WGS_84_GEO,
                getSpatialReference());
    }
}
