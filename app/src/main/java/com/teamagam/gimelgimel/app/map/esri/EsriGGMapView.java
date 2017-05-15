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
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.map.event.OnStatusChangedListener;
import com.esri.android.map.ogc.kml.KmlLayer;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Point;
import com.esri.core.map.ogc.kml.KmlNode;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.common.utils.Constants;
import com.teamagam.gimelgimel.app.map.esri.graphic.EsriSymbolCreator;
import com.teamagam.gimelgimel.app.map.esri.graphic.GraphicsLayerGGAdapter;
import com.teamagam.gimelgimel.app.map.esri.plugins.Compass;
import com.teamagam.gimelgimel.app.map.esri.plugins.ScaleBar;
import com.teamagam.gimelgimel.app.map.esri.plugins.SelfUpdatingViewPlugin;
import com.teamagam.gimelgimel.app.map.view.GGMapView;
import com.teamagam.gimelgimel.app.map.view.MapEntityClickedListener;
import com.teamagam.gimelgimel.app.map.viewModel.gestures.OnMapGestureListener;
import com.teamagam.gimelgimel.data.common.ExternalDirProvider;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerPresentation;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.KmlEntityInfo;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;
import com.teamagam.gimelgimel.domain.rasters.entity.IntermediateRaster;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;


public class EsriGGMapView extends MapView implements GGMapView {

    static final int INTERMEDIATE_LAYER_POSITION = 1;
    public static final int PLUGINS_PADDING = 25;

    private static final AppLogger sLogger = AppLoggerFactory.create();
    private static final String ESRI_STATE_PREF_KEY = "esri_state";

    @Inject
    ExternalDirProvider mExternalDirProvider;

    @Inject
    EsriSymbolCreator mEsriSymbolCreator;

    private IntermediateRasterDisplayer mIntermediateRasterDisplayer;
    private GraphicsLayerGGAdapter mGraphicsLayerGGAdapter;
    private TiledLayer mBaseLayer;
    private GraphicsLayer mGraphicsLayer;
    private Map<String, KmlLayer> mVectorLayerIdToKmlLayerMap;

    private MapEntityClickedListener mMapEntityClickedListener;
    private OnReadyListener mOnReadyListener;
    private OnMapGestureListener mOnMapGestureListener;

    private RelativeLayout mPluginsContainerLayout;

