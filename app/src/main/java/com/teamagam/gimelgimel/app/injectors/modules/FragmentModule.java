package com.teamagam.gimelgimel.app.injectors.modules;

import android.app.Fragment;

import com.teamagam.gimelgimel.presentation.scopes.PerFragment;

import dagger.Module;
import dagger.Provides;

/**
 * A module to wrap the Fragment state and expose it to the graph.
 */
@Module
public class FragmentModule {

    private final Fragment fragment;

    public FragmentModule(Fragment fragment) {
        this.fragment = fragment;
    }

    /**
     * Expose the fragment to dependents in the graph.
     */
    @Provides
    @PerFragment Fragment fragment() {
        return this.fragment;
    }
}