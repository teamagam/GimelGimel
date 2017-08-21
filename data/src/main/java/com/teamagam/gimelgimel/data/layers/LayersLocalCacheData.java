package com.teamagam.gimelgimel.data.layers;

import com.teamagam.gimelgimel.data.common.ExternalDirProvider;
import com.teamagam.gimelgimel.data.common.FilesDownloader;
import com.teamagam.gimelgimel.data.config.Constants;
import com.teamagam.gimelgimel.domain.layers.LayersLocalCache;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayer;
import java.io.File;
import java.net.URI;
import javax.inject.Inject;

public class LayersLocalCacheData implements LayersLocalCache {

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
  public URI cache(VectorLayer vectorLayer) {
    File file = getVectorLayerFile(vectorLayer);
    mFilesDownloader.download(vectorLayer.getUrl(), file);
    return file.toURI();
  }

  @Override
  public boolean isCached(VectorLayer vectorLayer) {
    return getVectorLayerFile(vectorLayer).exists();
  }

  @Override
  public URI getCachedURI(VectorLayer vectorLayer) {
    return getVectorLayerFile(vectorLayer).toURI();
  }

  public boolean clearCache() {
    boolean success = true;
    for (File file : mExternalVectorLayersDir.listFiles()) {
      success &= file.delete();
    }
    return success;
  }

  private File getVectorLayerFile(VectorLayer vectorLayer) {
    String fullFilename =
        mExternalVectorLayersDir + File.separator + mLayerFilenameSerializer.toFilename(
            vectorLayer);
    return new File(fullFilename);
  }
}
