package com.teamagam.gimelgimel.data.layers;

import android.content.Context;

import com.teamagam.gimelgimel.data.common.FilesDownloader;
import com.teamagam.gimelgimel.data.config.Constants;
import com.teamagam.gimelgimel.domain.layers.LayersLocalCache;
import com.teamagam.gimelgimel.domain.messages.entity.contents.VectorLayer;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class LayersLocalCacheData implements LayersLocalCache {

    private static final String KML_EXTENSION = ".kml";
    private static final String NAME_SEPARATOR = "_";

    private final Context mContext;
    private final File mExternalVectorLayersDir;
    @Inject
    FilesDownloader mFilesDownloader;

    @Inject
    LayersLocalCacheData(Context context) {
        mContext = context;
        File externalFilesDir = mContext.getExternalFilesDir(null);
        mExternalVectorLayersDir = new File(externalFilesDir +
                File.separator +
                Constants.VECTOR_LAYERS_CACHE_DIR_NAME);
    }

    @Override
    public Observable<URI> cache(VectorLayer vectorLayer, URL url) {
        return Observable.just(downloadToCache(vectorLayer, url));
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
        List<VectorLayer> vectorLayers = new ArrayList<>(vectorLayerFiles.length);
        for (File file : vectorLayerFiles) {
            VectorLayer vectorLayer = extractVectorLayerFromFile(file);
            vectorLayers.add(vectorLayer);
        }
        return vectorLayers;
    }

    private URI downloadToCache(VectorLayer vectorLayer, URL url) {
        File file = getVectorLayerFile(vectorLayer);
        mFilesDownloader.download(url, file);
        return file.toURI();
    }

    private File getVectorLayerFile(VectorLayer vectorLayer) {
        String fullFilename = mExternalVectorLayersDir +
                File.separator +
                generateTargetFilename(vectorLayer);
        return new File(fullFilename);
    }

    private String generateTargetFilename(VectorLayer vectorLayer) {
        return Constants.VECTOR_LAYER_CACHE_PREFIX +
                NAME_SEPARATOR +
                vectorLayer.getId() +
                NAME_SEPARATOR +
                vectorLayer.getName() +
                NAME_SEPARATOR +
                vectorLayer.getVersion() +
                KML_EXTENSION;
    }

    private VectorLayer extractVectorLayerFromFile(File file) {
        String filename = file.getName();
        String[] splitFilename = filename.split(NAME_SEPARATOR);
        if (splitFilename.length != 4) {
            throw new RuntimeException(String.format("VectorLayer filename mustn't contain " +
                    "an underscore.\nVectorLayer filename: %s", filename));
        }
        String id = splitFilename[1];
        String name = splitFilename[2];
        Integer version = Integer.valueOf(splitFilename[3]);
        return new VectorLayer(id, name, version);
    }
}
