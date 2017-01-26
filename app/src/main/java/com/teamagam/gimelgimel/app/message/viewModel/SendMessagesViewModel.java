package com.teamagam.gimelgimel.app.message.viewModel;

import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;
import com.teamagam.gimelgimel.app.message.view.SendMessagesFragment;
import com.teamagam.gimelgimel.domain.messages.SendImageMessageInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.SendTextMessageInteractorFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.inject.Inject;

public class SendMessagesViewModel extends BaseViewModel<SendMessagesFragment> {

    @Inject
    SendTextMessageInteractorFactory mSendTextInteractorFactory;

    @Inject
    SendImageMessageInteractorFactory mSendImageInteractorFactory;

    private String mText;

    @Inject
    public SendMessagesViewModel() {
    }

    @Override
    public void start() {
        super.start();

        mView.setSendTextFabClickable(isTextValid());
    }

    public void onSendTextFabClicked() {
        if (isTextValid()) {
            mSendTextInteractorFactory.create(mText).execute();
        }

        clearText();
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
        mView.setSendTextFabClickable(isTextValid());

        notifyChange();
    }

    public boolean isTextValid() {
        return mText != null && !mText.isEmpty() && isText(mText);
    }

    public void onSendImageButtonClicked() {
        mView.takePicture();
    }

    public void sendImage(String imagePath) {
        long imageTime = System.currentTimeMillis();

        mSendImageInteractorFactory.create(imageTime, imagePath).execute();
    }

    private void clearText() {
        setText("");
    }

    private boolean isText(String mText) {
        String stringWithSpacesOnly = "\\S";
        Pattern p = Pattern.compile(stringWithSpacesOnly);
        Matcher m = p.matcher(mText);

        return m.find();
    }
}
