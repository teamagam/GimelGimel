package com.teamagam.gimelgimel.data.response.entity;

import com.teamagam.gimelgimel.data.response.entity.contents.VectorLayerData;
import com.teamagam.gimelgimel.data.response.entity.visitor.ResponseVisitor;

public class VectorLayerResponse extends ServerResponse<VectorLayerData> {

  public VectorLayerResponse() {
    super(ServerResponse.VECTOR_LAYER);
  }

  @Override
  public void accept(ResponseVisitor visitor) {
    visitor.visit(this);
  }
}
