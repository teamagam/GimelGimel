package com.teamagam.gimelgimel.app.notifications;

import android.content.Context;
import android.support.v4.app.NotificationCompat;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.notifications.NotifyOnNewMessageInteractor;

public class AppNotifier implements NotifyOnNewMessageInteractor.NotificationDisplayer {

  private NotificationCompat.Builder mBuilder;

  public AppNotifier(Context context) {
    mBuilder = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.notification_icon);
  }

  @Override
  public void notifyNewMessage(ChatMessage chatMessage) {

  }
}
