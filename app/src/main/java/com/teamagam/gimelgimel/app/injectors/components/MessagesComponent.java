package com.teamagam.gimelgimel.app.injectors.components;


import com.teamagam.gimelgimel.app.injectors.modules.MessageModule;
import com.teamagam.gimelgimel.app.injectors.scopes.PerActivity;
import com.teamagam.gimelgimel.app.view.fragments.dialogs.SendMessageDialogFragment;

import javax.inject.Singleton;

import dagger.Component;

/**
 * A scope {@link PerActivity} component.
 * Injects user specific Fragments.
 */
@Singleton
@Component(
//        dependencies = ApplicationComponent.class,
        modules = {
                MessageModule.class
        }
)
public interface MessagesComponent{
    void inject(SendMessageDialogFragment sendMessageFragment);
}
