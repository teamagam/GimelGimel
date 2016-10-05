package com.teamagam.gimelgimel.domain.map.repository;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;

/**
 * A repository for GeoEntity(ies)
 */

public interface GeoEntitiesRepository {

    void add(GeoEntity geoEntity);

    GeoEntity remove(String id);

    GeoEntity get(String id);

    /**
     * @param geoEntity with the same id as the updated entity
     * @return the previous entity before the update
     */
    GeoEntity update(GeoEntity geoEntity);
}