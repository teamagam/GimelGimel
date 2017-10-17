package com.teamagam.gimelgimel.app.map;

import com.teamagam.gimelgimel.domain.dynamicLayers.DisplayDynamicLayersInteractor;
import com.teamagam.gimelgimel.domain.dynamicLayers.DynamicLayerPresentation;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicEntity;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;
import io.reactivex.functions.Function;
import java.util.HashMap;
import java.util.Map;

public class DynamicLayersMapDisplayer implements DisplayDynamicLayersInteractor.Displayer {
  private final Map<String, DynamicLayer> mIdToDisplayedDynamicLayerMap;
  private final GGMapView mGGMapView;

  public DynamicLayersMapDisplayer(GGMapView GGMapView) {
    mGGMapView = GGMapView;
    mIdToDisplayedDynamicLayerMap = new HashMap<>();
  }

  @Override
  public void display(DynamicLayerPresentation dl) {
    String id = dl.getId();
    hideDynamicLayerEntities(id);
    if (dl.isShown()) {
      showDynamicLayerEntities(dl, id);
    }
  }

  private void hideDynamicLayerEntities(String dynamicLayerId) {
    if (mIdToDisplayedDynamicLayerMap.containsKey(dynamicLayerId)) {
      updateEntitiesOnMap(dynamicLayerId, GeoEntityNotification::createRemove);
    }
  }

  private void updateEntitiesOnMap(String id, Function<GeoEntity, GeoEntityNotification> creator) {
    for (DynamicEntity entity : mIdToDisplayedDynamicLayerMap.get(id).getEntities()) {
      try {
        mGGMapView.updateMapEntity(creator.apply(entity.getGeoEntity()));
      } catch (Exception ignored) {
      }
    }
  }

  private void showDynamicLayerEntities(DynamicLayerPresentation dl, String id) {
    mIdToDisplayedDynamicLayerMap.put(id, dl);
    updateEntitiesOnMap(id, GeoEntityNotification::createAdd);
  }
}