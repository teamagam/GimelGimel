package com.teamagam.gimelgimel.domain.map.repository;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;

//import org.jetbrains.annotations.Nullable;

/**
 * A repository for GeoEntity(ies)
 */

public interface GeoEntitiesRepository {

    void add(GeoEntity geoEntity);

    GeoEntity remove(String id);

    /**
     *
     * @param id
     * @return null if the entity wasn't found
     */
    GeoEntity get(String id);

    /**
     * updates or adds a new entity if entity with the same id does not exist.
     *
     * @param geoEntity with the same id as the updated entity
     * @return the previous entity before the update or null if non
     */
    GeoEntity update(GeoEntity geoEntity);

}