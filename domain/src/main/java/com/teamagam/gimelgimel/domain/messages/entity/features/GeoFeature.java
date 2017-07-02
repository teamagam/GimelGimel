package com.teamagam.gimelgimel.domain.messages.entity.features;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.MessageFeatureVisitable;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.MessageFeatureVisitor;

public class GeoFeature implements MessageFeatureVisitable {

  private GeoEntity mGeoEntity;

  public GeoFeature(GeoEntity geoEntity) {
    mGeoEntity = geoEntity;
  }

  public GeoEntity getGeoEntity() {
    return mGeoEntity;
  }

  @Override
  public void accept(MessageFeatureVisitor visitor) {
    visitor.visit(this);
  }
}
