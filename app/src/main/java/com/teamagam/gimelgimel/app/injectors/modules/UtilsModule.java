package com.teamagam.gimelgimel.app.injectors.modules;

import android.content.Context;

import com.teamagam.gimelgimel.app.common.utils.GlideLoader;
import com.teamagam.gimelgimel.data.common.ExternalDirProvider;
import com.teamagam.gimelgimel.data.common.FilesDownloader;
import com.teamagam.gimelgimel.data.layers.IntermediateRasterLocalStorageData;
import com.teamagam.gimelgimel.data.layers.LayerFilenameSerializer;
import com.teamagam.gimelgimel.data.layers.LayersLocalCacheData;
import com.teamagam.gimelgimel.data.notifications.cellular_network.CellularNetworkTypeDataRepository;
import com.teamagam.gimelgimel.domain.layers.IntermediateRasterLocalStorage;
import com.teamagam.gimelgimel.domain.layers.LayersLocalCache;
import com.teamagam.gimelgimel.domain.notifications.cellular_network.CellularNetworkTypeRepository;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class UtilsModule {

    @Provides
    @Singleton
    LayersLocalCache provideLayersLocalCache(ExternalDirProvider externalDirProvider,
                                             FilesDownloader filesDownloader,
                                             LayerFilenameSerializer serializer) {
        File baseDir = externalDirProvider.getExternalFilesDir();

        return new LayersLocalCacheData(baseDir, filesDownloader, serializer);
    }

    @Provides
    @Singleton
    IntermediateRasterLocalStorage provideIntermediateRasterLocalStorage(
            ExternalDirProvider externalDirProvider) {
        File baseDir = externalDirProvider.getExternalFilesDir();

        return new IntermediateRasterLocalStorageData(baseDir);
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
