package com.teamagam.gimelgimel.domain.map.entities.mapEntities;

public abstract class AlertEntity extends AbsGeoEntity {

  private final int mSeverity;

  public AlertEntity(String id, int severity) {
    super(id);
    mSeverity = severity;
  }

  public int getSeverity() {
    return mSeverity;
  }
}