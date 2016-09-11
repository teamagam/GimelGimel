package com.teamagam.gimelgimel.app.injectors.components;


import com.teamagam.gimelgimel.app.injectors.modules.MessageModule;
import com.teamagam.gimelgimel.app.message.view.SendGeographicMessageDialog;
import com.teamagam.gimelgimel.app.view.fragments.dialogs.SendMessageDialogFragment;
import com.teamagam.gimelgimel.presentation.scopes.PerActivity;

import dagger.Component;

/**
 * A scope {@link PerActivity} component.
 * Injects messages specific Fragments.
 */
@PerActivity
@Component(
        dependencies = ApplicationComponent.class,
        modules = {
                MessageModule.class
        }
)
public interface MessagesComponent{
    void inject(SendMessageDialogFragment sendMessageFragment);
    void inject(SendGeographicMessageDialog sendGeoMessage);
}
