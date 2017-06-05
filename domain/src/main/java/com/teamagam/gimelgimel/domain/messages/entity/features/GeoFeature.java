package com.teamagam.gimelgimel.domain.messages.entity.features;

import com.teamagam.gimelgimel.domain.map.entities.mapEntities.GeoEntity;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.IFeatureMessageVisitable;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageFeatureVisitor;

public class GeoFeature implements IFeatureMessageVisitable {

  private GeoEntity mGeoEntity;

  public GeoFeature(GeoEntity geoEntity) {
    mGeoEntity = geoEntity;
  }

  public GeoEntity getGeoEntity() {
    return mGeoEntity;
  }

  @Override
  public void accept(IMessageFeatureVisitor visitor) {
    visitor.visit(this);
  }
}
