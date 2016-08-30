package com.teamagam.gimelgimel.app.injectors.modules;

import com.teamagam.gimelgimel.data.message.rest.GGMessagingAPI;
import com.teamagam.gimelgimel.data.message.rest.RestAPI;
import com.teamagam.gimelgimel.presentation.scopes.PerFragment;

import dagger.Module;
import dagger.Provides;

@Module
public class ApiModule {

    @Provides
    @PerFragment
    GGMessagingAPI provideGGMessagingAPI() {
        return new RestAPI().getMessagingAPI();
    }
}
