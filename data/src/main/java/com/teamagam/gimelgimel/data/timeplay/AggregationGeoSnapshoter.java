package com.teamagam.gimelgimel.data.timeplay;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.timeplay.GeoSnapshoter;
import java.util.ArrayList;
import java.util.List;

public class AggregationGeoSnapshoter implements GeoSnapshoter {

  private final GeoSnapshoter[] mSnapshoters;

  public AggregationGeoSnapshoter(GeoSnapshoter... snapshoters) {
    mSnapshoters = snapshoters;
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
