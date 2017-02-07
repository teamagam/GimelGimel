package com.teamagam.gimelgimel.domain.location;


import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.DoInteractor;
import com.teamagam.gimelgimel.domain.config.Constants;
import com.teamagam.gimelgimel.domain.location.respository.LocationRepository;
import com.teamagam.gimelgimel.domain.messages.entity.MessageUserLocation;
import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSample;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

import javax.inject.Inject;

import rx.Observable;

public class SendSelfLocationsInteractor extends DoInteractor<MessageUserLocation> {

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
    protected Observable<MessageUserLocation> buildUseCaseObservable() {
        return mLocationRepository.getLocationObservable()
                .map(this::createMessage)
                .flatMap(mMessagesRepository::sendMessage)
                .cast(MessageUserLocation.class);
    }

    private MessageUserLocation createMessage(LocationSample locationSample) {
        return new MessageUserLocation(null, getSenderId(), null, locationSample);
    }

    private String getSenderId() {
        return mUserPreferences.getString(Constants.USERNAME_PREFRENCE_KEY);
    }

}
