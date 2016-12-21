package com.teamagam.gimelgimel.data.sensors.repository;

import com.teamagam.gimelgimel.data.base.repository.ReplayRepository;
import com.teamagam.gimelgimel.domain.messages.entity.contents.SensorMetadata;
import com.teamagam.gimelgimel.domain.sensors.repository.SensorsRepository;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Observable;

public class SensorsDataRepository implements SensorsRepository {

    private ReplayRepository<SensorMetadata> mReplayRepo;
    private Map<String, SensorMetadata> mIdToSensor;

    @Inject
    public SensorsDataRepository() {
        mReplayRepo = ReplayRepository.createReplayAll();
        mIdToSensor = new HashMap<>();
    }

    @Override
    public void addSensor(SensorMetadata sensorMetadata) {
        mIdToSensor.put(sensorMetadata.getId(), sensorMetadata);
        mReplayRepo.add(sensorMetadata);
    }

    @Override
    public SensorMetadata get(String sensorId) {
        return mIdToSensor.get(sensorId);
    }

    @Override
    public Observable<SensorMetadata> getSensorObservable() {
        return mReplayRepo.getObservable();
    }
}
