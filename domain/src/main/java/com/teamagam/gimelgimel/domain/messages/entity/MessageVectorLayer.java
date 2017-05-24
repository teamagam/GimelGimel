package com.teamagam.gimelgimel.domain.messages.entity;

import com.teamagam.gimelgimel.domain.messages.entity.contents.VectorLayer;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageVisitor;
import java.net.URL;
import java.util.Date;

public class MessageVectorLayer extends Message {

  private final VectorLayer mVectorLayer;
  private final URL mUrl;

  public MessageVectorLayer(String messageId, String senderId, Date createdAt,
      VectorLayer vectorLayer, URL url) {
    super(messageId, senderId, createdAt);
    mVectorLayer = vectorLayer;
    mUrl = url;
  }

  @Override
  public void accept(IMessageVisitor visitor) {
    visitor.visit(this);
  }

  public VectorLayer getVectorLayer() {
    return mVectorLayer;
  }

  public URL getUrl() {
    return mUrl;
  }
}
