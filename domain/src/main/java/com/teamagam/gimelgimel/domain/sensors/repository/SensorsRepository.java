package com.teamagam.gimelgimel.domain.sensors.repository;

import com.teamagam.gimelgimel.domain.messages.entity.contents.SensorMetadata;
import rx.Observable;

public interface SensorsRepository {

  void addSensor(SensorMetadata sensorMetadata);

  SensorMetadata get(String sensorId);

  Observable<SensorMetadata> getSensorObservable();
}
