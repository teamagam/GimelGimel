package com.teamagam.gimelgimel.data.map.repository;

import com.teamagam.gimelgimel.domain.map.entities.ViewerCamera;
import com.teamagam.gimelgimel.domain.map.repository.ViewerCameraRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * Created on 11/3/2016.
 * TODO: complete text
 */
@Singleton
public class FrustumRepositoryData implements ViewerCameraRepository {

    private PublishSubject<ViewerCamera> mFrustumSubject;

    @Inject
    public FrustumRepositoryData(){
        mFrustumSubject = PublishSubject.create();
    }

    @Override
    public Observable<ViewerCamera> getObservable() {
        return mFrustumSubject.replay(1).autoConnect();
    }

    @Override
    public void set(ViewerCamera frustum) {
        mFrustumSubject.onNext(frustum);
    }

}
