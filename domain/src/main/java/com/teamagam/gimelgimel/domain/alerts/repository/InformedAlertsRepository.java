package com.teamagam.gimelgimel.domain.alerts.repository;

import java.util.Date;

public interface InformedAlertsRepository {

  Date getLatestInformedDate();

  void updateLatestInformedDate(Date date);
}
