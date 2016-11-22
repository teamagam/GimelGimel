package com.teamagam.gimelgimel.app.injectors.modules;

import android.content.Context;
import android.preference.PreferenceManager;

import com.teamagam.gimelgimel.app.utils.Constants;
import com.teamagam.gimelgimel.data.images.ImagesDataRepository;
import com.teamagam.gimelgimel.data.location.repository.LocationRepositoryImpl;
import com.teamagam.gimelgimel.data.map.repository.DisplayedEntitiesDataRepository;
import com.teamagam.gimelgimel.data.map.repository.GeoEntitiesDataRepository;
import com.teamagam.gimelgimel.data.map.repository.ViewerCameraRepositoryData;
import com.teamagam.gimelgimel.data.message.adapters.MessageDataMapper;
import com.teamagam.gimelgimel.data.message.repository.MessagesDataRepository;
import com.teamagam.gimelgimel.data.message.rest.GGMessagingAPI;
import com.teamagam.gimelgimel.data.notifications.PersistentConnectivityStatusRepositoryImpl;
import com.teamagam.gimelgimel.data.user.repository.UserPreferenceRepositoryImpl;
import com.teamagam.gimelgimel.domain.location.LocationEventFetcher;
import com.teamagam.gimelgimel.domain.location.respository.LocationRepository;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.map.repository.GeoEntitiesRepository;
import com.teamagam.gimelgimel.domain.map.repository.ViewerCameraRepository;
import com.teamagam.gimelgimel.domain.messages.repository.ImagesRepository;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.notifications.repository.ConnectivityStatusRepository;
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
    ImagesRepository provideImageRepository(Context context,
                                            GGMessagingAPI api,
                                            MessageDataMapper messageDataMapper) {
        return new ImagesDataRepository(context, api, messageDataMapper);
    }

    @Provides
    @Singleton
    UserPreferencesRepository provideUserPreferencesRepository(Context context) {
        return new UserPreferenceRepositoryImpl(PreferenceManager.getDefaultSharedPreferences(context));
    }

    @Provides
    @Singleton
    LocationRepository provideLocationRepository(LocationRepositoryImpl locationRepository) {
        return locationRepository;
    }


    @Provides
    @Singleton
    LocationEventFetcher provideLocationEventFetcher(LocationRepository locationRepository) {
        //uses the same instance as LocationRepository
        return (LocationRepositoryImpl) locationRepository;
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
}
