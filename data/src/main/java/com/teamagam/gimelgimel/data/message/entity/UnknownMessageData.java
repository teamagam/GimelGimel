package com.teamagam.gimelgimel.data.message.entity;

import com.teamagam.gimelgimel.data.message.entity.visitor.IMessageDataVisitor;
import java.util.Date;

public class UnknownMessageData extends MessageData {
  public UnknownMessageData(Date date) {
    super(DUMMY);
    setCreatedAt(date);
  }

  @Override
  public void accept(IMessageDataVisitor visitor) {
    visitor.visit(this);
  }
}
