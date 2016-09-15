
package com.teamagam.gimelgimel.app.message.viewModel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.teamagam.gimelgimel.BR;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.app.viewModels.ViewModel;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.messages.SendMessageInteractor;
import com.teamagam.gimelgimel.domain.messages.entity.MessageText;
import com.teamagam.gimelgimel.domain.messages.interfaces.UserPreferences;
import com.teamagam.gimelgimel.presentation.presenters.SendMessagePresenter;

import javax.inject.Inject;


public class SendMessageViewModel extends BaseObservable implements ViewModel {

    @Inject
    SendMessagePresenter sendMessagePresenter;

    @Inject
    UserPreferences mPref;

    @Inject
    SendMessageInteractor mSendMessageInteractor;

    protected Logger sLogger = LoggerFactory.create();

    private String mText;

    private IViewDismisser mView;

    @Inject
    public SendMessageViewModel() {
    }

    public void setView(IViewDismisser view) {
        mView = view;
    }


    public void onPositiveClicked() {
        sLogger.userInteraction("Clicked OK");

        String userName = mPref.getSenderName();

        MessageText msg = new MessageText(userName, mText);
        mSendMessageInteractor.sendMessage(msg, sendMessagePresenter.createSubscriber());

        mView.dismiss();
    }

    @Bindable
    public boolean isInputNotValid() {
        return mText == null || mText.isEmpty();
    }


    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
        notifyPropertyChanged(BR.inputNotValid);
    }


    public interface IViewDismisser {
        void dismiss();
    }
}



