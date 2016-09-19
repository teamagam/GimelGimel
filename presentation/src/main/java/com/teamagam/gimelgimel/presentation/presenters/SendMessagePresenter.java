package com.teamagam.gimelgimel.presentation.presenters;

import com.teamagam.gimelgimel.domain.messages.entity.MessageText;
import com.teamagam.gimelgimel.presentation.presenters.base.AbstractPresenter;
import com.teamagam.gimelgimel.presentation.presenters.base.BaseView;
import com.teamagam.gimelgimel.presentation.rx.subscribers.BaseSubscriber;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Subscriber;

@Singleton
public class SendMessagePresenter extends AbstractPresenter<SendMessagePresenter.View, MessageText> {


    @Inject
    public SendMessagePresenter() {
        super();
    }

    @Override
    public Subscriber<MessageText> createSubscriber() {
        return new BaseSubscriber<MessageText>() {
            @Override
            public void onNext(MessageText messageText) {
                getObservableViews()
                        .doOnNext(View::displayMessageStatus)
                        .subscribe();
            }
        };
    }

    public interface View extends BaseView {
        void displayMessageStatus();
    }
}
