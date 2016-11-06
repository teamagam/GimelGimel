package com.teamagam.gimelgimel.domain.map;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.DoInteractor;
import com.teamagam.gimelgimel.domain.map.repository.ViewerCameraRepository;

import rx.Observable;

@AutoFactory
public class LoadViewerCameraInteractor extends DoInteractor {

    private final ViewerCameraController mViewerCameraController;
    private final ViewerCameraRepository mViewerCameraRepository;

    protected LoadViewerCameraInteractor(
            @Provided ThreadExecutor threadExecutor, ViewerCameraController viewerCameraController,
            @Provided ViewerCameraRepository viewerCameraRepository) {
        super(threadExecutor);
        mViewerCameraController = viewerCameraController;
        mViewerCameraRepository = viewerCameraRepository;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return mViewerCameraRepository.getObservable()
                .doOnNext(mViewerCameraController::setViewerCamera);
    }
}
