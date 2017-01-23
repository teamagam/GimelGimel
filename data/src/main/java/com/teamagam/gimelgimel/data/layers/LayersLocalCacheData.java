package com.teamagam.gimelgimel.data.layers;

import android.content.Context;

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
    public LayersLocalCacheData() {
    }

    @Override
    public Observable<URI> cache(VectorLayer vectorLayer) {
        return Observable.just(vectorLayer)
                .map(this::downloadToCache);
    }

    @Override
    public boolean isCached(VectorLayer vectorLayer) {
        File file = new File(generateTargetURI(vectorLayer).getPath());
        return file.exists();
    }

    @Override
    public URI getCachedURI(VectorLayer vectorLayer) {
        return generateTargetURI(vectorLayer);
    }

    private URI downloadToCache(VectorLayer vectorLayer) {
        URI targetURI = generateTargetURI(vectorLayer);
        mFilesDownloader.download(vectorLayer.getRemoteUrl(), targetURI);
        return getCachedURI(vectorLayer);
    }

    private URI generateTargetURI(VectorLayer vectorLayer) {
        File externalFilesDir = mContext.getExternalFilesDir(null);

        String fullFilename = String.valueOf(externalFilesDir) +
                File.separator +
                Constants.VECTOR_LAYERS_CACHE_DIR_NAME +
                File.separator +
                generateTargetFilename(vectorLayer);

        return URI.create(fullFilename);
    }

    private String generateTargetFilename(VectorLayer vectorLayer) {
        return Constants.VECTOR_LAYER_CACHE_PREFIX + "_" + vectorLayer.getId();
    }
}
