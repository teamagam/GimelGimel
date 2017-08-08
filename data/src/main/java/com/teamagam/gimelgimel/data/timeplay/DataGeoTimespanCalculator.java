package com.teamagam.gimelgimel.data.timeplay;

import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.MessagesDao;
import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.UserLocationDao;
import com.teamagam.gimelgimel.domain.timeplay.GeoTimespanCalculator;
import java.util.Date;
import javax.inject.Inject;

public class DataGeoTimespanCalculator implements GeoTimespanCalculator {

  private static final Date ZERO_DATE = new Date(0);

  private final MessagesDao mMessagesDao;
  private final UserLocationDao mUserLocationDao;

  @Inject
  public DataGeoTimespanCalculator(MessagesDao messagesDao, UserLocationDao userLocationDao) {
    mMessagesDao = messagesDao;
    mUserLocationDao = userLocationDao;
  }

  @Override
  public Date getMinimumGeoItemDate() {
    Date minimumGeoMessageDate = getDateOrDefaultValue(mMessagesDao.getMinimumGeoMessageDate());
    Date minimumUserLocationDate = new Date(mUserLocationDao.getMinimumTimestamp());

    return getEarlierIgnoreDefault(minimumGeoMessageDate, minimumUserLocationDate);
  }

  @Override
  public Date getMaximumGeoItemDate() {
    Date maxMessagesDate = getDateOrDefaultValue(mMessagesDao.getMaximumGeoMessageDate());
    Date maxUsersDate = new Date(mUserLocationDao.getMaximumTimestamp());

    return maxMessagesDate.after(maxUsersDate) ? maxMessagesDate : maxUsersDate;
  }

  private Date getDateOrDefaultValue(Date date) {
    return date == null ? ZERO_DATE : date;
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
