package com.teamagam.gimelgimel.domain.location.respository;


import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSampleEntity;

import rx.Observable;

public interface UserLocationRepository {

    void add(LocationSampleEntity lastUserLocation);

    Observable<LocationSampleEntity> getObservable();
}
