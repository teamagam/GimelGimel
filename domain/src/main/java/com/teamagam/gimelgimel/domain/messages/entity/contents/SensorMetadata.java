package com.teamagam.gimelgimel.domain.messages.entity.contents;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;

public class SensorMetadata {

    private String mId;
    private String mName;
    private GeoEntity mGeoEntity;

    public SensorMetadata(
            String id,
            String name,
            GeoEntity geoEntity
    ) {
        mId = id;
        mName = name;
        mGeoEntity = geoEntity;
    }

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public GeoEntity getGeoEntity() {
        return mGeoEntity;
    }
}
