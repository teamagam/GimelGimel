package com.teamagam.gimelgimel.data.map.repository;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.KmlEntityInfo;
import com.teamagam.gimelgimel.domain.map.repository.CurrentlyPresentedKmlEntityInfoRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CurrentlyPresentedKmlEntityInfoDataRepository
        implements CurrentlyPresentedKmlEntityInfoRepository {

    private KmlEntityInfo mKmlEntityInfo;

    @Inject
    public CurrentlyPresentedKmlEntityInfoDataRepository() {
        mKmlEntityInfo = null;
    }

    @Override
    public KmlEntityInfo getCurrentlyPresentedKmlEntityInfo() {
        return mKmlEntityInfo;
    }

    @Override
    public void setCurrentlyPresentedKmlEntityInfo(KmlEntityInfo kmlEntityInfo) {
        mKmlEntityInfo = kmlEntityInfo;
    }
}
