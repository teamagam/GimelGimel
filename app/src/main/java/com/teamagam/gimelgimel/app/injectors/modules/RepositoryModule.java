package com.teamagam.gimelgimel.app.injectors.modules;

import android.content.Context;
import android.preference.PreferenceManager;
import com.teamagam.gimelgimel.app.common.utils.Constants;
import com.teamagam.gimelgimel.data.alerts.repository.AlertsDataRepository;
import com.teamagam.gimelgimel.data.alerts.repository.InformedAlertsDataRepository;
import com.teamagam.gimelgimel.data.layers.VectorLayersDataRepository;
import com.teamagam.gimelgimel.data.layers.VectorLayersVisibilityDataRepository;
import com.teamagam.gimelgimel.data.location.repository.LocationDataRepository;
import com.teamagam.gimelgimel.data.location.repository.UsersLocationDataRepository;
import com.teamagam.gimelgimel.data.map.repository.DisplayedEntitiesDataRepository;
import com.teamagam.gimelgimel.data.map.repository.GeoEntitiesDataRepository;
import com.teamagam.gimelgimel.data.map.repository.SelectedEntityDataRepository;
import com.teamagam.gimelgimel.data.map.repository.SingleDisplayedItemDataRepository;
import com.teamagam.gimelgimel.data.message.repository.MessagesDataRepository;
import com.teamagam.gimelgimel.data.message.repository.NewMessageIndicationDataRepository;
import com.teamagam.gimelgimel.data.message.repository.ObjectMessageDataMapper;
import com.teamagam.gimelgimel.data.message.repository.UnreadMessagesCountDataRepository;
import com.teamagam.gimelgimel.data.message.repository.cache.MessagesDataCache;
import com.teamagam.gimelgimel.data.message.repository.cache.room.mappers.MessagesEntityMapper;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.MessagesDao;
import com.teamagam.gimelgimel.data.notifications.PersistentConnectivityStatusRepositoryImpl;
import com.teamagam.gimelgimel.data.rasters.repository.IntermediateRasterVisibilityDataRepository;
import com.teamagam.gimelgimel.data.rasters.repository.IntermediateRastersRepositoryData;
import com.teamagam.gimelgimel.data.sensors.repository.SelectedSensorDataRepository;
import com.teamagam.gimelgimel.data.sensors.repository.SensorsDataRepository;
import com.teamagam.gimelgimel.data.user.repository.UserPreferenceRepositoryImpl;
import com.teamagam.gimelgimel.domain.alerts.repository.AlertsRepository;
import com.teamagam.gimelgimel.domain.alerts.repository.InformedAlertsRepository;
import com.teamagam.gimelgimel.domain.layers.repository.VectorLayersRepository;
import com.teamagam.gimelgimel.domain.layers.repository.VectorLayersVisibilityRepository;
import com.teamagam.gimelgimel.domain.location.LocationEventFetcher;
import com.teamagam.gimelgimel.domain.location.respository.LocationRepository;
import com.teamagam.gimelgimel.domain.location.respository.UsersLocationRepository;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.KmlEntityInfo;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.map.repository.GeoEntitiesRepository;
import com.teamagam.gimelgimel.domain.map.repository.SelectedEntityRepository;
import com.teamagam.gimelgimel.domain.map.repository.SingleDisplayedItemRepository;
import com.teamagam.gimelgimel.domain.messages.cache.MessagesCache;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.messages.repository.NewMessageIndicationRepository;
import com.teamagam.gimelgimel.domain.messages.repository.ObjectMessageMapper;
import com.teamagam.gimelgimel.domain.messages.repository.UnreadMessagesCountRepository;
import com.teamagam.gimelgimel.domain.notifications.entity.ConnectivityStatus;
import com.teamagam.gimelgimel.domain.notifications.repository.ConnectivityStatusRepository;
import com.teamagam.gimelgimel.domain.rasters.repository.IntermediateRasterVisibilityRepository;
import com.teamagam.gimelgimel.domain.rasters.repository.IntermediateRastersRepository;
import com.teamagam.gimelgimel.domain.sensors.repository.SelectedSensorRepository;
import com.teamagam.gimelgimel.domain.sensors.repository.SensorsRepository;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
@Module
public class RepositoryModule {

  @Provides
  @Singleton
  UserPreferencesRepository provideUserPreferencesRepository(Context context) {
    return new UserPreferenceRepositoryImpl(PreferenceManager.getDefaultSharedPreferences(context));
  }

  @Provides
  @Singleton
  LocationRepository provideLocationRepository(LocationDataRepository locationRepository) {
    return locationRepository;
  }

  @Provides
  @Singleton
  LocationEventFetcher provideLocationEventFetcher(LocationDataRepository locationRepository) {
    //uses the same instance as LocationRepository
    return locationRepository;
  }

