package com.teamagam.gimelgimel.app.injectors.components;

import android.content.Context;
import android.location.LocationListener;
import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.app.common.utils.GlideLoader;
import com.teamagam.gimelgimel.app.injectors.modules.ApiModule;
import com.teamagam.gimelgimel.app.injectors.modules.ApplicationModule;
import com.teamagam.gimelgimel.app.injectors.modules.DatabaseModule;
import com.teamagam.gimelgimel.app.injectors.modules.MessageModule;
import com.teamagam.gimelgimel.app.injectors.modules.RepositoryModule;
import com.teamagam.gimelgimel.app.injectors.modules.UtilsModule;
import com.teamagam.gimelgimel.app.map.actions.freedraw.FreeDrawActionFragment;
import com.teamagam.gimelgimel.app.map.actions.measure.MeasureActionFragment;
import com.teamagam.gimelgimel.app.map.actions.send.geometry.SendGeometryActionFragment;
import com.teamagam.gimelgimel.app.map.actions.send.quadrilateral.SendQuadrilateralActionFragment;
import com.teamagam.gimelgimel.app.map.view.DynamicLayerActionFragment;
import com.teamagam.gimelgimel.app.map.esri.EsriGGMapView;
import com.teamagam.gimelgimel.app.message.view.ImageFullscreenActivity;
import com.teamagam.gimelgimel.app.notifications.AppNotifier;
import com.teamagam.gimelgimel.data.common.FilesDownloader;
import com.teamagam.gimelgimel.data.dynamicLayers.room.dao.DynamicLayerDao;
import com.teamagam.gimelgimel.data.location.LocationFetcher;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.IconsDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.MessagesDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.UserLocationDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.VectorLayerDao;
import com.teamagam.gimelgimel.domain.alerts.repository.AlertsRepository;
import com.teamagam.gimelgimel.domain.alerts.repository.InformedAlertsRepository;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.rx.RetryWithDelay;
import com.teamagam.gimelgimel.domain.dynamicLayers.remote.DynamicLayerRemoteSourceHandler;
import com.teamagam.gimelgimel.domain.dynamicLayers.repository.DynamicLayersRepository;
import com.teamagam.gimelgimel.domain.icons.FetchIconsOnStartupInteractorFactory;
import com.teamagam.gimelgimel.domain.icons.repository.IconsRepository;
import com.teamagam.gimelgimel.domain.layers.LayersLocalCache;
import com.teamagam.gimelgimel.domain.layers.ProcessVectorLayersInteractor;
import com.teamagam.gimelgimel.domain.layers.VectorLayerExtentResolver;
import com.teamagam.gimelgimel.domain.layers.repository.VectorLayersRepository;
import com.teamagam.gimelgimel.domain.layers.repository.VectorLayersVisibilityRepository;
import com.teamagam.gimelgimel.domain.location.LocationEventFetcher;
import com.teamagam.gimelgimel.domain.location.SendSelfLocationsInteractor;
import com.teamagam.gimelgimel.domain.location.UserLocationsMapDisplaySynchronizerInteractor;
import com.teamagam.gimelgimel.domain.location.respository.LocationRepository;
import com.teamagam.gimelgimel.domain.location.respository.UsersLocationRepository;
import com.teamagam.gimelgimel.domain.map.SpatialEngine;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.KmlEntityInfo;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.map.repository.GeoEntitiesRepository;
import com.teamagam.gimelgimel.domain.map.repository.SelectedEntityRepository;
import com.teamagam.gimelgimel.domain.map.repository.SingleDisplayedItemRepository;
import com.teamagam.gimelgimel.domain.messages.ProcessMessagesInteractor;
import com.teamagam.gimelgimel.domain.messages.UpdateUnreadCountInteractor;
import com.teamagam.gimelgimel.domain.messages.poller.StartFetchingMessagesInteractor;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.messages.repository.NewMessageIndicationRepository;
import com.teamagam.gimelgimel.domain.messages.repository.ObjectMessageMapper;
import com.teamagam.gimelgimel.domain.messages.repository.UnreadMessagesCountRepository;
import com.teamagam.gimelgimel.domain.notifications.cellular_network.Update3GConnectivityStatusInteractor;
import com.teamagam.gimelgimel.domain.notifications.repository.ConnectivityStatusRepository;
import com.teamagam.gimelgimel.domain.notifications.repository.MessageNotifications;
import com.teamagam.gimelgimel.domain.rasters.IntermediateRasterExtentResolver;
import com.teamagam.gimelgimel.domain.rasters.LoadIntermediateRastersInteractor;
import com.teamagam.gimelgimel.domain.rasters.repository.IntermediateRasterVisibilityRepository;
import com.teamagam.gimelgimel.domain.rasters.repository.IntermediateRastersRepository;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;
import com.teamagam.gimelgimel.domain.utils.ApplicationStatus;
import com.teamagam.gimelgimel.domain.utils.PreferencesUtils;
import dagger.Component;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
@Component(modules = {
    ApplicationModule.class, RepositoryModule.class, ApiModule.class, UtilsModule.class,
    MessageModule.class, DatabaseModule.class
})
public interface ApplicationComponent {
  void inject(GGApplication ggApplication);

