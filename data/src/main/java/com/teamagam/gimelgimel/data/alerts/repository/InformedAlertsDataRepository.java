package com.teamagam.gimelgimel.data.alerts.repository;

import com.teamagam.gimelgimel.domain.alerts.repository.InformedAlertsRepository;
import java.util.Date;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class InformedAlertsDataRepository implements InformedAlertsRepository {

  private Date mLatestInformedDate;

  @Inject
  public InformedAlertsDataRepository() {
    mLatestInformedDate = new Date(0);
  }

  @Override
  public synchronized Date getLatestInformedDate() {
    return mLatestInformedDate;
  }

  @Override
  public synchronized void updateLatestInformedDate(Date date) {
    if (isBeforeLatest(date)) {
      throw new RuntimeException(
          "Invalid argument, date is earlier than currently known latest informed date");
    }
    mLatestInformedDate = date;
  }

  private boolean isBeforeLatest(Date date) {
    return date.before(mLatestInformedDate);
  }
}
