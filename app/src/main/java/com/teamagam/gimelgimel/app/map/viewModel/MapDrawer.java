package com.teamagam.gimelgimel.app.map.viewModel;

import com.teamagam.gimelgimel.app.map.view.GGMapView;
import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.notifications.entity.GeoEntityNotification;
import java.util.ArrayList;
import java.util.Collection;

public class MapDrawer {

  private GGMapView mGGMapView;
  private Collection<GeoEntity> mGeoEntities;

  public MapDrawer(GGMapView ggMapView) {
    mGGMapView = ggMapView;
    mGeoEntities = new ArrayList<>();
  }

  public void draw(GeoEntity geoEntity) {
    mGeoEntities.add(geoEntity);
    displayOnMap(geoEntity);
  }

  public void erase(GeoEntity geoEntity) {
    if (mGeoEntities.contains(geoEntity)) {
      eraseFromMap(geoEntity);
      mGeoEntities.remove(geoEntity);
    }
  }

  public void clear() {
    for (GeoEntity ge : mGeoEntities) {
      eraseFromMap(ge);
    }
    mGeoEntities.clear();
  }

  private void displayOnMap(GeoEntity geoEntity) {
    mGGMapView.updateMapEntity(GeoEntityNotification.createAdd(geoEntity));
  }

  private void eraseFromMap(GeoEntity geoEntity) {
    mGGMapView.updateMapEntity(GeoEntityNotification.createRemove(geoEntity));
  }
}
