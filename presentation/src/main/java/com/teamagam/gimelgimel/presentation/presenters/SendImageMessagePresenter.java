package com.teamagam.gimelgimel.presentation.presenters;

import com.teamagam.gimelgimel.domain.messages.entity.MessageImage;
import com.teamagam.gimelgimel.presentation.presenters.base.BaseView;
import com.teamagam.gimelgimel.presentation.rx.subscribers.BaseSubscriber;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

public class SendImageMessagePresenter {

    private List<View> mViews;

    public SendImageMessagePresenter() {
        mViews = new ArrayList<>();
    }

    public Subscriber createSubscriber() {
        return new SendImageSubscriber();
    }

    public void addView(View view) {
        mViews.add(view);
    }

    public void removeView(View view) {
        mViews.remove(view);
    }

    public interface View extends BaseView {
        void displaySuccessfulMessageStatus();
    }

    private class SendImageSubscriber extends  BaseSubscriber<MessageImage> {
        @Override
        public void onError(Throwable e) {
            for (View view: mViews) {
                view.showError(e.getMessage());
            }
        }

        @Override
        public void onNext(MessageImage message) {
            for (View view : mViews) {
                view.displaySuccessfulMessageStatus();
            }
        }
    }
}
