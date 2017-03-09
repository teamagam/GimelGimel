package com.teamagam.gimelgimel.data.layers;

import com.teamagam.gimelgimel.data.common.ExternalDirProvider;
import com.teamagam.gimelgimel.data.common.FilesDownloader;
import com.teamagam.gimelgimel.data.config.Constants;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.base.logging.LoggerFactory;
import com.teamagam.gimelgimel.domain.layers.LayersLocalCache;
import com.teamagam.gimelgimel.domain.messages.entity.contents.VectorLayer;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class LayersLocalCacheData implements LayersLocalCache {

    private static final Logger sLogger = LoggerFactory.create(
            LayersLocalCacheData.class.getSimpleName());

    private final File mExternalVectorLayersDir;
    private final FilesDownloader mFilesDownloader;
    private final LayerFilenameSerializer mLayerFilenameSerializer;

    @Inject
    LayersLocalCacheData(ExternalDirProvider externalDirProvider,
                         FilesDownloader filesDownloader,
                         LayerFilenameSerializer layerFilenameSerializer) {
        File externalFilesDir = externalDirProvider.getExternalFilesDir();
        mExternalVectorLayersDir = new File(externalFilesDir +
                File.separator +
                Constants.VECTOR_LAYERS_CACHE_DIR_NAME);
        mFilesDownloader = filesDownloader;
        mLayerFilenameSerializer = layerFilenameSerializer;
    }

    @Override
    public Observable<URI> cache(VectorLayer vectorLayer, URL url) {
        return Observable.just(null)
                .map(x -> downloadToCache(vectorLayer, url));
    }

    @Override
    public boolean isCached(VectorLayer vectorLayer) {
        return getVectorLayerFile(vectorLayer).exists();
    }

    @Override
    public URI getCachedURI(VectorLayer vectorLayer) {
        return getVectorLayerFile(vectorLayer).toURI();
    }

    @Override
    public Iterable<VectorLayer> getAllCachedLayers() {
        File[] vectorLayerFiles = mExternalVectorLayersDir.listFiles();
        if (vectorLayerFiles == null) {
            return Collections.emptyList();
        }
        return extractVectorLayersFromFiles(vectorLayerFiles);
    }

    private URI downloadToCache(VectorLayer vectorLayer, URL url) {
        File file = getVectorLayerFile(vectorLayer);
        mFilesDownloader.download(url, file);
        return file.toURI();
    }

    private File getVectorLayerFile(VectorLayer vectorLayer) {
        String fullFilename = mExternalVectorLayersDir +
                File.separator +
                mLayerFilenameSerializer.toFilename(vectorLayer);
        return new File(fullFilename);
    }

    private List<VectorLayer> extractVectorLayersFromFiles(File[] vectorLayerFiles) {
        List<VectorLayer> vectorLayers = new ArrayList<>(vectorLayerFiles.length);
        for (File file : vectorLayerFiles) {
            try {
                vectorLayers.add(mLayerFilenameSerializer.toVectorLayer(file.getName()));
            } catch (Exception e) {
                sLogger.w("Couldn't process file: " + file);
            }
        }
        return vectorLayers;
    }
}
