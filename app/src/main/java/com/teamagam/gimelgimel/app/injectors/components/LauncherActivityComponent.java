package com.teamagam.gimelgimel.app.injectors.components;

import com.teamagam.gimelgimel.app.injectors.scopes.PerActivity;
import com.teamagam.gimelgimel.app.view.LauncherActivity;

import dagger.Component;

@PerActivity
@Component(
        dependencies = ApplicationComponent.class
)
public interface LauncherActivityComponent {
    void inject(LauncherActivity launcherActivity);
}
