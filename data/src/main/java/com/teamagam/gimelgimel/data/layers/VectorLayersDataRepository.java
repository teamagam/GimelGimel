package com.teamagam.gimelgimel.data.layers;

import com.teamagam.gimelgimel.data.base.repository.SubjectRepository;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayer;
import com.teamagam.gimelgimel.domain.layers.repository.VectorLayersRepository;
import io.reactivex.Observable;
import java.util.Map;
import java.util.TreeMap;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class VectorLayersDataRepository implements VectorLayersRepository {

  private Map<String, VectorLayer> mIdToVectorLayersMap;
  private SubjectRepository<VectorLayer> mSubject;

  @Inject
  public VectorLayersDataRepository() {
    mIdToVectorLayersMap = new TreeMap<>();
    mSubject = SubjectRepository.createReplayAll();
  }

  @Override
  public Observable<VectorLayer> getVectorLayersObservable() {
    return mSubject.getObservable();
  }

  @Override
  public void put(VectorLayer vectorLayer) {
    mIdToVectorLayersMap.put(vectorLayer.getId(), vectorLayer);
    mSubject.add(vectorLayer);
  }

  @Override
  public VectorLayer get(String id) {
    return mIdToVectorLayersMap.get(id);
  }

  @Override
  public boolean contains(String id) {
    return mIdToVectorLayersMap.containsKey(id);
  }

  @Override
  public boolean isOutdatedVectorLayer(VectorLayer vectorLayer) {
    String id = vectorLayer.getId();
    return contains(id)
        && get(id).getVersion() >= vectorLayer.getVersion();
  }
}
