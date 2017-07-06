package com.teamagam.gimelgimel.data.response.entity.contents.geometry;

import com.google.gson.annotations.SerializedName;

public class Style {

  @SerializedName("icon")
  private IconData mIconData;

  @SerializedName("border-color")
  private String mBorderColor;

  @SerializedName("fill")
  private String mFillColor;

  public Style(IconData iconData, String borderColor, String fillColor) {
    mIconData = iconData;
    mBorderColor = borderColor;
    mFillColor = fillColor;
  }

  public IconData getIconData() {
    return mIconData;
  }

  public String getBorderColor() {
    return mBorderColor;
  }

  public String getFillColor() {
    return mFillColor;
  }
}
