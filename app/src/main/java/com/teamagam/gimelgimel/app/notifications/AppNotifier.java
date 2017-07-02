package com.teamagam.gimelgimel.app.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.utils.Constants;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivity;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.features.AlertFeature;
import com.teamagam.gimelgimel.domain.notifications.NotifyOnNewMessageInteractor;
import java.util.Objects;
import javax.inject.Inject;

import static android.support.v4.app.NotificationCompat.PRIORITY_MAX;

public class AppNotifier implements NotifyOnNewMessageInteractor.NotificationDisplayer {

  private static final long VIBRATION_START_DELAY_MS = 0;
  private static final long VIBRATION_DURATION_MS = 1500;
  private static final long[] VIBRATION_PATTERN =
      new long[] { VIBRATION_START_DELAY_MS, VIBRATION_DURATION_MS };

  private final SharedPreferences mSharedPreferences;
  private final Context mContext;
  private final Intent mMainActivityIntent;

  private String mAlertTitle;
  private String mAlertSummary;
  private String mAlertText;
  private String mMessageTitle;
  private String mMessageSummary;
  private String mMessageText;
  private String mShouldVibratePrefKey;
  private String mRingtonePrefKey;

  private NotificationManager mNotificationManager;

  @Inject
  public AppNotifier(Context context) {
    mContext = context;
    initStringResources(mContext);
    mNotificationManager =
        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    mMainActivityIntent = new Intent(context, MainActivity.class);
    mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
  }

  @Override
  public void notifyNewMessage(ChatMessage chatMessage) {
    if (chatMessage.contains(AlertFeature.class)) {
      notifyAlert();
    } else {
      notifyMessage();
    }
  }

  private void initStringResources(Context context) {
    initAlertStrings(context);
    initMessageStrings(context);
    initPrefKeys(context);
  }

  private void initAlertStrings(Context context) {
    mAlertTitle = context.getString(R.string.alert_notification_title);
    mAlertSummary = context.getString(R.string.alert_notification_summary);
    mAlertText = context.getString(R.string.alert_notification_text);
  }

  private void initMessageStrings(Context context) {
    mMessageTitle = context.getString(R.string.message_notification_title);
    mMessageSummary = context.getString(R.string.message_notification_summary);
    mMessageText = context.getString(R.string.message_notification_text);
  }

  private void initPrefKeys(Context context) {
    mShouldVibratePrefKey = context.getString(R.string.pref_notifications_vibration_key);
    mRingtonePrefKey = context.getString(R.string.pref_notifications_ringtone_key);
  }

  private void notifyAlert() {
    notify(R.drawable.ic_alert_notification, mAlertTitle, mAlertSummary, mAlertText,
        Constants.ALERTS_NOTIFICATION_ID);
  }

  private void notifyMessage() {
    notify(R.drawable.ic_lambda_logo, mMessageTitle, mMessageSummary, mMessageText,
        Constants.MESSAGES_NOTIFICATION_ID);
  }

  private void notify(int smallIconResId,
      String notificationTitle,
      String notificationSummary,
      String notificationText,
      int notificationId) {

    Notification notification =
        createNotification(smallIconResId, notificationTitle, notificationSummary,
            notificationText);
    mNotificationManager.notify(notificationId, notification);
  }

  private Notification createNotification(int smallIconResId,
      String notificationTitle,
      String notificationSummary,
      String notificationText) {
    NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext).setContentIntent(
        PendingIntent.getActivity(mContext, 0, mMainActivityIntent,
            PendingIntent.FLAG_UPDATE_CURRENT))
        .setPriority(PRIORITY_MAX)
        .setAutoCancel(true)
        .setSmallIcon(smallIconResId)
        .setStyle(createStyle(notificationTitle, notificationSummary, notificationText))
        .setContentTitle(notificationTitle)
        .setContentText(notificationText)
        .setSound(getPreferredRingtoneUri());

    if (shouldVibrate()) {
      builder.setVibrate(VIBRATION_PATTERN);
    }

    return builder.build();
  }

  private NotificationCompat.Style createStyle(String title, String summary, String text) {
    NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
    bigTextStyle.setBigContentTitle(title);
    bigTextStyle.setSummaryText(summary);
    bigTextStyle.bigText(text);

    return bigTextStyle;
  }

  private Uri getPreferredRingtoneUri() {
    String possibleUri = mSharedPreferences.getString(mRingtonePrefKey, "");
    return !Objects.equals(possibleUri, "") ? Uri.parse(possibleUri) : null;
  }

  private boolean shouldVibrate() {
    return mSharedPreferences.getBoolean(mShouldVibratePrefKey, true);
  }
}
