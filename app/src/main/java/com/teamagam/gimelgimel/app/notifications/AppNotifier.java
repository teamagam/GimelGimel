package com.teamagam.gimelgimel.app.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.utils.Constants;
import com.teamagam.gimelgimel.app.common.utils.Environment;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivity;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.features.AlertFeature;
import com.teamagam.gimelgimel.domain.notifications.NotifyOnNewMessageInteractor;

import static android.support.v4.app.NotificationCompat.PRIORITY_MAX;

public class AppNotifier implements NotifyOnNewMessageInteractor.NotificationDisplayer {

  private static final String ALERT_TITLE = "New Alert!";
  private static final String ALERT_SUMMARY = "Alert";
  private static final String ALERT_TEXT = "You have a new alert!";
  private static final String MESSAGE_TITLE = "New Message";
  private static final String MESSAGE_SUMMARY = "Message";
  private static final String MESSAGE_TEXT = "New incoming message";
  private static final long[] VIBRATION_PATTERN = new long[] { 100, 1500, 500 };

  private Environment mEnvironment;
  private NotificationManager mNotificationManager;
  private NotificationCompat.Builder mBuilder;

  public AppNotifier(Context context, Environment env) {
    mNotificationManager =
        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    Intent resultIntent = new Intent(context, MainActivity.class);

    mEnvironment = env;
    mBuilder = new NotificationCompat.Builder(context).setContentIntent(
        PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT))
        .setVibrate(VIBRATION_PATTERN)
        .setPriority(PRIORITY_MAX)
        .setAutoCancel(true);
  }

  @Override
  public void notifyNewMessage(ChatMessage chatMessage) {
    if (shouldNotify()) {
      showNotification(chatMessage);
    }
  }

  private boolean shouldNotify() {
    return !mEnvironment.isAppOnForeground();
  }

  private void showNotification(ChatMessage chatMessage) {
    if (chatMessage.contains(AlertFeature.class)) {
      notifyAlert();
    } else {
      notifyMessage();
    }
  }

  private void notifyAlert() {
    mBuilder.setSmallIcon(R.drawable.ic_alert_notification);
    mBuilder.setStyle(createStyle(ALERT_TITLE, ALERT_SUMMARY, ALERT_TEXT));
    mBuilder.setContentTitle(ALERT_TITLE);
    mBuilder.setContentText(ALERT_TEXT);

    Notification n = mBuilder.build();
    mNotificationManager.notify(Constants.ALERTS_NOTIFICATION_ID, n);
  }

  private void notifyMessage() {
    mBuilder.setSmallIcon(R.drawable.ic_lambda_logo);
    mBuilder.setStyle(createStyle(MESSAGE_TITLE, MESSAGE_SUMMARY, MESSAGE_TEXT));
    mBuilder.setContentTitle(MESSAGE_TITLE);
    mBuilder.setContentText(MESSAGE_TEXT);

    Notification n = mBuilder.build();
    mNotificationManager.notify(Constants.MESSAGES_NOTIFICATION_ID, n);
  }

  private NotificationCompat.Style createStyle(String title, String summary, String text) {
    NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
    bigTextStyle.setBigContentTitle(summary);
    bigTextStyle.setSummaryText(summary);
    bigTextStyle.bigText(text);

    return bigTextStyle;
  }
}
