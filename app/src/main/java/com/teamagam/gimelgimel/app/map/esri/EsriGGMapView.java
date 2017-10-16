package com.teamagam.gimelgimel.app.map.esri;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.LocationListener;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.layers.Layer;
import com.esri.arcgisruntime.mapping.view.SceneView;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.app.common.utils.Constants;
import com.teamagam.gimelgimel.app.map.GGMapView;
import com.teamagam.gimelgimel.app.map.MapDragEvent;
import com.teamagam.gimelgimel.app.map.MapEntityClickedListener;
import com.teamagam.gimelgimel.app.map.OnMapGestureListener;
import com.teamagam.gimelgimel.app.map.esri.graphic.EsriSymbolCreator;
import com.teamagam.gimelgimel.app.map.esri.graphic.GraphicsLayerGGAdapter;
import com.teamagam.gimelgimel.app.map.esri.plugins.Compass;
import com.teamagam.gimelgimel.app.map.esri.plugins.ScaleBar;
import com.teamagam.gimelgimel.app.map.esri.plugins.SelfUpdatingViewPlugin;
import com.teamagam.gimelgimel.data.common.ExternalDirProvider;
import com.teamagam.gimelgimel.domain.base.subscribers.ErrorLoggingObserver;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerPresentation;
import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.KmlEntityInfo;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;
import com.teamagam.gimelgimel.domain.rasters.entity.IntermediateRaster;
import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import java.io.File;
import java.util.ArrayList;
import java.util.TreeMap;
import javax.inject.Inject;

public class EsriGGMapView extends FrameLayout implements GGMapView {

  public static final int PLUGINS_PADDING = 25;
  static final int INTERMEDIATE_LAYER_POSITION = 1;
  private static final AppLogger sLogger = AppLoggerFactory.create();
  private static final String ESRI_STATE_PREF_KEY = "esri_state";

  @BindView(R.id.map_view)
  SceneView mSceneView;

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

  private Subject<MapDragEvent> mMapDragEventSubject;
  private SceneView.OnTouchListener mPannableMapTouchListener;
  private SceneView.OnTouchListener mUnpannableMapTouchListener;
  private Subject<Action> mComputationThreadSubject;

  public EsriGGMapView(Context context) {
    super(context);
    init(context);
  }

  public EsriGGMapView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public void pause() {
    mSceneView.pause();
    stopPlugin(mCompass);
    stopPlugin(mScaleBar);
  }

  public void unpause() {
    mSceneView.unpause();
    startPlugin(mCompass);
    startPlugin(mScaleBar);
  }

  public SceneView getSceneView() {
    return mSceneView;
  }

  @Override
  public void setOnMapGestureListener(OnMapGestureListener onMapGestureListener) {
    mOnMapGestureListener = onMapGestureListener;
  }

  @Override
  public void saveState() {
    SharedPreferences prefs = getDefaultSharedPreferences();
    String retainState = mSceneView.retainState();
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
    mSceneView.setScale(Constants.LOCATE_ME_BUTTON_VIEWER_SCALE);
    mLocationDisplayer.centerAndShowAzimuth();
  }

  @Override
  public PointGeometry getMapCenter() {
    Point point = projectToWgs84(mSceneView.getCenter());
    return new PointGeometry(point.getY(), point.getX());
  }

  @Override
  public Observable<MapDragEvent> getMapDragEventObservable() {
    return mMapDragEventSubject;
  }

  @Override
  public void setAllowPanning(boolean allow) {
    if (allow) {
      enablePanning();
    } else {
      disablePanning();
    }
  }

  @Override
  public void lookAt(com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry geometry) {
    Geometry esriGeometry = transformToEsri(geometry);
    if (esriGeometry instanceof Point) {
      lookAtPoint((Point) esriGeometry);
    } else {
      lookAtEnvelope(esriGeometry);
    }
  }

  @Override
  public void setIntermediateRaster(IntermediateRaster intermediateRaster) {
    runOnComputationThread(() -> mIntermediateRasterDisplayer.display(intermediateRaster));
  }

  @Override
  public void removeIntermediateRaster() {
    runOnComputationThread(() -> mIntermediateRasterDisplayer.clear());
  }

