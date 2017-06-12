package com.teamagam.gimelgimel.domain.location.entity;

import com.teamagam.gimelgimel.domain.config.Constants;
import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSample;

/**
 * Created on 11/23/2016.
 */

public class UserLocation {

  private final LocationSample mLocation;
  private final String mUser;

  public UserLocation(String user, LocationSample location) {
    mLocation = location;
    mUser = user;
  }

  public LocationSample getLocationSample() {
    return mLocation;
  }

  public String getUser() {
    return mUser;
  }

  public boolean isActive() {
    return mLocation.getAgeMillis() < Constants.USER_LOCATION_STALE_THRESHOLD_MS;
  }

  public boolean isStale() {
    long ageMillis = mLocation.getAgeMillis();
    return ageMillis >= Constants.USER_LOCATION_STALE_THRESHOLD_MS
        && ageMillis < Constants.USER_LOCATION_RELEVANCE_THRESHOLD_MS;
  }

  public boolean isIrrelevant() {
    return mLocation.getAgeMillis() >= Constants.USER_LOCATION_RELEVANCE_THRESHOLD_MS;
  }
}
