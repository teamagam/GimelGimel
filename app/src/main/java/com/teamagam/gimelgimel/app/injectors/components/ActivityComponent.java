package com.teamagam.gimelgimel.app.injectors.components;

import android.app.Activity;

import com.teamagam.gimelgimel.app.injectors.modules.ActivityModule;
import com.teamagam.gimelgimel.presentation.scopes.PerActivity;

import dagger.Component;


/**
 * A base component upon which fragment's components may depend.
 * Activity-level components should extend this component.
 * <p>
 * Subtypes of ActivityComponent should be decorated with annotation:
 * {@link PerActivity}
 */
@PerActivity
@Component(
        dependencies = ApplicationComponent.class,
        modules = ActivityModule.class
)
public interface ActivityComponent {
    //Exposed to sub-graphs.
    Activity activity();
}
