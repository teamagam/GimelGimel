package com.teamagam.gimelgimel.domain.timeplay;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import java.util.List;

public interface GeoSnapshoter {

  List<GeoEntity> snapshot(long maxTimestamp);
}