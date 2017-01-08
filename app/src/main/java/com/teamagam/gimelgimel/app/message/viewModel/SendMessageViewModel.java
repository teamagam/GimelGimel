package com.teamagam.gimelgimel.app.message.viewModel;

import android.databinding.Bindable;

import com.teamagam.gimelgimel.BR;
import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;
import com.teamagam.gimelgimel.app.common.logging.AppLogger;
import com.teamagam.gimelgimel.app.common.logging.AppLoggerFactory;
import com.teamagam.gimelgimel.domain.messages.SendTextMessageInteractor;
import com.teamagam.gimelgimel.domain.messages.SendTextMessageInteractorFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

public class SendMessageViewModel extends BaseViewModel<SendMessageViewModel.IViewDismisser> {
    protected AppLogger sLogger = AppLoggerFactory.create(this.getClass());
    @Inject
    SendTextMessageInteractorFactory mInteractorFactory;
    protected String mText;

    public void onPositiveClick() {
        sLogger.userInteraction("Clicked OK");

        executeInteractor();

        mView.dismiss();
    }

    protected void executeInteractor() {
        SendTextMessageInteractor interactor = mInteractorFactory.create(mText);
        interactor.execute();
    }

    @Bindable
    public boolean isInputNotValid() {
        return mText == null || mText.isEmpty() || !isText(mText);
    }

    private boolean isText(String mText) {
        Pattern p = Pattern.compile("\\S");
        Matcher m = p.matcher(mText);
        return m.find();
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
