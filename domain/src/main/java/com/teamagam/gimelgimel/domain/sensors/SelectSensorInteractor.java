package com.teamagam.gimelgimel.domain.sensors;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.DoInteractor;
import com.teamagam.gimelgimel.domain.messages.entity.contents.SensorMetadata;
import com.teamagam.gimelgimel.domain.sensors.repository.SelectedSensorRepository;
import com.teamagam.gimelgimel.domain.sensors.repository.SensorsRepository;
import rx.Observable;

@AutoFactory
public class SelectSensorInteractor extends DoInteractor<SensorMetadata> {

  private SensorsRepository mSensorsRepository;
  private SelectedSensorRepository mSelectedSensorRepository;
  private String mSelectedSensorId;

  protected SelectSensorInteractor(@Provided ThreadExecutor threadExecutor,
      @Provided SensorsRepository sensorsRepository,
      @Provided SelectedSensorRepository selectedSensorRepository,
      String selectedSensorId) {
    super(threadExecutor);
    mSensorsRepository = sensorsRepository;
    mSelectedSensorRepository = selectedSensorRepository;
    mSelectedSensorId = selectedSensorId;
  }

  @Override
  protected Observable<SensorMetadata> buildUseCaseObservable() {
    return Observable.just(mSelectedSensorId)
        .map(mSensorsRepository::get)
        .doOnNext(mSelectedSensorRepository::setSelected);
  }
}
