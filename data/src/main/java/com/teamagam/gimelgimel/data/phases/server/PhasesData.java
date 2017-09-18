package com.teamagam.gimelgimel.data.phases.server;

import com.google.gson.annotations.SerializedName;
import com.teamagam.gimelgimel.data.response.entity.contents.DynamicLayerData;

public class PhasesData {

  @SerializedName("id")
  private String mId;

  @SerializedName("name")
  private String mName;

  @SerializedName("description")
  private String mDescription;

  @SerializedName("phases")
  private DynamicLayerData[] mPhases;

  public PhasesData(String id, String name, String description, DynamicLayerData[] phases) {
    mId = id;
    mName = name;
    mDescription = description;
    mPhases = phases;
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

  public DynamicLayerData[] getPhases() {
    return mPhases;
  }
}
