package com.teamagam.gimelgimel.data.location.repository;

import com.teamagam.gimelgimel.domain.location.entity.UserLocation;
import com.teamagam.gimelgimel.domain.location.respository.UsersLocationRepository;
import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSample;
import com.teamagam.gimelgimel.domain.utils.SerializedSubjectBuilder;

import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.subjects.SerializedSubject;

@Singleton
public class UsersLocationDataRepository implements UsersLocationRepository {

    private Map<String, Stack<LocationSample>> mUsersLocations;

    private final SerializedSubject<UserLocation, UserLocation> mUsersLocationSubject;

    @Inject
    public UsersLocationDataRepository() {
        mUsersLocations = new TreeMap<>();

        mUsersLocationSubject = new SerializedSubjectBuilder().build();
    }

    @Override
    public void add(String userId, LocationSample lastUserLocation) {
        if (!mUsersLocations.containsKey(userId)) {
            mUsersLocations.put(userId, new Stack<>());
        }
        mUsersLocations.get(userId).add(lastUserLocation);
        mUsersLocationSubject.onNext(createUserLocation(userId, lastUserLocation));
    }

    @Override
    public Observable<UserLocation> getUsersLocationUpdates() {
        return mUsersLocationSubject.asObservable();
    }

    @Override
    public Observable<UserLocation> getLastLocations() {
        return rx.Observable.from(mUsersLocations.entrySet())
                .map(this::extractLastLocation);
    }

    private UserLocation extractLastLocation(Map.Entry<String, Stack<LocationSample>> entry) {
        return createUserLocation(entry.getKey(), entry.getValue().peek());
    }

    private UserLocation createUserLocation(String userId, LocationSample locationSample) {
        return new UserLocation(userId, locationSample);
    }

}
