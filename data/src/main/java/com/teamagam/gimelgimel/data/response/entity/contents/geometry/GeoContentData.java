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

  public GeoContentData(String id, Geometry geometry) {
    mId = id;
    mGeometry = geometry;
  }

  public GeoContentData(String id, Geometry geometry, Style style) {
    this(id, geometry);
    mStyle = style;
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
