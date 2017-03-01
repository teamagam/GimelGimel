package com.teamagam.gimelgimel.data.map.repository;

import com.teamagam.gimelgimel.data.base.repository.ReplayRepository;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.KmlEntityInfo;
import com.teamagam.gimelgimel.domain.map.repository.CurrentKmlEntityInfoRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class CurrentKmlEntityInfoDataRepository implements CurrentKmlEntityInfoRepository {

    private ReplayRepository<KmlEntityInfoEvent> mInnerRepo;
    private KmlEntityInfo mKmlEntityInfo;

    @Inject
    public CurrentKmlEntityInfoDataRepository() {
        mInnerRepo = ReplayRepository.createReplayCount(1);
        mKmlEntityInfo = null;
    }

    @Override
    public Observable<KmlEntityInfoEvent> getKmlEntityInfoEventsObservable() {
        return mInnerRepo.getObservable();
    }

    @Override
    public KmlEntityInfo getCurrentKmlEntityInfo() {
        return mKmlEntityInfo;
    }

    @Override
    public void setCurrentKmlEntityInfo(KmlEntityInfo kmlEntityInfo) {
        mKmlEntityInfo = kmlEntityInfo;
        mInnerRepo.add(
                mKmlEntityInfo == null ? KmlEntityInfoEvent.HIDE : KmlEntityInfoEvent.DISPLAY);
    }
}