  @Provides
  @Singleton
  MessagesRepository provideMessagesRepository(MessagesDataRepository messageRepo) {
    return messageRepo;
  }

  @Provides
  @Named("Entity")
  @Singleton
  ObjectMessageMapper provideEntityMessageMapper(ObjectMessageDataMapper mapper) {
    return mapper;
  }

  @Provides
  @Named("Alert")
  @Singleton
  ObjectMessageMapper provideAlertMessageMapper(ObjectMessageDataMapper mapper) {
    return mapper;
  }

  @Provides
  @Singleton
  UnreadMessagesCountRepository provideUnreadMessagesCountRepository(
      UnreadMessagesCountDataRepository unreadCountRepo) {
    return unreadCountRepo;
  }

  @Provides
  @Singleton
  GeoEntitiesRepository provideGeoRepository(GeoEntitiesDataRepository geoRepo) {
    return geoRepo;
  }

  @Provides
  @Singleton
  DisplayedEntitiesRepository provideDisplayedRepository(DisplayedEntitiesDataRepository geoDisplayedData) {
    return geoDisplayedData;
  }

  @Provides
  @Singleton
  SingleDisplayedItemRepository<KmlEntityInfo> provideCurrentlyPresentedKmlEntityRepository(
      SingleDisplayedItemDataRepository<KmlEntityInfo> currentKmlInfoRepo) {
    return currentKmlInfoRepo;
  }

  @Provides
  @Singleton
  VectorLayersRepository provideVectorLayersRepository(VectorLayersDataRepository vectorLayersRepo) {
    return vectorLayersRepo;
  }

  @Provides
  @Singleton
  VectorLayersVisibilityRepository provideVectorLayersVisibilityRepository(
      VectorLayersVisibilityDataRepository visibilityRepo) {
    return visibilityRepo;
  }

  @Provides
  @Singleton
  @Named("gps")
  ConnectivityStatusRepository provideGpsConnectivityStatusRepository() {
    return new PersistentConnectivityStatusRepositoryImpl(ConnectivityStatus.CONNECTED,
        Constants.GPS_STATUS_CONSISTENT_TIMEFRAME_MS);
  }

  @Provides
  @Singleton
  @Named("data")
  ConnectivityStatusRepository provideDataConnectivityStatusRepository() {
    return new PersistentConnectivityStatusRepositoryImpl(ConnectivityStatus.CONNECTED,
        Constants.DATA_STATUS_CONSISTENT_TIMEFRAME_MS);
  }

  @Provides
  @Singleton
  @Named("3g")
  ConnectivityStatusRepository provide3GConnectivityStatusRepository() {
    return new PersistentConnectivityStatusRepositoryImpl(ConnectivityStatus.CONNECTED,
        Constants.DATA_STATUS_CONSISTENT_TIMEFRAME_MS);
  }

  @Provides
  @Singleton
  UsersLocationRepository provideUserLocationRepository(UsersLocationDataRepository usersLocationDataRepository) {
    return usersLocationDataRepository;
  }

  @Provides
  @Singleton
  SensorsRepository provideSensorsRepository(SensorsDataRepository sensorsDataRepository) {
    return sensorsDataRepository;
  }

  @Provides
  @Singleton
  SelectedSensorRepository provideSelectedSensorRepository(SelectedSensorDataRepository selectedSensorDataRepository) {
    return selectedSensorDataRepository;
  }

  @Provides
  @Singleton
  AlertsRepository provideAlertsRepository(AlertsDataRepository alertsDataRepository) {
    return alertsDataRepository;
  }

  @Provides
  @Singleton
  InformedAlertsRepository provideInformedAlertsRepository(InformedAlertsDataRepository informedAlertsDataRepository) {
    return informedAlertsDataRepository;
  }

  @Provides
  @Singleton
  SelectedEntityRepository provideSelectedEntityRepository(SelectedEntityDataRepository selectedEntityDataRepository) {
    return selectedEntityDataRepository;
  }

  @Provides
  @Singleton
  IntermediateRastersRepository provideIntermediateRastersRepository() {
    return new IntermediateRastersRepositoryData();
  }

  @Provides
  @Singleton
  IntermediateRasterVisibilityRepository provideIntermediateRasterVisibilityRepository(
      IntermediateRasterVisibilityDataRepository intermediateRasterVisibilityDataRepository) {
    return intermediateRasterVisibilityDataRepository;
  }

  @Provides
  @Singleton
  NewMessageIndicationRepository provideNewMessageIndicationRepository(
      NewMessageIndicationDataRepository newMessageIndicationDataRepository) {
    return newMessageIndicationDataRepository;
  }

  @Provides
  @Singleton
  MessagesCache provideMessagesCache(MessagesDao dao, MessagesEntityMapper mapper) {
    return new MessagesDataCache(dao, mapper);
  }
}
