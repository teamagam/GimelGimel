package com.teamagam.gimelgimel.app.injectors.modules;

import com.teamagam.gimelgimel.presentation.presenters.SendGeoMessagePresenter;
import com.teamagam.gimelgimel.presentation.presenters.SendMessagePresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module that provides objects which will live during the application lifecycle.
 * all presenters are singletons.
 */
@Module
public class PresentersModule {

    @Provides
    @Singleton
    public SendGeoMessagePresenter provideGeoMessagePresenter() {
        return new SendGeoMessagePresenter();
    }

    @Provides
    @Singleton
    public SendMessagePresenter provideMessagePresenter() {
        return new SendMessagePresenter();
    }

}
