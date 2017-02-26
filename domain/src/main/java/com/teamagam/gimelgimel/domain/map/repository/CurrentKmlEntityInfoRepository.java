package com.teamagam.gimelgimel.domain.map.repository;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.KmlEntityInfo;

import rx.Observable;

public interface CurrentKmlEntityInfoRepository {
    Observable<KmlEntityInfoEvent> getKmlEntityInfoEventsObservable();

    KmlEntityInfo getCurrentKmlEntityInfo();

    void setCurrentKmlEntityInfo(KmlEntityInfo kmlEntityInfo);

    enum KmlEntityInfoEvent {
        DISPLAY, HIDE
    }
}
