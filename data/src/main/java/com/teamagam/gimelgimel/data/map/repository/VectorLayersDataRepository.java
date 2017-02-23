package com.teamagam.gimelgimel.data.map.repository;

import com.teamagam.gimelgimel.domain.map.repository.VectorLayersRepository;
import com.teamagam.gimelgimel.domain.messages.entity.contents.VectorLayer;

import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class VectorLayersDataRepository implements VectorLayersRepository {

    private Map<String, VectorLayer> mIdToVectorLayersMap;

    @Inject
    public VectorLayersDataRepository() {
        mIdToVectorLayersMap = new TreeMap<>();
    }

    @Override
    public void put(VectorLayer vectorLayer) {
        mIdToVectorLayersMap.put(vectorLayer.getId(), vectorLayer);
    }

    @Override
    public VectorLayer get(String id) {
        return mIdToVectorLayersMap.get(id);
    }

    @Override
    public boolean contains(String id) {
        return mIdToVectorLayersMap.containsKey(id);
    }
}
