package com.teamagam.gimelgimel.data.message.entity.contents;

import com.google.gson.annotations.SerializedName;
import com.teamagam.geogson.core.model.Point;

public class SensorMetadataData {

  @SerializedName("id")
  private String mId;

  @SerializedName("name")
  private String mName;

  @SerializedName("geometry")
  private Point mPoint;

  public SensorMetadataData(String id, String name, Point pointGeometryData) {
    mId = id;
    mName = name;
    mPoint = pointGeometryData;
  }

  public String getId() {
    return mId;
  }

  public void setId(String mId) {
    this.mId = mId;
  }

  public String getName() {
    return mName;
  }

  public void setName(String mName) {
    this.mName = mName;
  }

  public Point getPoint() {
    return mPoint;
  }

  public void setPoint(Point point) {
    mPoint = point;
  }
}
