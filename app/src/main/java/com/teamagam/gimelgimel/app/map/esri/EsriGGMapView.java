package com.teamagam.gimelgimel.app.map.esri;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOptions;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.Symbol;
import com.esri.core.symbol.TextSymbol;
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

    private static final SpatialReference WGS_84_GEO = SpatialReference.create(
            SpatialReference.WKID_WGS84_WEB_MERCATOR
    );


    private GraphicsLayer mGraphicLayer;
    Map<String, Integer> mEntityIdToGraphicId;

    public EsriGGMapView(Context context) {
        super(context);

        init();
    }

    public EsriGGMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        addLayer(new ArcGISTiledMapServiceLayer(
                "http://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer"
        ));
        setMapOptions(new MapOptions(MapOptions.MapType.SATELLITE));
//        setExtent(new Envelope(34, 29, 36, 33));

//        mGraphicLayer = new GraphicsLayer();
//        addLayer(mGraphicLayer);
        mEntityIdToGraphicId = new TreeMap<>();
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

    @Override
    public void setOnEntityClickedListener(MapEntityClickedListener mapEntityClickedListener) {

    }

    @Override
    public void showVectorLayer(VectorLayerPresentation vectorLayerPresentation) {

    }

    @Override
    public void hideVectorLayer(String vectorLayerId) {

    }

    private Point transformToEsri(PointGeometryApp point) {
        Point p;
        if (point.hasAltitude) {
            p = new Point(point.latitude, point.longitude, point.altitude);
        } else {
            p = new Point(point.latitude, point.altitude);
        }

        return p;
//        return (Point) GeometryEngine.project(p, WGS_84_GEO,
//                getSpatialReference());
    }
}
