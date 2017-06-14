package com.teamagam.gimelgimel.data.response.repository;

import com.teamagam.gimelgimel.domain.messages.repository.ObjectMessageMapper;
import java.util.Map;
import java.util.TreeMap;
import javax.inject.Inject;

public class ObjectMessageDataMapper implements ObjectMessageMapper {
  protected Map<String, String> mObjectToMessage;
  protected Map<String, String> mMessageToObject;

  @Inject
  public ObjectMessageDataMapper() {
    mObjectToMessage = new TreeMap<>();
    mMessageToObject = new TreeMap<>();
  }

  @Override
  public void addMapping(String messageId, String objectId) {
    mObjectToMessage.put(objectId, messageId);
    mMessageToObject.put(messageId, objectId);
  }

  @Override
  public String getObjectId(String messageId) {
    return mMessageToObject.get(messageId);
  }

  @Override
  public String getMessageId(String objectId) {
    return mObjectToMessage.get(objectId);
  }
}
