package com.teamagam.gimelgimel.domain.map.entities.mapEntities;

import com.teamagam.gimelgimel.domain.map.entities.geometries.PointGeometry;
import com.teamagam.gimelgimel.domain.map.entities.symbols.SensorSymbol;

public class SensorEntity {

  private PointGeometry mPointGeometry;
  private SensorSymbol mSensorSymbol;

  public SensorEntity(String id, String text, PointGeometry pointGeometry, boolean isSelected) {
    mPointGeometry = pointGeometry;
    mSensorSymbol = new SensorSymbol(isSelected, text);
  }
}