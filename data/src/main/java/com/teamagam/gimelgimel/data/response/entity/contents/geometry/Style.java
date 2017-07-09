package com.teamagam.gimelgimel.data.response.entity.contents.geometry;

import com.google.gson.annotations.SerializedName;

public class Style {

  @SerializedName("icon")
  private IconData mIconData;

  @SerializedName("border-color")
  private String mBorderColor;

  @SerializedName("fill")
  private String mFillColor;

  @SerializedName("border-style")
  private String mBorderStyle;

  public Style(IconData iconData, String borderColor, String fillColor, String borderStyle) {
    mIconData = iconData;
    mBorderColor = borderColor;
    mFillColor = fillColor;
    mBorderStyle = borderStyle;
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

  public String getBorderStyle() {
    return mBorderStyle;
  }
}
