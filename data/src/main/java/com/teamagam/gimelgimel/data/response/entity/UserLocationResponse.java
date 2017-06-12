package com.teamagam.gimelgimel.data.response.entity;

import com.teamagam.gimelgimel.data.response.entity.contents.LocationSampleData;
import com.teamagam.gimelgimel.data.response.entity.visitor.IResponseVisitor;

/**
 * UserLocation-Type class for {@link GGResponse}'s inner content
 */
public class UserLocationResponse extends GGResponse<LocationSampleData> {

  public UserLocationResponse(LocationSampleData sample) {
    super(GGResponse.USER_LOCATION);
    mContent = sample;
  }

  @Override
  public void accept(IResponseVisitor visitor) {
    visitor.visit(this);
  }
}
