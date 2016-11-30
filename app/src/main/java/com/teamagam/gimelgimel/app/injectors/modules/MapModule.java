package com.teamagam.gimelgimel.app.injectors.modules;

import android.app.Activity;

import com.teamagam.gimelgimel.app.injectors.scopes.PerActivity;
import com.teamagam.gimelgimel.app.map.viewModel.MapViewModel;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivity;
import com.teamagam.gimelgimel.domain.map.ViewerCameraController;

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
    ViewerCameraController provideCameraController(MapViewModel mapViewModel) {
        return mapViewModel;
    }

}
