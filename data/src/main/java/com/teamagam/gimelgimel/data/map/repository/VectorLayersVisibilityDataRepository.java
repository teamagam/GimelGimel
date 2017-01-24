package com.teamagam.gimelgimel.data.map.repository;

import com.teamagam.gimelgimel.data.base.repository.ReplayRepository;
import com.teamagam.gimelgimel.domain.map.repository.VectorLayersVisibilityRepository;
import com.teamagam.gimelgimel.domain.notifications.entity.VectorLayerVisibilityChange;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class VectorLayersVisibilityDataRepository implements VectorLayersVisibilityRepository {

    private final ReplayRepository<VectorLayerVisibilityChange> mVisibilityChangesLogRepository;

    @Inject
    public VectorLayersVisibilityDataRepository() {
        mVisibilityChangesLogRepository = ReplayRepository.createReplayAll();
    }

    @Override
    public Observable<VectorLayerVisibilityChange> getVisibilityChangesLogObservable() {
        return mVisibilityChangesLogRepository.getObservable();
    }

    @Override
    public void changeVectorLayerVisibility(VectorLayerVisibilityChange change) {
        mVisibilityChangesLogRepository.add(change);
    }
}
