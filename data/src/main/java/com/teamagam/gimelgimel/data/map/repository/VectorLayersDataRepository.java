package com.teamagam.gimelgimel.data.map.repository;

import com.teamagam.gimelgimel.data.base.repository.ReplayRepository;
import com.teamagam.gimelgimel.domain.map.repository.VectorLayersRepository;
import com.teamagam.gimelgimel.domain.messages.entity.contents.VectorLayer;

import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class VectorLayersDataRepository implements VectorLayersRepository {

    private ReplayRepository<VectorLayer> mVectorLayerInnerRepo;
    private Map<String, VectorLayer> mIdToVectorLayersMap;

    @Inject
    public VectorLayersDataRepository() {
        mIdToVectorLayersMap = new TreeMap<>();
        mVectorLayerInnerRepo = ReplayRepository.createReplayAll();
    }

    @Override
    public void add(VectorLayer vectorLayer) {
        if (!mIdToVectorLayersMap.containsKey(vectorLayer.getId())) {
            mIdToVectorLayersMap.put(vectorLayer.getId(), vectorLayer);
            mVectorLayerInnerRepo.add(vectorLayer);
        }
    }

    @Override
    public VectorLayer get(String id) {
        return mIdToVectorLayersMap.get(id);
    }

    @Override
    public VectorLayer remove(String id) {
        return mIdToVectorLayersMap.remove(id);
    }

    @Override
    public VectorLayer update(VectorLayer vectorLayer) {
        VectorLayer removed = remove(vectorLayer.getId());
        add(vectorLayer);
        return removed;
    }

    @Override
    public Observable<VectorLayer> getVectorLayersObservable() {
        return mVectorLayerInnerRepo.getObservable();
    }
}
