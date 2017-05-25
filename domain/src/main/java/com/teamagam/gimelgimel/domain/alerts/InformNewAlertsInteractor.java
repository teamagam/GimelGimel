package com.teamagam.gimelgimel.domain.alerts;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.alerts.entity.Alert;
import com.teamagam.gimelgimel.domain.alerts.repository.AlertsRepository;
import com.teamagam.gimelgimel.domain.alerts.repository.InformedAlertsRepository;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDisplayInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DisplaySubscriptionRequest;
import java.util.Date;

@AutoFactory
public class InformNewAlertsInteractor extends BaseSingleDisplayInteractor {

  private final AlertsRepository mAlertRepository;
  private final InformedAlertsRepository mInformedAlertsRepository;
  private final Displayer mDisplayer;

  InformNewAlertsInteractor(
      @Provided
          ThreadExecutor threadExecutor,
      @Provided
          PostExecutionThread postExecutionThread,
      @Provided
          AlertsRepository alertsRepository,
      @Provided
          InformedAlertsRepository informedAlertsRepository, Displayer displayer) {
    super(threadExecutor, postExecutionThread);
    mAlertRepository = alertsRepository;
    mInformedAlertsRepository = informedAlertsRepository;
    mDisplayer = displayer;
  }

  @Override
  protected SubscriptionRequest buildSubscriptionRequest(
      DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory factory) {

    return factory.create(mAlertRepository.getAlertsObservable(),
        alertObservable -> alertObservable.filter(this::shouldInform), mDisplayer::display);
  }

  private boolean shouldInform(Alert alert) {
    return isAfterLatestInformedDate(alert);
  }

  private boolean isAfterLatestInformedDate(Alert alert) {
    Date latestInformedDate = mInformedAlertsRepository.getLatestInformedDate();
    Date alertDate = new Date(alert.getTime());

    return alertDate.after(latestInformedDate);
  }

  public interface Displayer {
    void display(Alert alert);
  }
}
