package com.teamagam.gimelgimel.data.location.repository;


import com.teamagam.gimelgimel.data.map.repository.DisplayedEntitiesDataRepository;
import com.teamagam.gimelgimel.domain.location.respository.UserLocationRepository;
import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSampleEntity;

import java.util.Map;

import rx.Observable;

public class UserLocationDataRepository implements UserLocationRepository{

    private Map<String, Userloca> mUserLocations;
    private DisplayedEntitiesDataRepository mDisplayedRepo;

    @Override
    public void add(LocationSampleEntity lastUserLocation) {


    }

    @Override
    public Observable<LocationSampleEntity> getObservable() {
        return null;
    }

}
