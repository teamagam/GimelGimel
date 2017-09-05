package com.teamagam.gimelgimel.data.response.entity.contents;

import com.google.gson.annotations.SerializedName;
import com.teamagam.gimelgimel.data.response.entity.contents.geometry.GeoContentData;

public class DynamicLayerData {

  @SerializedName("id")
  private String mId;

  @SerializedName("name")
  private String mName;

  @SerializedName("description")
  private String mDescription;

  @SerializedName("entities")
  private GeoContentData[] mEntities;

  public DynamicLayerData(String id, String name, String description, GeoContentData[] entities) {
    mId = id;
    mName = name;
    mDescription = description;
    mEntities = entities;
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

  public GeoContentData[] getEntities() {
    return mEntities;
  }
}
