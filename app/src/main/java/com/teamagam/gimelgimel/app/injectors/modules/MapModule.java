package com.teamagam.gimelgimel.app.injectors.modules;

import android.app.Activity;

import com.teamagam.gimelgimel.app.injectors.scopes.PerActivity;
import com.teamagam.gimelgimel.app.map.viewModel.adapters.SymbolTransformer;
import com.teamagam.gimelgimel.app.map.viewModel.adapters.IMessageSymbolizer;
import com.teamagam.gimelgimel.app.view.MainActivity;

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
    IMessageSymbolizer provideMessageSymbolizer(SymbolTransformer symbolizer) {
        return symbolizer;
    }

}
