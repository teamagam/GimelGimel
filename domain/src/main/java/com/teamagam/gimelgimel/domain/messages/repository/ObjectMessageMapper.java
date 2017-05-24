package com.teamagam.gimelgimel.domain.messages.repository;

public interface ObjectMessageMapper {
  void addMapping(String messageId, String objectId);

  String getObjectId(String messageId);

  String getMessageId(String objectId);
}
