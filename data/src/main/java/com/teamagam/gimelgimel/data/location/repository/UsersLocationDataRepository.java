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

    private static final String LAYER_ID = "UsersLayer";

    private Map<String, List<LocationSample>> mUsersLocations;

    @Inject
    DisplayedEntitiesDataRepository mDisplayedEntitiesDataRepository;

    @Inject
    public UsersLocationDataRepository() {
        mUsersLocations = new TreeMap<>();
    }

    @Override
    public void add(String userId, LocationSample lastUserLocation) {
        UserEntity userEntity = new UserEntity(userId,
                userId, lastUserLocation.getLocation(), new UserSymbol(userId, true));
        userEntity.setLayerTag(LAYER_ID);

        if (mUsersLocations.containsKey(userId)) {
            mUsersLocations.get(userId).add(lastUserLocation);
            mDisplayedEntitiesDataRepository.update(userEntity);
        } else {
            LinkedList<LocationSample> userLocations = new LinkedList<>();
            userLocations.add(lastUserLocation);
            mUsersLocations.put(userId, userLocations);
            mDisplayedEntitiesDataRepository.show(userEntity);
        }
    }


}
