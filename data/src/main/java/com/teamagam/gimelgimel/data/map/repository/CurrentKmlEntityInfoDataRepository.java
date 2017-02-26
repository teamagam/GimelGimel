package com.teamagam.gimelgimel.data.map.repository;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.KmlEntityInfo;
import com.teamagam.gimelgimel.domain.map.repository.CurrentKmlEntityInfoRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

@Singleton
public class CurrentKmlEntityInfoDataRepository implements CurrentKmlEntityInfoRepository {

    private Subject<KmlEntityInfoEvent, KmlEntityInfoEvent> mSubject;
    private KmlEntityInfo mKmlEntityInfo;

    @Inject
    public CurrentKmlEntityInfoDataRepository() {
        mSubject = PublishSubject.<KmlEntityInfoEvent>create().toSerialized();
        mKmlEntityInfo = null;
    }

    @Override
    public Observable<KmlEntityInfoEvent> getKmlEntityInfoEventsObservable() {
        return mSubject.asObservable();
    }

    @Override
    public KmlEntityInfo getCurrentKmlEntityInfo() {
        return mKmlEntityInfo;
    }

    @Override
    public void setCurrentKmlEntityInfo(KmlEntityInfo kmlEntityInfo) {
        mKmlEntityInfo = kmlEntityInfo;
        mSubject.onNext(
                mKmlEntityInfo == null ? KmlEntityInfoEvent.HIDE : KmlEntityInfoEvent.DISPLAY);
    }
}
