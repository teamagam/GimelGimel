package com.teamagam.gimelgimel.presentation.presenters;

import com.teamagam.gimelgimel.domain.messages.entity.MessageImage;
import com.teamagam.gimelgimel.presentation.presenters.base.BaseView;
import com.teamagam.gimelgimel.presentation.rx.subscribers.BaseSubscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

public class SendImageMessagePresenter extends BaseSubscriber<MessageImage> {

    private List<View> mViews;

    @Inject
    public SendImageMessagePresenter() {
        mViews = new ArrayList<>();
    }

    @Override
    public void onError(Throwable e) {
        Observable.from(mViews)
                .doOnNext(view -> view.showError(e.getMessage()))
                .subscribe();
    }

    @Override
    public void onNext(MessageImage message) {
        Observable.from(mViews)
                .doOnNext(View::displayMessageStatus)
                .subscribe();
    }

    public void addView(View view) {
        mViews.add(view);
    }

    public void removeView(View view) {
        mViews.remove(view);
    }

    public interface View extends BaseView {
        void displayMessageStatus();
    }
}
