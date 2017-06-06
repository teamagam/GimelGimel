package com.teamagam.gimelgimel.data.message.entity;

import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;

public interface MessageParser {
  boolean isValid(ChatMessage message);

  MessageData parse(ChatMessage message);
}
