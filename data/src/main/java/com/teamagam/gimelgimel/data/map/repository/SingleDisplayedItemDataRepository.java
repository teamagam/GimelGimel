package com.teamagam.gimelgimel.data.map.repository;

import com.teamagam.gimelgimel.data.base.repository.SubjectRepository;
import com.teamagam.gimelgimel.domain.map.repository.SingleDisplayedItemRepository;
import javax.inject.Inject;
import javax.inject.Singleton;
import io.reactivex.Observable;

@Singleton
public class SingleDisplayedItemDataRepository<T> implements SingleDisplayedItemRepository<T> {

  private SubjectRepository<DisplayEvent> mInnerRepo;
  private T mItem;

  @Inject
  public SingleDisplayedItemDataRepository() {
    mInnerRepo = SubjectRepository.createReplayCount(1);
    mItem = null;
  }

  @Override
  public Observable<DisplayEvent> getDisplayEventsObservable() {
    return mInnerRepo.getObservable();
  }

  @Override
  public T getCurrentDisplayedItem() {
    return mItem;
  }

  @Override
  public void setCurrentDisplayedItem(T item) {
    mItem = item;
    mInnerRepo.add(mItem == null ? DisplayEvent.HIDE : DisplayEvent.DISPLAY);
  }
}
