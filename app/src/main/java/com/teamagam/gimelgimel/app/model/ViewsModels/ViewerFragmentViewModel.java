package com.teamagam.gimelgimel.app.model.ViewsModels;

import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.geometries.entities.PointGeometry;
import com.teamagam.gimelgimel.domain.images.SendImageMessageInteractor;
import com.teamagam.gimelgimel.domain.images.repository.ImagesRepository;
import com.teamagam.gimelgimel.presentation.presenters.SendImageMessagePresenter;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ViewerFragmentViewModel {

    SendImageMessageInteractor mInteractor;
    SendImageMessagePresenter mPresenter;

    // TODO: We have to remove the Presenter classes, it's definitely un-necessary
    public ViewerFragmentViewModel(SendImageMessagePresenter.View view, ImagesRepository repository) {
        mInteractor = new SendImageMessageInteractor(new ThreadExecutor() {
            @Override
            public Scheduler getScheduler() {
                return Schedulers.io();
            }
        }, new PostExecutionThread() {
            @Override
            public Scheduler getScheduler() {
                return AndroidSchedulers.mainThread();
            }
        }, repository);
        mPresenter = new SendImageMessagePresenter(mInteractor, view);
    }

    public void sendImage(String senderId, String imageUrl, double latitude, double longitude) {
        PointGeometry location = new PointGeometry(latitude, longitude);

        mPresenter.sendImageMessage(senderId, imageUrl, location);
    }

    public void sendImage(String senderId, String imageUrl) {
        mPresenter.sendImageMessage(senderId, imageUrl, null);
    }
}
