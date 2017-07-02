package com.teamagam.gimelgimel.data.response.entity.contents;

import com.google.gson.annotations.SerializedName;
import com.teamagam.geogson.core.model.Geometry;

public class GeoContentData {

  @SerializedName("geometry")
  private Geometry mGeometry;

  @SerializedName("text")
  private String mText;

  @SerializedName("locationType")
  private String mLocationType;

  public GeoContentData(Geometry geometry, String text, String locationType) {
    mGeometry = geometry;
    mText = text;
    mLocationType = locationType;
  }

  public GeoContentData(Geometry geometry, String text) {
    mGeometry = geometry;
    mText = text;
  }

  public Geometry getGeometry() {
    return mGeometry;
  }

  public void setGeometry(Geometry geometry) {
    mGeometry = geometry;
  }

  public String getText() {
    return mText;
  }

  public void setText(String text) {
    mText = text;
  }

  public String getLocationType() {
    return mLocationType;
  }

  public void setLocationType(String locationType) {
    mLocationType = locationType;
  }
}
