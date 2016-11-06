package com.teamagam.gimelgimel.domain.map;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.DoInteractor;
import com.teamagam.gimelgimel.domain.map.entities.ViewerCamera;
import com.teamagam.gimelgimel.domain.map.repository.ViewerCameraRepository;

import rx.Observable;

/**
 * Created on 11/3/2016.
 * TODO: complete text
 */
@AutoFactory
public class SaveViewerCameraInteractor extends DoInteractor {

    private ViewerCameraRepository mViewerCameraRepository;
    private ViewerCamera mViewerCamera;

    protected SaveViewerCameraInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided ViewerCameraRepository viewerCameraRepository,
            ViewerCamera viewerCamera
    ) {
        super(threadExecutor);
        mViewerCameraRepository = viewerCameraRepository;
        mViewerCamera = viewerCamera;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return Observable.just(mViewerCamera)
                .filter(o -> o != null)
                .doOnNext(mViewerCameraRepository::set);
    }
}
