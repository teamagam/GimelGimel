package com.teamagam.gimelgimel.app.map.esri;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.esri.arcgisruntime.data.TileCache;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.Geometry;
import com.esri.arcgisruntime.geometry.GeometryEngine;
import com.esri.arcgisruntime.geometry.Point;
import com.esri.arcgisruntime.layers.ArcGISMapImageLayer;
import com.esri.arcgisruntime.layers.ArcGISTiledLayer;
import com.esri.arcgisruntime.mapping.ArcGISScene;
import com.esri.arcgisruntime.mapping.ArcGISTiledElevationSource;
import com.esri.arcgisruntime.mapping.Basemap;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay;
import com.esri.arcgisruntime.mapping.view.LayerSceneProperties;
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
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;
import com.teamagam.gimelgimel.domain.rasters.entity.IntermediateRaster;
import io.reactivex.Observable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import java.io.File;
import javax.inject.Inject;

public class EsriGGMapView extends FrameLayout implements GGMapView {

  public static final int PLUGINS_PADDING = 25;
  static final int INTERMEDIATE_LAYER_POSITION = 1;
  private static final AppLogger sLogger = AppLoggerFactory.create();
  private static final String LAST_VIEWPOINT_PREF_KEY = "esri_state";

  @BindView(R.id.map_view)
  SceneView mSceneView;

  @Inject
  ExternalDirProvider mExternalDirProvider;

  @Inject
  EsriSymbolCreator mEsriSymbolCreator;

  private IntermediateRasterDisplayer mIntermediateRasterDisplayer;
  private GraphicsLayerGGAdapter mGraphicsLayerGGAdapter;
  private Basemap mBasemap;
  //private GraphicsLayer mGraphicsLayer;
  //private Map<String, KmlLayer> mVectorLayerIdToKmlLayerMap;

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
  private ArcGISTiledElevationSource mElevationSource;
  private GraphicsOverlay mGraphicsOverlay;

  public EsriGGMapView(Context context) {
    super(context);
    init(context);
  }

