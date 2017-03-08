package com.teamagam.gimelgimel.domain.location;


import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.config.Constants;
import com.teamagam.gimelgimel.domain.location.respository.LocationRepository;
import com.teamagam.gimelgimel.domain.messages.entity.MessageUserLocation;
import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSample;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

import java.util.Collections;

import javax.inject.Inject;

public class SendSelfLocationsInteractor extends BaseDataInteractor {

    private UserPreferencesRepository mUserPreferences;
    private MessagesRepository mMessagesRepository;
    private LocationRepository mLocationRepository;

    @Inject
    public SendSelfLocationsInteractor(ThreadExecutor threadExecutor,
                                       UserPreferencesRepository userPreferences,
                                       MessagesRepository messagesRepository,
                                       LocationRepository locationRepository) {
        super(threadExecutor);
        mUserPreferences = userPreferences;
        mMessagesRepository = messagesRepository;
        mLocationRepository = locationRepository;
    }

    @Override
    protected Iterable<SubscriptionRequest> buildSubscriptionRequests(
            DataSubscriptionRequest.SubscriptionRequestFactory factory) {

        return Collections.singletonList(buildSendRequest(factory));
    }

    private DataSubscriptionRequest buildSendRequest(
            DataSubscriptionRequest.SubscriptionRequestFactory factory) {
        return factory.create(
                mLocationRepository.getLocationObservable()
                        .map(this::createMessage)
                        .flatMap(mMessagesRepository::sendMessage)
        );
    }

    private MessageUserLocation createMessage(LocationSample locationSample) {
        return new MessageUserLocation(null, getSenderId(), null, locationSample);
    }

    private String getSenderId() {
        return mUserPreferences.getString(Constants.USERNAME_PREFRENCE_KEY);
    }
}