  void inject(ImageFullscreenActivity fullscreenActivity);

  void inject(EsriGGMapView esriGGMapView);

  void inject(SendQuadrilateralActionFragment sendQuadrilateralActionFragment);

  void inject(MeasureActionFragment measureActionFragment);

  void inject(SendGeometryActionFragment sendGeometryActionFragment);

  void inject(FreeDrawActionFragment freeDrawActionFragment);

  void inject(DynamicLayerActionFragment dynamicLayerActionFragment);

  //Exposed to sub-graphs.
  Context context();

  ThreadExecutor threadExecutor();

  PostExecutionThread postExecutionThread();

  MessagesRepository messagesRepository();

  @Named("Entity")
  ObjectMessageMapper entityMessageMapper();

  @Named("Alert")
  ObjectMessageMapper alertMessageMapper();

  UnreadMessagesCountRepository unreadCountMessagesRepository();

  LocationFetcher locationFetcher();

  LocationListener locationListener();

  LocationEventFetcher locationEventFetcher();

  LocationRepository locationRepository();

  UserPreferencesRepository userPreferencesRepository();

  GeoEntitiesRepository geoEntitiesRepository();

  DisplayedEntitiesRepository displayedEntitiesRepository();

  SingleDisplayedItemRepository<KmlEntityInfo> currentKmlEntityRepository();

  VectorLayersRepository vectorLayersRepository();

  DynamicLayersRepository dynamicLayersRepository();

  VectorLayersVisibilityRepository vectorLayersVisibilityRepository();

  StartFetchingMessagesInteractor startFetchingMessagesInteractor();

  ProcessMessagesInteractor processMessagesInteractor();

  UpdateUnreadCountInteractor updateUnreadCountInteractor();

  UserLocationsMapDisplaySynchronizerInteractor displayUserLocationsInteractor();

  @Named("gps")
  ConnectivityStatusRepository gpsConnectivityStatusRepository();

  @Named("data")
  ConnectivityStatusRepository dataConnectivityStatusRepository();

  @Named("3g")
  ConnectivityStatusRepository threeGConnectivityStatusRepository();

  FetchIconsOnStartupInteractorFactory fetchIconsOnStartupInteractorFactory();

  UsersLocationRepository usersLocationRepository();

  SendSelfLocationsInteractor sendMyLocationInteractor();

  Update3GConnectivityStatusInteractor update3GConnectivityStatusInteractor();

  ProcessVectorLayersInteractor processVectorLayersInteractor();

  LoadIntermediateRastersInteractor loadIntermediateRastersInteractor();

  LayersLocalCache layersLocalCache();

  GlideLoader glideFactory();

  PreferencesUtils preferencesUtils();

  AlertsRepository alertsRepository();

  InformedAlertsRepository informedAlertsRepository();

  SelectedEntityRepository selectedEntityRepository();

  IntermediateRastersRepository intermediateRastersRepository();

  IntermediateRasterVisibilityRepository intermediateRasterVisibilityRepository();

  MessageNotifications messageNotifications();

  NewMessageIndicationRepository newMessageIndicationRepository();

  SpatialEngine spatialEngine();

  FilesDownloader filesDownloader();

  ApplicationStatus applicationStatus();

  AppNotifier appNotifier();

  VectorLayerExtentResolver vectorLayerExtentResolver();

  IntermediateRasterExtentResolver intermediateRasterExtentResolver();

  VectorLayerDao vectorLayerDao();

  DynamicLayerDao dynamicLayerDao();

  MessagesDao messagesDao();

  UserLocationDao userLocationDao();

  IconsDao iconsDao();

  IconsRepository iconsRepository();

  DynamicLayerRemoteSourceHandler dynamicLayerRemoteSourceHandler();

  RetryWithDelay retryWithDelay();
}