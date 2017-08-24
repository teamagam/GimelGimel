package com.teamagam.gimelgimel.data.timeplay;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.timeplay.GeoSnapshoter;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class DataGeoSnapshoter implements GeoSnapshoter {

  private final UserGeoSnapshoter mUserGeoSnapshoter;
  private final GeoEntitiesSnapshoter mGeoEntitiesSnapshoter;

  @Inject
  public DataGeoSnapshoter(UserGeoSnapshoter userGeoSnapshoter,
      GeoEntitiesSnapshoter geoEntitiesSnapshoter) {
    mUserGeoSnapshoter = userGeoSnapshoter;
    mGeoEntitiesSnapshoter = geoEntitiesSnapshoter;
  }

  @Override
  public List<GeoEntity> snapshot(long maxTimestamp) {
    List<GeoEntity> userEntities = mUserGeoSnapshoter.snapshot(maxTimestamp);
    List<GeoEntity> geoEntities = mGeoEntitiesSnapshoter.snapshot(maxTimestamp);

    List<GeoEntity> res = new ArrayList<>(userEntities.size() + geoEntities.size());
    res.addAll(userEntities);
    res.addAll(geoEntities);

    return res;
  }
}
