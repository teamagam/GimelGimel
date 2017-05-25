package com.teamagam.gimelgimel.domain.map.entities.mapEntities;

abstract class AbsGeoEntity implements GeoEntity {

  private String mId;
  private String mText;

  AbsGeoEntity(String id, String text) {
    mId = id;
    mText = text;
  }

  @Override
  public String getId() {
    return mId;
  }

  public String getText() {
    return mText;
  }
}
