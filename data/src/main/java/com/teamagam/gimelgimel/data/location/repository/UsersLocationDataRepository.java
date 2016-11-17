package com.teamagam.gimelgimel.data.location.repository;


import com.teamagam.gimelgimel.data.map.repository.DisplayedEntitiesDataRepository;
import com.teamagam.gimelgimel.domain.location.respository.UsersLocationRepository;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.UserEntity;
import com.teamagam.gimelgimel.domain.map.entities.symbols.UserSymbol;
import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSample;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UsersLocationDataRepository implements UsersLocationRepository {

    private Map<String, List<LocationSample>> mUserLocations;

    @Inject
    DisplayedEntitiesDataRepository mDisplayedEntitiesDataRepository;

    @Inject
    public UsersLocationDataRepository() {
        mUserLocations = new TreeMap<>();
    }

    @Override
    public void add(String userId, LocationSample lastUserLocation) {
        UserEntity userEntity = new UserEntity(userId,
                userId, lastUserLocation.getLocation(), new UserSymbol(true));
        if (mUserLocations.containsKey(userId)){
            mUserLocations.get(userId).add(lastUserLocation);
            mDisplayedEntitiesDataRepository.show(userEntity);
        } else {
            LinkedList<LocationSample> userLocations = new LinkedList<>();
            userLocations.add(lastUserLocation);
            mUserLocations.put(userId, userLocations);
            mDisplayedEntitiesDataRepository.update(userEntity);
        }
    }



}
