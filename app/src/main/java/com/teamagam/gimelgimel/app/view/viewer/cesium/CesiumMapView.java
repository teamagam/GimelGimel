package com.teamagam.gimelgimel.app.view.viewer.cesium;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.teamagam.gimelgimel.BuildConfig;
import com.teamagam.gimelgimel.app.common.SynchronizedDataHolder;
import com.teamagam.gimelgimel.app.view.viewer.GGMapView;
import com.teamagam.gimelgimel.app.view.viewer.OnGGMapReadyListener;
import com.teamagam.gimelgimel.app.view.viewer.cesium.JavascriptInterfaces.CesiumReadyJavascriptInterface;
import com.teamagam.gimelgimel.app.view.viewer.cesium.JavascriptInterfaces.LocationUpdater;
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

    // A key to store the data in {@link Bundle} object.
    private static final String CURRENT_CAMERA_POSITION_KEY = "cesiumCameraPosition";

    private HashMap<String, GGLayer> mVectorLayers;
    private CesiumVectorLayersBridge mCesiumVectorLayersBridge;
    private CesiumMapBridge mCesiumMapBridge;
    private CesiumKMLBridge mCesiumKMLBridge;
    private OnGGMapReadyListener mOnGGMapReadyListener;
    private OnGGMapReadyListener mInternalOnGGMapReadyListener;

    /**
     * A synchronized data holder is used to allow multi-threaded scenarios
     * that occur while JS thread is updating data, that's simultaneously used
     * by another thread
     */
    private SynchronizedDataHolder<Boolean> mIsGGMapReadySynchronized;

    private LocationUpdater mLocationUpdater;

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

        //For debug only
        if (BuildConfig.DEBUG) {
            setWebContentsDebuggingEnabled(true);
        }

        mIsGGMapReadySynchronized = new SynchronizedDataHolder<>(false);

        // Set the WebClient. so we can change the behavior of the WebView
        setWebViewClient(new CesiumMapViewClient());

        initializeJavascriptInterfaces();
        this.loadUrl(FILE_ANDROID_ASSET_VIEWER);
    }

    private void initializeJavascriptInterfaces() {
        mLocationUpdater = new LocationUpdater();

        CesiumReadyJavascriptInterface cesiumReadyJavascriptInterface =
                new CesiumReadyJavascriptInterface(
                        new UiThreadRunnerCesiumReadyListener());

        addJavascriptInterface(mLocationUpdater, LocationUpdater.JAVASCRIPT_INTERFACE_NAME);
        addJavascriptInterface(cesiumReadyJavascriptInterface,
                CesiumReadyJavascriptInterface.JAVASCRIPT_INTERFACE_NAME);
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
                if (json == null && json.equals("null")) {
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
        return mLocationUpdater.getLastSelectedLocation();
    }

    public PointGeometry getLastViewedLocation() {
        return mLocationUpdater.getLastViewedLocation();
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

    @Override
    public void setOnReadyListener(OnGGMapReadyListener listener) {
        mOnGGMapReadyListener = listener;
    }

    @Override
    public boolean isReady() {
        return mIsGGMapReadySynchronized.getData();
    }

    @Override
    public void saveViewState(final Bundle outState) {
        if(outState != null) {
            outState.putParcelable(CURRENT_CAMERA_POSITION_KEY, getLastViewedLocation());
        }
    }

    @Override
    public void restoreViewState(Bundle inState) {
        // Make sure the Bundle isn't null.
        // Null is a valid value, because the Bundle can be null in some situations,
        // and throwing an exception here will crash the app,
        // instead of initialize with default values.
        if(inState != null) {

            // Also, check the savedLocation object, the bundle may return null or default,
            // if the save hasn't occurred yet.
            if(hasSavedLocation(inState)) {
                final PointGeometry savedLocation = inState.getParcelable(CURRENT_CAMERA_POSITION_KEY);

                restoreMapExtent(savedLocation);
            }
        }
    }

    private boolean hasSavedLocation(Bundle bundle) {
        PointGeometry savedLocation = bundle.getParcelable(CURRENT_CAMERA_POSITION_KEY);

        return savedLocation != null && savedLocation != PointGeometry.DEFAULT_POINT;
    }

    private void restoreMapExtent(final PointGeometry savedLocation) {
        // Wait for the map to be ready before zooming into the last view.
        if (isReady()) {
            zoomTo(savedLocation);
        } else {
            mInternalOnGGMapReadyListener = new OnGGMapReadyListener() {
                @Override
                public void onGGMapViewReady() {
                    zoomTo(savedLocation);
                }
            };
        }
    }

    /**
     * Implements on ready cesium event listener.
     * Runs an injected {@link OnGGMapReadyListener} object's call on UI thread
     */
    private class UiThreadRunnerCesiumReadyListener
            implements CesiumReadyJavascriptInterface.CesiumReadyListener {

        @Override
        public void onCesiumReady() {
            //Runs runnable on UI thread
            CesiumMapView.this.post(new Runnable() {
                @Override
                public void run() {
                    CesiumMapView.this.mIsGGMapReadySynchronized.setData(true);
                    OnGGMapReadyListener listener = CesiumMapView.this.mOnGGMapReadyListener;
                    OnGGMapReadyListener internalListener = CesiumMapView.this.mInternalOnGGMapReadyListener;
                    if (listener != null) {
                        listener.onGGMapViewReady();
                    }
                    if (internalListener != null) {
                        internalListener.onGGMapViewReady();
                    }
                }
            });
        }
    }
}
