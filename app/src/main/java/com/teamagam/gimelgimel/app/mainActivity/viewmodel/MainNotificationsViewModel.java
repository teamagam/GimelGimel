package com.teamagam.gimelgimel.app.mainActivity.viewmodel;

import android.content.Context;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.injectors.scopes.PerActivity;
import com.teamagam.gimelgimel.domain.notifications.DisplayMessageNotificationInteractor;
import com.teamagam.gimelgimel.domain.notifications.DisplayMessageNotificationInteractorFactory;
import javax.inject.Inject;

/**
 * Displaying messages notifications view-model
 */
@PerActivity
public class MainNotificationsViewModel {

  private DisplayMessageNotificationInteractor mInteractor;
  private DisplayMessageNotificationInteractorFactory mInteractorFactory;
  private IMessageNotificationView mView;
  private Context mContext;

  @Inject
  public MainNotificationsViewModel(DisplayMessageNotificationInteractorFactory interactorFactory,
      Context context) {
    mInteractorFactory = interactorFactory;
    mContext = context;
  }

  public void setView(IMessageNotificationView view) {
    mView = view;
  }

  public void start() {
    mInteractor = mInteractorFactory.create(new MessageSendingNotificationDisplayer());
    mInteractor.execute();
  }

  public void stop() {
    mInteractor.unsubscribe();
  }

  public interface IMessageNotificationView {
    void showMessageNotification(String msgText, int msgColor);
  }

  private class MessageSendingNotificationDisplayer
      implements DisplayMessageNotificationInteractor.Displayer {

    private final static int ERROR_COLOR = R.color.message_notification_error;
    private final static int SUCCESS_COLOR = R.color.message_notification_success;
    private final static int SENDING_COLOR = R.color.message_notification_sending;

    private String mSuccessMessage;
    private String mErrorMessage;
    private String mSendingMessage;

    public MessageSendingNotificationDisplayer() {
      mSendingMessage = mContext.getString(R.string.message_notification_sending);
      mErrorMessage = mContext.getString(R.string.message_notification_error);
      mSuccessMessage = mContext.getString(R.string.message_notification_success);
    }

    @Override
    public void displaySending() {
      mView.showMessageNotification(mSendingMessage, SENDING_COLOR);
    }

    @Override
    public void displaySent() {
      mView.showMessageNotification(mSuccessMessage, SUCCESS_COLOR);
    }

    @Override
    public void displayError() {
      mView.showMessageNotification(mErrorMessage, ERROR_COLOR);
    }
  }
}
