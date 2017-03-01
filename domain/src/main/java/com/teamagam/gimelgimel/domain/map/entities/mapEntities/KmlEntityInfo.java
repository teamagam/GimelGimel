package com.teamagam.gimelgimel.domain.map.entities.mapEntities;

import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;

public class KmlEntityInfo {
    private static final String DEFAULT_NAME = "No available name";
    private static final String DEFAULT_DESCRIPTION = "No available description.";

    private final String mName;
    private final String mDescription;
    private final String mVectorLayerId;
    private final Geometry mGeometry;

    public KmlEntityInfo(String name, String description, String vectorLayerId, Geometry geometry) {
        mName = name != null ? name : DEFAULT_NAME;
        mDescription = description != null ? description : DEFAULT_DESCRIPTION;
        mVectorLayerId = vectorLayerId;
        mGeometry = geometry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KmlEntityInfo that = (KmlEntityInfo) o;

        if (!mName.equals(that.mName)) return false;
        return mDescription.equals(that.mDescription);

    }

    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getVectorLayerId() {
        return mVectorLayerId;
    }

    public Geometry getGeometry() {
        return mGeometry;
    }
}
