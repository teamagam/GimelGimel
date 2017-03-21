package com.teamagam.gimelgimel.data.location.repository;

import com.teamagam.gimelgimel.domain.location.entity.UserLocation;
import com.teamagam.gimelgimel.domain.location.respository.UsersLocationRepository;
import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSample;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UsersLocationDataRepository implements UsersLocationRepository {

    private Map<String, Stack<LocationSample>> mUsersLocations;

    @Inject
    public UsersLocationDataRepository() {
        mUsersLocations = new TreeMap<>();
    }

    @Override
    public void add(String userId, LocationSample lastUserSample) {
        if (!mUsersLocations.containsKey(userId)) {
            mUsersLocations.put(userId, new Stack<>());
        }
        mUsersLocations.get(userId).add(lastUserSample);
    }

    @Override
    public Iterable<UserLocation> getLastLocations() {
        List<UserLocation> list = new ArrayList<>();
        for (Map.Entry<String, Stack<LocationSample>> entry : mUsersLocations.entrySet()) {
            list.add(extractLastLocation(entry));
        }
        return list;
    }

    private UserLocation extractLastLocation(Map.Entry<String, Stack<LocationSample>> entry) {
        return new UserLocation(entry.getKey(), entry.getValue().peek());
    }

}