  @Override
  public void updateMapEntity(GeoEntityNotification geoEntityNotification) {
    GeoEntity entity = geoEntityNotification.getGeoEntity();
    int action = geoEntityNotification.getAction();
    runOnComputationThread(() -> updateMap(entity, action));
  }

  @Override
  public void setOnEntityClickedListener(MapEntityClickedListener mapEntityClickedListener) {
    mMapEntityClickedListener = mapEntityClickedListener;
  }

  @Override
  public void showVectorLayer(VectorLayerPresentation vlp) {
    runOnComputationThread(() -> {
      hideIfDisplayed(vlp.getId());
      addLayer(vlp);
    });
  }

  @Override
  public void hideVectorLayer(String vectorLayerId) {
    runOnComputationThread(() -> {
      mSceneView.removeLayer(mVectorLayerIdToKmlLayerMap.get(vectorLayerId));
      mVectorLayerIdToKmlLayerMap.remove(vectorLayerId);
    });
  }

  @Override
  public void setOnReadyListener(OnReadyListener onReadyListener) {
    mOnReadyListener = onReadyListener;
  }

  private void init(Context context) {
    if (isInEditMode()) {
      return;
    }
    LayoutInflater inflater = LayoutInflater.from(context);
    View inflate = inflater.inflate(R.layout.esri_gg_map_view, this);
    ButterKnife.bind(inflate, this);
    ((GGApplication) context.getApplicationContext()).getApplicationComponent().inject(this);
    setBasemap();
    mSceneView.setAllowRotationByPinch(true);
    mVectorLayerIdToKmlLayerMap = new TreeMap<>();
    mLocationDisplayer = getLocationDisplayer(context);
    mIntermediateRasterDisplayer =
        new IntermediateRasterDisplayer(mSceneView, INTERMEDIATE_LAYER_POSITION);
    mMapDragEventSubject = PublishSubject.create();
    mPannableMapTouchListener = getPannableMapTouchListener();
    mUnpannableMapTouchListener = getUnpannableMapTouchListener();
    mComputationThreadSubject = PublishSubject.create();
    mComputationThreadSubject.observeOn(Schedulers.computation())
        .doOnNext(Action::run)
        .subscribe(new ErrorLoggingObserver<>());
  }

  private void setBasemap() {
    loadBasemap();
    setupNotificationOnBasemapLoaded();
  }

  private void loadBasemap() {
    mBaseLayer = getTiledLayer();
    mSceneView.addLayer(mBaseLayer);
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
    mSceneView.setOnStatusChangedListener(new OnStatusChangedListener() {
      @Override
      public void onStatusChanged(Object o, STATUS status) {
        if (isBaseLayerLoaded(o, status)) {
          onBasemapLoaded();
        }
      }

      private boolean isBaseLayerLoaded(Object o, STATUS status) {
        return status == STATUS.LAYER_LOADED && o instanceof Layer && o == mBaseLayer;
      }
    });
  }

  private void onBasemapLoaded() {
    setInitialExtent();
    addDynamicGraphicLayer();
    notifyMapReady();
    setPlugins();
    mSceneView.setOnStatusChangedListener(null);
    configureBasemap();
    setupSingleTapNotification();
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
    mSceneView.restoreState(esriState);
  }

  private SharedPreferences getDefaultSharedPreferences() {
    return PreferenceManager.getDefaultSharedPreferences(getContext());
  }

  private void setExtentOverIsrael() {
    Envelope israelEnvelope =
        new Envelope(Constants.ISRAEL_WEST_LONG_ENVELOPE, Constants.ISRAEL_SOUTH_LAT_ENVELOPE,
            Constants.ISRAEL_EAST_LONG_ENVELOPE, Constants.ISRAEL_NORTH_LAT_ENVELOPE);
    Envelope projectedIsraelEnvelope = (Envelope) projectFromWGS84(israelEnvelope);
    mSceneView.setExtent(projectedIsraelEnvelope);
  }

