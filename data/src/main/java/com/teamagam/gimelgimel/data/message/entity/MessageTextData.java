package com.teamagam.gimelgimel.data.message.entity;

import com.teamagam.gimelgimel.data.message.entity.visitor.IMessageDataVisitor;

/**
 * Text-Type class for {@link MessageData}'s inner content
 */
public class MessageTextData extends MessageData<String> {

  public MessageTextData(String text) {
    super(MessageData.TEXT);
    mContent = text;
  }

  @Override
  public void accept(IMessageDataVisitor visitor) {
    visitor.visit(this);
  }
}
