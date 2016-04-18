package com.teamagam.gimelgimel.app.view.viewer.cesium;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.teamagam.gimelgimel.BuildConfig;
import com.teamagam.gimelgimel.app.view.viewer.GGMapView;
import com.teamagam.gimelgimel.app.view.viewer.cesium.JavascriptInterfaces.SelectedLocationUpdater;
import com.teamagam.gimelgimel.app.view.viewer.data.GGLayer;
import com.teamagam.gimelgimel.app.view.viewer.data.KMLLayer;
import com.teamagam.gimelgimel.app.view.viewer.data.LayerChangedEventArgs;
import com.teamagam.gimelgimel.app.view.viewer.data.VectorLayer;
import com.teamagam.gimelgimel.app.view.viewer.data.entities.Entity;
import com.teamagam.gimelgimel.app.view.viewer.data.geometries.PointGeometry;

import java.util.Collection;
import java.util.HashMap;

/**
 * Wrapper view class for a WebView-based Cesium viewer
 */
public class CesiumMapView extends WebView implements GGMapView, VectorLayer.LayerChangedListener {

    public static final String FILE_ANDROID_ASSET_VIEWER =
            "file:///android_asset/cesiumHelloWorld.html";
    public static final String LOG_TAG = CesiumMapView.class.getSimpleName();

    private HashMap<String, GGLayer> mVectorLayers;
    private CesiumVectorLayersBridge mCesiumVectorLayersBridge;
    private CesiumMapBridge mCesiumMapBridge;
    private CesiumKMLBridge mCesiumKMLBridge;
    private SynchronizedDataHolder<PointGeometry> mSelectedLocationHolder;

    public CesiumMapView(Context context) {
        super(context);
        init(null, 0);
    }

    public CesiumMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CesiumMapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        mVectorLayers = new HashMap<>();
        CesiumBaseBridge.JavascriptCommandExecutor jsCommandExecutor =
                new CesiumBaseBridge.JavascriptCommandExecutor() {
                    @Override
                    public void executeJsCommand(String line) {
                        loadUrl(String.format("javascript:%s", line));
                    }

                    @Override
                    public void executeJsCommandForResult(String line,
                                                          ValueCallback<String> callback) {
                        Log.d(LOG_TAG, "JS for result: " + line);
                        evaluateJavascript(line, callback);
                    }
                };

        mCesiumVectorLayersBridge = new CesiumVectorLayersBridge(jsCommandExecutor);
        mCesiumMapBridge = new CesiumMapBridge(jsCommandExecutor);
        mCesiumKMLBridge = new CesiumKMLBridge(jsCommandExecutor);

        WebSettings thisWebSettings = getSettings();
        thisWebSettings.setAllowUniversalAccessFromFileURLs(true);
        thisWebSettings.setAllowFileAccessFromFileURLs(true);
        thisWebSettings.setJavaScriptEnabled(true);

        //TODO: is necessary ?
        thisWebSettings.setUseWideViewPort(true);
        thisWebSettings.setLoadWithOverviewMode(true);
        setWebViewClient(new WebViewClient());
        //

        mSelectedLocationHolder = new SynchronizedDataHolder<>(PointGeometry.DEFAULT_POINT);
        addJavascriptInterface(new SelectedLocationUpdater(mSelectedLocationHolder),
                SelectedLocationUpdater.JAVASCRIPT_INTERFACE_NAME);
        //For debug only
        if (BuildConfig.DEBUG) {
            setWebContentsDebuggingEnabled(true);
        }

