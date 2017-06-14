package com.teamagam.gimelgimel.data.response.entity;

import com.teamagam.gimelgimel.data.response.entity.contents.ImageMetadataData;
import com.teamagam.gimelgimel.data.response.entity.visitor.ResponseVisitor;

public class ImageMessageResponse extends ServerResponse<ImageMetadataData> {

  public ImageMessageResponse(ImageMetadataData meta) {
    super(ServerResponse.IMAGE);
    mContent = meta;
  }

  @Override
  public void accept(ResponseVisitor visitor) {
    visitor.visit(this);
  }
}
