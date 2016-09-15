package com.teamagam.gimelgimel.app.injectors.modules;

import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.images.SendImageMessageInteractor;
import com.teamagam.gimelgimel.domain.images.repository.ImagesRepository;
import com.teamagam.gimelgimel.domain.location.respository.LocationRepository;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class InteractorModule {

    @Provides
    @Singleton
    SendImageMessageInteractor provideSendImageMessageInteractor(ThreadExecutor threadExecutor,
                                                                 PostExecutionThread postExecutionThread,
                                                                 ImagesRepository imagesRepository,
                                                                 MessagesRepository messagesRepository,
                                                                 UserPreferencesRepository userPreferencesRepository,
                                                                 LocationRepository locationRepository) {
        return new SendImageMessageInteractor(threadExecutor, postExecutionThread,
                imagesRepository, messagesRepository, userPreferencesRepository, locationRepository);
    }

}