  private void addDynamicGraphicLayer() {
    mGraphicsLayer = new GraphicsLayer();
    mSceneView.addLayer(mGraphicsLayer);
    mGraphicsLayerGGAdapter = new GraphicsLayerGGAdapter(mGraphicsLayer, EsriUtils.WGS_84_GEO,
        mSceneView.getSpatialReference(), mEsriSymbolCreator);
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
    layout.setPadding(PLUGINS_PADDING, PLUGINS_PADDING, PLUGINS_PADDING, PLUGINS_PADDING);
    return layout;
  }

  private SceneView.LayoutParams getPluginsContainerLayoutParams() {
    return new SceneView.LayoutParams(SceneView.LayoutParams.MATCH_PARENT,
        SceneView.LayoutParams.MATCH_PARENT);
  }

  private void setCompass() {
    mCompass = new Compass(getContext(), null, mSceneView);
    mCompass.setOnClickListener(v -> rotateToNorth());

    mPluginsContainerLayout.addView(mCompass,
        createCompassLayoutParams(RelativeLayout.ALIGN_PARENT_TOP));

    mCompass.start();
  }

  private void rotateToNorth() {
    mLocationDisplayer.displaySelfLocation();
    mSceneView.getCurrentViewpointCamera().rotateTo(0, 0, 0);
  }

  private RelativeLayout.LayoutParams createCompassLayoutParams(int align) {
    RelativeLayout.LayoutParams params =
        new RelativeLayout.LayoutParams(mCompass.getBitmapWidth(), mCompass.getBitmapHeight());
    params.addRule(RelativeLayout.ALIGN_PARENT_END);
    params.addRule(align);
    return params;
  }

  private void setScaleBar() {
    mScaleBar = new ScaleBar(getContext(), null, mSceneView);
    mPluginsContainerLayout.addView(mScaleBar,
        createScaleBarLayoutParams(RelativeLayout.ALIGN_PARENT_BOTTOM));
    mScaleBar.start();
  }

  private RelativeLayout.LayoutParams createScaleBarLayoutParams(int align) {
    RelativeLayout.LayoutParams params =
        new RelativeLayout.LayoutParams(SceneView.LayoutParams.WRAP_CONTENT,
            SceneView.LayoutParams.WRAP_CONTENT);
    params.addRule(RelativeLayout.ALIGN_PARENT_START);
    params.addRule(align);
    return params;
  }

  private void configureBasemap() {
    optimizePanning();
    mSceneView.setMaxScale(getMapMaxScale());
  }

  private void optimizePanning() {
    mBaseLayer.setBufferEnabled(true);
    mBaseLayer.setBufferExpansionFactor(2f);
  }

  private double getMapMaxScale() {
    return Math.max(mBaseLayer.getMaxScale(), Constants.VIEWER_MIN_SCALE_RATIO);
  }

  private void setupSingleTapNotification() {
    mSceneView.setOnSingleTapListener(new SingleTapListener());
  }

  private void setupLongPressNotification() {
    mSceneView.setOnLongPressListener(new LongPressGestureNotifier());
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
    LocationListener locationListener =
        ((GGApplication) context.getApplicationContext()).getApplicationComponent()
            .locationListener();

    return new LocationDisplayer(mSceneView.getLocationDisplayManager(), locationListener);
  }

  private SceneView.OnTouchListener getPannableMapTouchListener() {
    return new MapDragEventsEmitterTouchListenerDecorator(
        new MapOnTouchListener(getContext(), mSceneView), this, mMapDragEventSubject,
        this::screenToGround);
  }

