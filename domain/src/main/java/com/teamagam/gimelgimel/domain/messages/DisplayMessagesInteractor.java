package com.teamagam.gimelgimel.domain.messages;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDisplayInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DisplaySubscriptionRequest;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.messages.entity.GeoEntityHolder;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.repository.EntityMessageMapper;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.messages.repository.UnreadMessagesCountRepository;
import com.teamagam.gimelgimel.domain.utils.MessagesUtil;

import java.util.Arrays;

import rx.Observable;

@AutoFactory
public class DisplayMessagesInteractor extends BaseDisplayInteractor {

    private final DisplayedEntitiesRepository mDisplayedEntitiesRepository;
    private final UnreadMessagesCountRepository mUnreadMessagesCountRepository;
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
            @Provided UnreadMessagesCountRepository unreadMessagesCountRepository,
            Displayer displayer) {
        super(threadExecutor, postExecutionThread);
        mMessagesRepository = messagesRepository;
        mMessagesUtil = messagesUtil;
        mDisplayedEntitiesRepository = displayedEntitiesRepository;
        mMapper = mapper;
        mUnreadMessagesCountRepository = unreadMessagesCountRepository;
        mDisplayer = displayer;
    }

    @Override
    protected Iterable<SubscriptionRequest> buildSubscriptionRequests(
            DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory factory) {

        DisplaySubscriptionRequest displayMessages = factory.create(
                mMessagesRepository.getMessagesObservable()
                        .map(this::createMessagePresentation),
                mDisplayer::show);

        DisplaySubscriptionRequest updateMapDisplayStatusChanges = factory.create(
                getMessageMapDisplayChanges()
                        .map(this::createMessagePresentation),
                mDisplayer::show);

        return Arrays.asList(displayMessages, updateMapDisplayStatusChanges);
    }

    private Observable<Message> getMessageMapDisplayChanges() {
        return mDisplayedEntitiesRepository.getObservable()
                .map(geoEntityNotification -> geoEntityNotification.getGeoEntity().getId())
                .flatMap(mMapper::getMessageId)
                .flatMap(mMessagesRepository::getMessage)
                .filter(m -> m != null);
    }

    private MessagePresentation createMessagePresentation(Message message) {
        boolean isShownOnMap = isShownOnMap(message);
        boolean isFromSelf = mMessagesUtil.isMessageFromSelf(message);
        boolean isRead = message.getCreatedAt().before(
                mUnreadMessagesCountRepository.getLastVisitTimestamp());
        return new MessagePresentation.Builder(message)
                .setIsFromSelf(isFromSelf)
                .setIsShownOnMap(isShownOnMap)
                .setIsRead(isRead)
                .build();
    }

    private boolean isShownOnMap(Message message) {
        if (!(message instanceof GeoEntityHolder)) {
            return false;
        }
        GeoEntityHolder bmg = (GeoEntityHolder) message;
        return mDisplayedEntitiesRepository.isShown(bmg.getGeoEntity());
    }


    public interface Displayer {
        void show(MessagePresentation message);
    }
}
