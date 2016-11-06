package com.teamagam.gimelgimel.app.injectors.modules;

import android.content.Context;

import com.teamagam.gimelgimel.data.images.ImagesDataRepository;
import com.teamagam.gimelgimel.data.location.repository.GpsLocationProvider;
import com.teamagam.gimelgimel.data.location.repository.LocationRepositoryImpl;
import com.teamagam.gimelgimel.data.map.repository.DisplayedEntitiesDataRepository;
import com.teamagam.gimelgimel.data.map.repository.GeoEntitiesDataRepository;
import com.teamagam.gimelgimel.data.map.repository.ViewerCameraRepositoryData;
import com.teamagam.gimelgimel.data.message.adapters.MessageDataMapper;
import com.teamagam.gimelgimel.data.message.repository.MessagesDataRepository;
import com.teamagam.gimelgimel.data.message.rest.GGMessagingAPI;
import com.teamagam.gimelgimel.data.user.repository.PreferencesProvider;
import com.teamagam.gimelgimel.data.user.repository.UserSettingsRepository;
import com.teamagam.gimelgimel.domain.location.respository.LocationRepository;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.map.repository.GeoEntitiesRepository;
import com.teamagam.gimelgimel.domain.map.repository.ViewerCameraRepository;
import com.teamagam.gimelgimel.domain.messages.repository.ImagesRepository;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

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
    UserPreferencesRepository providePreferencesRepository(
            PreferencesProvider preferencesProvider) {
        return new UserSettingsRepository(preferencesProvider);
    }

    @Provides
    @Singleton
    LocationRepository provideLocationRepository(GpsLocationProvider gpsLocationProvider) {
        return new LocationRepositoryImpl(gpsLocationProvider);
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
}
