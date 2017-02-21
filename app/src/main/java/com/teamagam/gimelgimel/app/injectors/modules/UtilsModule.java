package com.teamagam.gimelgimel.app.injectors.modules;

import android.content.Context;

import com.teamagam.gimelgimel.app.common.utils.GlideLoader;
import com.teamagam.gimelgimel.data.layers.LayersLocalCacheData;
import com.teamagam.gimelgimel.domain.layers.LayersLocalCache;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class UtilsModule {

    @Provides
    @Singleton
    LayersLocalCache provideLayersLocalCache(LayersLocalCacheData layersLocalCacheData){
        return layersLocalCacheData;
    }

    @Provides
    @Singleton
    GlideLoader provideGlideFactory(Context context) {
        return new GlideLoader(context);
    }
}
