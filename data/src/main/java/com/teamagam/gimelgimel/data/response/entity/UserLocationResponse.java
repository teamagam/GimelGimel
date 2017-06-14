package com.teamagam.gimelgimel.data.response.entity;

import com.teamagam.gimelgimel.data.response.entity.contents.LocationSampleData;
import com.teamagam.gimelgimel.data.response.entity.visitor.ResponseVisitor;

public class UserLocationResponse extends ServerResponse<LocationSampleData> {

  public UserLocationResponse(LocationSampleData sample) {
    super(ServerResponse.USER_LOCATION);
    mContent = sample;
  }

  @Override
  public void accept(ResponseVisitor visitor) {
    visitor.visit(this);
  }
}
