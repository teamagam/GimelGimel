package com.teamagam.gimelgimel.app.injectors.modules;

import com.teamagam.gimelgimel.presentation.presenters.SendImageMessagePresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PresenterModule {

    @Provides
    @Singleton
    SendImageMessagePresenter provideSendImageMessagePresenter(){
        return new SendImageMessagePresenter();
    }
}
