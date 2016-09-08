package com.teamagam.gimelgimel.presentation.presenters;

import com.teamagam.gimelgimel.domain.messages.entity.MessageGeo;
import com.teamagam.gimelgimel.presentation.presenters.base.AbstractPresenter;
import com.teamagam.gimelgimel.presentation.presenters.base.BaseView;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

public class SendGeoMessagePresenter extends AbstractPresenter<MessageGeo> {

    private List<WeakReference<View>> mViewWRList;

    @Inject
    public SendGeoMessagePresenter() {
        mViewWRList = new LinkedList<>();
    }

    public void addView(View view) {
        mViewWRList.add(new WeakReference<>(view));
    }

    private boolean isViewExists(WeakReference<View> viewWR) {
        return viewWR != null && viewWR.get() != null;
    }

    @Override
    public void onNext(MessageGeo message) {
        Observable.from(mViewWRList)
                .filter(this::isViewExists)
                .map(WeakReference::get)
                .doOnNext(BaseView::hideProgress)
                .doOnNext(view -> view.showMessage(message))
                .subscribe();
    }

    public interface View extends BaseView {

        void showMessage(MessageGeo messageGeo);

    }

}