    private LocationDisplayer mLocationDisplayer;
    private Compass mCompass;
    private ScaleBar mScaleBar;

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
        startPlugin(mCompass);
        startPlugin(mScaleBar);
    }

    @Override
    public void pause() {
        super.pause();
        stopPlugin(mCompass);
        stopPlugin(mScaleBar);
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
    public void setIntermediateRaster(IntermediateRaster intermediateRaster) {
        mIntermediateRasterDisplayer.display(intermediateRaster);
    }

    @Override
    public void removeIntermediateRaster() {
        mIntermediateRasterDisplayer.clear();
    }

    @Override
    public PointGeometry getMapCenter() {
        Point point = projectToWgs84(getCenter());
        return new PointGeometry(point.getY(), point.getX());
    }

    @Override
    public void lookAt(com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry geometry) {
        Geometry esriGeometry = transformToEsri(geometry);
        Point center = getGeometryCenter(esriGeometry);
        centerAt(center, true);
    }

    @Override
    public void lookAtSpecificPoint(com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry geometry) {
        Geometry esriGeometry = transformToEsri(geometry);
        Point center = getGeometryCenter(esriGeometry);
        centerAndZoom(center.getY(), center.getX(), 3);
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
        if (isInEditMode()) {
            return;
        }
        ((GGApplication) context.getApplicationContext()).getApplicationComponent().inject(this);
        setBasemap();
        setAllowRotationByPinch(true);
        mVectorLayerIdToKmlLayerMap = new TreeMap<>();
        mLocationDisplayer = getLocationDisplayer(context);
        mIntermediateRasterDisplayer =
                new IntermediateRasterDisplayer(this, INTERMEDIATE_LAYER_POSITION);
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
        return mExternalDirProvider.getExternalFilesDir()
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
                mGraphicsLayer,
                EsriUtils.WGS_84_GEO, getSpatialReference(),
                mEsriSymbolCreator);
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
        mCompass.setOnClickListener(v -> rotateToNorth());

        mPluginsContainerLayout.addView(
                mCompass, createCompassLayoutParams(RelativeLayout.ALIGN_PARENT_TOP));

        mCompass.start();
    }

    private void rotateToNorth() {
        mLocationDisplayer.displaySelfLocation();
        setRotationAngle(0);
    }

    private RelativeLayout.LayoutParams createCompassLayoutParams(int align) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                mCompass.getBitmapWidth(), mCompass.getBitmapHeight());
        params.addRule(RelativeLayout.ALIGN_PARENT_END);
        params.addRule(align);
        return params;
    }

    private void setScaleBar() {
        mScaleBar = new ScaleBar(getContext(), null, this);
        mPluginsContainerLayout.addView(mScaleBar,
                createScaleBarLayoutParams(RelativeLayout.ALIGN_PARENT_BOTTOM));
        mScaleBar.start();
    }

    private RelativeLayout.LayoutParams createScaleBarLayoutParams(int align) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_START);
        params.addRule(align);
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
                Constants.VIEWER_MIN_SCALE_RATIO);
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

    private void notifyKmlEntityClicked(KmlEntityInfo kmlEntityInfo) {
        if (mMapEntityClickedListener != null) {
            mMapEntityClickedListener.kmlEntityClicked(kmlEntityInfo);
        }
    }

    private LocationDisplayer getLocationDisplayer(Context context) {
        LocationListener locationListener = ((GGApplication) context.getApplicationContext())
                .getApplicationComponent()
                .locationListener();

        return new LocationDisplayer(getLocationDisplayManager(),
                locationListener);
    }

    private void stopPlugin(SelfUpdatingViewPlugin plugin) {
        if (plugin != null) {
            plugin.stop();
        }
    }

    private void startPlugin(SelfUpdatingViewPlugin plugin) {
        if (plugin != null) {
            plugin.start();
        }
    }

    private void setupLocationDisplayer() {
        mLocationDisplayer.displaySelfLocation();
        mLocationDisplayer.start();
    }

    private Geometry transformToEsri(
            com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry geometry) {
        return EsriUtils.transformAndProject(geometry, EsriUtils.WGS_84_GEO, getSpatialReference());
    }

    private Point getGeometryCenter(Geometry esriGeometry) {
        Envelope resEnvelope = new Envelope();
        esriGeometry.queryEnvelope(resEnvelope);
        return resEnvelope.getCenter();
    }

    private Geometry projectFromWGS84(Geometry p) {
        return GeometryEngine.project(p, EsriUtils.WGS_84_GEO, getSpatialReference());
    }

    private Point projectToWgs84(Point point) {
        return (Point) GeometryEngine.project(point, getSpatialReference(),
                EsriUtils.WGS_84_GEO);
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
            handleEntityClicks(screenX, screenY);
            handleKmlEntityClicks(screenX, screenY);
        }

        private void handleEntityClicks(float screenX, float screenY) {
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

        private void handleKmlEntityClicks(float screenX, float screenY) {
            KmlEntityInfo info = getClickedKmlInfo(screenX, screenY);
            if (info != null) {
                notifyKmlEntityClicked(info);
            }
        }

        private KmlEntityInfo getClickedKmlInfo(float screenX, float screenY) {
            ArrayList<String> layerIds = new ArrayList<>(mVectorLayerIdToKmlLayerMap.keySet());
            for (String id : layerIds) {
                KmlLayer layer = mVectorLayerIdToKmlLayerMap.get(id);
                KmlNode[] kmlNodes = getKmlNodes(screenX, screenY, layer);
                if (kmlNodes.length > 0) {
                    return createKmlEntityInfo(kmlNodes[0], id);
                }
            }
            return null;
        }

        private KmlNode[] getKmlNodes(float screenX, float screenY, KmlLayer layer) {
            return layer.getKmlNodes(screenX, screenY, Constants
                    .VIEWER_ENTITY_CLICKING_TOLERANCE_DP, false);
        }

        private KmlEntityInfo createKmlEntityInfo(KmlNode kmlNode, String layerId) {
            String entityName = kmlNode.getName();
            String description;
            description = extractDescription(kmlNode);
            Point center = kmlNode.getCenter();
            return new KmlEntityInfo(entityName, description, layerId, getGeometry(center));
        }

        private String extractDescription(KmlNode kmlNode) {
            try {
                return kmlNode.getBalloonStyle().getFormattedText().replaceAll("<.*?>", "").trim();
            } catch (NullPointerException e) {
                sLogger.w(String.format(
                        "Couldn't extract description. kml entity: '%s'", kmlNode.getName()));
                return null;
            }
        }

        private com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry getGeometry(
                Point center) {
            return new PointGeometry(center.getX(), center.getY());
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
            Point wgs84Point = projectToWgs84(mapPoint);
            return new PointGeometry(wgs84Point.getY(), wgs84Point.getX(), wgs84Point.getZ());
        }

        private void notifyOnLocationChosen(PointGeometry pointGeometry) {
            if (mOnMapGestureListener != null) {
                mOnMapGestureListener.onLocationChosen(pointGeometry);
            }
        }
    }
}