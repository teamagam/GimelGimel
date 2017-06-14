package com.teamagam.gimelgimel.domain.alerts;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.alerts.entity.Alert;
import com.teamagam.gimelgimel.domain.alerts.repository.AlertsRepository;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import io.reactivex.Observable;
import java.util.Collections;

@AutoFactory
public class AddAlertToRepositoryInteractor extends BaseDataInteractor {

  private final AlertsRepository mAlertsRepository;
  private final Alert mAlert;

  public AddAlertToRepositoryInteractor(
      @Provided
          ThreadExecutor threadExecutor,
      @Provided
          AlertsRepository alertsRepository, Alert alert) {
    super(threadExecutor);
    mAlertsRepository = alertsRepository;
    mAlert = alert;
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(
      DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    DataSubscriptionRequest request = factory.create(Observable.just(mAlert),
        alert -> alert.doOnNext(this::addToAlertRepository));

    return Collections.singletonList(request);
  }

  private void addToAlertRepository(Alert alert) {
    mAlertsRepository.addAlert(alert);
  }
}
