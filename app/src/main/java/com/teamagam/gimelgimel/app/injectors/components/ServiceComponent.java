package com.teamagam.gimelgimel.app.injectors.components;

import com.teamagam.gimelgimel.app.injectors.modules.MessageModule;
import com.teamagam.gimelgimel.app.injectors.scopes.PerActivity;
import com.teamagam.gimelgimel.app.network.services.GGLocationService;

import dagger.Component;

@PerActivity
@Component(
        dependencies = ApplicationComponent.class,
        modules = {
                MessageModule.class
        }
)
public interface ServiceComponent {
    void inject(GGLocationService ggLocationService);
}
