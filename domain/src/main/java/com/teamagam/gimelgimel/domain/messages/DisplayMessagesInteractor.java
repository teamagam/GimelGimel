package com.teamagam.gimelgimel.domain.messages;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDisplayInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DisplaySubscriptionRequest;
import com.teamagam.gimelgimel.domain.config.Constants;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.messages.entity.BaseMessageGeo;
import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@AutoFactory
public class DisplayMessagesInteractor extends BaseDisplayInteractor {

    private static final int MAP_INITIAL_CAPACITY = 20;

    private final DisplayedEntitiesRepository mDisplayedEntitiesRepository;
    private final Displayer mDisplayer;
    private final MessagesRepository mMessagesRepository;
    private final UserPreferencesRepository mUserPreferencesRepository;
    private final Map<GeoEntity, Message> mGeoEntityToMessageMap;


    DisplayMessagesInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided PostExecutionThread postExecutionThread,
            @Provided MessagesRepository messagesRepository,
            @Provided UserPreferencesRepository userPreferencesRepository,
            @Provided DisplayedEntitiesRepository displayedEntitiesRepository,
            Displayer displayer) {
        super(threadExecutor, postExecutionThread);
        mMessagesRepository = messagesRepository;
        mUserPreferencesRepository = userPreferencesRepository;
        mDisplayedEntitiesRepository = displayedEntitiesRepository;
        mDisplayer = displayer;

        mGeoEntityToMessageMap = new HashMap<>(MAP_INITIAL_CAPACITY);
    }

    @Override
    protected Iterable<SubscriptionRequest> buildSubscriptionRequests(
            DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory factory) {

        DisplaySubscriptionRequest displayMessages = factory.create(
                mMessagesRepository.getMessagesObservable(),
                this::showMessage);

        DisplaySubscriptionRequest displayRead = factory.create(
                mMessagesRepository.getReadMessagesObservable(),
                mDisplayer::read
        );

        DisplaySubscriptionRequest displaySelected = factory.create(
                mMessagesRepository.getSelectedMessageObservable(),
                mDisplayer::select);

        DisplaySubscriptionRequest displayShownStatus = factory.create(
                mDisplayedEntitiesRepository.getObservable(),
                this::updateRelatedMessageDisplayStatus);

        return Arrays.asList(displayMessages, displayRead, displaySelected, displayShownStatus);
    }

    private void showMessage(Message message) {
        mDisplayer.show(message, isMessageFromSelf(message));
        mapMessageGeoEntity(message);
    }

    private void mapMessageGeoEntity(Message message) {
        if (message instanceof BaseMessageGeo) {
            GeoEntity geoEntity = ((BaseMessageGeo) message).extractGeoEntity();
            mGeoEntityToMessageMap.put(geoEntity, message);
        }
    }

    private boolean isMessageFromSelf(Message message) {
        return message.getSenderId().equals(mUserPreferencesRepository.getPreference(
                Constants.USERNAME_PREFRENCE_KEY));
    }

    private void updateRelatedMessageDisplayStatus(GeoEntityNotification geoEntityNotification) {
        Message message = mGeoEntityToMessageMap.get(geoEntityNotification.getGeoEntity());

        if (message != null) {
            updateMessageDisplayStatus(geoEntityNotification, message);
        }
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

        void read(Message message);

        void select(Message message);
    }
}
