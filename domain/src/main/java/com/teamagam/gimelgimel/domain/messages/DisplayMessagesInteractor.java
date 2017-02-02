package com.teamagam.gimelgimel.domain.messages;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDisplayInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DisplaySubscriptionRequest;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.repository.EntityMessageMapper;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;
import com.teamagam.gimelgimel.domain.utils.MessagesUtil;

import java.util.Arrays;

@AutoFactory
public class DisplayMessagesInteractor extends BaseDisplayInteractor {

    private static final int MAP_INITIAL_CAPACITY = 20;

    private final DisplayedEntitiesRepository mDisplayedEntitiesRepository;
    private final Displayer mDisplayer;
    private final MessagesRepository mMessagesRepository;
    private final MessagesUtil mMessagesUtil;
    private final EntityMessageMapper mMapper;

    DisplayMessagesInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided PostExecutionThread postExecutionThread,
            @Provided MessagesRepository messagesRepository,
            @Provided MessagesUtil messagesUtil,
            @Provided DisplayedEntitiesRepository displayedEntitiesRepository,
            @Provided EntityMessageMapper mapper,
            Displayer displayer) {
        super(threadExecutor, postExecutionThread);
        mMessagesRepository = messagesRepository;
        mMessagesUtil = messagesUtil;
        mDisplayedEntitiesRepository = displayedEntitiesRepository;
        mMapper = mapper;
        mDisplayer = displayer;
    }

    @Override
    protected Iterable<SubscriptionRequest> buildSubscriptionRequests(
            DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory factory) {

        DisplaySubscriptionRequest displayMessages = factory.create(
                mMessagesRepository.getMessagesObservable(),
                this::showMessage);

        DisplaySubscriptionRequest displayShownStatus = factory.create(
                mDisplayedEntitiesRepository.getObservable(),
                this::updateRelatedMessageDisplayStatus);

        return Arrays.asList(displayMessages, displayShownStatus);
    }

    private void showMessage(Message message) {
        mDisplayer.show(message, mMessagesUtil.isMessageFromSelf(message));
    }

    private void updateRelatedMessageDisplayStatus(GeoEntityNotification geoEntityNotification) {
        mMapper.getMessageId(geoEntityNotification.getGeoEntity().getId())
                .flatMap(mMapper::getMessageId)
                .flatMap(mMessagesRepository::getMessage)
                .filter(message -> message != null)
                .subscribe(message -> {
                    updateMessageDisplayStatus(geoEntityNotification, message);
                });
    }

    private void updateMessageDisplayStatus(GeoEntityNotification geoEntityNotification,
                                            Message message) {
        if (geoEntityNotification.getAction() == GeoEntityNotification.ADD) {
            mDisplayer.messageShownOnMap(message);
        } else if (geoEntityNotification.getAction() == GeoEntityNotification.REMOVE) {
            mDisplayer.messageHiddenFromMap(message);
        }
    }


    public interface Displayer {
        void show(Message message, boolean isFromSelf);

        void messageShownOnMap(Message message);

        void messageHiddenFromMap(Message message);
    }
}
