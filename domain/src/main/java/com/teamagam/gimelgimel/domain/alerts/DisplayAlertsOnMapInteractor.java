package com.teamagam.gimelgimel.domain.alerts;

import com.teamagam.gimelgimel.domain.alerts.entity.Alert;
import com.teamagam.gimelgimel.domain.alerts.repository.AlertsRepository;
import com.teamagam.gimelgimel.domain.base.executor.ThreadExecutor;
import com.teamagam.gimelgimel.domain.base.interactors.AbsDisplayDataOnMapInteractor;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.AlertEntity;
import com.teamagam.gimelgimel.domain.map.repository.DisplayedEntitiesRepository;
import com.teamagam.gimelgimel.domain.map.repository.GeoEntitiesRepository;

import javax.inject.Inject;

import rx.Observable;

public class DisplayAlertsOnMapInteractor extends AbsDisplayDataOnMapInteractor<AlertEntity> {

    private AlertsRepository mAlertsRepository;

    @Inject
    DisplayAlertsOnMapInteractor(
            ThreadExecutor threadExecutor,
            DisplayedEntitiesRepository displayedEntitiesRepository,
            GeoEntitiesRepository geoEntitiesRepository,
            AlertsRepository alertsRepository) {
        super(threadExecutor, geoEntitiesRepository, displayedEntitiesRepository);
        mAlertsRepository = alertsRepository;
    }

    @Override
    protected Observable<AlertEntity> getEntityObservable() {
        return mAlertsRepository.getAlertsObservable()
                .map(Alert::getEntity);
    }
}
