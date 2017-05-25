package com.teamagam.gimelgimel.domain.messages.entity;

import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageVisitor;
import java.util.Date;

public class UnknownMessage extends Message {

  private static String EMPTY = "";

  public UnknownMessage(Date createdAt) {
    super(EMPTY, EMPTY, createdAt);
  }

  @Override
  public void accept(IMessageVisitor visitor) {
  }
}
