package com.teamagam.gimelgimel.app.map.cesium;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.ValueCallback;

import com.teamagam.gimelgimel.BuildConfig;
import com.teamagam.gimelgimel.app.common.data.SynchronizedDataHolder;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.map.cesium.JavascriptInterfaces.CesiumEntityClickListener;
import com.teamagam.gimelgimel.app.map.cesium.JavascriptInterfaces.CesiumMapGestureDetector;
import com.teamagam.gimelgimel.app.map.cesium.JavascriptInterfaces.CesiumViewerCameraInterface;
import com.teamagam.gimelgimel.app.map.cesium.JavascriptInterfaces.CesiumXWalkResourceClient;
import com.teamagam.gimelgimel.app.map.cesium.JavascriptInterfaces.CesiumXWalkUIClient;
import com.teamagam.gimelgimel.app.map.cesium.bridges.CesiumBaseBridge;
import com.teamagam.gimelgimel.app.map.cesium.bridges.CesiumGestureBridge;
import com.teamagam.gimelgimel.app.map.cesium.bridges.CesiumKMLBridge;
import com.teamagam.gimelgimel.app.map.cesium.bridges.CesiumMapBridge;
import com.teamagam.gimelgimel.app.map.cesium.bridges.CesiumUIJavascriptCommandExecutor;
import com.teamagam.gimelgimel.app.map.cesium.bridges.CesiumVectorLayersBridge;
import com.teamagam.gimelgimel.app.map.model.EntityUpdateEventArgs;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometryApp;
import com.teamagam.gimelgimel.app.map.view.GGMapView;
import com.teamagam.gimelgimel.app.map.viewModel.gestures.OnMapGestureListener;
import com.teamagam.gimelgimel.app.utils.Constants;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.domain.map.entities.ViewerCamera;

import org.xwalk.core.XWalkPreferences;
import org.xwalk.core.XWalkView;

import rx.Observable;

/**
 * Wrapper view class for a WebView-based Cesium viewer
 */
public class CesiumMapView
        extends XWalkView
        implements GGMapView,
        CesiumXWalkUIClient.CesiumJsErrorListener,
        CesiumXWalkResourceClient.CesiumReadyListener,
        CesiumEntityClickListener.OnEntityClickListener {

    private static final AppLogger sLogger = AppLoggerFactory.create(CesiumMapView.class);

    private CesiumVectorLayersBridge mCesiumVectorLayersBridge;
    private CesiumMapBridge mCesiumMapBridge;
    private CesiumKMLBridge mCesiumKMLBridge;
    private OnGGMapReadyListener mOnGGMapReadyListener;

    /**
     * A synchronized data holder is used to allow multi-threaded scenarios
     * that occur while JS thread is updating data, that's simultaneously used
     * by another thread
     */
    private SynchronizedDataHolder<Boolean> mIsGGMapReadySynchronized;
    private SynchronizedDataHolder<Boolean> mIsHandlingError;

    private CesiumMapGestureDetector mCesiumMapGestureDetector;
    private CesiumGestureBridge mCesiumGestureBridge;
    private CesiumViewerCameraInterface mCesiumViewerCameraInterface;
    private MapEntityClickedListener mMapEntityClickedListener;


    public CesiumMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        mIsHandlingError = new SynchronizedDataHolder<>(false);

        XWalkPreferences.setValue(XWalkPreferences.ALLOW_UNIVERSAL_ACCESS_FROM_FILE, true);
        XWalkPreferences.setValue(XWalkPreferences.JAVASCRIPT_CAN_OPEN_WINDOW, false);

        //For debug only
        if (BuildConfig.DEBUG) {
            XWalkPreferences.setValue(XWalkPreferences.REMOTE_DEBUGGING, true);
        }

        mIsGGMapReadySynchronized = new SynchronizedDataHolder<>(false);

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
        addJavascriptInterface(cesiumEntityClickListener,
                CesiumEntityClickListener.JAVASCRIPT_INTERFACE_NAME);
        mCesiumMapGestureDetector = new CesiumMapGestureDetector(this, mCesiumGestureBridge);
        addJavascriptInterface(mCesiumMapGestureDetector,
                CesiumMapGestureDetector.JAVASCRIPT_INTERFACE_NAME);
        mCesiumViewerCameraInterface = new CesiumViewerCameraInterface();
        addJavascriptInterface(mCesiumViewerCameraInterface,
                CesiumViewerCameraInterface.JAVASCRIPT_INTERFACE_NAME);
    }

    @Override
    public void addLayer(String layerId) {
        //todo: adds only Vector Layer.
        mCesiumVectorLayersBridge.addLayer(layerId);
    }

    @Override
    public void removeLayer(String layerId) {
        //todo: adds only Vector Layer.
        mCesiumVectorLayersBridge.removeLayer(layerId);
    }

    @Override
    public void setExtent(float west, float south, float east, float north) {
        mCesiumMapBridge.setExtent(west, south, east, north);
    }

    @Override
    public void lookAt(PointGeometryApp point) {
        mCesiumMapBridge.lookAt(point);
    }

    @Override
    public void lookAt(PointGeometryApp point, float cameraHeight) {
        mCesiumMapBridge.lookAt(point, cameraHeight);
    }

    @Override
    public void setCameraPosition(ViewerCamera viewerCamera) {
        mCesiumMapBridge.setCameraPosition(viewerCamera);
    }

    @Override
    public void readAsyncCenterPosition(final ValueCallback<PointGeometryApp> callback) {
        ValueCallback<String> stringToPointGeometryAdapterCallback = new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String json) {
                if (json == null || json.equals("null")) {
                    sLogger.w("no value returned");
                } else if (json.equals("")) {
                    sLogger.w("empty returned");
                } else {
                    PointGeometryApp point = CesiumUtils.getPointGeometryFromJson(json);
                    sLogger.d(point.toString());
                    callback.onReceiveValue(point);
                }
            }
        };
        mCesiumMapBridge.getPosition(stringToPointGeometryAdapterCallback);
    }

    @Override
    public Observable<ViewerCamera> getViewerCameraObservable() {
        return mCesiumViewerCameraInterface.getViewerCameraObservable();
    }

    @Override
    public void updateMapEntity(EntityUpdateEventArgs eventArgs) {
        switch (eventArgs.eventType) {
            case EntityUpdateEventArgs.LAYER_CHANGED_EVENT_TYPE_ADD: {
                mCesiumVectorLayersBridge.addEntity(eventArgs.layerId, eventArgs.entity);
                break;
            }
            case EntityUpdateEventArgs.LAYER_CHANGED_EVENT_TYPE_UPDATE: {
                mCesiumVectorLayersBridge.updateEntity(eventArgs.layerId, eventArgs.entity);
                break;
            }
            case EntityUpdateEventArgs.LAYER_CHANGED_EVENT_TYPE_REMOVE: {
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
    public void setOnEntityClickedListener(MapEntityClickedListener mapEntityClickedListener) {
        mMapEntityClickedListener = mapEntityClickedListener;
    }

    @Override
    public boolean isReady() {
        return mIsGGMapReadySynchronized.getData();
    }

    @Override
    public void onCesiumError(String error) {
        if (error.contains("ImageryProvider")) {
            mIsHandlingError.setData(true);
        }
    }

    @Override
    public void setGGMapGestureListener(OnMapGestureListener onMapGestureListener) {
        mCesiumMapGestureDetector.setOnMapGestureListener(onMapGestureListener);
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
            }
        });
    }

    @Override
    public void onCesiumEntityClick(String layerId, String entityId) {
        mMapEntityClickedListener.entityClicked(layerId, entityId);
    }
}
