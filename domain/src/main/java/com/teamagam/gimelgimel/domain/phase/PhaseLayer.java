package com.teamagam.gimelgimel.domain.phase;

import com.teamagam.gimelgimel.domain.base.repository.IdentifiedData;
import com.teamagam.gimelgimel.domain.dynamicLayers.entity.DynamicLayer;
import java.util.List;

public class PhaseLayer implements IdentifiedData {

  private final String mId;
  private final String mName;
  private final String mDescription;
  private final long mTimestamp;
  private final List<DynamicLayer> mPhases;

  public PhaseLayer(String id,
      String name,
      String description,
      long timestamp,
      List<DynamicLayer> phases) {
    mId = id;
    mName = name;
    mPhases = phases;
    mDescription = description;
    mTimestamp = timestamp;
  }

  public String getId() {
    return mId;
  }

  public String getName() {
    return mName;
  }

  public String getDescription() {
    return mDescription;
  }

  public long getTimestamp() {
    return mTimestamp;
  }

  public List<DynamicLayer> getPhases() {
    return mPhases;
  }

  public DynamicLayer getPhase(int i) {
    return mPhases.get(i);
  }

  @Override
  public String toString() {
    return "PhaseLayer{"
        + "mId='"
        + mId
        + '\''
        + ", mName='"
        + mName
        + '\''
        + ", mDescription='"
        + mDescription
        + '\''
        + ", mTimestamp="
        + mTimestamp
        + ", mPhases="
        + mPhases
        + '}';
  }
}
