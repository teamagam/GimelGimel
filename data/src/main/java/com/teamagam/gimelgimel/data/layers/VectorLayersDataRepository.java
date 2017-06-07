package com.teamagam.gimelgimel.data.layers;

import com.teamagam.gimelgimel.domain.layers.repository.VectorLayersRepository;
import com.teamagam.gimelgimel.domain.messages.entity.contents.VectorLayerContent;
import java.util.Map;
import java.util.TreeMap;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class VectorLayersDataRepository implements VectorLayersRepository {

  private Map<String, VectorLayerContent> mIdToVectorLayersMap;

  @Inject
  public VectorLayersDataRepository() {
    mIdToVectorLayersMap = new TreeMap<>();
  }

  @Override
  public void put(VectorLayerContent vectorLayerContent) {
    mIdToVectorLayersMap.put(vectorLayerContent.getId(), vectorLayerContent);
  }

  @Override
  public VectorLayerContent get(String id) {
    return mIdToVectorLayersMap.get(id);
  }

  @Override
  public boolean contains(String id) {
    return mIdToVectorLayersMap.containsKey(id);
  }
}
