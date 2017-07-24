package com.teamagam.gimelgimel.domain.dynamicLayers.remote;

import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import io.reactivex.Observable;

public interface DynamicLayerRemoteSourceHandler {

  Observable<Void> addEntity(DynamicLayer dynamicLayer, GeoEntity geoEntity);
}
