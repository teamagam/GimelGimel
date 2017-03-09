package com.teamagam.gimelgimel.data.layers;

import android.text.TextUtils;

import com.teamagam.gimelgimel.data.config.Constants;
import com.teamagam.gimelgimel.domain.messages.entity.contents.VectorLayer;

import java.util.ArrayList;

import javax.inject.Inject;

public class LayerFilenameSerializer {

    private static final int LAYER_PREF = 0;
    private static final int ID_POSITION = 1;
    private static final int NAME_POSITION = 2;
    private static final int VERSION_POSITION = 3;
    private static final int SEVERITY_POSITION = 4;
    private static final int CATEGORY_POSITION = 5;

    private static final int FILENAME_PARTS_COUNT = 6;

    private static final String KML_EXTENSION = ".kml";
    private static final String NAME_SEPARATOR = "_";

    @Inject
    public LayerFilenameSerializer() {
    }

    public String toFilename(VectorLayer vectorLayer) {
        ArrayList<String> nameElements = new ArrayList<>(4);
        nameElements.add(LAYER_PREF, Constants.VECTOR_LAYER_CACHE_PREFIX);
        nameElements.add(ID_POSITION, vectorLayer.getId());
        nameElements.add(NAME_POSITION, vectorLayer.getName());
        nameElements.add(VERSION_POSITION, String.valueOf(vectorLayer.getVersion()));
        nameElements.add(SEVERITY_POSITION, vectorLayer.getSeverity().name());
        nameElements.add(CATEGORY_POSITION, vectorLayer.getCategory().name());
        return TextUtils.join(NAME_SEPARATOR, nameElements) + KML_EXTENSION;
    }

    public VectorLayer toVectorLayer(String filename) {
        String[] splitFilename = splitFilenameToComponents(filename);
        String pref = splitFilename[LAYER_PREF];
        String id = splitFilename[ID_POSITION];
        String name = splitFilename[NAME_POSITION];
        Integer version = Integer.valueOf(splitFilename[VERSION_POSITION]);
        VectorLayer.Severity severity = VectorLayer.Severity.parseCaseInsensitive(
                splitFilename[SEVERITY_POSITION]);
        VectorLayer.Category category = VectorLayer.Category.parseCaseInsensitive(
                splitFilename[CATEGORY_POSITION]);

        if (pref.equals(Constants.VECTOR_LAYER_CACHE_PREFIX)) {
            return new VectorLayer(id, name, version, severity, category);
        }

        throw new RuntimeException(String.format(
                "Unrecognized file (not a VectorLayer) was found: %s", filename));
    }

    private String[] splitFilenameToComponents(String filename) {
        String filenameNoExtension = filename.substring(0, filename.lastIndexOf("."));
        String[] splitFilename = filenameNoExtension.split(NAME_SEPARATOR);
        if (splitFilename.length != FILENAME_PARTS_COUNT) {
            throw new RuntimeException(String.format("VectorLayer filename must obey to the " +
                    "naming convention.\nVectorLayer filename: %s", filename));
        }
        return splitFilename;
    }
}
