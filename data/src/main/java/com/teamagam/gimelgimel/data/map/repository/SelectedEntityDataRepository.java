package com.teamagam.gimelgimel.data.map.repository;

import com.teamagam.gimelgimel.domain.map.repository.SelectedEntityRepository;

import javax.inject.Inject;

public class SelectedEntityDataRepository implements SelectedEntityRepository {

    private String mGeoEntity;

    @Inject
    public SelectedEntityDataRepository() {
        mGeoEntity = null;
    }

    @Override
    public String getSelectedEntityId() {
        return mGeoEntity;
    }

    @Override
    public void setSelected(String entityId) {
        mGeoEntity = entityId;
    }
}