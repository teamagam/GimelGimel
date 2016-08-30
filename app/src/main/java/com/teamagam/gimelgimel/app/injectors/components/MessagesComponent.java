package com.teamagam.gimelgimel.app.injectors.components;


import com.teamagam.gimelgimel.app.injectors.modules.ApiModule;
import com.teamagam.gimelgimel.app.injectors.modules.MessageModule;
import com.teamagam.gimelgimel.app.injectors.modules.RepositoryModule;
import com.teamagam.gimelgimel.app.model.ViewsModels.ViewerFragmentViewModel;
import com.teamagam.gimelgimel.app.view.fragments.dialogs.SendMessageDialogFragment;
import com.teamagam.gimelgimel.presentation.scopes.PerFragment;

import dagger.Component;

/**
 * A scope {@link PerFragment} component.
 * Injects user specific Fragments.
 */
@PerFragment
@Component(
        dependencies = ApplicationComponent.class,
        modules = {
                ApiModule.class,
                RepositoryModule.class,
                MessageModule.class
        }
)
public interface MessagesComponent{
    void inject(SendMessageDialogFragment sendMessageFragment);

    void inject(ViewerFragmentViewModel viewerFragmentViewModel);
}
