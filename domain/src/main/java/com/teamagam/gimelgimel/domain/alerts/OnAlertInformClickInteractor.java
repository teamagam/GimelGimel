package com.teamagam.gimelgimel.domain.alerts;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.alerts.entity.Alert;
import com.teamagam.gimelgimel.domain.alerts.entity.GeoAlert;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.map.GoToLocationMapInteractorFactory;
import com.teamagam.gimelgimel.domain.map.SelectEntityInteractorFactory;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import com.teamagam.gimelgimel.domain.messages.SelectMessageInteractorFactory;
import com.teamagam.gimelgimel.domain.messages.repository.ObjectMessageMapper;

import java.util.Collections;

import javax.inject.Named;

import rx.Observable;

@AutoFactory
public class OnAlertInformClickInteractor extends BaseDataInteractor {

    private final ObjectMessageMapper mMapper;
    private final UpdateLatestInformedAlertTimeInteractorFactory mUpdateLatestInformedAlertTimeInteractorFactory;
    private final GoToLocationMapInteractorFactory mGoToLocationMapInteractorFactory;
    private final SelectEntityInteractorFactory mSelectEntityInteractorFactory;
    private final SelectMessageInteractorFactory mSelectMessageInteractorFactory;
    private final Alert mAlert;

    public OnAlertInformClickInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided @Named("Alert") ObjectMessageMapper alertMessageMapper,
            @Provided UpdateLatestInformedAlertTimeInteractorFactory updateLatestInformedAlertTimeInteractorFactory,
            @Provided com.teamagam.gimelgimel.domain.map.GoToLocationMapInteractorFactory goToLocationMapInteractorFactory,
            @Provided com.teamagam.gimelgimel.domain.map.SelectEntityInteractorFactory selectEntityInteractorFactory,
            @Provided com.teamagam.gimelgimel.domain.messages.SelectMessageInteractorFactory selectMessageInteractorFactory,
            Alert alert) {
        super(threadExecutor);
        mMapper = alertMessageMapper;
        mUpdateLatestInformedAlertTimeInteractorFactory = updateLatestInformedAlertTimeInteractorFactory;
        mGoToLocationMapInteractorFactory = goToLocationMapInteractorFactory;
        mSelectEntityInteractorFactory = selectEntityInteractorFactory;
        mSelectMessageInteractorFactory = selectMessageInteractorFactory;
        mAlert = alert;
    }

    @Override
    protected Iterable<SubscriptionRequest> buildSubscriptionRequests(
            DataSubscriptionRequest.SubscriptionRequestFactory factory) {
        DataSubscriptionRequest dataSubscriptionRequest = factory.create(
                Observable.just(mAlert),
                alertObservable -> alertObservable
                        .doOnNext(this::updateLastInformedAlertTime)
                        .doOnNext(this::showInChatIfNecessary)
                        .filter(alert -> alert instanceof GeoAlert)
                        .doOnNext(this::goToAlertLocation)
                        .doOnNext(this::selectEntity)

        );

        return Collections.singletonList(dataSubscriptionRequest);
    }


    private void updateLastInformedAlertTime(Alert alert) {
        mUpdateLatestInformedAlertTimeInteractorFactory.create(alert).execute();
    }

    private void showInChatIfNecessary(Alert alert) {
        if (alert.isChatAlert()) {
            String messageId = mMapper.getMessageId(alert.getId());
            mSelectMessageInteractorFactory.create(messageId).execute();
        }
    }

    private void goToAlertLocation(Alert alert) {
        Geometry geometry = ((GeoAlert) alert).getEntity().getGeometry();
        mGoToLocationMapInteractorFactory.create(geometry).execute();
    }

    private void selectEntity(Alert alert) {
        mSelectEntityInteractorFactory.create(((GeoAlert) alert).getEntity().getId()).execute();
    }
}
