package com.teamagam.gimelgimel.domain.alerts;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.alerts.entity.Alert;
import com.teamagam.gimelgimel.domain.alerts.repository.AlertsRepository;
import com.teamagam.gimelgimel.domain.alerts.repository.InformedAlertsRepository;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseSingleDisplayInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DisplaySubscriptionRequest;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.messages.repository.ObjectMessageMapper;

import java.util.Date;

import javax.inject.Named;

@AutoFactory
public class InformNewAlertsInteractor extends BaseSingleDisplayInteractor {

    private final AlertsRepository mAlertRepository;
    private final MessagesRepository mMessagesRepository;
    private final InformedAlertsRepository mInformedAlertsRepository;
    private final ObjectMessageMapper mMapper;
    private final Displayer mDisplayer;

    InformNewAlertsInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided PostExecutionThread postExecutionThread,
            @Provided AlertsRepository alertsRepository,
            @Provided MessagesRepository messagesRepository,
            @Provided InformedAlertsRepository informedAlertsRepository,
            @Provided @Named("Alert") ObjectMessageMapper alertMessageMapper,
            Displayer displayer) {
        super(threadExecutor, postExecutionThread);
        mAlertRepository = alertsRepository;
        mMessagesRepository = messagesRepository;
        mInformedAlertsRepository = informedAlertsRepository;
        mMapper = alertMessageMapper;
        mDisplayer = displayer;
    }

    @Override
    protected SubscriptionRequest buildSubscriptionRequest(
            DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory factory) {

        return factory.create(
                mAlertRepository.getAlertsObservable(),
                alertObservable ->
                        alertObservable
                                .filter(this::shouldInform)
                ,
                mDisplayer::display);
    }

    private boolean shouldInform(Alert alert) {
        return isAfterLatestInformedDate(alert);
    }

    private boolean isAfterLatestInformedDate(Alert alert) {
        Date latestInformedDate = mInformedAlertsRepository.getLatestInformedDate();
        Message message = mMessagesRepository.getMessage(mMapper.getMessageId(alert.getId()));

        return message.getCreatedAt().after(latestInformedDate);
    }

    public interface Displayer {
        void display(Alert alert);
    }
}
