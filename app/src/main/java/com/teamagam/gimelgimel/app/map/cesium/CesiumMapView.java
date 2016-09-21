package com.teamagam.gimelgimel.app.map.cesium;

import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.ValueCallback;

import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.BuildConfig;
import com.teamagam.gimelgimel.app.common.SynchronizedDataHolder;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.app.network.receivers.ConnectivityStatusReceiver;
import com.teamagam.gimelgimel.app.utils.Constants;
import com.teamagam.gimelgimel.app.map.view.GGMapView;
import com.teamagam.gimelgimel.app.map.cesium.JavascriptInterfaces.CesiumEntityClickListener;
import com.teamagam.gimelgimel.app.map.cesium.JavascriptInterfaces.CesiumMapGestureDetector;
import com.teamagam.gimelgimel.app.map.cesium.JavascriptInterfaces.CesiumXWalkResourceClient;
import com.teamagam.gimelgimel.app.map.cesium.JavascriptInterfaces.CesiumXWalkUIClient;
import com.teamagam.gimelgimel.app.map.cesium.bridges.CesiumBaseBridge;
import com.teamagam.gimelgimel.app.map.cesium.bridges.CesiumGestureBridge;
import com.teamagam.gimelgimel.app.map.cesium.bridges.CesiumKMLBridge;
import com.teamagam.gimelgimel.app.map.cesium.bridges.CesiumMapBridge;
import com.teamagam.gimelgimel.app.map.cesium.bridges.CesiumUIJavascriptCommandExecutor;
import com.teamagam.gimelgimel.app.map.cesium.bridges.CesiumVectorLayersBridge;
import com.teamagam.gimelgimel.app.map.model.GGLayer;
import com.teamagam.gimelgimel.app.map.model.KMLLayer;
import com.teamagam.gimelgimel.app.map.model.LayerChangedEventArgs;
import com.teamagam.gimelgimel.app.map.model.VectorLayer;
import com.teamagam.gimelgimel.app.map.model.entities.Entity;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometry;
import com.teamagam.gimelgimel.app.map.viewModel.gestures.OnMapGestureListener;

import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkView;

import java.util.Collection;
import java.util.HashMap;

/**
 * Wrapper view class for a WebView-based Cesium viewer
 */
