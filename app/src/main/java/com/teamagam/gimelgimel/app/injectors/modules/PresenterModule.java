package com.teamagam.gimelgimel.app.injectors.modules;

import com.teamagam.gimelgimel.presentation.presenters.SendGeoMessagePresenter;
import com.teamagam.gimelgimel.presentation.presenters.SendImageMessagePresenter;
import com.teamagam.gimelgimel.presentation.presenters.SendMessagePresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PresenterModule {

    @Provides
    @Singleton
    SendImageMessagePresenter provideSendImageMessagePresenter() {
        return new SendImageMessagePresenter();
    }

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
