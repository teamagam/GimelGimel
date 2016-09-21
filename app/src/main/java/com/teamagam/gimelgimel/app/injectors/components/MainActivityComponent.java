package com.teamagam.gimelgimel.app.injectors.components;

import com.teamagam.gimelgimel.app.injectors.modules.ActivityModule;
import com.teamagam.gimelgimel.app.injectors.modules.MapModule;
import com.teamagam.gimelgimel.app.injectors.modules.MessageModule;
import com.teamagam.gimelgimel.app.map.view.ViewerFragment;
import com.teamagam.gimelgimel.app.message.view.SendGeographicMessageDialog;
import com.teamagam.gimelgimel.app.message.view.SendMessageDialogFragment;
import com.teamagam.gimelgimel.presentation.scopes.PerActivity;

import dagger.Component;

/**
 * A scope {@link PerActivity} component.
 * Injects map specific Fragment ({@link ViewerFragment}).
 */
@PerActivity
@Component(
        dependencies = ApplicationComponent.class,
        modules = {
                ActivityModule.class,
                MapModule.class,
                MessageModule.class        }
)
public interface MainActivityComponent extends ActivityComponent {
    void inject(ViewerFragment viewerFragment);

    void inject(SendMessageDialogFragment sendMessageFragment);

    void inject(SendGeographicMessageDialog sendGeoMessage);
}

