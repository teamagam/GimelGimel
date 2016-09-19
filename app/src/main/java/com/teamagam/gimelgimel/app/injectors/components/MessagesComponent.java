package com.teamagam.gimelgimel.app.injectors.components;


import com.teamagam.gimelgimel.app.message.view.SendMessageDialogFragment;
import com.teamagam.gimelgimel.app.model.ViewsModels.SendImageMessageViewModel;
import com.teamagam.gimelgimel.app.model.ViewsModels.ViewerFragmentViewModel;
import com.teamagam.gimelgimel.presentation.scopes.PerActivity;
import com.teamagam.gimelgimel.presentation.scopes.PerFragment;

import dagger.Component;

/**
 * A scope {@link PerFragment} component.
 * Injects user specific Fragments.
 */
@PerActivity
@Component(
        dependencies = ApplicationComponent.class,
        modules = {
        }
)
public interface MessagesComponent {
    void inject(SendMessageDialogFragment sendMessageFragment);

    void inject(ViewerFragmentViewModel viewerFragmentViewModel);

    void inject(SendImageMessageViewModel sendImageMessageViewModel);
}
