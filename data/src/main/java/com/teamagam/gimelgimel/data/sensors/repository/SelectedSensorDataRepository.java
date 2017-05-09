package com.teamagam.gimelgimel.data.sensors.repository;

import com.teamagam.gimelgimel.data.base.repository.SubjectRepository;
import com.teamagam.gimelgimel.domain.messages.entity.contents.SensorMetadata;
import com.teamagam.gimelgimel.domain.sensors.repository.SelectedSensorRepository;

import javax.inject.Inject;

import rx.Observable;

public class SelectedSensorDataRepository implements SelectedSensorRepository {

    private final SubjectRepository<SensorMetadata> mSelectedInnerRepo;

    @Inject
    public SelectedSensorDataRepository() {
        mSelectedInnerRepo = SubjectRepository.createReplayCount(1);
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
