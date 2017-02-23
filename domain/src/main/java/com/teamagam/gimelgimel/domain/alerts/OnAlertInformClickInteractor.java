package com.teamagam.gimelgimel.domain.alerts;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.alerts.entity.Alert;
import com.teamagam.gimelgimel.domain.alerts.entity.GeoAlert;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.map.GoToLocationMapInteractorFactory;
import com.teamagam.gimelgimel.domain.map.entities.geometries.Geometry;
import com.teamagam.gimelgimel.domain.messages.SelectMessageInteractorFactory;

import java.util.Collections;

import rx.Observable;

@AutoFactory
public class OnAlertInformClickInteractor extends BaseDataInteractor {

    private final UpdateLatestInformedAlertTimeInteractorFactory mUpdateLatestInformedAlertTimeInteractorFactory;
    private final GoToLocationMapInteractorFactory mGoToLocationMapInteractorFactory;
    private final SelectMessageInteractorFactory mSelectMessageInteractorFactory;
    private final Alert mAlert;

    public OnAlertInformClickInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided UpdateLatestInformedAlertTimeInteractorFactory updateLatestInformedAlertTimeInteractorFactory,
            @Provided com.teamagam.gimelgimel.domain.map.GoToLocationMapInteractorFactory goToLocationMapInteractorFactory,
            @Provided com.teamagam.gimelgimel.domain.messages.SelectMessageInteractorFactory selectMessageInteractorFactory,
            Alert alert) {
        super(threadExecutor);
        mUpdateLatestInformedAlertTimeInteractorFactory = updateLatestInformedAlertTimeInteractorFactory;
        mGoToLocationMapInteractorFactory = goToLocationMapInteractorFactory;
        mSelectMessageInteractorFactory = selectMessageInteractorFactory;
        mAlert = alert;
    }

    @Override
    protected Iterable<SubscriptionRequest> buildSubscriptionRequests(
            DataSubscriptionRequest.SubscriptionRequestFactory factory) {
        DataSubscriptionRequest dataSubscriptionRequest = factory.create(
                Observable.just(mAlert)
                        .doOnNext(this::updateLastInformedAlertTime)
                        .doOnNext(this::goToAlertLocation)
                        .doOnNext(this::showInChatIfNecessary)
        );

        return Collections.singletonList(dataSubscriptionRequest);
    }

    private void updateLastInformedAlertTime(Alert alert) {
        mUpdateLatestInformedAlertTimeInteractorFactory.create(alert).execute();
    }

    private void goToAlertLocation(Alert alert) {
        if (alert instanceof GeoAlert) {
            Geometry geometry = ((GeoAlert) alert).getEntity().getGeometry();
            mGoToLocationMapInteractorFactory.create(geometry).execute();
        }
    }

    private void showInChatIfNecessary(Alert alert) {
        if (alert.isChatAlert()) {
            mSelectMessageInteractorFactory.create(alert.getMessageId()).execute();
        }
    }
}
