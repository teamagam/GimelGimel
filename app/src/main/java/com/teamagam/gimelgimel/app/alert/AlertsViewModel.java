package com.teamagam.gimelgimel.app.alert;

import android.content.Context;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.domain.alerts.InformNewAlertsInteractor;
import com.teamagam.gimelgimel.domain.alerts.InformNewAlertsInteractorFactory;
import com.teamagam.gimelgimel.domain.alerts.OnAlertInformClickInteractorFactory;
import com.teamagam.gimelgimel.domain.alerts.entity.Alert;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.features.TextFeature;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.messages.repository.ObjectMessageMapper;
import javax.inject.Named;

@AutoFactory
public class AlertsViewModel {

  private final InformNewAlertsInteractorFactory mInformNewAlertsInteractorFactory;
  private final OnAlertInformClickInteractorFactory mOnAlertInformClickInteractorFactory;
  private final AlertDisplayer mAlertDisplayer;
  private ObjectMessageMapper mObjectDataMapper;
  private MessagesRepository mMessagesRepository;
  private Context mContext;

  private InformNewAlertsInteractor mInformNewAlertsInteractor;

  private Alert mLatestDisplayedAlert;

  public AlertsViewModel(@Provided Context context,
      @Provided InformNewAlertsInteractorFactory alertFactory,
      @Provided OnAlertInformClickInteractorFactory onAlertInformClickInteractorFactory,
      AlertDisplayer alertDisplayer,
      @Provided @Named("Alert") ObjectMessageMapper objectMessageMapper,
      @Provided MessagesRepository messagesRepository) {
    mContext = context;
    mObjectDataMapper = objectMessageMapper;
    mMessagesRepository = messagesRepository;
    mInformNewAlertsInteractorFactory = alertFactory;
    mOnAlertInformClickInteractorFactory = onAlertInformClickInteractorFactory;
    mAlertDisplayer = alertDisplayer;
  }

  public void start() {
    mInformNewAlertsInteractor = mInformNewAlertsInteractorFactory.create(new MyDisplayer());
    mInformNewAlertsInteractor.execute();
  }

  public void stop() {
    if (mInformNewAlertsInteractor != null) {
      mInformNewAlertsInteractor.unsubscribe();
    }

    hideAlert();
  }

  public void onAlertClick() {
    hideAlert();
    onAlertInformClickInteraction();
  }

  private void onAlertInformClickInteraction() {
    mOnAlertInformClickInteractorFactory.create(mLatestDisplayedAlert).execute();
  }

  private void hideAlert() {
    if (mAlertDisplayer.isShowingAlert()) {
      mAlertDisplayer.hideAlert();
    }
  }

  public interface AlertDisplayer {
    void showAlert(String title, String description);

    void hideAlert();

    boolean isShowingAlert();
  }

  private class MyDisplayer implements InformNewAlertsInteractor.Displayer {

    @Override
    public void display(Alert alert) {
      hideAlert();
      mLatestDisplayedAlert = alert;
      String messageId = mObjectDataMapper.getMessageId(alert.getId());
      ChatMessage chatMessage = mMessagesRepository.getMessage(messageId);
      String messageText = chatMessage.getFeatureByType(TextFeature.class).getText();
      mAlertDisplayer.showAlert(createTitle(alert), messageText);
    }

    private String createTitle(Alert alert) {
      if (alert.getType() == Alert.Type.VECTOR_LAYER) {
        return mContext.getString(R.string.alert_notification_new_vector_layer);
      }
      return mContext.getString(R.string.alert_notification_new_alert);
    }
  }
}


