package com.teamagam.gimelgimel.domain.map.repository;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;

public interface GeoEntitiesRepository {

    void add(GeoEntity geoEntity);

    GeoEntity remove(String id);

    GeoEntity get(String id);

    GeoEntity update(GeoEntity geoEntity);

}