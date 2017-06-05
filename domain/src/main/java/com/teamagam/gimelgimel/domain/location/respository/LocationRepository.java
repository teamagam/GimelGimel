package com.teamagam.gimelgimel.domain.location.respository;

import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSample;
import io.reactivex.Observable;

public interface LocationRepository {

  Observable<LocationSample> getLocationObservable();

  LocationSample getLastLocationSample();

  LocationSample getLastServerSyncedLocationSample();

  void setLastServerSyncedLocationSample(LocationSample locationSample);
}
