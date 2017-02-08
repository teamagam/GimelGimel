package com.teamagam.gimelgimel.app.map.esri;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

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
import com.esri.core.map.Graphic;
import com.esri.core.symbol.Symbol;
import com.esri.core.symbol.TextSymbol;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.map.model.EntityUpdateEventArgs;
import com.teamagam.gimelgimel.app.map.model.entities.Entity;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.map.model.symbols.SymbolApp;
import com.teamagam.gimelgimel.app.map.view.GGMapView;
import com.teamagam.gimelgimel.app.map.view.MapEntityClickedListener;
import com.teamagam.gimelgimel.app.map.viewModel.gestures.OnMapGestureListener;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerPresentation;
import com.teamagam.gimelgimel.domain.map.entities.ViewerCamera;

import java.util.Map;
import java.util.TreeMap;

import rx.Observable;


public class EsriGGMapView extends MapView implements GGMapView {

    private static final AppLogger sLogger = AppLoggerFactory.create();
    private static final SpatialReference WGS_84_GEO = SpatialReference.create(
            SpatialReference.WKID_WGS84
    );


    private GraphicsLayer mGraphicLayer;
    Map<String, Integer> mEntityIdToGraphicId;
    private MapEntityClickedListener mMapEntityClickedListener;

    public EsriGGMapView(Context context) {
        super(context);

        init();
    }

    public EsriGGMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    public View getView() {
        return this;
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setGGMapGestureListener(OnMapGestureListener onMapGestureListener) {

    }

    @Override
    public void addLayer(String layerId) {

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
    public void setCameraPosition(ViewerCamera viewerCamera) {

    }

    @Override
    public Observable<ViewerCamera> getViewerCameraObservable() {
        return null;
    }

    @Override
    public void updateMapEntity(EntityUpdateEventArgs eventArgs) {
        Entity entity = eventArgs.entity;
        switch (eventArgs.eventType) {
            case EntityUpdateEventArgs.LAYER_CHANGED_EVENT_TYPE_ADD:
                addToMap(entity);
                break;
            case EntityUpdateEventArgs.LAYER_CHANGED_EVENT_TYPE_REMOVE:
                removeFromMap(entity);
                break;
            case EntityUpdateEventArgs.LAYER_CHANGED_EVENT_TYPE_UPDATE:
                removeFromMap(entity);
                addToMap(entity);
                break;
            default:
                //log
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

    private void init() {
        setBasemap();

        setAllowRotationByPinch(true);

        logTappedCoordinates();

        addDynamicGraphicLayer();
    }

    private void setExtentOverIsrael() {
        Envelope israelEnvelope = new Envelope(33, 29, 36, 34);
        Envelope projectedIsraelEnvelope = (Envelope) projectFromWGS84(israelEnvelope);
        setExtent(projectedIsraelEnvelope);
    }

    private void addDynamicGraphicLayer() {
        mGraphicLayer = new GraphicsLayer();
        addLayer(mGraphicLayer);

        mEntityIdToGraphicId = new TreeMap<>();
    }

    private void logTappedCoordinates() {
        setOnSingleTapListener(new OnSingleTapListener() {
            @Override
            public void onSingleTap(float screenx, float screeny) {
                Point point = EsriGGMapView.this.toMapPoint(screenx, screeny);
                sLogger.d("tapped : " + point);
                int[] graphicIDs = mGraphicLayer.getGraphicIDs(screenx, screeny, 5, 1);
                Graphic graphic = mGraphicLayer.getGraphic(graphicIDs[0]);
//                mMapEntityClickedListener.entityClicked("", );
            }
        });
    }

    private void setBasemap() {
        addLayer(new ArcGISTiledMapServiceLayer(
                "http://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer"
        ));

        setMapOptions(new MapOptions(MapOptions.MapType.SATELLITE));

        setOnStatusChangedListener(new OnStatusChangedListener() {
            @Override
            public void onStatusChanged(Object o, STATUS status) {
                if (status == STATUS.LAYER_LOADED) {
                    if (o instanceof Layer) {
                        setExtentOverIsrael();
                        setOnStatusChangedListener(null);
                    }
                }
            }
        });
    }

    private void addToMap(Entity entity) {
        Graphic graphic = createGraphic(entity);
        int graphicId = mGraphicLayer.addGraphic(graphic);
        mEntityIdToGraphicId.put(entity.getId(), graphicId);
    }

    private void removeFromMap(Entity entity) {
        int gId = mEntityIdToGraphicId.get(entity.getId());
        mGraphicLayer.removeGraphic(gId);
    }

    private Graphic createGraphic(Entity entity) {
        Geometry geometry = transformToEsri((PointGeometryApp) entity.getGeometry());
        Symbol symbol = createSymbol(entity.getSymbol());
        return new Graphic(geometry, symbol);
    }

    private Symbol createSymbol(SymbolApp symbol) {
        return new TextSymbol(10, "Pin", Color.RED);
    }

    private Point transformToEsri(PointGeometryApp point) {
        Point p;
        if (point.hasAltitude) {
            p = new Point(point.longitude, point.latitude, point.altitude);
        } else {
            p = new Point(point.longitude, point.latitude);
        }

//        return p;

        return (Point) projectFromWGS84(p);
    }

    private Geometry projectFromWGS84(Geometry p) {
        return GeometryEngine.project(p, WGS_84_GEO,
                getSpatialReference());
    }
}
