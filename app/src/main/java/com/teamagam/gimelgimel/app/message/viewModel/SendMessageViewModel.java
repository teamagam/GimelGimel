
package com.teamagam.gimelgimel.app.message.viewModel;

import android.databinding.Bindable;

import com.teamagam.gimelgimel.BR;

import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.app.viewModels.BaseViewModel;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.domain.messages.SendTextMessageInteractor;
import com.teamagam.gimelgimel.domain.messages.SendTextMessageInteractorFactory;

import javax.inject.Inject;


public class SendMessageViewModel extends BaseViewModel<SendMessageViewModel.IViewDismisser> {

    @Inject
    SendTextMessageInteractorFactory mInteractorFactory;

    protected Logger sLogger = LoggerFactory.create();

    private String mText;

    @Inject
    public SendMessageViewModel() {
    }

    public void onPositiveClicked() {
        sLogger.userInteraction("Clicked OK");

        executeInteractor();

        mView.dismiss();
    }

    private void executeInteractor() {
        SendTextMessageInteractor interactor = mInteractorFactory.create(mText);
        interactor.execute();
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



