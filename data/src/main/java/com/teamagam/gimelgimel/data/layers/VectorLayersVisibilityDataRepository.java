package com.teamagam.gimelgimel.data.layers;

import com.teamagam.gimelgimel.data.base.repository.ReplayRepository;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerVisibilityChange;
import com.teamagam.gimelgimel.domain.layers.repository.VectorLayersVisibilityRepository;

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
    public Observable<VectorLayerVisibilityChange> getChangesObservable() {
        return mVisibilityChangesLogRepository.getObservable();
    }

    @Override
    public void addChange(VectorLayerVisibilityChange change) {
        mVisibilityChangesLogRepository.add(change);
    }
}
