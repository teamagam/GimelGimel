package com.teamagam.gimelgimel.presentation.presenters;

import com.teamagam.gimelgimel.domain.messages.SendMessageInteractor;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.entity.MessageText;
import com.teamagam.gimelgimel.presentation.interfaces.PresenterSharedPreferences;
import com.teamagam.gimelgimel.presentation.presenters.base.AbstractPresenter;
import com.teamagam.gimelgimel.presentation.presenters.base.BaseView;
import com.teamagam.gimelgimel.presentation.rx.subscribers.BaseSubscriber;
import com.teamagam.gimelgimel.presentation.scopes.PerFragment;

import java.util.List;

import javax.inject.Inject;

@PerFragment
public class SendMessagePresenter extends AbstractPresenter {

    private final PresenterSharedPreferences mSharedPreferences;
    private List<View> mViews;

    private SendMessageInteractor mSendMessageInteractor;

    @Inject
    public SendMessagePresenter(SendMessageInteractor sendMessageInteractor, PresenterSharedPreferences
            sharedPreferences) {
        super();
        mSendMessageInteractor = sendMessageInteractor;
        mSharedPreferences = sharedPreferences;
    }

    public void setView(View view) {
        mViews.add(view);
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void stop() {
    }

    @Override
    public void destroy() {
    }

    public interface View extends BaseView {
        void displayMessageStatus();
    }

}
