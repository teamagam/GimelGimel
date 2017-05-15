package com.teamagam.gimelgimel.domain.location;


import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDataInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DataSubscriptionRequest;
import com.teamagam.gimelgimel.domain.config.Constants;
import com.teamagam.gimelgimel.domain.location.respository.LocationRepository;
import com.teamagam.gimelgimel.domain.map.SpatialEngine;
import com.teamagam.gimelgimel.domain.messages.entity.MessageUserLocation;
import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSample;
import com.teamagam.gimelgimel.domain.messages.repository.MessagesRepository;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;

import java.util.Collections;

import javax.inject.Inject;

public class SendSelfLocationsInteractor extends BaseDataInteractor {

    private UserPreferencesRepository mUserPreferences;
    private SpatialEngine mSpatialEngine;
    private MessagesRepository mMessagesRepository;
    private LocationRepository mLocationRepository;

    @Inject
    public SendSelfLocationsInteractor(ThreadExecutor threadExecutor,
                                       UserPreferencesRepository userPreferences,
                                       SpatialEngine spatialEngine,
                                       MessagesRepository messagesRepository,
                                       LocationRepository locationRepository) {
        super(threadExecutor);
        mUserPreferences = userPreferences;
        mSpatialEngine = spatialEngine;
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
                mLocationRepository.getLocationObservable(),
                locationSampleObservable ->
                        locationSampleObservable
                                .filter(this::shouldUpdateServer)
                                .map(this::createMessage)
                                .flatMap(mMessagesRepository::sendMessage)
        );
    }

    private boolean shouldUpdateServer(LocationSample locationSample) {
        LocationSample lastLocationSample = mLocationRepository.getLastLocationSample();
        long timeDeltaMs = locationSample.getTime() - lastLocationSample.getTime();
        double distanceDeltaMeters = mSpatialEngine.distanceInMeters(
                locationSample.getLocation(),
                lastLocationSample.getLocation());
        return timeDeltaMs > Constants.LOCATION_TIME_CHANGE_SERVER_UPDATE_THRESHOLD ||
                distanceDeltaMeters > Constants.LOCATION_CHANGE_METERS_SERVER_UPDATE_THRESHOLD;
    }

    private MessageUserLocation createMessage(LocationSample locationSample) {
        return new MessageUserLocation(null, getSenderId(), null, locationSample);
    }

    private String getSenderId() {
        return mUserPreferences.getString(Constants.USERNAME_PREFERENCE_KEY);
    }
}
