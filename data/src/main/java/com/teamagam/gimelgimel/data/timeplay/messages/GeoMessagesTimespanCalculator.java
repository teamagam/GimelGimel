package com.teamagam.gimelgimel.data.timeplay.messages;

import com.teamagam.gimelgimel.data.message.repository.cache.room.dao.MessagesDao;
import com.teamagam.gimelgimel.domain.timeplay.GeoTimespanCalculator;
import java.util.Date;
import javax.inject.Inject;

public class GeoMessagesTimespanCalculator implements GeoTimespanCalculator {

  public static final Date ZERO_DATE = new Date(0);

  private final MessagesDao mMessagesDao;

  @Inject
  public GeoMessagesTimespanCalculator(MessagesDao messagesDao) {
    mMessagesDao = messagesDao;
  }

  @Override
  public Date getMinimumGeoItemDate() {
    return getDateOrDefaultValue(mMessagesDao.getMinimumGeoMessageDate());
  }

  @Override
  public Date getMaximumGeoItemDate() {
    return getDateOrDefaultValue(mMessagesDao.getMaximumGeoMessageDate());
  }

  private Date getDateOrDefaultValue(Date date) {
    return date == null ? ZERO_DATE : date;
  }
}
