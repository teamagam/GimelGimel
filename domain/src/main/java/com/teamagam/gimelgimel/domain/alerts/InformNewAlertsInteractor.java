package com.teamagam.gimelgimel.domain.alerts;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.alerts.repository.AlertsRepository;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDisplayInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DisplaySubscriptionRequest;

import java.util.Arrays;

/**
 * Created by Admin on 27/12/2016.
 */

@AutoFactory
public class InformNewAlertsInteractor extends BaseDisplayInteractor {


    private final AlertsRepository mAlertRepository;
    private final InformNewAlertsInteractor.Informer mInformer;

    public InformNewAlertsInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided PostExecutionThread postExecutionThread,
            @Provided AlertsRepository alertsRepository,
            Informer informer) {
        super(threadExecutor, postExecutionThread);
        mAlertRepository = alertsRepository;
        mInformer = informer;
    }

    @Override
    protected Iterable<SubscriptionRequest> buildSubscriptionRequests(
            DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory factory) {

        DisplaySubscriptionRequest informNewAlert = factory.create(
                mAlertRepository.getAlertsObservable(), mInformer::inform);
        return Arrays.asList(informNewAlert);
    }

    public interface Informer {
        void inform(Object obj);
    }
}
