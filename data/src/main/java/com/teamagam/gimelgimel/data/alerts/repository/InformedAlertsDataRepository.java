package com.teamagam.gimelgimel.data.alerts.repository;

import com.teamagam.gimelgimel.domain.alerts.repository.InformedAlertsRepository;
import com.teamagam.gimelgimel.domain.user.repository.UserPreferencesRepository;
import java.util.Date;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class InformedAlertsDataRepository implements InformedAlertsRepository {

  private static final String LAST_INFORMED_TIMESTAMP = "last_informed_timestamp";
  private static final long LAST_INFORMED_DEFAULT_TIMESTAMP = 0L;

  private UserPreferencesRepository mUserPreferencesRepository;

  @Inject
  public InformedAlertsDataRepository(UserPreferencesRepository userPreferencesRepository) {
    mUserPreferencesRepository = userPreferencesRepository;
  }

  @Override
  public synchronized Date getLatestInformedDate() {
    if (!mUserPreferencesRepository.contains(LAST_INFORMED_TIMESTAMP)) {
      setDefaultValue();
    }
    return getCurrentValue();
  }

  @Override
  public synchronized void updateLatestInformedDate(Date date) {
    if (isBeforeLatest(date)) {
      throw new RuntimeException(
          "Invalid argument, date is earlier than currently known latest informed date");
    }
    setValue(date.getTime());
  }

  private Date getCurrentValue() {
    return new Date(mUserPreferencesRepository.getLong(LAST_INFORMED_TIMESTAMP));
  }

  private void setDefaultValue() {
    setValue(LAST_INFORMED_DEFAULT_TIMESTAMP);
  }

  private void setValue(long lastInformedDefaultTimestamp) {
    mUserPreferencesRepository.setPreference(LAST_INFORMED_TIMESTAMP, lastInformedDefaultTimestamp);
  }

  private boolean isBeforeLatest(Date date) {
    return date.before(getCurrentValue());
  }
}
