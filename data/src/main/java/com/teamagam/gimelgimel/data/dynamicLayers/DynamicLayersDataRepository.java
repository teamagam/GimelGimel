package com.teamagam.gimelgimel.data.dynamicLayers;

import com.teamagam.gimelgimel.data.base.repository.SubjectRepository;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.dynamicLayers.repository.DynamicLayersRepository;
import io.reactivex.Observable;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DynamicLayersDataRepository implements DynamicLayersRepository {

  private Map<String, DynamicLayer> mDynamicLayersMap;
  private SubjectRepository<DynamicLayer> mSubjectRepository;

  @Inject
  public DynamicLayersDataRepository() {
    mDynamicLayersMap = new HashMap<>();
    mSubjectRepository = SubjectRepository.createReplayAll();
  }

  @Override
  public void put(DynamicLayer dynamicLayer) {
    mDynamicLayersMap.put(dynamicLayer.getId(), dynamicLayer);
    mSubjectRepository.add(dynamicLayer);
  }

  @Override
  public DynamicLayer getById(String id) {
    if (mDynamicLayersMap.containsKey(id)) {
      return mDynamicLayersMap.get(id);
    }
    throw new RuntimeException(String.format("DynamicLayer with id %s does not exist.", id));
  }

  @Override
  public boolean contains(String id) {
    return mDynamicLayersMap.containsKey(id);
  }

  @Override
  public Observable<DynamicLayer> getObservable() {
    return mSubjectRepository.getObservable();
  }
}
