package com.teamagam.gimelgimel.domain.location.entity;

import com.teamagam.gimelgimel.domain.config.Constants;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.UserEntity;
import com.teamagam.gimelgimel.domain.map.entities.symbols.UserSymbol;
import com.teamagam.gimelgimel.domain.messages.entity.contents.LocationSample;

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

  public UserEntity createUserEntity() {
    UserSymbol symbol = createUserSymbol();
    return new UserEntity(createUserEntityId(), getLocationSample().getLocation(),
        symbol);
  }

  private String createUserEntityId() {
    return String.format("%s_%s", mUser, mLocation.getTime());
  }

  private UserSymbol createUserSymbol() {
    if (isActive()) {
      return UserSymbol.createActive(getUser(), false);
    } else {
      return UserSymbol.createStale(getUser(), false);
    }
  }
}
