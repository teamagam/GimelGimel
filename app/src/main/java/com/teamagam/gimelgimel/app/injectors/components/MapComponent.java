package com.teamagam.gimelgimel.app.injectors.components;

import com.teamagam.gimelgimel.app.injectors.modules.ActivityModule;
import com.teamagam.gimelgimel.app.injectors.modules.MapModule;
import com.teamagam.gimelgimel.app.map.view.ViewerFragment;
import com.teamagam.gimelgimel.presentation.scopes.PerActivity;
import com.teamagam.gimelgimel.presentation.scopes.PerFragment;

import dagger.Component;

/**
 * A scope {@link PerFragment} component.
 * Injects map specific Fragment ({@link ViewerFragment}).
 */
@PerActivity
@Component(
        dependencies = ApplicationComponent.class,
        modules = {
                ActivityModule.class,
                MapModule.class
        }
)
public interface MapComponent extends ActivityComponent{
    void inject(ViewerFragment viewerFragment);
}