  private SceneView.OnTouchListener getUnpannableMapTouchListener() {
    return new MapDragEventsEmitterTouchListenerDecorator(new IgnoreDragMapOnTouchListener(this),
        this, mMapDragEventSubject, this::screenToGround);
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

  private void enablePanning() {
    mSceneView.setOnTouchListener(mPannableMapTouchListener);
  }

  private void disablePanning() {
    mSceneView.setOnTouchListener(mUnpannableMapTouchListener);
  }

  private Geometry transformToEsri(com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry geometry) {
    return EsriUtils.transformAndProject(geometry, EsriUtils.WGS_84_GEO,
        mSceneView.getSpatialReference());
  }

  private Geometry projectFromWGS84(Geometry p) {
    return GeometryEngine.project(p, EsriUtils.WGS_84_GEO, mSceneView.getSpatialReference());
  }

  private Point projectToWgs84(Point point) {
    return (Point) GeometryEngine.project(point, mSceneView.getSpatialReference(),
        EsriUtils.WGS_84_GEO);
  }

  private void lookAtPoint(Point esriGeometry) {
    mSceneView.centerAt(esriGeometry, true);
    mSceneView.setScale(Constants.VIEWER_LOOK_AT_POINT_SCALE, true);
  }

  private void lookAtEnvelope(Geometry esriGeometry) {
    Envelope envelope = new Envelope();
    esriGeometry.queryEnvelope(envelope);
    mSceneView.setExtent(envelope, Constants.VIEWER_LOOK_AT_ENVELOPE_PADDING_DP, true);
  }

  private void runOnComputationThread(Action action) {
    mComputationThreadSubject.onNext(action);
  }

  private void updateMap(GeoEntity entity, int action) {
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
        sLogger.w("Update map entity could not be done because of unknown action code: " + action);
    }
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
    mSceneView.addLayer(mVectorLayerIdToKmlLayerMap.get(vlp.getId()));
  }

  private PointGeometry screenToGround(float screenX, float screenY) {
    try {
      Point mapPoint = mSceneView.toMapPoint(screenX, screenY);
      Point wgs84Point = projectToWgs84(mapPoint);
      return new PointGeometry(wgs84Point.getY(), wgs84Point.getX(), wgs84Point.getZ());
    } catch (Exception e) {
      sLogger.w("Couldn't transform screen to ground: [X,Y] " + screenX + " , " + screenY, e);
      return null;
    }
  }

  private class SingleTapListener implements OnSingleTapListener {

    @Override
    public void onSingleTap(float screenX, float screenY) {
      handleEntityNotification(screenX, screenY);
      notifySingleTap(screenX, screenY);
    }

    private void handleEntityNotification(float screenX, float screenY) {
      handleGraphicEntityClickNotification(screenX, screenY);
      handleKmlEntityClickNotification(screenX, screenY);
    }

    private void handleGraphicEntityClickNotification(float screenX, float screenY) {
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

    private void handleKmlEntityClickNotification(float screenX, float screenY) {
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
      return layer.getKmlNodes(screenX, screenY, Constants.VIEWER_ENTITY_CLICKING_TOLERANCE_DP,
          false);
    }

    private KmlEntityInfo createKmlEntityInfo(KmlNode kmlNode, String layerId) {
      String entityName = getEntityName(kmlNode);
      String description = getEntityDescription(kmlNode);

      Point center = kmlNode.getCenter();
      return new KmlEntityInfo(entityName, description, layerId, getGeometry(center));
    }

    private String getEntityName(KmlNode kmlNode) {
      try {
        String kmlEntityBalloon = kmlNode.getBalloonStyle().getFormattedText();
        KmlBalloonDataParser parser = new KmlBalloonDataParser(kmlEntityBalloon);

        return parser.parseName();
      } catch (Exception ex) {
        sLogger.e("Couldn't parse name from kml", ex);
        return "";
      }
    }

    private String getEntityDescription(KmlNode kmlNode) {
      try {
        String kmlEntityBalloon = kmlNode.getBalloonStyle().getFormattedText();
        KmlBalloonDataParser parser = new KmlBalloonDataParser(kmlEntityBalloon);

        return parser.parseDescription();
      } catch (Exception ex) {
        sLogger.e("Couldn't parse description from kml", ex);
        return "";
      }
    }

    private com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry getGeometry(Point center) {
      return new PointGeometry(center.getX(), center.getY());
    }

    private void notifySingleTap(float screenX, float screenY) {
      if (mOnMapGestureListener != null) {
        mOnMapGestureListener.onTap(screenToGround(screenX, screenY));
      }
    }
  }

  private class LongPressGestureNotifier implements OnLongPressListener {
    @Override
    public boolean onLongPress(float screenX, float screenY) {
      notifyOnLongPress(screenToGround(screenX, screenY));
      return false;
    }

    private void notifyOnLongPress(PointGeometry pointGeometry) {
      if (mOnMapGestureListener != null) {
        mOnMapGestureListener.onLongPress(pointGeometry);
      }
    }
  }
}