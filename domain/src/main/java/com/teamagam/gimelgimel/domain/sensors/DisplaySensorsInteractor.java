package com.teamagam.gimelgimel.domain.sensors;

import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import com.teamagam.gimelgimel.domain.base.executor.PostExecutionThread;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.Interactor;
import com.teamagam.gimelgimel.domain.base.subscribers.SimpleSubscriber;
import com.teamagam.gimelgimel.domain.messages.entity.contents.SensorMetadata;
import com.teamagam.gimelgimel.domain.sensors.repository.SelectedSensorRepository;
import com.teamagam.gimelgimel.domain.sensors.repository.SensorsRepository;

import rx.Subscription;

@AutoFactory
public class DisplaySensorsInteractor implements Interactor {

    private final ThreadExecutor mThreadExecutor;
    private final PostExecutionThread mPostExecutionThread;
    private final SensorsRepository mSensorsRepository;
    private SelectedSensorRepository mSelectedSensorRepository;
    private final Displayer mDisplayer;
    private Subscription mSubscription;
    private Subscription mSelectedSubscription;


    public DisplaySensorsInteractor(
            @Provided ThreadExecutor threadExecutor,
            @Provided PostExecutionThread postExecutionThread,
            @Provided SensorsRepository sensorsRepository,
            @Provided SelectedSensorRepository selectedSensorRepository,
            Displayer displayer) {
        mThreadExecutor = threadExecutor;
        mPostExecutionThread = postExecutionThread;
        mSensorsRepository = sensorsRepository;
        mSelectedSensorRepository = selectedSensorRepository;
        mDisplayer = displayer;
    }


    //TODO: refactor code to share code for execution and unsubscription of observable


    @Override
    public void execute() {
        mSubscription = mSensorsRepository
                .getSensorObservable()
                .subscribeOn(mThreadExecutor.getScheduler())
                .observeOn(mPostExecutionThread.getScheduler())
                .subscribe(new SimpleSubscriber<SensorMetadata>() {
                    @Override
                    public void onNext(SensorMetadata sensorMetadata) {
                        mDisplayer.display(sensorMetadata);
                    }
                });

        mSelectedSubscription = mSelectedSensorRepository.getObservable()
                .subscribeOn(mThreadExecutor.getScheduler())
                .observeOn(mPostExecutionThread.getScheduler())
                .subscribe(new SimpleSubscriber<SensorMetadata>() {
                    @Override
                    public void onNext(SensorMetadata sensorMetadata) {
                        mDisplayer.displayAsSelected(sensorMetadata);
                    }
                });
    }

    @Override
    public void unsubscribe() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }

        if (mSelectedSubscription != null && !mSelectedSubscription.isUnsubscribed()) {
            mSelectedSubscription.unsubscribe();
        }
    }

    public interface Displayer {
        void display(SensorMetadata sensorMetadata);

        void displayAsSelected(SensorMetadata sensorMetadata);
    }
}
