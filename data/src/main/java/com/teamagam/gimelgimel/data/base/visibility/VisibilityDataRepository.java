package com.teamagam.gimelgimel.data.base.visibility;

import com.teamagam.gimelgimel.data.base.repository.SubjectRepository;
import com.teamagam.gimelgimel.domain.base.visiblity.VisibilityChange;
import com.teamagam.gimelgimel.domain.base.visiblity.VisibilityRepository;
import io.reactivex.Observable;
import java.util.HashMap;
import java.util.Map;

public abstract class VisibilityDataRepository<T extends VisibilityChange>
    implements VisibilityRepository<T> {

  private final SubjectRepository<T> mVisibilityChangesLogRepository;
  private final Map<String, Boolean> mIdToVisibilityMap;

  public VisibilityDataRepository() {
    mVisibilityChangesLogRepository = SubjectRepository.createReplayAll();
    mIdToVisibilityMap = new HashMap<>();
  }

  @Override
  public Observable<T> getChangesObservable() {
    return mVisibilityChangesLogRepository.getObservable();
  }

  @Override
  public void addChange(T change) {
    mVisibilityChangesLogRepository.add(change);
    mIdToVisibilityMap.put(change.getId(), change.isVisible());
  }

  @Override
  public boolean isVisible(String id) {
    return mIdToVisibilityMap.get(id);
  }
}
