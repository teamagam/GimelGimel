package com.teamagam.gimelgimel.app.injectors.modules;

import android.app.Activity;

import com.teamagam.gimelgimel.app.map.model.symbols.EntityMessageSymbolizer;
import com.teamagam.gimelgimel.app.map.model.symbols.IMessageSymbolizer;
import com.teamagam.gimelgimel.app.view.MainActivity;
import com.teamagam.gimelgimel.presentation.scopes.PerActivity;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module that provides map related collaborators.
 */
@Module
public class MapModule {

    public MapModule() {
    }

    @Provides
    @PerActivity
    MainActivity provideMainActivity(Activity activity) {
        return (MainActivity) activity;
    }

    @Provides
    @PerActivity
    @Named("entitySymbolizer")
    IMessageSymbolizer provideMessageSymbolizer(EntityMessageSymbolizer symbolizer) {
        return symbolizer;
    }

}
