package com.teamagam.gimelgimel.domain.alerts;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.alerts.entity.Alert;
import com.teamagam.gimelgimel.domain.alerts.repository.InformedAlertsRepository;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.messages.repository.ObjectMessageMapper;

import java.util.Collections;

import javax.inject.Named;

import rx.Observable;

@AutoFactory
public class UpdateLatestInformedAlertTimeInteractor extends BaseDataInteractor {

    private final InformedAlertsRepository mInformedAlertsRepository;
    private final MessagesRepository mMessagesRepository;
    private final ObjectMessageMapper mMapper;
    private final Alert mAlert;

    public UpdateLatestInformedAlertTimeInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided InformedAlertsRepository informedAlertsRepository,
            @Provided MessagesRepository messagesRepository,
            @Provided @Named("Alert") ObjectMessageMapper alertMessageMapper,
            Alert alert) {
        super(threadExecutor);
        mInformedAlertsRepository = informedAlertsRepository;
        mMessagesRepository = messagesRepository;
        mMapper = alertMessageMapper;
        mAlert = alert;
    }

    @Override
    protected Iterable<SubscriptionRequest> buildSubscriptionRequests(
            DataSubscriptionRequest.SubscriptionRequestFactory factory) {
        DataSubscriptionRequest dataSubscriptionRequest = factory.create(
                Observable.just(mAlert),
                this::updateInformDate
        );
        return Collections.singletonList(dataSubscriptionRequest);
    }

    private Observable<?> updateInformDate(Observable<Alert> alertObservable) {
        return alertObservable
                .map(a -> mMapper.getMessageId(a.getId()))
                .map(mMessagesRepository::getMessage)
                .map(Message::getCreatedAt)
                .doOnNext(mInformedAlertsRepository::updateLatestInformedDate);
    }
}
