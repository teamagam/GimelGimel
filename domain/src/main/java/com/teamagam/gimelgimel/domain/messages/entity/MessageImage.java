package com.teamagam.gimelgimel.domain.messages.entity;

import com.teamagam.gimelgimel.domain.messages.entity.contents.ImageMetadata;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageVisitor;
import java.util.Date;

/**
 * Created on 5/18/2016.
 * Image-Type class for {@link Message}
 */
public class MessageImage extends Message {

  private ImageMetadata mImageMetadata;

  public MessageImage(String messageId, String senderId, Date createdAt, ImageMetadata metadata) {
    super(messageId, senderId, createdAt);
    mImageMetadata = metadata;
  }

  @Override
  public void accept(IMessageVisitor visitor) {
    visitor.visit(this);
  }

  public ImageMetadata getImageMetadata() {
    return mImageMetadata;
  }
}