        this.loadUrl(FILE_ANDROID_ASSET_VIEWER);
    }


    @Override
    public void addLayer(GGLayer layer) {
        String layerId = layer.getId();
        if (mVectorLayers.containsKey(layerId)) {
            throw new IllegalArgumentException("A layer with this id already exists!");
        }
        mVectorLayers.put(layerId, layer);

        if (layer instanceof VectorLayer) {
            VectorLayer vectorLayer = (VectorLayer) layer;
            mCesiumVectorLayersBridge.addLayer(vectorLayer);
            vectorLayer.setOnLayerChangedListener(this);
        } else if (layer instanceof KMLLayer) {//KML
            KMLLayer kmlLayer = (KMLLayer) layer;
            mCesiumKMLBridge.addLayer(kmlLayer);
        } else {
            throw new UnsupportedOperationException("Given layer type is not supported");
        }

        mVectorLayers.put(layerId, layer);
    }

    @Override
    public void removeLayer(String layerId) {
        GGLayer layer = mVectorLayers.get(layerId);
        if (layer == null) {
            //No layer with given id exists
            return;
        }

        if (layer instanceof VectorLayer) {
            VectorLayer vectorLayer = (VectorLayer) layer;
            mCesiumVectorLayersBridge.removeLayer(vectorLayer);
            vectorLayer.removeLayerChangedListener();
        } else if (layer instanceof KMLLayer) {//KML
            KMLLayer kmlLayer = (KMLLayer) layer;
            mCesiumKMLBridge.removeLayer(kmlLayer);
        } else {
            throw new UnsupportedOperationException("Given layer type is not supported");
        }

        mVectorLayers.remove(layer.getId());
    }

    @Override
    public Collection<GGLayer> getLayers() {
        return mVectorLayers.values();
    }

    @Override
    public GGLayer getLayer(String id) {
        return mVectorLayers.get(id);
    }

    @Override
    public void setExtent(float west, float south, float east, float north) {
        mCesiumMapBridge.setExtent(west, south, east, north);
    }

    @Override
    public void setExtent(Collection<Entity> entities) {
        mCesiumMapBridge.setExtent(entities);
    }

    @Override
    public void zoomTo(float longitude, float latitude, float altitude) {
        mCesiumMapBridge.zoomTo(longitude, latitude, altitude);
    }

    @Override
    public void zoomTo(float longitude, float latitude) {
        mCesiumMapBridge.zoomTo(longitude, latitude);
    }

    @Override
    public void zoomTo(PointGeometry point) {
        mCesiumMapBridge.zoomTo(point);
    }

    @Override
    public void readAsyncCenterPosition(final ValueCallback<PointGeometry> callback) {
        ValueCallback<String> stringToPointGeometryAdapterCallback = new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String json) {
                if (json == null) {
                    Log.w(LOG_TAG, "no value returned");
                } else if (json.equals("")) {
                    Log.w(LOG_TAG, "empty returned");
                } else {
                    PointGeometry point = CesiumUtils.getPointGeometryFromJson(json);
                    Log.d("Cesium Bridge",
                            String.format("%.2f,%.2f", point.latitude, point.longitude));
                    callback.onReceiveValue(point);
                }
            }
        };
        mCesiumMapBridge.getPosition(stringToPointGeometryAdapterCallback);
    }

    @Override
    public PointGeometry getLastTouchedLocation() {
        return mSelectedLocationHolder.getData();
    }


    @Override
    public void layerChanged(LayerChangedEventArgs eventArgs) {
        switch (eventArgs.eventType) {
            case LayerChangedEventArgs.LAYER_CHANGED_EVENT_TYPE_ADD: {
                mCesiumVectorLayersBridge.addEntity(eventArgs.layerId, eventArgs.entity);
                break;
            }
            case LayerChangedEventArgs.LAYER_CHANGED_EVENT_TYPE_UPDATE: {
                mCesiumVectorLayersBridge.updateEntity(eventArgs.layerId, eventArgs.entity);
                break;
            }
            case LayerChangedEventArgs.LAYER_CHANGED_EVENT_TYPE_REMOVE: {
                mCesiumVectorLayersBridge.removeEntity(eventArgs.layerId, eventArgs.entity);
                break;
            }
            default: {
                throw new IllegalArgumentException("Unsupported layer changed event type!");
            }
        }
    }

    @Override
    public View getView() {
        return this;
    }
}
