package com.teamagam.gimelgimel.data.layers;

import com.teamagam.gimelgimel.data.base.repository.SubjectRepository;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayerVisibilityChange;
import com.teamagam.gimelgimel.domain.layers.repository.VectorLayersVisibilityRepository;
import javax.inject.Inject;
import javax.inject.Singleton;
import io.reactivex.Observable;

@Singleton
public class VectorLayersVisibilityDataRepository implements VectorLayersVisibilityRepository {

  private final SubjectRepository<VectorLayerVisibilityChange> mVisibilityChangesLogRepository;

  @Inject
  public VectorLayersVisibilityDataRepository() {
    mVisibilityChangesLogRepository = SubjectRepository.createReplayAll();
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
