package com.teamagam.gimelgimel.app.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.utils.Constants;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivity;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.features.AlertFeature;
import com.teamagam.gimelgimel.domain.notifications.NotifyOnNewMessageInteractor;

import static android.support.v4.app.NotificationCompat.PRIORITY_MAX;

public class AppNotifier implements NotifyOnNewMessageInteractor.NotificationDisplayer {

  private static final long VIBRATION_START_DELAY = 0;
  private static final long VIBRATION_DURATION = 1500;
  private static final long[] VIBRATION_PATTERN =
      new long[] { VIBRATION_START_DELAY, VIBRATION_DURATION };

  private String mAlertTitle;
  private String mAlertSummary;
  private String mAlertText;
  private String mMessageTitle;
  private String mMessageSummary;
  private String mMessageText;
  private NotificationManager mNotificationManager;
  private NotificationCompat.Builder mBuilder;

  public AppNotifier(Context context) {
    initializeNotificationStrings(context);
    mNotificationManager =
        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    Intent resultIntent = new Intent(context, MainActivity.class);

    mBuilder = new NotificationCompat.Builder(context).setContentIntent(
        PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT))
        .setVibrate(VIBRATION_PATTERN)
        .setPriority(PRIORITY_MAX)
        .setAutoCancel(true);
  }

  @Override
  public void notifyNewMessage(ChatMessage chatMessage) {
    if (chatMessage.contains(AlertFeature.class)) {
      notifyAlert();
    } else {
      notifyMessage();
    }
  }

  private void initializeNotificationStrings(Context context) {
    initializeAlertStrings(context);
    initializeMessageStrings(context);
  }

  private void initializeAlertStrings(Context context) {
    mAlertTitle = context.getString(R.string.alert_notification_title);
    mAlertSummary = context.getString(R.string.alert_notification_summary);
    mAlertText = context.getString(R.string.alert_notification_text);
  }

  private void initializeMessageStrings(Context context) {
    mMessageTitle = context.getString(R.string.message_notification_title);
    mMessageSummary = context.getString(R.string.message_notification_summary);
    mMessageText = context.getString(R.string.message_notification_text);
  }

  private void notifyAlert() {
    mBuilder.setSmallIcon(R.drawable.ic_alert_notification);
    mBuilder.setStyle(createStyle(mAlertTitle, mAlertSummary, mAlertText));
    mBuilder.setContentTitle(mAlertTitle);
    mBuilder.setContentText(mAlertText);

    Notification n = mBuilder.build();
    mNotificationManager.notify(Constants.ALERTS_NOTIFICATION_ID, n);
  }

  private void notifyMessage() {
    mBuilder.setSmallIcon(R.drawable.ic_lambda_logo);
    mBuilder.setStyle(createStyle(mMessageTitle, mMessageSummary, mMessageText));
    mBuilder.setContentTitle(mMessageTitle);
    mBuilder.setContentText(mMessageText);

    Notification n = mBuilder.build();
    mNotificationManager.notify(Constants.MESSAGES_NOTIFICATION_ID, n);
  }

  private NotificationCompat.Style createStyle(String title, String summary, String text) {
    NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
    bigTextStyle.setBigContentTitle(title);
    bigTextStyle.setSummaryText(summary);
    bigTextStyle.bigText(text);

    return bigTextStyle;
  }
}
