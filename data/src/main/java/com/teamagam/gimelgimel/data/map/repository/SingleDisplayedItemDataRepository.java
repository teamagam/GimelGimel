package com.teamagam.gimelgimel.data.map.repository;

import com.teamagam.gimelgimel.data.base.repository.ReplayRepository;
import com.teamagam.gimelgimel.domain.map.repository.SingleDisplayedItemRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

@Singleton
public class SingleDisplayedItemDataRepository<T> implements SingleDisplayedItemRepository<T> {

    private ReplayRepository<DisplayEvent> mInnerRepo;
    private T mItem;

    @Inject
    public SingleDisplayedItemDataRepository() {
        mInnerRepo = ReplayRepository.createReplayCount(1);
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
