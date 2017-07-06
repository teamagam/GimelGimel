package com.teamagam.gimelgimel.data.response.entity;

import com.teamagam.gimelgimel.data.response.entity.contents.geometry.GeoContentData;
import com.teamagam.gimelgimel.data.response.entity.visitor.ResponseVisitor;

public class GeometryMessageResponse extends ServerResponse<GeoContentData> {

  public GeometryMessageResponse(GeoContentData location) {
    super(ServerResponse.GEO);
    mContent = location;
  }

  @Override
  public void accept(ResponseVisitor visitor) {
    visitor.visit(this);
  }
}


