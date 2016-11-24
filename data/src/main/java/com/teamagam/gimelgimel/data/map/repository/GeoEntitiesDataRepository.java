package com.teamagam.gimelgimel.data.map.repository;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.repository.GeoEntitiesRepository;

import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GeoEntitiesDataRepository implements GeoEntitiesRepository {

    private Map<String, GeoEntity> mGeoEntitiesMap;
    private DisplayedEntitiesDataRepository mDisplayedRepo;

    @Inject
    public GeoEntitiesDataRepository(DisplayedEntitiesDataRepository displayedRepo) {
        mDisplayedRepo = displayedRepo;
        mGeoEntitiesMap = new TreeMap<>();
    }

    @Override
    public void add(GeoEntity geoEntity) {
        if (!mGeoEntitiesMap.containsKey(geoEntity.getId())) {
            mGeoEntitiesMap.put(geoEntity.getId(), geoEntity);
        }
    }

    @Override
    public GeoEntity remove(String id) {
        return mGeoEntitiesMap.remove(id);
    }

    @Override
    public GeoEntity get(String id) {
        return mGeoEntitiesMap.get(id);
    }

    @Override
    public GeoEntity update(GeoEntity geoEntity) {
        GeoEntity removed = remove(geoEntity.getId());
        add(geoEntity);
        if(mDisplayedRepo.isEntityShown(geoEntity)) {
            mDisplayedRepo.update(geoEntity);
        }
        return removed;
    }
}
