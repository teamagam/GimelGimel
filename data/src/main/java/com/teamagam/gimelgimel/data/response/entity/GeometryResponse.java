package com.teamagam.gimelgimel.data.response.entity;

import com.teamagam.gimelgimel.data.response.entity.contents.GeoContentData;
import com.teamagam.gimelgimel.data.response.entity.visitor.IResponseVisitor;

/**
 * A class for geo messages (using GeoContentData as its content)
 */
public class GeometryResponse extends GGResponse<GeoContentData> {

  public GeometryResponse(GeoContentData location) {
    super(GGResponse.GEO);
    mContent = location;
  }

  @Override
  public void accept(IResponseVisitor visitor) {
    visitor.visit(this);
  }
}


