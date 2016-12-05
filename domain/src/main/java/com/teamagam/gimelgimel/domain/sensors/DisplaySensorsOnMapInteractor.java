package com.teamagam.gimelgimel.domain.sensors;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.DoInteractor;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.map.repository.GeoEntitiesRepository;
import com.teamagam.gimelgimel.domain.messages.entity.contents.SensorMetadata;
import com.teamagam.gimelgimel.domain.sensors.repository.SensorsRepository;

import javax.inject.Inject;

import rx.Observable;

public class DisplaySensorsOnMapInteractor extends DoInteractor<SensorMetadata> {

    private SensorsRepository mSensorsRepository;
    private DisplayedEntitiesRepository mDisplayedEntitiesRepository;
    private GeoEntitiesRepository mGeoEntitiesRepository;

    @Inject
    protected DisplaySensorsOnMapInteractor(
            ThreadExecutor threadExecutor,
            SensorsRepository sensorsRepository,
            DisplayedEntitiesRepository displayedEntitiesRepository,
            GeoEntitiesRepository geoEntitiesRepository) {
        super(threadExecutor);
        mSensorsRepository = sensorsRepository;
        mDisplayedEntitiesRepository = displayedEntitiesRepository;
        mGeoEntitiesRepository = geoEntitiesRepository;
    }

    @Override
    protected Observable<SensorMetadata> buildUseCaseObservable() {
        return mSensorsRepository.getSensorObservable()
                .doOnNext(
                        sensorMetadata -> mGeoEntitiesRepository.add(sensorMetadata.getGeoEntity()))
                .doOnNext(this::addSensorToDisplayedEntities);
    }

    private void addSensorToDisplayedEntities(SensorMetadata sensorMetadata) {
        if (!mDisplayedEntitiesRepository.isNotShown(sensorMetadata.getGeoEntity())) {
            mDisplayedEntitiesRepository.hide(sensorMetadata.getGeoEntity());
        }
        mDisplayedEntitiesRepository.show(sensorMetadata.getGeoEntity());
    }
}
