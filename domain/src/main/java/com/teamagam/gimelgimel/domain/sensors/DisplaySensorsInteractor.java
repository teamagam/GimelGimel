package com.teamagam.gimelgimel.domain.sensors;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseDisplayInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DisplaySubscriptionRequest;
import com.teamagam.gimelgimel.domain.messages.entity.contents.SensorMetadata;
import com.teamagam.gimelgimel.domain.sensors.repository.SelectedSensorRepository;
import com.teamagam.gimelgimel.domain.sensors.repository.SensorsRepository;
import java.util.Arrays;

@AutoFactory
public class DisplaySensorsInteractor extends BaseDisplayInteractor {

  private final SensorsRepository mSensorsRepository;
  private final Displayer mDisplayer;
  private SelectedSensorRepository mSelectedSensorRepository;

  public DisplaySensorsInteractor(
      @Provided
          ThreadExecutor threadExecutor,
      @Provided
          PostExecutionThread postExecutionThread,
      @Provided
          SensorsRepository sensorsRepository,
      @Provided
          SelectedSensorRepository selectedSensorRepository, Displayer displayer) {
    super(threadExecutor, postExecutionThread);
    mSensorsRepository = sensorsRepository;
    mSelectedSensorRepository = selectedSensorRepository;
    mDisplayer = displayer;
  }

  @Override
  protected Iterable<SubscriptionRequest> buildSubscriptionRequests(
      DisplaySubscriptionRequest.DisplaySubscriptionRequestFactory factory) {
    DisplaySubscriptionRequest displaySensors =
        factory.createSimple(mSensorsRepository.getSensorObservable(), mDisplayer::display);

    DisplaySubscriptionRequest displaySelected =
        factory.createSimple(mSelectedSensorRepository.getObservable(),
            mDisplayer::displayAsSelected);

    return Arrays.asList(displaySensors, displaySelected);
  }

  public interface Displayer {
    void display(SensorMetadata sensorMetadata);

    void displayAsSelected(SensorMetadata sensorMetadata);
  }
}
