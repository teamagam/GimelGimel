package com.teamagam.gimelgimel.app.map;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.KmlEntityInfo;

public interface MapEntityClickedListener {

  void entityClicked(String entityId);

  void kmlEntityClicked(KmlEntityInfo kmlEntityInfo);
}
