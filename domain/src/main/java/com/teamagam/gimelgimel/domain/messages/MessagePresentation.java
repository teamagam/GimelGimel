package com.teamagam.gimelgimel.domain.messages;

import com.teamagam.gimelgimel.domain.messages.entity.Message;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageVisitable;
import com.teamagam.gimelgimel.domain.messages.entity.visitor.IMessageVisitor;

public class MessagePresentation implements IMessageVisitable{

  private Message mMessage;
  private boolean mIsFromSelf;
  private boolean mIsShownOnMap;
  private boolean mIsNotified;
  private boolean mIsSelected;

  private MessagePresentation(Message message, boolean isFromSelf, boolean isShownOnMap,
      boolean isNotified, boolean isSelected) {
    mMessage = message;
    mIsFromSelf = isFromSelf;
    mIsShownOnMap = isShownOnMap;
    mIsNotified = isNotified;
    mIsSelected = isSelected;
  }

  public Message getMessage() {
    return mMessage;
  }

  public boolean isFromSelf() {
    return mIsFromSelf;
  }

  public boolean isShownOnMap() {
    return mIsShownOnMap;
  }

  public boolean isNotified() {
    return mIsNotified;
  }

  public boolean isSelected() {
    return mIsSelected;
  }

  @Override
  public void accept(IMessageVisitor visitor) {
    mMessage.accept(visitor);
  }

  static class Builder {
    private Message mMessage;
    private boolean mIsFromSelf;
    private boolean mIsShownOnMap;
    private boolean mIsNotified;
    private boolean mIsSelected;

    public Builder(Message message) {
      mMessage = message;
    }

    Builder setIsFromSelf(boolean isFromSelf) {
      mIsFromSelf = isFromSelf;
      return this;
    }

    Builder setIsShownOnMap(boolean isShownOnMap) {
      mIsShownOnMap = isShownOnMap;
      return this;
    }

    Builder setIsNotified(boolean isNotified) {
      mIsNotified = isNotified;
      return this;
    }

    Builder setIsSelected(boolean isSelected) {
      mIsSelected = isSelected;
      return this;
    }

    MessagePresentation build() {
      return new MessagePresentation(mMessage, mIsFromSelf, mIsShownOnMap, mIsNotified,
          mIsSelected);
    }
  }
}