package com.teamagam.gimelgimel.domain.map.entities.mapEntities;

abstract class AbsGeoEntity implements GeoEntity {

  private String mId;
  private String mText;

  AbsGeoEntity(String id, String text) {
    mId = id;
    mText = text;
  }

  @Override
  public String toString() {
    return String.format("GeoEntity, id: %s\ntext: %s\n", mId, mText);
  }

  @Override
  public String getId() {
    return mId;
  }

  @Override
  public String getText() {
    return mText;
  }
}
