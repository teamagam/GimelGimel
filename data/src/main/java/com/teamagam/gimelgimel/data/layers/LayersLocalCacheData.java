package com.teamagam.gimelgimel.data.layers;

import android.content.Context;
import android.text.TextUtils;

import com.teamagam.gimelgimel.data.common.FilesDownloader;
import com.teamagam.gimelgimel.data.config.Constants;
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

    private static final int LAYER_PREF = 0;
    private static final int ID_POSITION = 1;
    private static final int NAME_POSITION = 2;
    private static final int VERSION_POSITION = 3;
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
                generateTargetFilename(vectorLayer);
        return new File(fullFilename);
    }

    private String generateTargetFilename(VectorLayer vectorLayer) {
        ArrayList<String> nameElements = new ArrayList<>(4);
        nameElements.add(LAYER_PREF, Constants.VECTOR_LAYER_CACHE_PREFIX);
        nameElements.add(ID_POSITION, vectorLayer.getId());
        nameElements.add(NAME_POSITION, vectorLayer.getName());
        nameElements.add(VERSION_POSITION, String.valueOf(vectorLayer.getVersion()));
        return TextUtils.join(NAME_SEPARATOR, nameElements) + KML_EXTENSION;
    }

    private List<VectorLayer> extractVectorLayersFromFiles(File[] vectorLayerFiles) {
        List<VectorLayer> vectorLayers = new ArrayList<>(vectorLayerFiles.length);
        for (File file : vectorLayerFiles) {
            vectorLayers.add(extractVectorLayerFromFile(file));
        }
        return vectorLayers;
    }

    private VectorLayer extractVectorLayerFromFile(File file) {
        String[] splitFilename = splitFilenameToComponents(file.getName());
        String pref = splitFilename[LAYER_PREF];
        String id = splitFilename[ID_POSITION];
        String name = splitFilename[NAME_POSITION];
        Integer version = Integer.valueOf(splitFilename[VERSION_POSITION]);
        if (pref.equals(Constants.VECTOR_LAYER_CACHE_PREFIX)) {
            return new VectorLayer(id, name, version);
        }
        throw new RuntimeException(String.format(
                "Unrecognized file (not a VectorLayer) was found: %s", file.getName()));
    }

    private String[] splitFilenameToComponents(String filename) {
        String filenameNoExtension = filename.substring(0, filename.lastIndexOf("."));
        String[] splitFilename = filenameNoExtension.split(NAME_SEPARATOR);
        if (splitFilename.length != 4) {
            throw new RuntimeException(String.format("VectorLayer filename must obey to the " +
                    "naming convention.\nVectorLayer filename: %s", filename));
        }
        return splitFilename;
    }
}
