package com.teamagam.gimelgimel.domain.alerts;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.alerts.entity.Alert;
import com.teamagam.gimelgimel.domain.alerts.repository.InformedAlertsRepository;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import io.reactivex.Observable;
import java.util.Collections;
import java.util.Date;

@AutoFactory
public class UpdateLatestInformedAlertTimeInteractor extends BaseDataInteractor {

  private final InformedAlertsRepository mInformedAlertsRepository;
  private final Alert mAlert;

  public UpdateLatestInformedAlertTimeInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided InformedAlertsRepository informedAlertsRepository,
      Alert alert) {
    super(threadExecutor);
    mInformedAlertsRepository = informedAlertsRepository;
    mAlert = alert;
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(DataSubscriptionRequest.SubscriptionRequestFactory factory) {
    DataSubscriptionRequest dataSubscriptionRequest =
        factory.create(Observable.just(mAlert), this::updateInformDate);
    return Collections.singletonList(dataSubscriptionRequest);
  }

  private Observable<?> updateInformDate(Observable<Alert> alertObservable) {
    return alertObservable.map(Alert::getTime)
        .map(Date::new)
        .doOnNext(mInformedAlertsRepository::updateLatestInformedDate);
  }
}
