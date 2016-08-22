package com.teamagam.gimelgimel.app.injectors.components;

import android.app.Fragment;

import com.teamagam.gimelgimel.app.injectors.modules.FragmentModule;
import com.teamagam.gimelgimel.presentation.scopes.PerFragment;

import dagger.Component;

/**
 * A base component upon which fragment's components may depend.
 * Fragment-level components should extend this component.
 *
 * Subtypes of FragmentComponent should be decorated with annotation:
 * {@link PerFragment}
 */
@PerFragment
@Component(
        dependencies = ApplicationComponent.class,
        modules = {
                FragmentModule.class
        }
)
public interface FragmentComponent{
//    void inject(BaseFragment fragment);

    Fragment fragment();
}