public class CesiumMapView
        extends XWalkView
        implements GGMapView, VectorLayer.LayerChangedListener,
        ConnectivityStatusReceiver.NetworkAvailableListener,
        CesiumXWalkUIClient.CesiumJsErrorListener,
        CesiumXWalkResourceClient.CesiumReadyListener,
        CesiumEntityClickListener.OnEntityClickListener {

    private static final Logger sLogger = LoggerFactory.create(CesiumMapView.class);

    // A key to store the data in {@link Bundle} object.
    private static final String CURRENT_CAMERA_POSITION_KEY = "cesiumCameraPosition";

    private HashMap<String, GGLayer> mVectorLayers;
    private CesiumVectorLayersBridge mCesiumVectorLayersBridge;
    private CesiumMapBridge mCesiumMapBridge;
    private CesiumKMLBridge mCesiumKMLBridge;
    private OnGGMapReadyListener mOnGGMapReadyListener;
    private ConnectivityStatusReceiver mConnectivityStatusReceiver;
    private OnGGMapReadyListener mInternalOnGGMapReadyListener;

    /**
     * A synchronized data holder is used to allow multi-threaded scenarios
     * that occur while JS thread is updating data, that's simultaneously used
     * by another thread
     */
    private SynchronizedDataHolder<Boolean> mIsGGMapReadySynchronized;
    private SynchronizedDataHolder<Boolean> mIsHandlingError;

    private CesiumMapGestureDetector mCesiumMapGestureDetector;
    private CesiumGestureBridge mCesiumGestureBridge;


    public CesiumMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        mVectorLayers = new HashMap<>();
        mIsHandlingError = new SynchronizedDataHolder<>(false);

        XWalkPreferences.setValue(XWalkPreferences.ALLOW_UNIVERSAL_ACCESS_FROM_FILE, true);
        XWalkPreferences.setValue(XWalkPreferences.JAVASCRIPT_CAN_OPEN_WINDOW, false);

        //For debug only
        if (BuildConfig.DEBUG) {
            XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);
        }

        mIsGGMapReadySynchronized = new SynchronizedDataHolder<>(false);
        mConnectivityStatusReceiver = new ConnectivityStatusReceiver(this);

        // Set the WebClient. so we can change the behavior of the WebView
        setUIClient(new CesiumXWalkUIClient(this, this));
        setResourceClient(new CesiumXWalkResourceClient(this, this));

        initializeJavascriptBridges();
        initializeJavascriptInterfaces();

        mIsGGMapReadySynchronized.setData(false);
        load(Constants.CESIUM_HTML_LOCAL_FILEPATH, null);
    }

    private void initializeJavascriptBridges() {
        CesiumBaseBridge.JavascriptCommandExecutor jsCommandExecutor =
                new CesiumUIJavascriptCommandExecutor(this);

        mCesiumVectorLayersBridge = new CesiumVectorLayersBridge(jsCommandExecutor);
        mCesiumMapBridge = new CesiumMapBridge(jsCommandExecutor);
        mCesiumKMLBridge = new CesiumKMLBridge(jsCommandExecutor);
        mCesiumGestureBridge = new CesiumGestureBridge(jsCommandExecutor);
    }


    private void initializeJavascriptInterfaces() {
        CesiumEntityClickListener cesiumEntityClickListener = new CesiumEntityClickListener(this);
        addJavascriptInterface(cesiumEntityClickListener, CesiumEntityClickListener.JAVASCRIPT_INTERFACE_NAME);
        mCesiumMapGestureDetector = new CesiumMapGestureDetector(this, mCesiumGestureBridge);
        addJavascriptInterface(mCesiumMapGestureDetector, CesiumMapGestureDetector.JAVASCRIPT_INTERFACE_NAME);
    }


    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mConnectivityStatusReceiver != null) {
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mConnectivityStatusReceiver);
        }
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
    public void flyTo(PointGeometry point) {
        mCesiumMapBridge.flyTo(point);
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
                if (json == null || json.equals("null")) {
                    sLogger.w("no value returned");
                } else if (json.equals("")) {
                    sLogger.w("empty returned");
                } else {
                    PointGeometry point = CesiumUtils.getPointGeometryFromJson(json);
                    sLogger.d(point.toString());
                    callback.onReceiveValue(point);
                }
            }
        };
        mCesiumMapBridge.getPosition(stringToPointGeometryAdapterCallback);
    }

    @Override
    public PointGeometry getLastViewedLocation() {
        return mCesiumMapGestureDetector.getLastViewedLocation();
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
    public void onCesiumError(String error) {
        if (error.contains("ImageryProvider")) {
            mIsHandlingError.setData(true);
            IntentFilter intentFilter = new IntentFilter(ConnectivityStatusReceiver.INTENT_NAME);
            LocalBroadcastManager.getInstance(getContext()).registerReceiver(mConnectivityStatusReceiver,
                    intentFilter);
        }
    }

    @Override
    public void onNetworkAvailableChange(boolean isNetworkAvailable) {
        if (mIsHandlingError.getData() && isNetworkAvailable) {
            mCesiumMapBridge.reloadImageryProvider();
            mIsHandlingError.setData(false);
        }
    }

    @Override
    public void saveViewState(final Bundle outState) {
        if (outState != null) {
            outState.putParcelable(CURRENT_CAMERA_POSITION_KEY, getLastViewedLocation());
        }
    }

    @Override
    public void restoreViewState(Bundle inState) {
        // Make sure the Bundle isn't null.
        // Null is a valid value, because the Bundle can be null in some situations,
        // and throwing an exception here will crash the app,
        // instead of initialize with default values.
        if (inState != null) {

            // Also, check the savedLocation object, the bundle may return null or default,
            // if the save hasn't occurred yet.
            if (hasSavedLocation(inState)) {
                final PointGeometry savedLocation = inState.getParcelable(CURRENT_CAMERA_POSITION_KEY);

                restoreMapExtent(savedLocation);
            }
        }
    }

    @Override
    public void setGGMapGestureListener(OnMapGestureListener onMapGestureListener) {
        mCesiumMapGestureDetector.setOnMapGestureListener(onMapGestureListener);
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
    @Override
    public void onCesiumReady() {
        //Runs runnable on UI thread
        CesiumMapView.this.post(new Runnable() {
            @Override
            public void run() {
                sLogger.d("load finished");
                if (mIsGGMapReadySynchronized.getData()) {
                    return;
                }
                mIsGGMapReadySynchronized.setData(true);

                if (mOnGGMapReadyListener != null) {
                    mOnGGMapReadyListener.onGGMapViewReady();
                }
                if (mInternalOnGGMapReadyListener != null) {
                    mInternalOnGGMapReadyListener.onGGMapViewReady();
                }

            }

        });
    }

    @Override
    public void onCesiumEntityClick(String layerId, String entityId) {
        validateLayerExists(layerId);

        GGLayer layer = mVectorLayers.get(layerId);
        if (!(layer instanceof VectorLayer)) {
            sLogger.d("layer is not a vector layer");
            return;
        }
        validateEntityExists((VectorLayer) layer, entityId);

        Entity entity = ((VectorLayer) layer).getEntity(entityId);
        entity.clicked();
    }

    private void validateEntityExists(VectorLayer vectorLayer, String entityId) {
        if (vectorLayer.getEntity(entityId) == null) {
            sLogger.w("entity Id not found in vector layer");
            throw new IllegalArgumentException("entity not found with Id: " + entityId);
        }
    }

    private void validateLayerExists(String layerId) {
        if (!mVectorLayers.containsKey(layerId)) {
            sLogger.w("layer Id not found");
            throw new IllegalArgumentException("layer not found with Id: " + layerId);
        }
    }
}