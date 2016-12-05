package com.teamagam.gimelgimel.data.sensors.repository;

import com.teamagam.gimelgimel.data.base.repository.ReplayRepository;
import com.teamagam.gimelgimel.domain.messages.entity.contents.SensorMetadata;
import com.teamagam.gimelgimel.domain.sensors.repository.SensorsRepository;

import rx.Observable;

public class SensorsDataRepository implements SensorsRepository {

    private ReplayRepository<SensorMetadata> mReplayRepo;

    public SensorsDataRepository() {
        mReplayRepo = ReplayRepository.createReplayAll();
    }

    @Override
    public void addSensor(SensorMetadata sensorMetadata) {
        mReplayRepo.add(sensorMetadata);
    }

    @Override
    public Observable<SensorMetadata> getSensorObservable() {
        return mReplayRepo.getObservable();
    }
}
