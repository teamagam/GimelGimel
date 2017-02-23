package com.teamagam.gimelgimel.domain.map.entities.mapEntities;

import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import com.teamagam.gimelgimel.domain.messages.entity.contents.VectorLayer;

public class KmlEntityInfo {
    private final String mName;
    private final String mDescription;
    private final VectorLayer mVectorLayer;
    private final Geometry mGeometry;

    public KmlEntityInfo(String name, String description, VectorLayer vectorLayer, Geometry geometry) {
        mName = name;
        mDescription = description;
        mVectorLayer = vectorLayer;
        mGeometry = geometry;
    }

    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public VectorLayer getVectorLayer() {
        return mVectorLayer;
    }

    public Geometry getGeometry() {
        return mGeometry;
    }
}
