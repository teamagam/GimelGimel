package com.teamagam.gimelgimel.app.map.view;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;

public interface MapEntityClickedListener {

    void entityClicked(String entityId);

    void kmlEntityClicked(GeoEntity kmlEntity);
}
