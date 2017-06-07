package com.teamagam.gimelgimel.data.layers;

import com.teamagam.gimelgimel.data.common.ExternalDirProvider;
import com.teamagam.gimelgimel.data.common.FilesDownloader;
import com.teamagam.gimelgimel.data.config.Constants;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.base.logging.LoggerFactory;
import com.teamagam.gimelgimel.domain.layers.LayersLocalCache;
import com.teamagam.gimelgimel.domain.messages.entity.contents.VectorLayerContent;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;

public class LayersLocalCacheData implements LayersLocalCache {

  private static final Logger sLogger =
      LoggerFactory.create(LayersLocalCacheData.class.getSimpleName());

  private final File mExternalVectorLayersDir;
  private final FilesDownloader mFilesDownloader;
  private final LayerFilenameSerializer mLayerFilenameSerializer;

  @Inject
  public LayersLocalCacheData(ExternalDirProvider externalDirProvider,
      FilesDownloader filesDownloader,
      LayerFilenameSerializer layerFilenameSerializer) {
    mExternalVectorLayersDir = new File(externalDirProvider.getExternalFilesDir()
        + File.separator
        + Constants.VECTOR_LAYERS_CACHE_DIR_NAME);
    mFilesDownloader = filesDownloader;
    mLayerFilenameSerializer = layerFilenameSerializer;
  }

  @Override
  public Observable<URI> cache(VectorLayerContent vectorLayerContent, URL url) {
    return Observable.just(null).map(x -> downloadToCache(vectorLayerContent, url));
  }

  @Override
  public boolean isCached(VectorLayerContent vectorLayerContent) {
    return getVectorLayerFile(vectorLayerContent).exists();
  }

  @Override
  public URI getCachedURI(VectorLayerContent vectorLayerContent) {
    return getVectorLayerFile(vectorLayerContent).toURI();
  }

  @Override
  public Iterable<VectorLayerContent> getAllCachedLayers() {
    File[] vectorLayerFiles = mExternalVectorLayersDir.listFiles();
    if (vectorLayerFiles == null) {
      return Collections.emptyList();
    }
    return extractVectorLayersFromFiles(vectorLayerFiles);
  }

  private URI downloadToCache(VectorLayerContent vectorLayerContent, URL url) {
    File file = getVectorLayerFile(vectorLayerContent);
    mFilesDownloader.download(url, file);
    return file.toURI();
  }

  private File getVectorLayerFile(VectorLayerContent vectorLayerContent) {
    String fullFilename =
        mExternalVectorLayersDir + File.separator + mLayerFilenameSerializer.toFilename(
            vectorLayerContent);
    return new File(fullFilename);
  }

  private List<VectorLayerContent> extractVectorLayersFromFiles(File[] vectorLayerFiles) {
    List<VectorLayerContent> vectorLayerContents = new ArrayList<>(vectorLayerFiles.length);
    for (File file : vectorLayerFiles) {
      try {
        vectorLayerContents.add(mLayerFilenameSerializer.toVectorLayer(file.getName()));
      } catch (Exception e) {
        sLogger.w("Couldn't process file: " + file);
      }
    }
    return vectorLayerContents;
  }
}
