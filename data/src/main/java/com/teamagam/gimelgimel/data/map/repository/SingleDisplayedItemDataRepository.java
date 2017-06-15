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
  public T getItem() {
    return mItem;
  }

  @Override
  public void setItem(T item) {
    if (item == null) {
      clear();
      return;
    }
    mItem = item;
    mInnerRepo.add(DisplayEvent.DISPLAY);
  }

  @Override
  public void clear() {
    mItem = null;
    mInnerRepo.add(DisplayEvent.HIDE);
  }
}
