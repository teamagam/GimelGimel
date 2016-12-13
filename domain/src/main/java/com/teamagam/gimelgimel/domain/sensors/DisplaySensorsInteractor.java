package com.teamagam.gimelgimel.domain.sensors;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.BaseInteractor;
import com.teamagam.gimelgimel.domain.messages.entity.contents.SensorMetadata;
import com.teamagam.gimelgimel.domain.sensors.repository.SelectedSensorRepository;
import com.teamagam.gimelgimel.domain.sensors.repository.SensorsRepository;

import java.util.Arrays;

@AutoFactory
public class DisplaySensorsInteractor extends BaseInteractor {

    private final SensorsRepository mSensorsRepository;
    private SelectedSensorRepository mSelectedSensorRepository;
    private final Displayer mDisplayer;


    public DisplaySensorsInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided PostExecutionThread postExecutionThread,
            @Provided SensorsRepository sensorsRepository,
            @Provided SelectedSensorRepository selectedSensorRepository,
            Displayer displayer) {
        super(threadExecutor, postExecutionThread);
        mSensorsRepository = sensorsRepository;
        mSelectedSensorRepository = selectedSensorRepository;
        mDisplayer = displayer;
    }


    @Override
    protected Iterable<SubscriptionRequest> buildSubscriptionRequests() {
        SubscriptionRequest<SensorMetadata> displaySensors = new SubscriptionRequest<>(
                mSensorsRepository.getSensorObservable(),
                mDisplayer::display);

        SubscriptionRequest<SensorMetadata> displaySelected = new SubscriptionRequest<>(
                mSelectedSensorRepository.getObservable(),
                mDisplayer::displayAsSelected
        );

        return Arrays.asList(displaySensors, displaySelected);
    }

    public interface Displayer {
        void display(SensorMetadata sensorMetadata);

        void displayAsSelected(SensorMetadata sensorMetadata);
    }
}
