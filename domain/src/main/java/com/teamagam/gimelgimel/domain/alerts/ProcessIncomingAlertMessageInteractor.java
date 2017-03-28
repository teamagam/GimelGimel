package com.teamagam.gimelgimel.domain.alerts;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.alerts.entity.GeoAlert;
import com.teamagam.gimelgimel.domain.alerts.repository.AlertsRepository;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.map.DrawEntityOnMapInteractorFactory;
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
    private final DrawEntityOnMapInteractorFactory mDrawEntityOnMapInteractorFactory;
    private final MessageAlert mMessageAlert;

    public ProcessIncomingAlertMessageInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided AlertsRepository alertsRepository,
            @Provided com.teamagam.gimelgimel.domain.messages.AddPolledMessageToRepositoryInteractorFactory addPolledMessageToRepositoryInteractorFactory,
            @Provided com.teamagam.gimelgimel.domain.map.DrawEntityOnMapInteractorFactory drawEntityOnMapInteractorFactory,
            MessageAlert messageAlert) {
        super(threadExecutor);
        mAlertsRepository = alertsRepository;
        mAddPolledMessageToRepositoryInteractorFactory = addPolledMessageToRepositoryInteractorFactory;
        mDrawEntityOnMapInteractorFactory = drawEntityOnMapInteractorFactory;
        mMessageAlert = messageAlert;
    }

    @Override
    protected Iterable<SubscriptionRequest> buildSubscriptionRequests(
            DataSubscriptionRequest.SubscriptionRequestFactory factory) {
        DataSubscriptionRequest request = factory.create(
                Observable.just(mMessageAlert),
                messageAlertObservable ->
                        messageAlertObservable
                                .doOnNext(this::addToChatIfNeeded)
                                .doOnNext(this::drawOnMapIfNeeded)
                                .doOnNext(this::addToAlertRepository)

        );

        return Collections.singletonList(request);
    }

    private void addToAlertRepository(MessageAlert messageAlert) {
        mAlertsRepository.addAlert(messageAlert.getAlert());
    }

    private void addToChatIfNeeded(MessageAlert messageAlert) {
        if (shouldAddToChat(messageAlert)) {
            mAddPolledMessageToRepositoryInteractorFactory.create(messageAlert).execute();
        }
    }

    private void drawOnMapIfNeeded(MessageAlert messageAlert) {
        if (messageAlert.getAlert() instanceof GeoAlert) {
            GeoAlert alert = (GeoAlert) messageAlert.getAlert();
            drawGeoAlert(alert);
        }
    }

    private void drawGeoAlert(GeoAlert alert) {
        mDrawEntityOnMapInteractorFactory.create(alert.getEntity()).execute();
    }

    private boolean shouldAddToChat(MessageAlert messageAlert) {
        return messageAlert.getAlert().isChatAlert();
    }
}