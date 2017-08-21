package com.teamagam.gimelgimel.data.layers;

import android.text.TextUtils;
import com.teamagam.gimelgimel.data.config.Constants;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayer;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class LayerFilenameSerializer {

  private static final int LAYER_PREF = 0;
  private static final int ID_POSITION = 1;
  private static final int NAME_POSITION = 2;
  private static final int VERSION_POSITION = 3;
  private static final int CATEGORY_POSITION = 4;

  private static final int FILENAME_PARTS_COUNT = 5;

  private static final String KML_EXTENSION = ".kml";
  private static final String NAME_SEPARATOR = "_";

  @Inject
  public LayerFilenameSerializer() {
  }

  public String toFilename(VectorLayer vectorLayer) {
    List<String> nameElements = getNameElements(vectorLayer);

    return TextUtils.join(NAME_SEPARATOR, nameElements) + KML_EXTENSION;
  }

  private List<String> getNameElements(VectorLayer vectorLayer) {
    ArrayList<String> nameElements = new ArrayList<>(4);
    nameElements.add(LAYER_PREF, Constants.VECTOR_LAYER_CACHE_PREFIX);
    nameElements.add(ID_POSITION, vectorLayer.getId());
    nameElements.add(NAME_POSITION, vectorLayer.getName());
    nameElements.add(VERSION_POSITION, String.valueOf(vectorLayer.getVersion()));
    nameElements.add(CATEGORY_POSITION, vectorLayer.getCategory().name());

    return nameElements;
  }
}
