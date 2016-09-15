package com.teamagam.gimelgimel.presentation.presenters;

import com.teamagam.gimelgimel.domain.geometries.SendGeoMessageInteractor;
import com.teamagam.gimelgimel.domain.messages.entity.MessageGeo;
import com.teamagam.gimelgimel.presentation.presenters.base.AbstractPresenter;
import com.teamagam.gimelgimel.presentation.presenters.base.BaseView;
import com.teamagam.gimelgimel.presentation.presenters.base.SimpleSubscriber;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Subscriber;

@Singleton
public class SendGeoMessagePresenter extends AbstractPresenter<SendGeoMessagePresenter.View, MessageGeo> {

    @Inject
    SendGeoMessageInteractor mGeometryInteractor;

    @Inject
    public SendGeoMessagePresenter() {
        super();
    }

    @Override
    public Subscriber<MessageGeo> createSubscriber() {
        return new MessageGeoSubscriber();
    }

    public void sendMessage(MessageGeo message) {
        mGeometryInteractor.sendGeoMessageEntity(message, createSubscriber());
    }

    private class MessageGeoSubscriber extends SimpleSubscriber<MessageGeo> {
        @Override
        public void onNext(final MessageGeo messageGeo) {
            SendGeoMessagePresenter.super.getObservableViews()
                    .doOnNext(BaseView::hideProgress)
                    .doOnNext(view -> view.showMessage(messageGeo))
                    .subscribe();
        }
    }

    public interface View extends BaseView {

        void showMessage(MessageGeo messageGeo);

    }

}
