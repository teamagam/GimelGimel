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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KmlEntityInfo that = (KmlEntityInfo) o;

        if (mName != null ? !mName.equals(that.mName) : that.mName != null) return false;
        return mDescription != null ? mDescription.equals(that.mDescription) : that.mDescription == null;

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