  public EsriGGMapView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
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
      //mSceneView.removeLayer(mVectorLayerIdToKmlLayerMap.get(vectorLayerId));
      //mVectorLayerIdToKmlLayerMap.remove(vectorLayerId);
    });
  }

  @Override
  public void setOnReadyListener(OnReadyListener onReadyListener) {
    mOnReadyListener = onReadyListener;
  }

  @Override
  public void setOnMapGestureListener(OnMapGestureListener onMapGestureListener) {
    mOnMapGestureListener = onMapGestureListener;
  }

  @Override
  public void saveState() {
    SharedPreferences prefs = getDefaultSharedPreferences();
    String viewpoint = mSceneView.getCurrentViewpoint(Viewpoint.Type.BOUNDING_GEOMETRY).toJson();
    prefs.edit().putString(LAST_VIEWPOINT_PREF_KEY, viewpoint).apply();
  }

  @Override
  public void restoreState() {
    if (hasLastExtent()) {
      restoreLastExtent();
    }
  }

  @Override
  public void centerOverCurrentLocationWithAzimuth() {
    // TODO implement
    // Esri 100.1.0 doesn't support LocationDisplay in SceneView (only in MapView)
  }

  @Override
  public void setIntermediateRaster(IntermediateRaster intermediateRaster) {

  }

  @Override
  public void removeIntermediateRaster() {

  }

  @Override
  public PointGeometry getMapCenter() {
    Point point = projectToWgs84(mSceneView.getCurrentViewpoint(Viewpoint.Type.CENTER_AND_SCALE)
        .getTargetGeometry()
        .getExtent()
        .getCenter());
    return new PointGeometry(point.getY(), point.getX());
  }

  @Override
  public Observable<MapDragEvent> getMapDragEventObservable() {
    return Observable.empty();
  }

  @Override
  public void setAllowPanning(boolean allow) {

  }

  private void init(Context context) {
    if (isInEditMode()) {
      return;
    }
    LayoutInflater inflater = LayoutInflater.from(context);
    View inflate = inflater.inflate(R.layout.esri_gg_map_view, this);
    ButterKnife.bind(inflate, this);
    ((GGApplication) context.getApplicationContext()).getApplicationComponent().inject(this);
    mBasemap = getTiledBasemap();
    mElevationSource = new ArcGISTiledElevationSource(Constants.ARC_GIS_TILED_ELEVATION_SOURCE_URL);
    ArcGISScene scene = new ArcGISScene(mBasemap);
    scene.getBaseSurface().getElevationSources().add(mElevationSource);
    scene.addDoneLoadingListener(this::onBasemapDoneLoading);
    mSceneView.setScene(scene);
    mGraphicsOverlay = new GraphicsOverlay();
    mGraphicsOverlay.getSceneProperties()
        .setSurfacePlacement(LayerSceneProperties.SurfacePlacement.DRAPED);
    mSceneView.getGraphicsOverlays().add(mGraphicsOverlay);
    mGraphicsLayerGGAdapter = new GraphicsLayerGGAdapter(mGraphicsOverlay, EsriUtils.WGS_84_GEO,
        mSceneView.getSpatialReference(), mEsriSymbolCreator);
    //mMapView.setAllowRotationByPinch(true);
    //mVectorLayerIdToKmlLayerMap = new TreeMap<>();
    //mLocationDisplayer = getLocationDisplayer(context);
    //mIntermediateRasterDisplayer =
    //    new IntermediateRasterDisplayer(mMapView, INTERMEDIATE_LAYER_POSITION);
    //mMapDragEventSubject = PublishSubject.create();
    //mPannableMapTouchListener = getPannableMapTouchListener();
    //mUnpannableMapTouchListener = getUnpannableMapTouchListener();
    mComputationThreadSubject = PublishSubject.create();
    mComputationThreadSubject.observeOn(Schedulers.computation())
        .doOnNext(Action::run)
        .subscribe(new ErrorLoggingObserver<>());
  }

  private void notifyMapReady() {
    if (mOnReadyListener != null) {
      mOnReadyListener.onReady();
    }
  }

  private Point projectToWgs84(Point point) {
    return (Point) GeometryEngine.project(point, EsriUtils.WGS_84_GEO);
  }

  private Geometry transformToEsri(com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry geometry) {
    return EsriUtils.transformAndProject(geometry, EsriUtils.WGS_84_GEO,
        mSceneView.getSpatialReference());
  }

  private void lookAtPoint(Point esriPoint) {
    mSceneView.setViewpointAsync(new Viewpoint(esriPoint, Constants.LOOK_AT_POINT_SCALE),
        Constants.LOOK_AT_ANIMATION_DURATION_SEC);
  }

  private void lookAtEnvelope(Geometry esriGeometry) {
    Envelope envelope = esriGeometry.getExtent();
    mSceneView.setViewpointAsync(new Viewpoint(envelope), Constants.LOOK_AT_ANIMATION_DURATION_SEC);
    mSceneView.getCurrentViewpointCamera().elevate(Constants.VIEWER_LOOK_AT_ENVELOPE_ELEVATION);
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
    //return mVectorLayerIdToKmlLayerMap.containsKey(vectorLayerId);
    return false;
  }

  private void addLayer(VectorLayerPresentation vlp) {
    //mVectorLayerIdToKmlLayerMap.put(vlp.getId(), new KmlLayer(vlp.getLocalURI().getPath()));
    //mSceneView.addLayer(mVectorLayerIdToKmlLayerMap.get(vlp.getId()));
  }
  //
  //
  //
  //
  //
  //
  //
  //
  //

  public void pause() {
    mSceneView.pause();
    stopPlugin(mCompass);
    stopPlugin(mScaleBar);
  }

  public void unpause() {
    mSceneView.resume();
    startPlugin(mCompass);
    startPlugin(mScaleBar);
  }

  //
  //
  //
  //
  //
  //
  //
  //
  //

  private Basemap getTiledBasemap() {
    String localTpkFile = getLocalTpkFilepath();
    if (isFileExists(localTpkFile)) {
      sLogger.d("Creating base map from local tpk");
      return new Basemap(new ArcGISTiledLayer(new TileCache(localTpkFile)));
    } else {
      sLogger.d("Creating base map from remote service");
      return new Basemap(new ArcGISMapImageLayer(Constants.ARC_GIS_TILED_MAP_SERVICE_URL));
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

  private void onBasemapDoneLoading() {
    //setInitialExtent();
    //addDynamicGraphicLayer();
    notifyMapReady();
    setPlugins();
    //mSceneView.setOnStatusChangedListener(null);
    //configureBasemap();
    //setupSingleTapNotification();
    //setupLongPressNotification();
    //setupLocationDisplayer();
  }
  //
  //private void setInitialExtent() {
  //  if (hasLastExtent()) {
  //    restoreLastExtent();
  //  } else {
  //    setExtentOverIsrael();
  //  }
  //}

  private boolean hasLastExtent() {
    return getDefaultSharedPreferences().contains(LAST_VIEWPOINT_PREF_KEY);
  }

  private void restoreLastExtent() {
    SharedPreferences prefs = getDefaultSharedPreferences();
    String viewpoint = prefs.getString(LAST_VIEWPOINT_PREF_KEY, "");
    mSceneView.setViewpoint(Viewpoint.fromJson(viewpoint));
  }

  private SharedPreferences getDefaultSharedPreferences() {
    return PreferenceManager.getDefaultSharedPreferences(getContext());
  }

  //private void setExtentOverIsrael() {
  //  Envelope israelEnvelope =
  //      new Envelope(Constants.ISRAEL_WEST_LONG_ENVELOPE, Constants.ISRAEL_SOUTH_LAT_ENVELOPE,
  //          Constants.ISRAEL_EAST_LONG_ENVELOPE, Constants.ISRAEL_NORTH_LAT_ENVELOPE);
  //  Envelope projectedIsraelEnvelope = (Envelope) projectFromWGS84(israelEnvelope);
  //  mSceneView.setExtent(projectedIsraelEnvelope);
  //}
  //
  //private void addDynamicGraphicLayer() {
  //  mGraphicsLayer = new GraphicsLayer();
  //  mSceneView.addLayer(mGraphicsLayer);
  //  mGraphicsLayerGGAdapter = new GraphicsLayerGGAdapter(mGraphicsLayer, EsriUtils.WGS_84_GEO,
  //      mSceneView.getSpatialReference(), mEsriSymbolCreator);
  //}
  //
  //private void notifyMapReady() {
  //  if (mOnReadyListener != null) {
  //    mOnReadyListener.onReady();
  //  }
  //}

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
    //mLocationDisplayer.displaySelfLocation();
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

  //private void configureBasemap() {
  //  optimizePanning();
  //  mMapView.setMaxScale(getMapMaxScale());
  //}
  //
  //private void optimizePanning() {
  //  mBasemap.setBufferEnabled(true);
  //  mBasemap.setBufferExpansionFactor(2f);
  //}
  //
  //private double getMapMaxScale() {
  //  return Math.max(mBasemap.getMaxScale(), Constants.VIEWER_MIN_SCALE_RATIO);
  //}
  //
  //private void setupSingleTapNotification() {
  //  mMapView.setOnSingleTapListener(new EsriGGMapView2.SingleTapListener());
  //}
  //
  //private void setupLongPressNotification() {
  //  mMapView.setOnLongPressListener(new EsriGGMapView2.LongPressGestureNotifier());
  //}
  //
  //private void notifyEntityClicked(String entityId) {
  //  if (mMapEntityClickedListener != null) {
  //    mMapEntityClickedListener.entityClicked(entityId);
  //  }
  //}
  //
  //private void notifyKmlEntityClicked(KmlEntityInfo kmlEntityInfo) {
  //  if (mMapEntityClickedListener != null) {
  //    mMapEntityClickedListener.kmlEntityClicked(kmlEntityInfo);
  //  }
  //}
  //
  //private LocationDisplayer getLocationDisplayer(Context context) {
  //  LocationListener locationListener =
  //      ((GGApplication) context.getApplicationContext()).getApplicationComponent()
  //          .locationListener();
  //
  //  return new LocationDisplayer(mSceneView.getLocationDisplay(), locationListener);
  //}
  //
  //private SceneView.OnTouchListener getPannableMapTouchListener() {
  //  return new MapDragEventsEmitterTouchListenerDecorator(
  //      new MapOnTouchListener(getContext(), mMapView), this, mMapDragEventSubject,
  //      this::screenToGround);
  //}
  //
  //private SceneView.OnTouchListener getUnpannableMapTouchListener() {
  //  return new MapDragEventsEmitterTouchListenerDecorator(new IgnoreDragMapOnTouchListener(this),
  //      this, mMapDragEventSubject, this::screenToGround);
  //}

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

  //private void setupLocationDisplayer() {
  //  mLocationDisplayer.displaySelfLocation();
  //  mLocationDisplayer.start();
  //}
}
