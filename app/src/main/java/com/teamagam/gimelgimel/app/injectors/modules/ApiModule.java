package com.teamagam.gimelgimel.app.injectors.modules;

import com.teamagam.gimelgimel.data.message.rest.GGMessagingAPI;
import com.teamagam.gimelgimel.data.message.rest.RestAPI;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApiModule {

    @Provides
    @Singleton
    RestAPI provideGeneralRestAPI() {
        return new RestAPI();
    }

    @Provides
    @Singleton
    GGMessagingAPI provideGGMessagingAPI(RestAPI restAPI) {
        return restAPI.getMessagingAPI();
    }
}
