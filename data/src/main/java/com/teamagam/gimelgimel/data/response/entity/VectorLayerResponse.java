package com.teamagam.gimelgimel.data.response.entity;

import com.teamagam.gimelgimel.data.response.entity.contents.VectorLayerData;
import com.teamagam.gimelgimel.data.response.entity.visitor.IResponseVisitor;

public class VectorLayerResponse extends GGResponse<VectorLayerData> {

  public VectorLayerResponse() {
    super(GGResponse.VECTOR_LAYER);
  }

  @Override
  public void accept(IResponseVisitor visitor) {
    visitor.visit(this);
  }
}
