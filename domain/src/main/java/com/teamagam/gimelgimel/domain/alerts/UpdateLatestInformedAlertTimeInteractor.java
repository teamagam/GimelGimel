package com.teamagam.gimelgimel.domain.alerts;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.alerts.entity.Alert;
import com.teamagam.gimelgimel.domain.alerts.repository.InformedAlertsRepository;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;

import java.util.Collections;

import rx.Observable;

@AutoFactory
public class UpdateLatestInformedAlertTimeInteractor extends BaseDataInteractor {

    private final InformedAlertsRepository mInformedAlertsRepository;
    private final Alert mAlert;

    public UpdateLatestInformedAlertTimeInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided InformedAlertsRepository informedAlertsRepository,
            Alert alert) {
        super(threadExecutor);
        mInformedAlertsRepository = informedAlertsRepository;
        mAlert = alert;
    }

    @Override
    protected Iterable<SubscriptionRequest> buildSubscriptionRequests(
            DataSubscriptionRequest.SubscriptionRequestFactory factory) {
        DataSubscriptionRequest dataSubscriptionRequest =
                factory.create(Observable.just(mAlert)
                        .map(Alert::getDate)
                        .doOnNext(mInformedAlertsRepository::updateLatestInformedDate));
        return Collections.singletonList(dataSubscriptionRequest);
    }
}
