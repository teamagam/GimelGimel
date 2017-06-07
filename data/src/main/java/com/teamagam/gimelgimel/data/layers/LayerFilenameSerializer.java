package com.teamagam.gimelgimel.data.layers;

import android.text.TextUtils;
import com.teamagam.gimelgimel.data.config.Constants;
import com.teamagam.gimelgimel.domain.messages.entity.contents.VectorLayerContent;
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

  public String toFilename(VectorLayerContent vectorLayerContent) {
    List<String> nameElements = getNameElements(vectorLayerContent);

    return TextUtils.join(NAME_SEPARATOR, nameElements) + KML_EXTENSION;
  }

  public VectorLayerContent toVectorLayer(String filename) {
    String[] splitFilename = splitFilenameToComponents(filename);
    String pref = splitFilename[LAYER_PREF];
    String id = splitFilename[ID_POSITION];
    String name = splitFilename[NAME_POSITION];
    Integer version = Integer.valueOf(splitFilename[VERSION_POSITION]);
    VectorLayerContent.Severity severity = VectorLayerContent.Severity.REGULAR;
    VectorLayerContent.Category category =
        VectorLayerContent.Category.parseCaseInsensitive(splitFilename[CATEGORY_POSITION]);

    if (pref.equals(Constants.VECTOR_LAYER_CACHE_PREFIX)) {
      return new VectorLayerContent(id, name, version, severity, category);
    }

    throw new RuntimeException(
        String.format("Unrecognized file (not a VectorLayer) was found: %s", filename));
  }

  private List<String> getNameElements(VectorLayerContent vectorLayerContent) {
    ArrayList<String> nameElements = new ArrayList<>(4);
    nameElements.add(LAYER_PREF, Constants.VECTOR_LAYER_CACHE_PREFIX);
    nameElements.add(ID_POSITION, vectorLayerContent.getId());
    nameElements.add(NAME_POSITION, vectorLayerContent.getName());
    nameElements.add(VERSION_POSITION, String.valueOf(vectorLayerContent.getVersion()));
    nameElements.add(CATEGORY_POSITION, vectorLayerContent.getCategory().name());

    return nameElements;
  }

  private String[] splitFilenameToComponents(String filename) {
    String filenameNoExtension = filename.substring(0, filename.lastIndexOf("."));
    String[] splitFilename = filenameNoExtension.split(NAME_SEPARATOR);
    if (splitFilename.length != FILENAME_PARTS_COUNT) {
      throw new RuntimeException(String.format(
          "VectorLayer filename must obey to the " + "naming convention.\nVectorLayer filename: %s",
          filename));
    }
    return splitFilename;
  }
}
