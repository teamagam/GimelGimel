package com.teamagam.gimelgimel.domain.map.repository;

import com.teamagam.gimelgimel.domain.notifications.entity.VectorLayerVisibilityChange;

import rx.Observable;

public interface VectorLayersVisibilityRepository {
    Observable<VectorLayerVisibilityChange> getVisibilityChangesLogObservable();

    void changeVectorLayerVisibility(VectorLayerVisibilityChange vectorLayerVisibilityChange);
}
