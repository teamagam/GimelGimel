package com.teamagam.gimelgimel.app.injectors.modules;

import android.content.Context;

import com.teamagam.gimelgimel.app.common.utils.GlideLoader;
import com.teamagam.gimelgimel.data.layers.LayersLocalCacheData;
import com.teamagam.gimelgimel.data.notifications.cellular_network.CellularNetworkTypeDataRepository;
import com.teamagam.gimelgimel.data.rasters.IntermediateRastersLocalStorageData;
import com.teamagam.gimelgimel.domain.layers.LayersLocalCache;
import com.teamagam.gimelgimel.domain.notifications.cellular_network.CellularNetworkTypeRepository;
import com.teamagam.gimelgimel.domain.rasters.IntermediateRastersLocalStorage;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class UtilsModule {

    @Provides
    @Singleton
    LayersLocalCache provideLayersLocalCache(LayersLocalCacheData layersLocalCacheData) {
        return layersLocalCacheData;
    }

    @Provides
    @Singleton
    IntermediateRastersLocalStorage provideIntermediateRasterLocalStorage(
            IntermediateRastersLocalStorageData intermediateRastersLocalStorageData) {
        return intermediateRastersLocalStorageData;
    }

    @Provides
    @Singleton
    GlideLoader provideGlideFactory(Context context) {
        return new GlideLoader(context);
    }

    @Provides
    @Singleton
    CellularNetworkTypeRepository provideCellularNetworkTypeNotifier(
            CellularNetworkTypeDataRepository dataCellularNetworkTypeNotifier) {
        return dataCellularNetworkTypeNotifier;
    }
}
