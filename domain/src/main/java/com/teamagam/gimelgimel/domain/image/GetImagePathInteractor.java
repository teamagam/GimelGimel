package com.teamagam.gimelgimel.domain.image;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.SyncInteractor;
import com.teamagam.gimelgimel.domain.messages.repository.ImagesRepository;

import rx.Observable;
import rx.Subscriber;

/**
 * Created on 9/22/2016.
 * TODO: complete text
 */
@AutoFactory
public class GetImagePathInteractor extends SyncInteractor<String>{

    private final ImagesRepository mImagesRepository ;

    protected GetImagePathInteractor(@Provided ThreadExecutor threadExecutor,
                                     @Provided PostExecutionThread postExecutionThread,
                                     @Provided ImagesRepository imagesRepository,
                                     Subscriber<String> useCaseSubscriber) {
        super(threadExecutor, postExecutionThread, useCaseSubscriber);
        mImagesRepository = imagesRepository;
    }

    @Override
    protected Observable<String> buildUseCaseObservable() {
        return mImagesRepository.createImageTempPath();
    }
}
