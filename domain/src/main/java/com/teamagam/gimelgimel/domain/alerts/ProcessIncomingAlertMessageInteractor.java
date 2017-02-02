package com.teamagam.gimelgimel.domain.alerts;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.alerts.repository.AlertsRepository;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.map.DrawEntityOnMapInteractorFactory;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.AlertEntity;
import com.teamagam.gimelgimel.domain.messages.entity.MessageAlert;
import com.teamagam.gimelgimel.domain.messages.repository.EntityMessageMapper;

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
    private final EntityMessageMapper mEntityMessageMapper;
    private final MessageAlert mMessageAlert;

    public ProcessIncomingAlertMessageInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided AlertsRepository alertsRepository,
            @Provided com.teamagam.gimelgimel.domain.messages.AddPolledMessageToRepositoryInteractorFactory addPolledMessageToRepositoryInteractorFactory,
            @Provided com.teamagam.gimelgimel.domain.map.DrawEntityOnMapInteractorFactory drawEntityOnMapInteractorFactory,
            @Provided EntityMessageMapper entityMessageMapper,
            MessageAlert messageAlert) {
        super(threadExecutor);
        mAlertsRepository = alertsRepository;
        mAddPolledMessageToRepositoryInteractorFactory = addPolledMessageToRepositoryInteractorFactory;
        mDrawEntityOnMapInteractorFactory = drawEntityOnMapInteractorFactory;
        mEntityMessageMapper = entityMessageMapper;
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
        if (isBubbleAlertMessage(messageAlert)) {
            mAddPolledMessageToRepositoryInteractorFactory.create(messageAlert).execute();
            mEntityMessageMapper.addMapping(messageAlert.getMessageId(),
                    getAlertGeoEntity(messageAlert).getId());
            mDrawEntityOnMapInteractorFactory.create(getAlertGeoEntity(messageAlert));
        }
    }

    private AlertEntity getAlertGeoEntity(MessageAlert messageAlert) {
        return messageAlert.getAlert().getEntity();
    }

    private boolean isBubbleAlertMessage(MessageAlert messageAlert) {
        return messageAlert.getAlert().isBubbleAlert();
    }
}
