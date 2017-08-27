package com.teamagam.gimelgimel.data.timeplay.users;

import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.UserLocationDao;
import com.teamagam.gimelgimel.domain.timeplay.GeoTimespanCalculator;
import java.util.Date;
import javax.inject.Inject;

public class UsersGeoTimespanCalculator implements GeoTimespanCalculator {

  private static final Date ZERO_DATE = new Date(0);

  private final UserLocationDao mUserLocationDao;

  @Inject
  public UsersGeoTimespanCalculator(UserLocationDao userLocationDao) {
    mUserLocationDao = userLocationDao;
  }

  @Override
  public Date getMinimumGeoItemDate() {
    long minimumTimestamp = mUserLocationDao.getMinimumTimestamp();
    return toDateOrDefault(minimumTimestamp);
  }

  @Override
  public Date getMaximumGeoItemDate() {
    long maximumTimestamp = mUserLocationDao.getMaximumTimestamp();
    return toDateOrDefault(maximumTimestamp);
  }

  private Date toDateOrDefault(long timestamp) {
    if (timestamp <= 0) {
      return ZERO_DATE;
    }
    return new Date(timestamp);
  }
}
