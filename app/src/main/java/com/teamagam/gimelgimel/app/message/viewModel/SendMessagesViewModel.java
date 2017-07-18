package com.teamagam.gimelgimel.app.message.viewModel;

import com.teamagam.gimelgimel.app.common.base.ViewModels.BaseViewModel;
import com.teamagam.gimelgimel.app.message.view.SendMessagesFragment;
import com.teamagam.gimelgimel.domain.messages.QueueImageMessageForSendingInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.QueueTextMessageForSendingInteractorFactory;
import com.teamagam.gimelgimel.domain.utils.TextUtils;
import javax.inject.Inject;

public class SendMessagesViewModel extends BaseViewModel<SendMessagesFragment> {

  @Inject
  QueueTextMessageForSendingInteractorFactory mSendTextInteractorFactory;

  @Inject
  QueueImageMessageForSendingInteractorFactory mSendImageInteractorFactory;

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
      mView.hideKeyboard();
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
    return mText != null && !mText.isEmpty() && !TextUtils.isOnlyWhiteSpaces(mText);
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
}
