package com.teamagam.gimelgimel.app.injectors.modules;

import android.app.Activity;
import android.content.Context;

import com.teamagam.gimelgimel.app.map.MapViewModel;
import com.teamagam.gimelgimel.app.view.MainActivity;
import com.teamagam.gimelgimel.presentation.scopes.PerActivity;

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
    MapViewModel provideMapViewModel(Context context, Activity activity) {
        return new MapViewModel(context, provideMainActivity(activity));
    }

    @Provides
    @PerActivity
    MainActivity provideMainActivity(Activity activity){
        return (MainActivity) activity;
    }
}
