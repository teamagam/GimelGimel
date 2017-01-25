package com.teamagam.gimelgimel.domain.alerts;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.alerts.entity.Alert;
import com.teamagam.gimelgimel.domain.alerts.repository.AlertsRepository;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.messages.entity.MessageAlert;

import java.util.Collections;

import rx.Observable;

@AutoFactory
public class ProcessIncomingAlertMessageInteractor extends BaseDataInteractor {

    /**
     * Using fully qualified named due to AutoFactory code generation bug.
     * You can see more (and possibly follow to see if anything changes) here:
     * https://github.com/google/auto/issues/124
     */
    private final com.teamagam.gimelgimel.domain.messages.AddPolledMessageToRepositoryInteractorFactory mAddPolledMessageToRepositoryInteractorFactory;

    private final AlertsRepository mAlertsRepository;
    private final MessageAlert mMessageAlert;

    public ProcessIncomingAlertMessageInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided AlertsRepository alertsRepository,
            @Provided com.teamagam.gimelgimel.domain.messages.AddPolledMessageToRepositoryInteractorFactory addPolledMessageToRepositoryInteractorFactory,
            MessageAlert messageAlert) {
        super(threadExecutor);
        mAlertsRepository = alertsRepository;
        mAddPolledMessageToRepositoryInteractorFactory = addPolledMessageToRepositoryInteractorFactory;
        mMessageAlert = messageAlert;
    }

    @Override
    protected Iterable<SubscriptionRequest> buildSubscriptionRequests(
            DataSubscriptionRequest.SubscriptionRequestFactory factory) {
        DataSubscriptionRequest request = factory.create(
                Observable.just(mMessageAlert)
                        .doOnNext(this::addToAlertRepository)
                        .doOnNext(this::displayBubbleAlerts)
        );

        return Collections.singletonList(request);
    }

    private void addToAlertRepository(MessageAlert messageAlert) {
        mAlertsRepository.addAlert(messageAlert.getAlert());
    }

    private void displayBubbleAlerts(MessageAlert messageAlert) {
        if (isBubbleAlert(messageAlert.getAlert())) {
            mAddPolledMessageToRepositoryInteractorFactory.create(messageAlert).execute();
        }
    }

    private boolean isBubbleAlert(Alert alert) {
        return Alert.TYPE_BUBBLE.equals(alert.getSource());
    }
}
