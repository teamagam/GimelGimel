package com.teamagam.gimelgimel.domain.notifications.entity;

/**
 * Data class for message notification
 */
public class MessageNotification {

  public static final int SENDING = 1;
  public static final int SUCCESS = 2;
  public static final int ERROR = 3;
  private final int mState;

  private MessageNotification(int state) {
    mState = state;
  }

  public static MessageNotification createSendingNotification() {
    return new MessageNotification(SENDING);
  }

  public static MessageNotification createSuccessNotification() {
    return new MessageNotification(SUCCESS);
  }

  public static MessageNotification createErrorNotification() {
    return new MessageNotification(ERROR);
  }

  public int getState() {
    return mState;
  }
}
