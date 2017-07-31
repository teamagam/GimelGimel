package com.teamagam.gimelgimel.data.response.entity.contents.geometry;

import com.google.gson.annotations.SerializedName;
import com.teamagam.geogson.core.model.Geometry;

public class GeoContentData {

  @SerializedName("id")
  private String mId;

  @SerializedName("geometry")
  private Geometry mGeometry;

  @SerializedName("text")
  private String mText;

  @SerializedName("style")
  private Style mStyle;

  public GeoContentData(Geometry geometry, String text, Style style) {
    mGeometry = geometry;
    mText = text;
    mStyle = style;
  }

  public GeoContentData(Geometry geometry, String text) {
    mGeometry = geometry;
    mText = text;
  }

  public String getId() {
    return mId;
  }

  public Geometry getGeometry() {
    return mGeometry;
  }

  public String getText() {
    return mText;
  }

  public void setText(String text) {
    mText = text;
  }

  public Style getStyle() {
    return mStyle;
  }
}
