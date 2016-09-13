package com.teamagam.gimelgimel.presentation.presenters;

import com.teamagam.gimelgimel.domain.messages.entity.MessageGeo;
import com.teamagam.gimelgimel.presentation.presenters.base.AbstractPresenter;
import com.teamagam.gimelgimel.presentation.presenters.base.BaseView;
import com.teamagam.gimelgimel.presentation.presenters.base.SimpleSubscriber;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Subscriber;

@Singleton
public class SendGeoMessagePresenter extends AbstractPresenter<MessageGeo> {

    private List<WeakReference<View>> mViewWRList;

    @Inject
    public SendGeoMessagePresenter() {
        mViewWRList = new LinkedList<>();
    }

    public void addView(View view) {
        mViewWRList.add(new WeakReference<>(view));
    }

    public void removeView(View view) {
        for (WeakReference<View> viewWR : mViewWRList) {
            if (view.equals(viewWR.get())) {
                mViewWRList.remove(viewWR);
                return;
            }
        }
    }

    @Override
    public Subscriber<MessageGeo> createSubscriber() {
        return new MessageGeoSubscriber();
    }

    public void sendMessage(MessageGeo message) {
//        mGeometryInteractor.sendGeoMessageEntity(message, createSubscriber());
    }

    private class MessageGeoSubscriber extends SimpleSubscriber<MessageGeo> {
        @Override
        public void onNext(final MessageGeo messageGeo) {
            SendGeoMessagePresenter.this.getObservableViews(mViewWRList)
                    .doOnNext(BaseView::hideProgress)
                    .doOnNext(view -> view.showMessage(messageGeo))
                    .subscribe();
        }
    }

    public interface View extends BaseView {

        void showMessage(MessageGeo messageGeo);

    }

}
