package com.teamagam.gimelgimel.data.timeplay;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.timeplay.GeoSnapshoter;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class DataGeoSnapshoter implements GeoSnapshoter {

  private final GeoSnapshoter[] mSnapshoters;

  @Inject
  public DataGeoSnapshoter(UserGeoSnapshoter userGeoSnapshoter,
      GeoMessagesSnapshoter geoMessagesSnapshoter) {
    mSnapshoters = new GeoSnapshoter[] { userGeoSnapshoter, geoMessagesSnapshoter };
  }

  @Override
  public List<GeoEntity> snapshot(long maxTimestamp) {
    List<GeoEntity> res = new ArrayList<>();
    for (GeoSnapshoter geoSnapshoter : mSnapshoters) {
      res.addAll(geoSnapshoter.snapshot(maxTimestamp));
    }
    return res;
  }
}
