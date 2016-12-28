package com.teamagam.gimelgimel.domain.sensors;

import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.AbsDisplayDataOnMapInteractor;
import com.teamagam.gimelgimel.domain.base.interactors.DoInteractor;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.SensorEntity;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.map.repository.GeoEntitiesRepository;
import com.teamagam.gimelgimel.domain.messages.entity.contents.SensorMetadata;
import com.teamagam.gimelgimel.domain.sensors.repository.SensorsRepository;

import javax.inject.Inject;

import rx.Observable;

public class DisplaySensorsOnMapInteractor extends AbsDisplayDataOnMapInteractor<SensorEntity> {

    private SensorsRepository mSensorsRepository;

    @Inject
    DisplaySensorsOnMapInteractor(
            ThreadExecutor threadExecutor,
            SensorsRepository sensorsRepository,
            DisplayedEntitiesRepository displayedEntitiesRepository,
            GeoEntitiesRepository geoEntitiesRepository) {
        super(threadExecutor, geoEntitiesRepository, displayedEntitiesRepository);
        mSensorsRepository = sensorsRepository;
    }

    @Override
    protected Observable<SensorEntity> getEntityObservable() {
        return mSensorsRepository.getSensorObservable()
                .map(SensorMetadata::getGeoEntity);
    }

}
