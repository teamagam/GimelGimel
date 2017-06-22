package com.teamagam.gimelgimel.app.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.utils.Constants;
import com.teamagam.gimelgimel.app.mainActivity.view.MainActivity;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.features.AlertFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.GeoFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.ImageFeature;
import com.teamagam.gimelgimel.domain.messages.entity.features.TextFeature;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.MessageFeatureVisitor;
import com.teamagam.gimelgimel.domain.notifications.NotifyOnNewMessageInteractor;
import java.util.ArrayList;
import java.util.List;

public class AppNotifier implements NotifyOnNewMessageInteractor.NotificationDisplayer {

  private Context mContext;
  private NotificationManager mNotificationManager;
  private NotificationCompat.Builder mBuilder;

  public AppNotifier(Context context) {
    mContext = context;
    mNotificationManager =
        (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
    Intent resultIntent = new Intent(mContext, MainActivity.class);

    mBuilder = new NotificationCompat.Builder(mContext).setSmallIcon(R.drawable.ic_lambda_logo)
        .setContentIntent(
            PendingIntent.getActivity(mContext, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT))
        .setAutoCancel(true);
  }

  @Override
  public void notifyNewMessage(ChatMessage chatMessage) {
    if(chatMessage.contains(AlertFeature.class)) {
      notifyAlert(chatMessage);
    } else {
      notifyMessage(chatMessage);
    }
  }

  private void notifyAlert(ChatMessage chatMessage) {
    mBuilder.setStyle(createAlertStyle(chatMessage));

    Notification n = mBuilder.build();
    mNotificationManager.notify(Constants.MESSAGES_NOTIFICATION_ID, n);
  }

  private void notifyMessage(ChatMessage chatMessage) {
    mBuilder.setStyle(createMessageStyle(chatMessage));

    Notification n = mBuilder.build();
    mNotificationManager.notify(Constants.MESSAGES_NOTIFICATION_ID, n);
  }

  private NotificationCompat.Style createAlertStyle(ChatMessage chatMessage) {
    return null;
  }

  private NotificationCompat.Style createMessageStyle(ChatMessage chatMessage) {
    NotificationCompat.MessagingStyle messagingStyle = new NotificationCompat.MessagingStyle("");

    messagingStyle.addMessage(new NotificationMessageCreator().create(chatMessage));

    return messagingStyle;
  }

  private class NotificationMessageCreator implements MessageFeatureVisitor {

    private List<String> mLines;
    private long mTime;
    private String mSender;

    public NotificationCompat.MessagingStyle.Message create(ChatMessage chatMessage) {
      initialize();
      setBasicData(chatMessage);
      chatMessage.accept(this);

      return new NotificationCompat.MessagingStyle.Message(
          TextUtils.join(System.lineSeparator(), mLines), mTime, mSender);
    }

    @Override
    public void visit(TextFeature feature) {
      mLines.add(feature.getText());
    }

    @Override
    public void visit(GeoFeature feature) {
      mLines.add(0, "Geometry -");
    }

    @Override
    public void visit(ImageFeature feature) {
      mLines.add(0, "Image -");
    }

    @Override
    public void visit(AlertFeature feature) {
      String alertText = String.format("Alert: %s", feature.getAlert().getText());
      mLines.add(alertText);
    }

    private void initialize() {
      mLines = new ArrayList<>();
    }

    private void setBasicData(ChatMessage chatMessage) {
      mTime = chatMessage.getCreatedAt().getTime();
      mSender = chatMessage.getSenderId();
    }
  }
}
