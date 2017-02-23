package com.teamagam.gimelgimel.domain.map.repository;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.KmlEntityInfo;

public interface CurrentlyPresentedKmlEntityInfoRepository {
    KmlEntityInfo getCurrentlyPresentedKmlEntityInfo();

    void setCurrentlyPresentedKmlEntityInfo(KmlEntityInfo kmlEntityInfo);
}
