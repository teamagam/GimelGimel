package com.teamagam.gimelgimel.data.response.entity;

import com.teamagam.gimelgimel.data.response.entity.contents.DynamicLayerData;
import com.teamagam.gimelgimel.data.response.entity.visitor.ResponseVisitor;

public class DynamicLayerResponse extends ServerResponse<DynamicLayerData> {

  public DynamicLayerResponse() {
    super(ServerResponse.DYNAMIC_LAYER);
  }

  @Override
  public void accept(ResponseVisitor visitor) {
    visitor.visit(this);
  }
}
