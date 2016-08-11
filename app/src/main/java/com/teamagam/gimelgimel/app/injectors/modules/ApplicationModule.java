package com.teamagam.gimelgimel.app.injectors.modules;


import android.content.Context;

import com.teamagam.gimelgimel.app.GGApplication;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module that provides objects which will live during the application lifecycle.
 */
@Module
public class ApplicationModule {

    private final GGApplication mApplication;

    public ApplicationModule(GGApplication application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    MessagesRepository provideUserRepository(MessagesRepository messageRepo) {
        return messageRepo;
    }
}
