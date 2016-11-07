package com.teamagam.gimelgimel.data.map.repository;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.map.repository.GeoEntitiesRepository;

import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GeoEntitiesDataRepository implements GeoEntitiesRepository {

    private Map<String, GeoEntity> mGeoEntitiesMap;
    private DisplayedEntitiesRepository mDisplayedRepo;

    @Inject
    public GeoEntitiesDataRepository(DisplayedEntitiesRepository displayedRepo) {
        mDisplayedRepo = displayedRepo;
        mGeoEntitiesMap = new TreeMap<>();
    }

    @Override
    public void add(GeoEntity geoEntity) {
        mGeoEntitiesMap.put(geoEntity.getId(), geoEntity);
    }

    @Override
    public GeoEntity remove(String id) {
        return mGeoEntitiesMap.remove(id);
    }

    @Nullable
    @Override
    public GeoEntity get(String id) {
        return mGeoEntitiesMap.get(id);
    }

    @Override
    public GeoEntity update(GeoEntity geoEntity){
        GeoEntity removed = remove(geoEntity.getId());
        add(geoEntity);
        mDisplayedRepo.hide(removed);
        mDisplayedRepo.show(geoEntity);
        return removed;
    }
}
