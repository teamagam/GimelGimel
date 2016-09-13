package com.teamagam.gimelgimel.presentation.presenters;

import com.teamagam.gimelgimel.domain.images.SendImageMessageInteractor;
import com.teamagam.gimelgimel.domain.messages.entity.MessageImage;
import com.teamagam.gimelgimel.presentation.presenters.base.BaseView;
import com.teamagam.gimelgimel.presentation.rx.subscribers.BaseSubscriber;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;

public class SendImageMessagePresenter {

    SendImageMessageInteractor mInteractor;

    private List<View> mViews;

    public SendImageMessagePresenter(SendImageMessageInteractor interactor) {
        mInteractor = interactor;
        mViews = new ArrayList<>();
    }

    public void sendImage(MessageImage messageImage, String imagePath) {
        Subscriber subscriber = createSubscriber();
        mInteractor.sendImageMessage(subscriber, messageImage, imagePath);
    }

    private Subscriber createSubscriber() {
        return new SendImageSubscriber();
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

    private class SendImageSubscriber extends  BaseSubscriber<MessageImage> {
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
    }
}
