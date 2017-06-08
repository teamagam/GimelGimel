package com.teamagam.gimelgimel.app.Alerts.viewmodel;

import android.content.Context;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.domain.alerts.InformNewAlertsInteractor;
import com.teamagam.gimelgimel.domain.alerts.InformNewAlertsInteractorFactory;
import com.teamagam.gimelgimel.domain.alerts.OnAlertInformClickInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.messages.entity.features.TextFeature;

@AutoFactory
public class AlertsViewModel {

  private final InformNewAlertsInteractorFactory mInformNewAlertsInteractorFactory;
  private final OnAlertInformClickInteractorFactory mOnAlertInformClickInteractorFactory;
  private final AlertDisplayer mAlertDisplayer;
  private Context mContext;

  private InformNewAlertsInteractor mInformNewAlertsInteractor;

  private ChatMessage mLatestDisplayedAlert;

  public AlertsViewModel(@Provided Context context,
      @Provided InformNewAlertsInteractorFactory alertFactory,
      @Provided OnAlertInformClickInteractorFactory onAlertInformClickInteractorFactory,
      AlertDisplayer alertDisplayer) {
    mContext = context;
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
    public void display(ChatMessage alert) {
      hideAlert();
      mLatestDisplayedAlert = alert;

      TextFeature feature = alert.getFeatureByType(TextFeature.class);
      mAlertDisplayer.showAlert(createTitle(feature), feature.getText());
    }

    private String createTitle(TextFeature textFeature) {
      String title = textFeature.getTitle();
      if (title != null) {
        return title;
      }

      return mContext.getString(R.string.alert_notification_new_alert);
    }
  }
}


