package com.teamagam.gimelgimel.data.timeplay;

import com.teamagam.gimelgimel.domain.timeplay.GeoTimespanCalculator;
import java.util.Date;
import javax.inject.Inject;

public class DataGeoTimespanCalculator implements GeoTimespanCalculator {

  private final GeoMessagesTimespanCalculator mGeoMessagesTimespanCalculator;
  private final UsersGeoTimespanCalculator mUsersGeoTimespanCalculator;

  @Inject
  public DataGeoTimespanCalculator(GeoMessagesTimespanCalculator geoMessagesTimespanCalculator,
      UsersGeoTimespanCalculator usersGeoTimespanCalculator) {
    mGeoMessagesTimespanCalculator = geoMessagesTimespanCalculator;
    mUsersGeoTimespanCalculator = usersGeoTimespanCalculator;
  }

  @Override
  public Date getMinimumGeoItemDate() {
    Date minimumGeoMessageDate = mGeoMessagesTimespanCalculator.getMinimumGeoItemDate();
    Date minimumUserLocationDate = mUsersGeoTimespanCalculator.getMinimumGeoItemDate();

    return getEarlierIgnoreDefault(minimumGeoMessageDate, minimumUserLocationDate);
  }

  @Override
  public Date getMaximumGeoItemDate() {
    Date maxMessagesDate = mGeoMessagesTimespanCalculator.getMaximumGeoItemDate();
    Date maxUsersDate = mUsersGeoTimespanCalculator.getMaximumGeoItemDate();

    return maxMessagesDate.after(maxUsersDate) ? maxMessagesDate : maxUsersDate;
  }

  private Date getEarlierIgnoreDefault(Date minimumGeoMessageDate, Date minimumUserLocationDate) {
    if (isDefaultDate(minimumGeoMessageDate)) {
      return minimumUserLocationDate;
    }
    if (isDefaultDate(minimumUserLocationDate)) {
      return minimumGeoMessageDate;
    }

    return minimumGeoMessageDate.before(minimumUserLocationDate) ? minimumGeoMessageDate
        : minimumUserLocationDate;
  }

  private boolean isDefaultDate(Date date) {
    return date.getTime() == 0L;
  }
}
