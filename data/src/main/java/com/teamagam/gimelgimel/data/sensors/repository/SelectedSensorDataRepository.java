package com.teamagam.gimelgimel.data.sensors.repository;

import com.teamagam.gimelgimel.data.base.repository.ReplayRepository;
import com.teamagam.gimelgimel.domain.messages.entity.contents.SensorMetadata;
import com.teamagam.gimelgimel.domain.sensors.repository.SelectedSensorRepository;

import javax.inject.Inject;

import rx.Observable;

public class SelectedSensorDataRepository implements SelectedSensorRepository {

    private final ReplayRepository<SensorMetadata> mSelectedInnerRepo;

    @Inject
    public SelectedSensorDataRepository() {
        mSelectedInnerRepo = ReplayRepository.createReplayCount(1);
    }

    @Override
    public void setSelected(SensorMetadata selected) {
        mSelectedInnerRepo.add(selected);
    }

    @Override
    public Observable<SensorMetadata> getObservable() {
        return mSelectedInnerRepo.getObservable();
    }
}
