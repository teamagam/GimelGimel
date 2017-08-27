package com.teamagam.gimelgimel.domain.map.entities.mapEntities;

abstract class AbsGeoEntity implements GeoEntity {

  private String mId;

  AbsGeoEntity(String id) {
    mId = id;
  }

  @Override
  public String toString() {
    return String.format("GeoEntity, id: %s\n", mId);
  }

  @Override
  public String getId() {
    return mId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    AbsGeoEntity that = (AbsGeoEntity) o;

    return mId.equals(that.mId);
  }

  @Override
  public int hashCode() {
    return mId.hashCode();
  }
}
