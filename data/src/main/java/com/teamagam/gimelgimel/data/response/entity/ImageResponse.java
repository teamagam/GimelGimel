package com.teamagam.gimelgimel.data.response.entity;

import com.teamagam.gimelgimel.data.response.entity.contents.ImageMetadataData;
import com.teamagam.gimelgimel.data.response.entity.visitor.IResponseVisitor;

/**
 * Created on 5/18/2016.
 * Image-Type class for {@link GGResponse}'s inner content
 */
public class ImageResponse extends GGResponse<ImageMetadataData> {

  public ImageResponse(ImageMetadataData meta) {
    super(GGResponse.IMAGE);
    mContent = meta;
  }

  @Override
  public void accept(IResponseVisitor visitor) {
    visitor.visit(this);
  }
}
