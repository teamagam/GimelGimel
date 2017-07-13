package com.teamagam.gimelgimel.data.response.entity.contents.geometry;

import com.google.gson.annotations.SerializedName;

public class IconData {

  @SerializedName("id")
  private String mIconId;

  @SerializedName("color")
  private String mColor;

  public IconData(String iconId, String color) {
    mIconId = iconId;
    mColor = color;
  }

  public String getIconId() {
    return mIconId;
  }

  public String getColor() {
    return mColor;
  }
}