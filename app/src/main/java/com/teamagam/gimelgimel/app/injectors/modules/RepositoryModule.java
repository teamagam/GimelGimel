package com.teamagam.gimelgimel.app.injectors.modules;

import com.teamagam.gimelgimel.data.images.ImagesDataRepository;
import com.teamagam.gimelgimel.data.message.adapters.MessageDataMapper;
import com.teamagam.gimelgimel.data.message.rest.GGMessagingAPI;
import com.teamagam.gimelgimel.domain.images.repository.ImagesRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Singleton
@Module
public class RepositoryModule {

    @Provides
    @Singleton
    ImagesRepository provideImageRepository(GGMessagingAPI api, MessageDataMapper messageDataMapper) {
        return new ImagesDataRepository(api, messageDataMapper);
    }
}
