package com.teamagam.gimelgimel.app.message.model.contents;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;

/**
 * A class represents a location pinned by the user
 */
public class GeoContentApp {

    private GeoEntity mGeoEntity;

    public GeoContentApp(GeoEntity geoEntity) {
        mGeoEntity = geoEntity;
    }

    public GeoEntity getGeoEntity() {
        return mGeoEntity;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("GeographicLocationEntity[");
        s.append("geo entity: ");
        s.append(mGeoEntity);
        s.append(']');
        return s.toString();
    }
}
