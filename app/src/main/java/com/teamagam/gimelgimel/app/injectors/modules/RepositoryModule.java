package com.teamagam.gimelgimel.app.injectors.modules;

import android.content.Context;
import android.preference.PreferenceManager;

import com.teamagam.gimelgimel.app.common.utils.Constants;
import com.teamagam.gimelgimel.data.location.repository.LocationDataRepository;
import com.teamagam.gimelgimel.data.location.repository.UsersLocationDataRepository;
import com.teamagam.gimelgimel.data.map.repository.DisplayedEntitiesDataRepository;
import com.teamagam.gimelgimel.data.map.repository.GeoEntitiesDataRepository;
import com.teamagam.gimelgimel.data.map.repository.ViewerCameraRepositoryData;
import com.teamagam.gimelgimel.data.message.repository.MessagesDataRepository;
import com.teamagam.gimelgimel.data.notifications.PersistentConnectivityStatusRepositoryImpl;
import com.teamagam.gimelgimel.data.sensors.repository.SelectedSensorDataRepository;
import com.teamagam.gimelgimel.data.sensors.repository.SensorsDataRepository;
import com.teamagam.gimelgimel.data.user.repository.UserPreferenceRepositoryImpl;
import com.teamagam.gimelgimel.domain.location.LocationEventFetcher;
import com.teamagam.gimelgimel.domain.location.respository.LocationRepository;
import com.teamagam.gimelgimel.domain.location.respository.UsersLocationRepository;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.map.repository.GeoEntitiesRepository;
import com.teamagam.gimelgimel.domain.map.repository.ViewerCameraRepository;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.notifications.repository.ConnectivityStatusRepository;
import com.teamagam.gimelgimel.domain.sensors.repository.SelectedSensorRepository;
import com.teamagam.gimelgimel.domain.sensors.repository.SensorsRepository;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Singleton
@Module
public class RepositoryModule {

    @Provides
    @Singleton
    UserPreferencesRepository provideUserPreferencesRepository(Context context) {
        return new UserPreferenceRepositoryImpl(
                PreferenceManager.getDefaultSharedPreferences(context));
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
    MessagesRepository provideMessageRepository(MessagesDataRepository messageRepo) {
        return messageRepo;
    }

    @Provides
    @Singleton
    GeoEntitiesRepository provideGeoRepository(GeoEntitiesDataRepository geoRepo) {
        return geoRepo;
    }

    @Provides
    @Singleton
    DisplayedEntitiesRepository provideDisplayedRepository(
            DisplayedEntitiesDataRepository geoDisplayedData) {
        return geoDisplayedData;
    }

    @Provides
    @Singleton
    ViewerCameraRepository provideViewerCameraRepository(
            ViewerCameraRepositoryData repositoryData) {
        return repositoryData;
    }

    @Provides
    @Singleton
    @Named("gps")
    ConnectivityStatusRepository provideGpsConnectivityStatusRepository() {
        return new PersistentConnectivityStatusRepositoryImpl(
                Constants.GPS_STATUS_CONSISTENT_TIMEFRAME_MS);
    }

    @Provides
    @Singleton
    @Named("data")
    ConnectivityStatusRepository provideDataConnectivityStatusRepository() {
        return new PersistentConnectivityStatusRepositoryImpl(
                Constants.DATA_STATUS_CONSISTENT_TIMEFRAME_MS);
    }

    @Provides
    @Singleton
    UsersLocationRepository provideUserLocationRepository(
            UsersLocationDataRepository usersLocationDataRepository) {
        return usersLocationDataRepository;
    }

    @Provides
    @Singleton
    SensorsRepository provideSensorsRepository() {
        return new SensorsDataRepository();
    }

    @Provides
    @Singleton
    SelectedSensorRepository provideSelectedSesonrRepository() {
        return new SelectedSensorDataRepository();
    }
}
