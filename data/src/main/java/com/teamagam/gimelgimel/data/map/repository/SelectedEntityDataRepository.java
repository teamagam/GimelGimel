package com.teamagam.gimelgimel.data.map.repository;

import com.teamagam.gimelgimel.domain.map.repository.SelectedEntityRepository;

import javax.inject.Inject;

public class SelectedEntityDataRepository implements SelectedEntityRepository {

    private String mGeoEntityId;

    @Inject
    public SelectedEntityDataRepository() {
        mGeoEntityId = null;
    }

    @Override
    public String getSelectedEntityId() {
        return mGeoEntityId;
    }

    @Override
    public void setSelected(String entityId) {
        mGeoEntityId = entityId;
    }
}