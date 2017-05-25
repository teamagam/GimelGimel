package com.teamagam.gimelgimel.domain.sensors.repository;

import com.teamagam.gimelgimel.domain.messages.entity.contents.SensorMetadata;
import rx.Observable;

public interface SelectedSensorRepository {
  void setSelected(SensorMetadata selected);

  Observable<SensorMetadata> getObservable();
}
