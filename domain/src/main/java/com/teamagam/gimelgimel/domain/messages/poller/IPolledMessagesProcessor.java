package com.teamagam.gimelgimel.domain.messages.poller;

import com.teamagam.gimelgimel.domain.location.entity.UserLocation;
import com.teamagam.gimelgimel.domain.messages.entity.ChatMessage;
import com.teamagam.gimelgimel.domain.layers.entitiy.VectorLayer;
import java.util.Collection;

/**
 * Defines processing functionality for polled messages
 */
public interface IPolledMessagesProcessor {
  void process(ChatMessage polledMessage);

  void process(Collection<ChatMessage> polledMessages);

  void process(VectorLayer vectorLayer);

  void process(UserLocation userLocation);
}
