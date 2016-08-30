package com.teamagam.gimelgimel.app.injectors.modules;

import com.teamagam.gimelgimel.data.geometry.entity.mapper.GeometryDataMapper;
import com.teamagam.gimelgimel.data.images.ImagesDataRepository;
import com.teamagam.gimelgimel.data.message.adapters.MessageDataMapper;
import com.teamagam.gimelgimel.data.message.rest.GGMessagingAPI;
import com.teamagam.gimelgimel.domain.images.repository.ImagesRepository;
import com.teamagam.gimelgimel.presentation.scopes.PerFragment;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Singleton
@Module
public class RepositoryModule {

    @Provides
    @PerFragment
    ImagesRepository provideImageRepository(GGMessagingAPI api) {
        GeometryDataMapper geometryDataMapper = new GeometryDataMapper();
        MessageDataMapper messageDataMapper = new MessageDataMapper(geometryDataMapper);

        return new ImagesDataRepository(api, messageDataMapper);
    }
}
