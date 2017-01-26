package com.teamagam.gimelgimel.data.layers;

import android.content.Context;

import com.teamagam.gimelgimel.data.common.FilesDownloader;
import com.teamagam.gimelgimel.data.config.Constants;
import com.teamagam.gimelgimel.domain.layers.LayersLocalCache;
import com.teamagam.gimelgimel.domain.messages.entity.contents.VectorLayer;

import java.io.File;
import java.net.URI;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class LayersLocalCacheData implements LayersLocalCache {

    @Inject
    Context mContext;

    @Inject
    FilesDownloader mFilesDownloader;

    @Inject
    LayersLocalCacheData() {
    }

    @Override
    public Observable<URI> cache(VectorLayer vectorLayer) {
        return Observable.just(vectorLayer)
                .map(this::downloadToCache);
    }

    @Override
    public boolean isCached(VectorLayer vectorLayer) {
        return getVectorLayerFile(vectorLayer).exists();
    }

    @Override
    public URI getCachedURI(VectorLayer vectorLayer) {
        return getVectorLayerFile(vectorLayer).toURI();
    }

    private URI downloadToCache(VectorLayer vectorLayer) {
        File file = getVectorLayerFile(vectorLayer);
        mFilesDownloader.download(vectorLayer.getRemoteUrl(), file);
        return file.toURI();
    }

    private File getVectorLayerFile(VectorLayer vectorLayer) {
        File externalFilesDir = mContext.getExternalFilesDir(null);

        String fullFilename = externalFilesDir +
                File.separator +
                Constants.VECTOR_LAYERS_CACHE_DIR_NAME +
                File.separator +
                generateTargetFilename(vectorLayer);

        return new File(fullFilename);
    }

    private String generateTargetFilename(VectorLayer vectorLayer) {
        return Constants.VECTOR_LAYER_CACHE_PREFIX + "_" + vectorLayer.getId();
    }
}
